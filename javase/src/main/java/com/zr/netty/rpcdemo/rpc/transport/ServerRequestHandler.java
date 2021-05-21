package com.zr.netty.rpcdemo.rpc.transport;

import com.zr.netty.rpcdemo.rpc.Dispatcher;
import com.zr.netty.rpcdemo.rpc.protocol.MyContent;
import com.zr.netty.rpcdemo.rpc.protocol.MyHeader;
import com.zr.netty.rpcdemo.rpc.util.Packmsg;
import com.zr.netty.rpcdemo.rpc.util.SerDerUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author zhourui
 * @Date 2021/5/19 17:19
 */
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

    Dispatcher dis;

    public ServerRequestHandler(Dispatcher dis) {
        this.dis = dis;
    }

    // provider:
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packmsg requestPkg = (Packmsg) msg;

        System.out.println("server header: " + requestPkg.getContent().getArgs()[0]);

        // 假设处理完了，要给客户端返回了

        // 因为是个RPC，要有requestID
        // 在client那一侧也要解决解码问题

        // 关注RPC通信协议，来的时候flaf 0x14141414

        // 有新的header + content
        String ioThreadName = Thread.currentThread().getName();
        // 1. 直接在当前方法，处理IO和业务和返回
        // 2. 使用netty自己的eventLoop来处理业务及返回
        // 3. 自己创建线程池
        ctx.executor().execute(new Runnable() {
            //        ctx.executor().parent().next().execute(new Runnable() {
            @Override
            public void run() {

                String serviceName = requestPkg.getContent().getName();
                String method = requestPkg.getContent().getMethodName();
                Object c = dis.get(serviceName);
                Class<?> clazz = c.getClass();
                Object res = null;
                try {
                    Method m = clazz.getMethod(method, requestPkg.getContent().getParameterTypes());
                    res = m.invoke(c, requestPkg.getContent().getArgs());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                String execThreadName = Thread.currentThread().getName();

                MyContent content = new MyContent();
//                String s = "io thread: " + ioThreadName + " exec thread: " + execThreadName + " from args: " + requestPkg.content.getArgs()[0];
                content.setRes(res);
                byte[] contentByte = SerDerUtil.ser(content);

                MyHeader resHeader = new MyHeader();
                resHeader.setRequestID(requestPkg.getHeader().getRequestID());
                resHeader.setFlag(0x14141424);
                resHeader.setDataLen(contentByte.length);

                byte[] headerByte = SerDerUtil.ser(resHeader);
                ByteBuf byteBuf = PooledByteBufAllocator.DEFAULT.directBuffer(headerByte.length + contentByte.length);

                byteBuf.writeBytes(headerByte);
                byteBuf.writeBytes(contentByte);
                ctx.writeAndFlush(byteBuf);
            }
        });

    }
}
