package com.hjay.sdp.create.single;

import java.io.*;

/**
 * 解决序列化方式破坏单例
 */
public class BrokenSingletonSerializeResolve {

    public static void main(String[] args) throws Exception {
        writeObjectToFile();
        // 每次反序列化出来的都是新的对象
        SingletonSerializeResolve instance = readObjectFromFile();
        SingletonSerializeResolve instance2 = readObjectFromFile();
        System.out.println(instance == instance2);
    }

    public static final String path = "D:\\HJay\\code\\HJAY\\JAY\\Test\\src\\main\\java\\com\\hjay\\sdp\\create\\single\\BrokenSingletonReflect.txt";


    public static SingletonSerializeResolve readObjectFromFile() throws Exception {
        // 创建输入流对象
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
        // 读取对象
        SingletonSerializeResolve object = (SingletonSerializeResolve) inputStream.readObject();

        // 释放资源
        inputStream.close();
        return object;
    }


    /**
     * 向文件中写入
     * @throws IOException
     */
    public static void writeObjectToFile() throws IOException {
        // 1. 获取对象
        SingletonSerializeResolve instance = SingletonSerializeResolve.getInstance();
        // 2.创建输出流
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
        // 3. 写
        outputStream.writeObject(instance);
        // 4. 释放资源
        outputStream.close();

    }
}
