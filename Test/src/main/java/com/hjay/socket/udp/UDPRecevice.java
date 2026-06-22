package com.hjay.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * UDP使用
 * 不使用socket-stream，使用DatagramSocket
 */
public class UDPRecevice {

	public static void main(String[] args) {
		DatagramSocket ds = null;
		byte[] buf = new byte[1024];
		DatagramPacket dp = null;
		try {
			ds = new DatagramSocket(9000);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// dp初始化最简单的方式，只需要一个字节数组用来保存数据包信息和字节数组长度
		// 端口信息等可以再在后面进行set
		dp = new DatagramPacket(buf, 1024);
		
		try {
			ds.receive(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String str = new String(dp.getData(), 0, dp.getLength()) + "from " + dp.getAddress().getHostAddress() + ":" + dp.getPort();
		
		System.out.println(str);
		ds.close();
	}
}
