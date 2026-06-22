package com.hjay.socket.socket.hello;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket使用
 * server
 */
public class HelloServer {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		PrintWriter out = null;
		try {
			// 实例化server
			serverSocket = new ServerSocket(9999);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Could not listen on port:9999");
			System.exit(1);
		}
		
		Socket clientSocket = null;
		try {
			// 监听客户端连接
			clientSocket = serverSocket.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Accept failed");
			System.exit(1);
		}
		
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		out.println("hello world!");
		clientSocket.close();
		serverSocket.close();
	}
}
