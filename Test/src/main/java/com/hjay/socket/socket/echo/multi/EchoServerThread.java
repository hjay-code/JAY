package com.hjay.socket.socket.echo.multi;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * socket使用
 * 多线程使用
 */
public class EchoServerThread {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		boolean listening = true;
		try {
			serverSocket = new ServerSocket(1111);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(listening) {
			new EchoMultiServerThread(serverSocket.accept()).start();
		}
		// 当监听状态为false时，服务才关闭
		serverSocket.close();
	}
}
