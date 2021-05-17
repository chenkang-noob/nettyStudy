package com.imnoob.nio;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ChannelTest {

    public static void main(String[] args) throws Exception{

        function2();
    }
    private static void function1() throws Exception{
        FileInputStream inputStream = new FileInputStream("E:\\desktop\\1.txt");
        FileChannel channel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("E:\\desktop\\2.txt");
        FileChannel channel1 = outputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        while (true){
            byteBuffer.clear();
            int read = channel.read(byteBuffer);
            if (read != -1){
                byteBuffer.flip();
                channel1.write(byteBuffer);
            }else{
                break;
            }
        }

        channel.close();
        channel1.close();
    }

    private static void function2() throws Exception {
        FileInputStream inputStream = new FileInputStream("E:\\desktop\\1.txt");
        FileChannel channel = inputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("E:\\desktop\\2.txt");
        FileChannel channel1 = outputStream.getChannel();

        channel.transferTo(0, channel.size(), channel1);

        channel.close();
        channel1.close();
    }

    private static void function3() throws Exception {
        //内存映射
        RandomAccessFile accessFile = new RandomAccessFile("E:\\desktop\\1.txt", "rw");

        FileChannel channel = accessFile.getChannel();

        /**
         * 参数一：读写模式
         * 参数而：起始位置
         * 参数三：大小
         *
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.putChar(0, 'c');
        map.putChar(1, 'k');

        accessFile.close();

    }
}
