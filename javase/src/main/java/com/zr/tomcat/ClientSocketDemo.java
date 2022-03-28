package com.zr.tomcat;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author zhourui
 * @Date 2021/6/8 10:48
 */
public class ClientSocketDemo {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 9999);

            OutputStream outputStream = socket.getOutputStream();

            int i = 1;
            while (true) {
                System.out.println(++i);
                outputStream.write("1".getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
