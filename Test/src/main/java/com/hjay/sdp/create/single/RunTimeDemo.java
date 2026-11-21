package com.hjay.sdp.create.single;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * JDK 中的Runtime 就是饿汉式单例模式
 */
public class RunTimeDemo {

    public static void main(String[] args) throws Exception {
        // 获取对象
        Runtime runtime = Runtime.getRuntime();

        // 调用runtime 方法，参数为 指令
        Process exec = runtime.exec("ipconfig");
        // 等待执行完成
        exec.waitFor();
        InputStream inputStream = exec.getInputStream();
        // cmd 默认 gbk
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        inputStream.close();
        inputStreamReader.close();
        bufferedReader.close();
        exec.destroy();
    }
}
