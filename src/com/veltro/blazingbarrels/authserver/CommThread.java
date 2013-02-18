package com.veltro.blazingbarrels.authserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Thread for the transfer of account authorization DatagramPackets
 * 
 * @author LinearLogic
 * @since 0.0.4
 */
public class CommThread extends Thread {

	private DatagramSocket socket = null;

	/**
	 * Constructs the Thread superclass and attempts to open a DatagramSocket on the specified port
	 * 
	 * @param port The port on which to receive packets from BlazingBarrels clients and BBGameServer instances
	 */
	public CommThread(int port) {
		super("CommThread");
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			System.err.println("FAILED TO BIND TO PORT " + port);
			e.printStackTrace();
		}
	}

	/**
	 * Runs the thread, receiving auth packets and creating/dispatching response packets
	 */
	public void run() {
		while (true) {
			try {
				byte[] buffer = new byte[256];
		
		        // Receive authorization-related packet from BB gameserver or client
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				String[] info = new String(packet.getData(), 0, packet.getLength()).split("\\s+");
				String response = null;

				switch(info.length) {
					case 1: // Packet contains a BBGameServer instance's query about an account's authorization status
						response = (AccountManager.isAuthorized(info[0])) ? "isauth " + info[0] + " 1" : "isauth " + info[0] + " 0";
						break;
					case 2: // A client is requesting the deauthorization of an account
						if (info[0].equals("deauth"))
							AccountManager.deauthorize(info[1]);
						break;
					case 3: // A client is attempting to authorize an account
						if (info[0].equals("auth")) {
							if (info[2].equals("p@ssw0rd")) {
								AccountManager.authorize(info[1]);
								response = "auth 1";
							} else
								response = "auth 0";
						}
						break;
					default:
						break;
				}

				if (response == null) // There is no response packet to be sent
					continue;

				// Create and send a response packet
		        buffer = response.getBytes();
		        InetAddress address = packet.getAddress();
		        int port = packet.getPort();
		        packet = new DatagramPacket(buffer, buffer.length, address, port);
		        socket.send(packet);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
