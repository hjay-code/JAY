package com.hjay.socket.tcp.hello;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP使用
 * TCP-server
 */
public class HelloServerTCP {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		Socket client = null;
		System.out.println("等待客户端进行连接...");
		client = server.accept();
		OutputStream out = client.getOutputStream();
		PrintStream pout = new PrintStream(out);
		pout.println("Hello World!");
		
		pout.close();
		out.close();
		client.close();
		server.close();
	}
}
