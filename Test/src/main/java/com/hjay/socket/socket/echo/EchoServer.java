package com.hjay.socket.socket.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * socket使用
 * echo样例
 */
public class EchoServer {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			serverSocket = new ServerSocket(1111);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Socket incoming = null;
		while(true) {
			incoming = serverSocket.accept();
			out = new PrintWriter(incoming.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			
			out.println("Hello! ...");
			out.println("Enter BYE to exit");
			out.flush();
			
			while(true) {
				String str = in.readLine();
				if(str == null) {
					break;
				}else {
					out.println("Echo:" + str);
					out.flush();
					if(str.trim().equalsIgnoreCase("BYE")) {
						break;
					}
				}
				
			}
			out.close();
			in.close();
			incoming.close();
			serverSocket.close();
		}
	}
}
