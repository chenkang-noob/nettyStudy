package com.imnoob.nio.groupChat;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {

    private Selector selector;

    private ServerSocketChannel channel;

    private final Integer PORT = 9000;

    public ChatServer() {
        try {
            selector = Selector.open();

            channel = ServerSocketChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(PORT));
            channel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("服务器初始化成功");
        }catch (Exception e){
            System.out.println("服务器初始化失败");
            e.printStackTrace();
        }
    }

    public void serverRun() throws Exception{
        System.out.println("启动成功");
        while (selector.select() > 0){
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> ite = keys.iterator();

            while (ite.hasNext()){
                SelectionKey next = ite.next();

                if (next.isAcceptable()){
                    SocketChannel accept = channel.accept();
                    accept.configureBlocking(false);
                    accept.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(1024));
                }else if (next.isReadable()){
                    readMsg(next);
                }

                ite.remove();
            }


        }
    }

    public void readMsg(SelectionKey key) throws Exception{
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel channel = (SocketChannel) key.channel();

        StringBuilder msgs = new StringBuilder();
        int len = 0;
        while ((len = channel.read(buffer)) > 0){
            buffer.flip();
            msgs.append(new String(buffer.array()));
            buffer.clear();
        }
        if (len == -1){
            channel.close();
            return;
        }
        String src = channel.getRemoteAddress().toString();
        System.out.println("客户端:"+src+"发生消息"+msgs);

        sendMsg(channel,msgs.toString());
    }

    public void sendMsg(Channel self,String msg) throws Exception {
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey item : keys) {
            Channel tmp = item.channel();

            if (tmp instanceof SocketChannel && tmp != self && tmp != channel){
                SocketChannel dest = (SocketChannel) tmp;
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                dest.write(wrap);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        ChatServer chatServer = new ChatServer();

        chatServer.serverRun();
    }
}
