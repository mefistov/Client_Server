package EchoServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
@SuppressWarnings("deprecation")
public void main(String[] args) {
	ServerSocket echo_Server = null;
	String line;
	DataInputStream dis      = null;
	PrintStream dos          = null;
	Socket client_Socket     = null;
	
	try {
		echo_Server = new ServerSocket(6666);
		
	}catch(IOException e) {
		e.printStackTrace();
	}
	
	System.out.println("Server had started. To stop it press <CTRL><C>.");
	try {
		client_Socket = echo_Server.accept();
		dis           = new DataInputStream(client_Socket.getInputStream());
		dos           = new PrintStream(client_Socket.getOutputStream());
		
		while(true) {
			line = dis.readLine();
			dos.println("From Server with love" + line);
		}
	}catch(IOException e) {
		e.printStackTrace();
	}
}
}
