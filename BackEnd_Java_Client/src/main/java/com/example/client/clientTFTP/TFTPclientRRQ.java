package com.example.client.clientTFTP;

import java.lang.reflect.Field;
import java.net.*;
import java.security.MessageDigest;
import java.io.*;
import java.util.*;
import java.util.zip.Checksum;

class TFTPclientRRQ {
	protected InetAddress server;
	protected String fileName;
	protected String dataMode;
	protected  String path ="C:\\21-IF5000-TFTP\\BackEnd_Java_Client\\src\\main\\java\\com\\example\\client\\images\\";
	protected TFTPdata[] imageBytes;
	protected FileOutputStream outFile;
	public TFTPclientRRQ(InetAddress ip, String name, String mode) {
		server = ip;
		fileName = name;
		dataMode = mode;
		imageBytes = new TFTPdata[1000];


		try {// Create socket and open output file
			DatagramSocket sock = new DatagramSocket();
			sock.setSoTimeout(2000); // set time out to 2s

			outFile = new FileOutputStream(path+fileName); //parent folder
			// Send request to server
			TFTPread reqPak = new TFTPread(fileName, dataMode);
			reqPak.send(server, 6973, sock);

			TFTPack ack = null;
			InetAddress newIP = server; // for transfer
			int newPort = 0; // for transfer
			int timeoutLimit = 5;
			int testloss = 1; // test only

			// Process the transfer
			System.out.println("\u001B[34mDownloading\u001B[0m");
			for (int blkNum = 1, bytesOut = 512; bytesOut == 512; blkNum++) {
				while (timeoutLimit != 0) {
					try {
						TFTPpacket inPak = TFTPpacket.receive(sock);
						//check packet type
						if (inPak instanceof TFTPerror) {
							TFTPerror p = (TFTPerror) inPak;
							throw new TftpException(p.message());
						} else if (inPak instanceof TFTPdata) {
							TFTPdata p = (TFTPdata) inPak;

							// visual effect to user
							if (blkNum % 500 == 0) {
								System.out.print("\b.>");
							}
							if (blkNum % 15000 == 0) {
								System.out.println("\b.");
							}

							newIP = p.getAddress();
							// check port num.
							if (newPort != 0 && newPort != p.getPort()) { // wrong port
								continue; // ignore this packet
							}
							newPort = p.getPort();
							// check block num.

//							if (blkNum != p.blockNumber()) { //old data
//								throw new SocketTimeoutException();
//							}

							// everything is fine then write to the file
							bytesOut = p.write(outFile);
//							// send ack to the server
							ack = new TFTPack(p.blockNumber());
							ack.send(newIP, newPort, sock);

							break;
						} else
							throw new TftpException("Unexpected response from server");
					}
					// #######handle time out
					catch (SocketTimeoutException t) {
						// no response to read request, try again
						if (blkNum == 1) {
							System.out.println("failed to reach the server");
							reqPak.send(server, 6973, sock);
							timeoutLimit--;
						}
						// no response to the last ack
						else {
							System.out.println("connecion time out, resend last ack. timeoutlimit left=" + timeoutLimit);
							ack = new TFTPack(blkNum - 1);
							ack.send(newIP, newPort, sock);
							timeoutLimit--;
						}
					}
				}
				if (timeoutLimit == 0) {
					throw new TftpException("Connection failed");
				}
			}
			System.out.println("\u001B[32m\nDownload Finished.\u001B[0m\nFilename: " + fileName);
			System.out.println("SHA1 Checksum: " + CheckSum.getChecksum("C:\\21-IF5000-TFTP\\BackEnd_Java_Client\\src\\main\\java\\com\\example\\client\\images\\"+fileName));

			outFile.close();

			sock.close();
		} catch (IOException e) {
			System.out.println("IO error, transfer aborted");
			File wrongFile = new File(fileName);
			wrongFile.delete();
		} catch (TftpException e) {
			System.out.println(e.getMessage());
			File wrongFile = new File(fileName);
			wrongFile.delete();
		}
	}
}