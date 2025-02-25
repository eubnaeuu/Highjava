package kr.or.ddit.tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MultichatServer {
	// 대화명, 클라이언트의 Socket을 저장하기 위한 Map변수 선언
	Map<String, Socket> clients;
	
	// 생성자
	public MultichatServer() {
		// 동기화 처리가 가능하도록 Map객체 생성
		clients = Collections.synchronizedMap(
				new HashMap<String, Socket>());
	}

	// 서버 시작
	
	public void startServer() {
		Socket socket = null;
		
		// try with resources 문법
		try(ServerSocket serverSocket = new ServerSocket(7777)) {
			System.out.print("서버가 시작되었습니다");
			
			while(true) {  // 무한루프 :  복수의 접속자를 받아야 하기에
				// 클라이언트의 접속을 대기한다
				
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() 
				+ " : " + socket.getPort() + " ] 에서 접속하였습니다");

						// 메시지를 전송 처리하는 스레드 생성 및 실행
				ServerReceiver receiver = new ServerReceiver(socket);
				receiver.start();
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 대화방 즉, Map에 저장된 전체 유저에게 안내메시지를 전송하는 메서드 (msg만 출력)
	 * @param msg
	 */
	public void sendMessage(String msg) {
		// Map에 저장된 유저의 대화명 리스트 추출(key값 구하기)
		Iterator<String > it = clients.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				String name = it.next(); // 대화명
				
				// 대화명에 해당하는 Socket의 OuputStream 구하기
				DataOutputStream dos = new DataOutputStream(clients.get(name).getOutputStream());
				dos.writeUTF(msg); // 메시지 보내기
				
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * 대화방 즉, Map에 저장된 전체 유저에게 안내메시지를 전송하는 메서드 (name + msg 출력)
	 * @param msg
	 * @param from 작성자이름
	 */
	public void sendMessage(String msg, String from) {
		// Map에 저장된 유저의 대화명 리스트 추출(key값 구하기)
		Iterator<String > it = clients.keySet().iterator();
		
		while(it.hasNext()) {
			try {
				String name = it.next(); // 대화명
				
				// 대화명에 해당하는 Socket의 OuputStream 구하기        (client.get(name) : name에 해당하는 value값)
				DataOutputStream dos = new DataOutputStream(clients.get(name).getOutputStream());
				dos.writeUTF("["+from+"]"+ msg); // 메시지 보내기
				
			}catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	// 서버에서 클라이언트로 메시지를 전송할 스레드를 Inner클래스로 정의
	// => Inner클래스에서는 부모 클래스의 멤버들을 직접 사용할 수 있다.
	class ServerReceiver extends Thread {    
		private Socket socket;
		private DataInputStream dis;
		private String name;

		public ServerReceiver(Socket socket) { // ★ 내부클래스(?)
			this.socket = socket;
			try {
				
				// 수신용
				dis = new DataInputStream(socket.getInputStream());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			try {
				//서버에서는 클라이언트가 보내는 최초의 메시지 즉, 대화명을 수신해야한다
				name = dis.readUTF();
				// 대화명을 받아서 다른 모든 클라이언트에게 대화방 참여 메시지를 보낸다
				sendMessage("#" + name + "님이 입장했습니다.");
				
				// 대화명과 소켓정보를 Map에 저장한다
				clients.put(name, socket);
				System.out.println("현재 서버 접속자 수는 " + clients.size() + "명 입니다");
				
				// 이 이후의 메시지 처리는 반복문으로 처리한다
				// 한 클라이언트가 보낸 메시지를 다른 모든 클라이언트에게 보내준다
				while(dis != null ) {
					sendMessage(dis.readUTF(), name);
				}
			}catch(IOException ex){
				ex.printStackTrace();
			} finally {
				// 이 finally영역이 실행된다는 것은 클라이언트의 접속이 종료되었다는 의미
				sendMessage(name + "님이 나가셨습니다");
				// Map에서 해당 대화명을 삭제한다
				clients.remove(name);
				System.out.println("[" + socket.getInetAddress() + " :  " + socket.getPort() + "] 에서 접속을 종료했습니다");
				System.out.println("현재 접속자 수는 " + clients.size() + " 명입니다");
			}
		}
	}

	public static void main(String[] args) {
		new MultichatServer().startServer();
	}
}
