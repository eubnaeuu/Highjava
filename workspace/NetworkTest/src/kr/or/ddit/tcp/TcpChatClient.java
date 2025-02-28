package kr.or.ddit.tcp;

import java.io.IOException;
import java.net.Socket;

public class TcpChatClient {
	public static void main(String[] args) {
		Socket socket = null;
			try {
				// 127.0.0.1
				// localhost
				
				socket = new Socket("localhost", 7777);
				Sender sender = new Sender(socket);
				Receiver receiver = new Receiver(socket);
				
				sender.start();
				receiver.start();
				
			}catch(IOException ex) {
				ex.printStackTrace();
			}
	}
}
