package com.example.client.clientTFTP;

import java.net.InetAddress;
import java.net.UnknownHostException;
class UseException extends Exception {
	public UseException() {
		super();
	}

	public UseException(String s) {
		super(s);
	}
}

public class TFTPClient {
	public void executeClient(String host, String fileName, String mode, String type) {
		mode = mode.isEmpty()? "octet": mode; //default mode

		try {
			InetAddress server = InetAddress.getByName(host);

			//process read request
			if(type.matches("R")){
				TFTPclientRRQ r = new TFTPclientRRQ(server, fileName, mode);}
			//process write request
			else if(type.matches("W")){
				TFTPclientWRQ w = new TFTPclientWRQ(server, fileName, mode);
			}
			else{throw new UseException("wrong command. \n--Usage-- \n [Type(R/W?)]");}

		} catch (UnknownHostException e) {
			System.out.println("Unknown host " + host);
		} catch (UseException e) {
			System.out.println(e.getMessage());
		}
	}
}