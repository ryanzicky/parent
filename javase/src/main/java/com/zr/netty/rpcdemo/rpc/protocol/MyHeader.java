package com.zr.netty.rpcdemo.rpc.protocol;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author zhourui
 * @Date 2021/5/19 15:45
 */
public class MyHeader implements Serializable {
    // 通信上的协议
    /**
     * 1. ooxx值
     * 2. uuid
     * 3. data_len
     */
    int flag; // 32bit可以设置很多信息
    long requestID;
    long dataLen;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getRequestID() {
        return requestID;
    }

    public void setRequestID(long requestID) {
        this.requestID = requestID;
    }

    public long getDataLen() {
        return dataLen;
    }

    public void setDataLen(long dataLen) {
        this.dataLen = dataLen;
    }

    public static MyHeader createHeader(byte[] msg) {
        MyHeader header = new MyHeader();
        int size = msg.length;
        int f = 0x14141414;
        long requestID = Math.abs(UUID.randomUUID().getLeastSignificantBits());
        // 0x14 0000 0000
        header.setFlag(f);
        header.setDataLen(size);
        header.setRequestID(requestID);

        return header;
    }
}
