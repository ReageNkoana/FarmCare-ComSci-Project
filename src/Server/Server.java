package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * The server class for the FarmCare application, allowing farmers as clients to connect to the server.
 * This class creates a server socket, listens for client connections, and handles each client connection
 * by creating a new thread for it.
 * 
 * @author Nkoana RRM
 * @version mini_project
 */
public class Server {

 private ServerSocket ss;
 private boolean running ;
 
 
 
 /**
  * Constructs a new Server instance with the specified port.
  *
  * @param port The port number on which the server will listen for client connections.
  */
 public Server(int port) {
	 try {
		 ss= new ServerSocket(port);
		 running=true;
		 startServer();
		 
	 }catch(IOException ex) {
		 ex.printStackTrace();
	 }
	 
 }
 
 
 
 /**
  * Starts the server, listening for client connections and handling each connection in a separate thread.
  */
	private void startServer() {
	System.out.println("Starting server...");
	
	while(running) {
	try {
		Socket s= ss.accept();
		System.out.println("New connected client");
		FarmHandler fh= new FarmHandler(s);
		
		Thread t= new Thread(fh);
		t.start();
		
	} catch(IOException ex) {
		 ex.printStackTrace();
	 }
	}
	
}

	
	/**
     * The main method to start the server on a specified port.
     *
     * @param args Command-line arguments (not used).
     */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			Server fh= new Server(2024);
	}

}
