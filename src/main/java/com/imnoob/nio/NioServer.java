package com.imnoob.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {

    public static void main(String[] args) throws Exception{
        ServerSocketChannel channel = ServerSocketChannel.open();

        channel.configureBlocking(false);

        Selector selector = Selector.open();

        channel.socket().bind(new InetSocketAddress(9000));

        channel.register(selector, SelectionKey.OP_ACCEPT);

        while (selector.select() > 0){
            Set<SelectionKey> keys = selector.selectedKeys();

            Iterator<SelectionKey> ite = keys.iterator();
            while (ite.hasNext()){
                SelectionKey next = ite.next();

                if (next.isAcceptable()){
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("客户端连接成功 生成了一个 socketChannel " + socketChannel.hashCode());
                    //将  SocketChannel 设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //将socketChannel 注册到selector, 关注事件为 OP_READ， 同时给socketChannel
                    //关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                    System.out.println("客户端连接后 ，注册的selectionkey 数量=" + selector.keys().size()); //2,3,4..

                }else if (next.isReadable()){
                    SocketChannel channel1 = (SocketChannel) next.channel();
                    ByteBuffer buffer = (ByteBuffer) next.attachment();
                    System.out.println("有读取事件发生");
                    int len = 0;
                    while (( len= channel1.read(buffer)) > 0){
                        buffer.flip();
                        System.out.println("消息："+new String(buffer.array()));
                        buffer.clear();
                    }

                }
                ite.remove();

            }
        }


    }
}
