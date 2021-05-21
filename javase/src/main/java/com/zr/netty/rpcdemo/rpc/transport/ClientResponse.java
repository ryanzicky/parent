package com.zr.netty.rpcdemo.rpc.transport;

import com.zr.netty.rpcdemo.rpc.ResponseMappingCallback;
import com.zr.netty.rpcdemo.rpc.util.Packmsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author zhourui
 * @Date 2021/5/19 17:20
 */
public class ClientResponse extends ChannelInboundHandlerAdapter {

    // consumer
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packmsg responsePkg = (Packmsg) msg;

        ResponseMappingCallback.runCallBack(responsePkg);
    }
}
