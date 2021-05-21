package com.zr.netty.rpcdemo.rpc.transport;

import com.zr.netty.rpcdemo.rpc.ResponseMappingCallback;
import com.zr.netty.rpcdemo.rpc.protocol.MyContent;
import com.zr.netty.rpcdemo.rpc.protocol.MyHeader;
import com.zr.netty.rpcdemo.rpc.util.SerDerUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author zhourui
 * @Date 2021/5/19 16:56
 */
public class ClientFactory {

    int poolSize = 1;
    Random rand = new Random();
    NioEventLoopGroup clientWorker;

    public ClientFactory() {
    }

    private static final ClientFactory factory;
    static {
        factory = new ClientFactory();
    }

    public static ClientFactory getFactory() {
        return factory;
    }

    public static CompletableFuture<Object> transport(MyContent content) {

        // content 就是货物，现在可以用自定义的rpc传输协议，也可以用http协议作为载体传输
        // 先手工用了http协议作为载体，那这样是不是代表我们未来可以用provider是一个tomcat jetty 基于http协议的一个容器
        // 有无状态来自于你使用的什么协议，那么http协议肯定是无状态的，每个请求对应一个连接
        // dubbo 是一个rpc框架，netty是一个io框架
        // dubbo 中传输协议上，可以是自定义的rpc传输协议，http协议

        String type = "http";
        CompletableFuture<Object> res = new CompletableFuture<>();
        if (type.equals("rpc")) {
            byte[] msgBody = SerDerUtil.ser(content);
            MyHeader header = MyHeader.createHeader(msgBody);
            byte[] msgHeader = SerDerUtil.ser(header);
            System.out.println("main::: " + msgHeader.length);

            ClientFactory factory = ClientFactory.getFactory();
            NioSocketChannel clientChannel = factory.getClient(new InetSocketAddress("localhost", 9090));

            ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(msgHeader.length + msgBody.length);

            long id = header.getRequestID();
            ResponseMappingCallback.addCallBack(id, res);
            byteBuf.writeBytes(msgHeader);
            byteBuf.writeBytes(msgBody);
            ChannelFuture channelFuture = clientChannel.writeAndFlush(byteBuf);
        } else {
            // 使用http协议作为载体
            // 1. 用url现成的工具(包含了http的编解码，发送，socket，连接)
//            utlTS(content, res);
            // 2. 自己操心：on netty(io框架) + 已经提供的http相关的编解码
            nettyTS(content, res);
        }
        return res;

    }

    /**
     * 使用netty
     *
     * @param content
     * @param res
     */
    private static void nettyTS(MyContent content, CompletableFuture<Object> res) {
        // 在这个执行之前 我们的server端 provider端已经开发完了，已经是 on netty 的http server了
        // 现在做的是consumer端的代码修改，改成on netty 的http client
        // 刚才一切都顺利，关注未来的问题

        // 每个请求对应一个连接
        // 1. 通过netty建立 io 建立连接
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        Bootstrap client = bs.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // 3. 接收 预埋的回调，根据netty对socket io 事件的相应
                                        // 客户端的msg是啥：完整的http-response
                                        FullHttpResponse response = (FullHttpResponse) msg;
                                        System.out.println(response.toString());

                                        ByteBuf resContent = response.content();
                                        byte[] data = new byte[resContent.readableBytes()];
                                        resContent.readBytes(data);

                                        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent o = (MyContent)oin.readObject();

                                        res.complete(o.getRes());
                                    }
                                });
                    }
                });// 未来连接后，收到数据的处理handler
        try {
            ChannelFuture syncFuture = client.connect("localhost", 9090).sync();
            // 2. 发送
            Channel clientChannel = syncFuture.channel();
            byte[] data = SerDerUtil.ser(content);

            DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0,
                    HttpMethod.POST, "/",
                    Unpooled.copiedBuffer(data));

            request.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, data.length);
            clientChannel.writeAndFlush(request).sync(); // 作为client 向 server端发送：http request
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * 自定义协议
     *
     * @param content
     * @param res
     */
    private static void utlTS(MyContent content, CompletableFuture<Object> res) {
        // 这中方式是每请求占用一个连接的方式，因为使用的是http协议
        Object obj = null;
        try {
            URL url = new URL("http://localhost:9090/");
            HttpURLConnection hc = (HttpURLConnection) url.openConnection();
            // post
            hc.setRequestMethod("POST");
            hc.setDoOutput(true); // body
            hc.setDoInput(true);

            OutputStream out = hc.getOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(content); // 这里真的发送了吗

            if (hc.getResponseCode() == 200) {
                InputStream in = hc.getInputStream();
                ObjectInputStream oin = new ObjectInputStream(in);
                MyContent myContent = (MyContent) oin.readObject();
                obj = myContent.getRes();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        res.complete(obj);
    }

    // 一个Consumer可以连接很多的provider，每一个provider都有自己的pool k，v
    ConcurrentHashMap<InetSocketAddress, ClientPool> outboxs = new ConcurrentHashMap<>();

    public synchronized NioSocketChannel getClient(InetSocketAddress address) {
        // TODO 在并发情况下一定要谨慎
        ClientPool clientPool = outboxs.get(address);
        if (clientPool == null) {
            synchronized (outboxs) {
                if (clientPool == null) {
                    outboxs.putIfAbsent(address, new ClientPool(poolSize));
                    clientPool = outboxs.get(address);
                }
            }
        }

        int i = rand.nextInt(poolSize);

        if (clientPool.clients[i] != null && clientPool.clients[i].isActive()) {
            return clientPool.clients[i];
        } else {
            synchronized (clientPool.lock[i]) {
                if (clientPool.clients[i] == null || !clientPool.clients[i].isActive()) {
                    clientPool.clients[i] = create(address);
                }
            }
        }
        return clientPool.clients[i];
    }

    private NioSocketChannel create(InetSocketAddress address) {
        // 基于 netty 的客户端创建方式
        clientWorker = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();
        ChannelFuture connect = bs.group(clientWorker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ServerDecode());
                        p.addLast(new ClientResponse()); // 解决给谁的 requestID
                    }
                }).connect(address);
        try {
            NioSocketChannel client = (NioSocketChannel) connect.sync().channel();
            return client;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
