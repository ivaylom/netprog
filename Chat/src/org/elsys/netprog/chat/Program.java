package org.elsys.netprog.chat;

public class Program {

	public static void main(String[] args) throws Exception {
		//server [port]
		//client [address] [port]
		
		if (args.length < 2 || args.length > 3) {
			throw new Exception("Invalid argument count");
		}
		
		if (args[0].equals("server")) {
			
			System.out.println("SERVER");
		} else if (args[0].equals("client")) {
			System.out.println("CLIENT");
		} else {
			throw new Exception("Argument[0] must be 'server' or 'client'.");
		}
		
	}

}
