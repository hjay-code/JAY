package com.hjay.socket.udp;

import java.io.IOException;
import java.net.*;

/**
 *  * UDP使用
 * 不使用socket-stream，使用DatagramSocket
 */
public class UDPSend {

	public static void main(String[] args) {
		DatagramSocket ds = null;
		DatagramPacket dp = null;
		
		try {
			ds = new DatagramSocket(3000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String str = "Hello World!";
		try {
			// 定义了数据包的内容，长度，目标ip地址和端口号
			dp = new DatagramPacket(str.getBytes(), str.length(), InetAddress.getByName("localhost"), 9000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ds.close();
	}
}
