package com.zr.tomcat;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author zhourui
 * @Date 2021/6/8 10:48
 */
public class ServerSocketDemo {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9999);

            Socket socket = serverSocket.accept();

            System.in.read();

            InputStream inputStream = socket.getInputStream();
            while (true) {
                byte[] bytes = new byte[1000];
                int n = inputStream.read(bytes);

                System.out.println(new String(bytes, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
