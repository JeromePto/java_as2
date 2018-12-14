package client_server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import commonData.*;

public class Client {
	
	private static int portNumberStatic = 5050;
	private static String LED0_PATH = "/sys/class/leds/led0";
	
    private Socket socket = null;
    private ObjectOutputStream os = null;
    private ObjectInputStream is = null;
    private String name;
	private int sampleRate;
	private boolean running;
	private boolean connected;
	private long startTime;
	private static boolean verbose = false;
	private static boolean randomize = false;
	private String fileLocation = "coucou";
	private boolean ledOk;
	private SmoothRandom sr;
	private SmoothRandom sr2;
	
	
	// the constructor expects the IP address of the server - the port is fixed
    public Client(String serverIP, int portNumber, String name) {
    	this.running = true;
    	this.sampleRate = 1000;
    	this.ledOk = false;
    	this.sr = new SmoothRandom(0.03d);
    	this.sr2 = new SmoothRandom(0.07d);
    	
    	this.name = name;
    	
    	if(System.getProperty("os.name").equals("Linux")) {
	    	this.fileLocation = "/sys/class/thermal/thermal_zone0/temp";
	    	
	    	try {
	    		BufferedWriter bw = new BufferedWriter ( new FileWriter (LED0_PATH+"/trigger"));
				bw.write("none");
				bw.close();
				bw = new BufferedWriter ( new FileWriter (LED0_PATH+"/brightness"));
				bw.write("0");
				bw.close();
				this.ledOk = true;
	    	}
	    	catch (IOException e) {
				System.out.println("Failed to access the Raspberry LED + " + e.toString());
				System.out.println("Continue Without");
				this.ledOk = false;
			}
	    }
    	else {
    		this.fileLocation = "coucou";
    		this.ledOk = false;
    	}
    	
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
	    		
	    		getData(readingData());
	    		
	    		if(verbose) System.out.println("Wait for " + sampleRate + " ms");
	    		try {
					Thread.sleep((long)((float)(sampleRate)*0.3f));
				} 
	    		catch (InterruptedException e) {
					System.out.println("Error in the wait: " + e.toString());
				}
	    		catch (IllegalArgumentException e) {
	    			System.out.println("Error: " + e.toString());
	    		}
	    		setLed(false);
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
    	DataServerToClient dataRecived = null;
    	System.out.println("01. -> Sending Command (" + dataToSend.toString() + ")");
    	this.send(dataToSend);
    	try{
    		//dataRecived = null;
    		dataRecived = (DataServerToClient) receive();
    		
    		System.out.println("01. <- Received a DataServerToClient object from the client (" + dataRecived.toString() + ").");
    		
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
    		
    		if(verbose) System.out.println("05. <- The Server responded with: ");
    		if(verbose) System.out.println("    <- " + dataRecived.toString());
    	}
    	catch (Exception e){
    		System.out.println("XX. There was an invalid object sent back from the server");
    	}
    	//System.out.println("06. -- Disconnected from Server.");
}
	
    // method to send a generic object.
    private void send(Object o) {
    	setLed(true);
		try {
			if(verbose) System.out.println("02. -> Sending an object...");
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
			if(verbose) System.out.println("03. -- About to receive an object...");
		    o = is.readObject();
		    if(verbose) System.out.println("04. <- Object received...");
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
    		f = new File(fileLocation);
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
    	
    	if(randomize) temperature = random(temperature);
    	long time = System.currentTimeMillis();
    	
    	DataClientToServer data = new DataClientToServer(name, temperature, time);
    	
    	return data;
    }
    
    private float random(float a) {
    	return rangeToRange(sr.nextFloat(20, 80)+sr2.nextFloat(-20, 20),0, 100, 20, 80);
    }
    
    private void setLed(boolean state) {
    	if(ledOk) {
    		try {
				BufferedWriter bw = new BufferedWriter ( new FileWriter (LED0_PATH+"/brightness"));
				bw.write(state ? "1" : "0");
				bw.close();
    		}
    		catch(IOException e){
    			System.out.println("Failed to access the Raspberry LED + " + e.toString());
				System.out.println("Continue Without");
				ledOk = false;
    		}
    	}
    }
    
    private static float rangeToRange(float valueIn, float inMin, float inMax, float outMin, float outMax) {
		float tmp = valueIn;
		tmp = (tmp-inMin) / (inMax-inMin);
		tmp = (tmp * (outMax - outMin)) + outMin;
		return tmp;
	}

    public static void main(String args[])
    {    	
    	System.out.println("**. Java Client Application - EE402 OOP Module, DCU");
    	try {
    		if(args.length < 2) {
    			throw new IllegalArgumentException("2 Parameters needed, " + args.length + " passed");
    		}
    		
    		if(args.length == 3) {
    			if(args[2].contains("v")) {
    				verbose = true;
    			}
    			if(args[2].contains("r")) {
    				randomize = true;
    			}
    		}
    		
    		new Client(args[0], portNumberStatic, args[1]);
    		
    	}
    	catch(NumberFormatException e) {
    		System.out.println("Invalid string for conversion to int: " + e.toString());
    	}
    	catch(Exception e) {
    		System.out.println("Exception Occurred: " + e.toString());
    	}
    	System.out.println("**. End of Application.");
    }
}

/*
cd /D D:\Documents\Programme\eclipse-workspace\client_server\bin
java client_server.Client localhost client

b8:27:eb:49:f7:e5
169.254.245.74

15h02 un peu avant
minux
*/