package com.hjay.netty.demo.heima.c3;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class TestFileChannelTransferTo {

    public static void main(String[] args) {
        try(
                FileChannel from = new FileInputStream("D:\\HJay\\Java\\Netty\\hello-netty\\src\\main\\resources\\demo\\data.txt").getChannel();
                FileChannel to = new FileOutputStream("D:\\HJay\\Java\\Netty\\hello-netty\\src\\main\\resources\\demo\\to.txt").getChannel();
                ){
            // 效率较低 但是jdk新版本更新了流相关，可能效率提高了已经
            // from.transferTo(0, from.size(), to);

            // 提高效率,底层利用操作系统的零拷贝进行优化，建议使用大数据文件进行测试
            long size = from.size();
            for(long left = size; left >0;){
                System.out.println("position:" + (size - left) + " left:" + left);
                left -= from.transferTo((size - left), left, to);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
