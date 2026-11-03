package com.hjay.netty.demo.netty.protocol;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 消息系列化接口
 */
public interface Serializer {

    /**
     * 反序列化方法
     * @param clazz
     * @param bytes
     * @return
     * @param <T>
     */
    <T> T deserializer(Class<T> clazz, byte[] bytes);


    /**
     * 序列化算法
     * @param object
     * @return
     * @param <T>
     */
    <T> byte[] serializer(T object);

    enum Algorithm implements Serializer{
        Java {
            @Override
            public <T> T deserializer(Class<T> clazz, byte[] bytes) {
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (T) objectInputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("反序列化失败", e);
                }

            }

            @Override
            public <T> byte[] serializer(T object) {
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(object);
                    return byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    throw new RuntimeException("序列化失败",e);
                }
            }
        },

        // 谷歌的json
        Json {
            @Override
            public <T> T deserializer(Class<T> clazz, byte[] bytes) {
                String sjon = new String(bytes, StandardCharsets.UTF_8);
                return new Gson().fromJson(sjon, clazz);
            }

            @Override
            public <T> byte[] serializer(T object) {
                String json = new Gson().toJson(object);
                return json.getBytes(StandardCharsets.UTF_8);// 最好规定字符集
            }
        }
    }
}
