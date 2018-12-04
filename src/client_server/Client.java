package client_server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import commonData.*;

public class Client {
	
	//private static int portNumber = 5050;
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    private String name;
	private int sampleRate;
	private boolean running;
	private boolean connected;
	private long startTime;

	// the constructor expects the IP address of the server - the port is fixed
    public Client(String serverIP, int portNumber, String name) {
    	this.running = true;
    	this.sampleRate = 1000;
    	
    	this.name = name;
    	
    	while(this.running) {
	    	if(!connectToServer(serverIP, portNumber)) {
	    		System.out.println("XX. Failed to open socket connection to: " + serverIP);
	    	}
	    	else
	    	{
	    		this.connected = true;
	    	}
	    	
	    	while(this.connected) {
	    		startTime = System.currentTimeMillis();
	    		// Task
	    		
	    		getData(readingData());
	    		
	    		while(System.currentTimeMillis() < startTime + sampleRate) {}
	    	}
    	}
    }

    private boolean connectToServer(String serverIP, int portNumber) {
    	try { // open a new socket to the server
    		this.socket = new Socket(serverIP,portNumber);
    		this.os = new ObjectOutputStream(this.socket.getOutputStream());
    		this.is = new ObjectInputStream(this.socket.getInputStream());
    		System.out.println("00. -> Connected to Server:" + this.socket.getInetAddress() 
    				+ " on port: " + this.socket.getPort());
    		System.out.println("    -> from local address: " + this.socket.getLocalAddress()
    				+ " and port: " + this.socket.getLocalPort());
    	} 
        catch (Exception e) {
        	System.out.println("XX. Failed to Connect to the Server at port: " + portNumber);
        	System.out.println("    Exception: " + e.toString());	
        	return false;
        }
		return true;
    }
    
    private void getData(DataClientToServer dataToSend) {
    	DataServerToClient dataRecived;
    	System.out.println("01. -> Sending Command (" + dataToSend.toString() + ") to the server...");
    	this.send(dataToSend);
    	try{
    		dataRecived = (DataServerToClient) receive();
    		
    		sampleRate = dataRecived.getSampleRate();
    		if(dataRecived.getMessage().equals("CREATED")) {
    			
    		}
    		else if(dataRecived.getMessage().equals("PROCESSED")) {
    			
    		}
    		else
    		{
    			System.out.println("XX. Server sent back: " + dataRecived.getMessage() + "\nProgram Exit");
    			running = false;
    			connected = false;
    		}
    		
    		System.out.println("05. <- The Server responded with: ");
    		System.out.println("    <- " + dataRecived.toString());
    	}
    	catch (Exception e){
    		System.out.println("XX. There was an invalid object sent back from the server");
    	}
    	System.out.println("06. -- Disconnected from Server.");
}
	
    // method to send a generic object.
    private void send(Object o) {
		try {
		    System.out.println("02. -> Sending an object...");
		    os.writeObject(o);
		    os.flush();
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Sending:" +  e.toString());
		    connected = false;
		}
    }

    // method to receive a generic object.
    private Object receive() 
    {
		Object o = null;
		try {
			System.out.println("03. -- About to receive an object...");
		    o = is.readObject();
		    System.out.println("04. <- Object received...");
		} 
	    catch (Exception e) {
		    System.out.println("XX. Exception Occurred on Receiving:" + e.toString());
		    connected = false;
		}
		return o;
    }
    
    private DataClientToServer readingData()
    {
    	
    	float temperature = 0;
    	File f = null;
    	Scanner sc = null;
    	
    	try {
    		f = new File("coucou");
    		sc = new Scanner(f);
    		String tmp = sc.nextLine();
    		sc.close();
    		if(tmp.length() != 5) throw new Exception("Error in data form");
			temperature = new Float(tmp.substring(0, 2) + "." + tmp.substring(2, 5));
		}
    	catch (FileNotFoundException e) {
    		System.out.println("XX. Exception with File location: " +  e.toString());
    		System.out.println("Program Exit");
    		System.exit(-1);
		}
    	catch (Exception e) {
    		System.out.println("XX. Exception with data: " +  e.toString());
    		System.out.println("Program Exit");
    		System.exit(-1);
    	}
    	
    	DataClientToServer data = new DataClientToServer(name, temperature);
    	
    	return data;
    }

    public static void main(String args[]) 
    {    	
    	System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
    	try {
    		if(args.length != 3) {
    			throw new IllegalArgumentException("3 Parameters needed, " + args.length + " passed");
    		}
    		
    		int portNumber = new Integer(args[1]);
    		
    		new Client(args[0], portNumber, args[2]);
    		
    	}
    	catch(NumberFormatException e) {
    		System.out.println("Invalid string for conversion to int: " + e.getMessage());
    	}
    	catch(Exception e) {
    		System.out.println("Exception Occurred: " + e.getMessage());	
    	}
    	System.out.println("**. End of Application.");
    }
}

/*
cd /D D:\Documents\Programme\eclipse-workspace\client_server\bin
java client_server.Client localhost 5050 client1
*/