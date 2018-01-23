package Multy_Thread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MTServer {
	private static ServerSocket server_Socket        = null;
	private static Socket       client_Socket        = null;
	private static final int    MAXIMUM_CLIENT_COUNT = 10;
	private static final ClientThread[] threads      = new ClientThread[MAXIMUM_CLIENT_COUNT];


	public void main(String[] args) {
		int port_Number = 3333;
		if(args.length < 1) {
			System.out.println("Usage: java MultiThreadChatServer <portNumber>\n"
					+ "Now using port number=" + port_Number);
		} else {
			port_Number = Integer.valueOf(args[0]).intValue();
		}

		try {
			server_Socket = new ServerSocket(port_Number);
		}catch(IOException e) {
			e.printStackTrace();
		}


		while(true) {
			try {
				client_Socket = server_Socket.accept();
				int i = 0;
				for(i = 0; i < MAXIMUM_CLIENT_COUNT; i++) {
					if(threads[i] == null) {
						(threads[i] = new ClientThread(client_Socket, threads)).start();
						break;
					}
				}
				if(i == MAXIMUM_CLIENT_COUNT) {
					PrintStream dos = new PrintStream(client_Socket.getOutputStream());
					dos.println("No empty server space.Try later.");
					dos.close();
					client_Socket.close();
				}
			}catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}

	class ClientThread extends Thread {

		  private DataInputStream is = null;
		  private PrintStream os = null;
		  private Socket clientSocket = null;
		  private final ClientThread[] threads;
		  private int maxClientsCount;

		  public ClientThread(Socket clientSocket, ClientThread[] threads) {
		    this.clientSocket = clientSocket;
		    this.threads = threads;
		    maxClientsCount = threads.length;
		  }

		  public void run() {
		    int maxClientsCount = this.maxClientsCount;
		    ClientThread[] threads = this.threads;

		    try {
		     
		      is = new DataInputStream(clientSocket.getInputStream());
		      os = new PrintStream(clientSocket.getOutputStream());
		      os.println("Enter your name.");
		      String name = is.readLine().trim();
		      os.println("Hello " + name
		          + " to our chat room.\nTo leave enter /quit in a new line");
		      for (int i = 0; i < maxClientsCount; i++) {
		        if (threads[i] != null && threads[i] != this) {
		          threads[i].os.println("*** A new user " + name
		              + " entered the chat room !!! ***");
		        }
		      }
		      while (true) {
		        String line = is.readLine();
		        if (line.startsWith("/quit")) {
		          break;
		        }
		        for (int i = 0; i < maxClientsCount; i++) {
		          if (threads[i] != null) {
		            threads[i].os.println("<" + name + "&gr; " + line);
		          }
		        }
		      }
		      for (int i = 0; i < maxClientsCount; i++) {
		        if (threads[i] != null && threads[i] != this) {
		          threads[i].os.println("*** The user " + name
		              + " is leaving the chat room !!! ***");
		        }
		      }
		      os.println("*** Bye " + name + " ***");

		     
		      for (int i = 0; i < maxClientsCount; i++) {
		        if (threads[i] == this) {
		          threads[i] = null;
		        }
		      }

		      
		      is.close();
		      os.close();
		      clientSocket.close();
		    } catch (IOException e) {
		    }
		  }
		}
	
