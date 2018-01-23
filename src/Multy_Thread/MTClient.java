package Multy_Thread;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
public class MTClient implements Runnable {
	private static Socket          client_Socket  = null;
	private static DataInputStream dis            = null;
	private static PrintStream     dos            = null;
	private static BufferedReader  input_Line     = null;
	private static boolean         closed         = false;

	public static void main(String[] args) {
		int port_Number = 6669;
		String host = "localhost";

		if(args.length < 2) {
			System.out.println("Usage: java MultiThreadChatClient <host> <portNumber>\n"
					+ "Now using host=" + host 
					+ ", portNumber="   + port_Number);
		}else {
			host = args[0];
			port_Number = Integer.valueOf(args[1]).intValue();
		}

		try {
			client_Socket = new Socket(host, port_Number);
			input_Line    = new BufferedReader(new InputStreamReader(System.in));
			dos           = new PrintStream(client_Socket.getOutputStream());
			dis           = new DataInputStream(client_Socket.getInputStream());

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host "
					+ host);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to the host "
					+ host);
		}
		if(client_Socket != null && 
				dos != null && 
				dis != null) {
			try {
				new Thread(new MTClient()).start();
				while(!closed) {
					dos.close();
					dis.close();
					client_Socket.close();
				}
			}catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}
	}
	public void run() {
		String response_line;
		try {
			while((response_line = dis.readLine()) != null) {
				System.out.println(response_line);
				if(response_line.indexOf("*** Bye") != -1) 
					break;
			}
			closed = true;
		}catch (IOException e) {
			System.err.println("IOException:  " + e);
		}
	}
}
