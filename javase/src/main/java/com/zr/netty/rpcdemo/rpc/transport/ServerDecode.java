package com.zr.netty.rpcdemo.rpc.transport;

import com.zr.netty.rpcdemo.rpc.protocol.MyContent;
import com.zr.netty.rpcdemo.rpc.protocol.MyHeader;
import com.zr.netty.rpcdemo.rpc.util.Packmsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @Author zhourui
 * @Date 2021/5/19 17:18
 */
public class ServerDecode extends ByteToMessageDecoder {

    // 父类一定有 channelread -> bytebuf
    //
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> out) throws Exception {

        while (buf.readableBytes() >= 112) {

            byte[] bytes = new byte[112];
            buf.getBytes(buf.readerIndex(), bytes); // 从哪里读，读多少 readIndex不变
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(in);
            MyHeader header = (MyHeader) oin.readObject();

            // Decode在两个方向都使用
            // 通信的协议
            if (buf.readableBytes() - 112 >= header.getDataLen()) {
                // 处理指针
                buf.readBytes(112); // 移动指针到body开始位置
                byte[] data = new byte[(int) header.getDataLen()];
                buf.readBytes(data);
                ByteArrayInputStream bin = new ByteArrayInputStream(data);
                ObjectInputStream doin = new ObjectInputStream(bin);

                if (header.getFlag() == 0x14141414) {
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new Packmsg(header, content));
                } else if (header.getFlag() == 0x14141424) {
                    MyContent content = (MyContent) doin.readObject();
                    out.add(new Packmsg(header, content));
                }

            } else {
                break;
            }
        }
    }
}
