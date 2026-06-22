package com.hjay.socket.tcp.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * TCP多线程客户端
 */
public class EchoTCPThread implements Runnable{
	
	private Socket client;
	
	public EchoTCPThread(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintStream pout = new PrintStream(client.getOutputStream());
			boolean temp = true;
			String str;
			while(temp) {// 循环接收消息
				str = buf.readLine();// 此处客户端如果不发送消息会卡死
				if(str == null || "".equals(str)) {
					System.out.println("客户端发送内容为空...");
					temp = false;
				}else if("bye".equals(str)) {
					System.out.println("客户端取消连接...");
					temp = false;
				}else {
					pout.println("Echo:" + str);
				}
			}
			System.out.println("TCP-SERVRE关闭连接...");
			pout.close();
			client.close();
		} catch (IOException e) {
			System.out.println("TCP-SERVER接收客户端消息出错...");
		}
	}

}
