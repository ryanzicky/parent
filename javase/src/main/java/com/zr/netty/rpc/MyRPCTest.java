package com.zr.netty.rpc;

/**
 * @Description TODO
 * @Author zhourui
 * @Date 2021/5/18 11:20
 */

import com.zr.netty.rpcdemo.proxy.MyProxy;
import com.zr.netty.rpcdemo.rpc.Dispatcher;
import com.zr.netty.rpcdemo.rpc.protocol.MyContent;
import com.zr.netty.rpcdemo.rpc.service.Car;
import com.zr.netty.rpcdemo.rpc.service.Fly;
import com.zr.netty.rpcdemo.rpc.service.Person;
import com.zr.netty.rpcdemo.rpc.service.impl.MyCar;
import com.zr.netty.rpcdemo.rpc.service.impl.MyFly;
import com.zr.netty.rpcdemo.rpc.transport.MyHttpRpcHandler;
import com.zr.netty.rpcdemo.rpc.util.SerDerUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1. 先假设一个需求，写一个RPC
 * 2. 来回通信，连接数量，粘包拆包
 * 3. 动态代理，序列化，反序列化，协议封装
 * 4. 连接池
 * 5. 就像调用本地方法一样去调用远程的方法，面向java中就是所谓的面向interface开发
 */
public class MyRPCTest {

    @Test
    public void startServer() {

        MyCar car = new MyCar();
        MyFly fly = new MyFly();
        Dispatcher dis = Dispatcher.getDis();
        dis.register(Car.class.getName(), car);
        dis.register(Fly.class.getName(), fly);

        NioEventLoopGroup boss = new NioEventLoopGroup(20);
        NioEventLoopGroup worker = boss;

        ServerBootstrap sbs = new ServerBootstrap();
        ChannelFuture bind = sbs.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        System.out.println("server accept client port: " + ch.remoteAddress().getPort());
                        ChannelPipeline p = ch.pipeline();
                        // 1. 自定义的rpc
//                        p.addLast(new ServerDecode());
//                        p.addLast(new ServerRequestHandler(dis));
                        // 在自己定义协议的时候你关注过哪些问题: 粘包拆包的问题，header + body
                        // 2. 小火车，传输协议用的就是http了
                        // 其实netty提供了一套编解码
                        p.addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(1024 * 512))
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        // http协议，这个msg是一个啥：完整的http-request
                                        FullHttpRequest request = (FullHttpRequest) msg;
                                        System.out.println(request.toString()); // 因为现在consumer使用的是一个现成的url

                                        // 这个就是consumer 序列化的MyContent
                                        ByteBuf content = request.content();
                                        byte[] data = new byte[content.readableBytes()];
                                        content.readBytes(data);
                                        ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(data));
                                        MyContent myContent = (MyContent) oin.readObject();


                                        String serviceName = myContent.getName();
                                        String method = myContent.getMethodName();
                                        Object c = dis.get(serviceName);
                                        Class<?> clazz = c.getClass();
                                        Object res = null;
                                        try {

                                            Method m = clazz.getMethod(method, myContent.getParameterTypes());
                                            res = m.invoke(c, myContent.getArgs());

                                        } catch (NoSuchMethodException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }

                                        MyContent resContent = new MyContent();
                                        resContent.setRes(res);
                                        byte[] contentByte = SerDerUtil.ser(resContent);

                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_0,
                                                HttpResponseStatus.OK,
                                                Unpooled.copiedBuffer(contentByte));

                                        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, contentByte.length);
                                        // http协议，header + body
                                        ctx.writeAndFlush(response);
                                    }
                                });
                    }
                }).bind(new InetSocketAddress("localhost", 9090));
        try {
            bind.sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void startHttpServer() {

        MyCar car = new MyCar();
        MyFly fly = new MyFly();
        Dispatcher dis = Dispatcher.getDis();
        dis.register(Car.class.getName(), car);
        dis.register(Fly.class.getName(), fly);

        // tomcat jetty [servlet]
        Server server = new Server(new InetSocketAddress("localhost", 9090));
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        server.setHandler(handler);
        handler.addServlet(MyHttpRpcHandler.class, "/*"); // web.xml

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 模拟consumer端
    @Test
    public void get() {

//        new Thread(() -> {
//            startServer();
//        }).start();
//
//        System.out.println("server started......");

        AtomicInteger num = new AtomicInteger(0);
        int size = 50;
        Thread[] threads = new Thread[size];
        for (int i = 0; i < size; i++) {
            threads[i] = new Thread(() -> {
                Car car = MyProxy.proxyGet(Car.class); // 动态代理 实现 // 是真的要去触发 RPC 调用吗
                String arg = "hello" + num.incrementAndGet();
                String res = car.ooxx(arg);
                System.out.println("client over msg: " + res + " src arg: " + arg);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tetsRPC() {
        Car car = MyProxy.proxyGet(Car.class);
        Person zhangsan = car.oxox("zhangsan", 16);
        System.out.println(zhangsan);
    }

}