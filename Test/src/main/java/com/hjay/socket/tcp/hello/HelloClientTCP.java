package com.hjay.socket.tcp.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * TCP使用
 * TCP-client
 */
public class HelloClientTCP {

	public static void main(String[] args) throws Exception {
		Socket client = new Socket("localhost", 8888);
//		多线程需要发送消息
		PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
		pout.println("这是客户端...");
		pout.flush();
		BufferedReader buf = null;
		buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
		String str = buf.readLine();
		System.out.println("client接收到的内容是: " + str);
		client.close();
	}
}
