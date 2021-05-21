package com.zr.netty.rpcdemo.rpc.util;

import com.zr.netty.rpcdemo.rpc.protocol.MyContent;
import com.zr.netty.rpcdemo.rpc.protocol.MyHeader;

/**
 * @Author zhourui
 * @Date 2021/5/18 16:52
 */
public class Packmsg {

    MyHeader header;
    MyContent content;

    public Packmsg(MyHeader header, MyContent content) {
        this.header = header;
        this.content = content;
    }

    public MyHeader getHeader() {
        return header;
    }

    public void setHeader(MyHeader header) {
        this.header = header;
    }

    public MyContent getContent() {
        return content;
    }

    public void setContent(MyContent content) {
        this.content = content;
    }
}
