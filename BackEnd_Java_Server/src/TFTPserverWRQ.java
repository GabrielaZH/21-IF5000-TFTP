import java.net.*;
import java.io.*;
import java.util.*;



class TFTPserverWRQ extends Thread {

	protected DatagramSocket sock;
	protected InetAddress host;
	protected int port;
	protected FileOutputStream outFile;
	protected TFTPpacket req;
	protected int timeoutLimit = 5;
	protected File saveFile;
	protected String fileName;
	protected String folderName;
	private String path = "C:\\21-IF5000-TFTP\\BackEnd_Java_Server\\images\\";

	// Initialize read request
	public TFTPserverWRQ(TFTPwrite request) throws TftpException {
		try {
			req = request;
			sock = new DatagramSocket(); // new port for transfer
			sock.setSoTimeout(1000);

			host = request.getAddress();
			port = request.getPort();
			fileName = request.fileName();


			//obtain client name
				String[] tmp = new String[2];
			    folderName="";
				for(int i=0;i<fileName.length();i++){
					if(fileName.charAt(i)=='_'){
						tmp[0]=fileName.substring(0,i);
						fileName=tmp[1]=fileName.substring(i+1,fileName.length());
						folderName=tmp[0];
						break;
					}
				}

			//create folder
			File newDirectory = new File( path+folderName);
			if(!newDirectory.exists()) {
				newDirectory.mkdir();
			}

			//save image
			saveFile = new File(path+folderName+ "\\"+fileName);


			if (!saveFile.exists()) {
				outFile = new FileOutputStream(saveFile);
				TFTPack a = new TFTPack(0);
				a.send(host, port, sock); // send ack 0 at first, ready to
											// receive
				this.start();
			} else
				throw new TftpException("access violation, file exists");

		} catch (Exception e) {
			TFTPerror ePak = new TFTPerror(1, e.getMessage()); // error code 1
			try {
				ePak.send(host, port, sock);
			} catch (Exception f) {
			}

			System.out.println("Client start failed:" + e.getMessage());
		}
	}

	public void run() {
		/*int bytesRead = TFTPpacket.maxTftpPakLen;*/
		// handle write request
		if (req instanceof TFTPwrite) {
			try {
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
								/*System.out.println("incoming data " + p.blockNumber());*/
								// check blk num
								if (/*testloss==20||*/p.blockNumber() != blkNum) { //expect to be the same
									//System.out.println("loss. testloss="+testloss+"timeoutLimit="+timeoutLimit);
									//testloss++;
									throw new SocketTimeoutException();
								}
								//write to the file and send ack
								bytesOut = p.write(outFile);
								TFTPack a = new TFTPack(blkNum);
								a.send(host, port, sock);
								//testloss++;
								break;
							}
						} catch (SocketTimeoutException t2) {
							System.out.println("Time out, resend ack");
							TFTPack a = new TFTPack(blkNum - 1);
							a.send(host, port, sock);
							timeoutLimit--;
						}
					}
					if(timeoutLimit==0){throw new Exception("Connection failed");}
				}
				System.out.println("\u001B[32mTransfer completed.(Client " +host +")\u001B[0m");
				System.out.println("Filename: "+fileName + "\nSHA1 checksum: "+CheckSum.getChecksum(path+folderName+"\\"+fileName)+"\n");
				
			} catch (Exception e) {
				TFTPerror ePak = new TFTPerror(1, e.getMessage());
				try {
					ePak.send(host, port, sock);
				} catch (Exception f) {
				}

				System.out.println("Client failed:  " + e.getMessage());
				saveFile.delete();
			}
		}
	}
}