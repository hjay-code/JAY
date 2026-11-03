package com.hjay.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * socket client
 */
public class SocketClient {
    public static void main(String[] args) {
        Socket client = null;
        try {
            client = new Socket("localhost", 9090);
            InputStream inputStream = client.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            OutputStream outputStream = client.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            System.out.println("请输入要发送的内容");
            new Thread(() -> {
                while (true){
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.next();
                    printWriter.println(input);
                    printWriter.flush();
                }

            }).start();
            new Thread(() -> {
                while (true){
                    try {
                        String readData = bufferedReader.readLine();
                        System.out.println(readData);
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
