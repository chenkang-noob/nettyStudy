package com.imnoob.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BioServer {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(9000);

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(20, 50, 1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(100));

        while (true){
            Socket accept = serverSocket.accept();
            System.out.println("获取连接:"+accept);
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    hander(accept);
                }
            });
        }
    }

    public static void hander(Socket socket){
        System.out.println("正在通信： "+Thread.currentThread().getName());
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            while (true){
                int read = inputStream.read(bytes);
                if (read == -1){
                    System.out.println("读取完毕");
                    break;
                }else {
                    System.out.println(new String(bytes,read));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
