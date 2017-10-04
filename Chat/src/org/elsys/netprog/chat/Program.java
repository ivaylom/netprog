package org.elsys.netprog.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Program {

	public static void main(String[] args) throws Exception {
		//server [port]
		//client [address] [port]
		
		if (args.length < 2 || args.length > 3) {
			throw new Exception("Invalid argument count");
		}
		
		if (args[0].equals("server")) {
			int port = Integer.parseInt(args[1]);
			ServerSocket serverSocket = new ServerSocket(port);
		    Socket clientSocket = serverSocket.accept();
		    
		    listenOnSocket(clientSocket);
		    listenOnConsole(clientSocket);
		} else if (args[0].equals("client")) {
			String address = args[1];
			int port = Integer.parseInt(args[2]);
			
			Socket socket = new Socket(address, port);
			
			listenOnSocket(socket);
			listenOnConsole(socket);
		} else {
			throw new Exception("Argument[0] must be 'server' or 'client'.");
		}
		
	}
	
	private static void listenOnSocket(Socket socket) throws IOException {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					BufferedReader in = new BufferedReader(
				            new InputStreamReader(socket.getInputStream()));
				
					String input;
					
				    while ((input = in.readLine()) != null) {
				        System.out.println(input);
				    }
				}
				catch (Throwable t) {
					
				}
				
			}});
		thread.start();
	}
	
	private static void listenOnConsole(Socket socket) throws IOException {
		BufferedReader stdIn = new BufferedReader(
	            new InputStreamReader(System.in));
		
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		String userInput;
		while ((userInput = stdIn.readLine()) != null) {
	        out.println(userInput);
	    }
	}

}
