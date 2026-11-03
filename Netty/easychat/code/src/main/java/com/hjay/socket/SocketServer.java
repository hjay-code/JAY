package com.hjay.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * socket server
 */
public class SocketServer {
    private static HashMap<String, Socket> map = new HashMap<>();

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
             serverSocket = new ServerSocket(9090);
            System.out.println("服务已启动，等待客户端连接");
            while (true){// 使用死循环不停的接收不同客户端连接
                Socket socket = serverSocket.accept();
                String hostAddress = socket.getInetAddress().getHostAddress();
                int port = socket.getPort();
                System.out.println("有客户端连接，IP:" + hostAddress + "，端口:" + port);
                map.put(hostAddress + port, socket);// 使用ip+prot作为key，存放不同的客户端连接

                // 使用死循环，不停循环接收一个客户端发送消息
                new Thread(() -> {
                    while (true){
                        InputStream inputStream = null;
                        //OutputStream outputStream = null;
                        try {
                            inputStream = socket.getInputStream();
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            String readData = bufferedReader.readLine();
                            System.out.println("收到客户端消息:" + readData);

                            // 循环发给所有的客户端
                            map.forEach((k,v) ->{
                                try {
                                    OutputStream outputStream = v.getOutputStream();
                                    PrintWriter printWriter = new PrintWriter(outputStream);
                                    // 谁发送的消息就返回谁的ip+port
                                    printWriter.println("server返回"+socket.getInetAddress().getHostAddress()+socket.getPort()+":" + readData);
                                    printWriter.flush();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                }).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
