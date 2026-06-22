package com.hjay.socket.socket.hello;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * socket使用
 * client
 */
public class HelloClient {

	public static void main(String[] args) throws IOException {
		Socket helloSocket = null;
		BufferedReader in = null;
		try {
			helloSocket = new Socket("localhost", 9999);
			in = new BufferedReader(new InputStreamReader(helloSocket.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Don't know about host:localhost!");
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Couldn't get I/O for the connection.");
			System.exit(1);
		}
		
		System.out.println(in.readLine());
		in.close();
		helloSocket.close();
	}
}
