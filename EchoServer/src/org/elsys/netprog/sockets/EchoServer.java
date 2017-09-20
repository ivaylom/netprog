package org.elsys.netprog.sockets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(10001);
		    Socket clientSocket = serverSocket.accept();
		    System.out.println("client connected from " +
		    		clientSocket.getInetAddress());
		    PrintWriter out =
		        new PrintWriter(clientSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(clientSocket.getInputStream()));
		    
		    String inputLine;

		    while ((inputLine = in.readLine()) != null) {
		    	try
		    		{
			    		String[] split = inputLine.split(" ");
			    		int l = Integer.parseInt(split[0]);
			    		int r = Integer.parseInt(split[2]);
			    		int result = 0;
			    		switch (split[1])
			    		{
			    		case "+":
			    			result = l+r;
			    			break;
			    		case "-":
			    			result = l-r;
			    			break;
			    		case "/":
			    			result = l/r;
			    			break;
			    		case "*":
			    			result = l*r;
			    			break;
			    		default:
			    			throw new Exception("Invalid operation");
			    		}
			        out.println(result);
			        System.out.println(result);
			        if (inputLine.equals("exit"))
			            break;
		        } catch (Throwable t) {
		        		out.println(t.getMessage());
		        		System.out.println(t.getMessage());
		        }
		    }
		} catch (Throwable t) {
			System.out.println(t.getMessage());
		} finally {
			if (serverSocket != null && !serverSocket.isClosed()) {
				serverSocket.close();
			}
			
			System.out.println("Server closed");
		}
	}

}
