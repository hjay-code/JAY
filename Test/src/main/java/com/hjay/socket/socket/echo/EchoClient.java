package com.hjay.socket.socket.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * socket使用
 * echo样例
 */
public class EchoClient {

	public static void main(String[] args) throws IOException {
		Socket echoSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			echoSocket = new Socket("localhost", 1111);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(in.readLine());
		System.out.println(in.readLine());
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		String userInput;
		while((userInput = stdIn.readLine())!= null) {
			out.println(userInput);
			System.out.println(in.readLine());
		}
		
		out.close();
		in.close();
		echoSocket.close();
	}
}
