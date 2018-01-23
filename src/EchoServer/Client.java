package EchoServer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Socket client_Socket       = null;
		DataInputStream dis        = null;
		PrintStream dos            = null;
		DataInputStream input_Line = null;

		try {
			client_Socket = new Socket("localhost", 6666);
			dos           = new PrintStream(client_Socket.getOutputStream());
			dis           = new DataInputStream(client_Socket.getInputStream());
			input_Line    = new DataInputStream(new BufferedInputStream(System.in));

		}catch(UnknownHostException ee) {
			System.err.println("Do not know info about host");
		}catch(IOException e) {
			System.err.println("Couldn't get I/O for the connection to host");
		}


		if(client_Socket != null && 
				dos      != null && 
				dis      != null) {
			try {
				System.out.println("Client started. Type any text. To quit it type 'OK'.");
				String response_Line;
				dos.println(input_Line.readLine());
				while((response_Line = dis.readLine()) != null) {
					System.out.println(response_Line);
					if(response_Line.indexOf("OK") != -1) {
						break;
					}
					dos.println(input_Line.readLine());
				}
				dos.close();
				dis.close();
				client_Socket.close();
			}catch(UnknownHostException ee) {
				System.err.println("Do not know info about host");
			}catch(IOException e) {
				System.err.println("Couldn't get I/O for the connection to host");
			}
		}
	}
}
