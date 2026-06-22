package com.hjay.socket.tcp.thread;

import java.net.ServerSocket;

/**
 * TCP-SERVER
 */
public class EchoTCPServer {

	public static void main(String[] args) throws Exception {
		ServerSocket server = new ServerSocket(8888);
		boolean flag = true;
		while(flag) {
			System.out.println("等待客户端连接...");
			new Thread(new EchoTCPThread(server.accept())).start();
		}
		server.close();
	}
}
