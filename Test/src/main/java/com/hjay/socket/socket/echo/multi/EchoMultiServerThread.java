package com.hjay.socket.socket.echo.multi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * socket使用
 * 多线程处理多客户
 */
public class EchoMultiServerThread extends Thread{

	private Socket socket = null;
	
	public EchoMultiServerThread(Socket socket) {
		super("EchoMultiServerThread");
		this.socket = socket;
	}
	
	@Override
	public void run() {
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(this.socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out.println("Hello!");
			out.println("Enter BYE to exit");
			out.flush();
			
			while(true) {
				String str = in.readLine();
				if(str == null) {
					break;
				}else {
					out.println("Echo: " + str);
					out.flush();
					if(str.trim().equalsIgnoreCase("BYE")) {
						break;
					}
				}
			}
			out.close();
			in.close();
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
