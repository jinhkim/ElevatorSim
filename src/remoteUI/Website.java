package remoteUI;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import elevatorSystem.Controller;
import elevatorSystem.Safety;

import remoteUI.ElementalHttpServer.RequestListenerThread;

import java.util.*;
import java.lang.*;
import java.net.*;

import mainUI.graphs.PassengerWaitGraph;
import mainUI.graphs.ElevatorDistanceGraph;
import mainUI.graphs.ElevatorPositionGraph;
import mainUI.graphs.PassengerRideTime;
public class Website extends Thread{

	private static Controller controller;
	private static Safety safety;
	private String input;
	private int PORT;
	private int elevators;
	private int floors;
	private String[] dataarray;
	private Method method;
	private PassengerWaitGraph PWaitGraph;
	private ElevatorDistanceGraph EDistanceGraph;
	private ElevatorPositionGraph EPositionGraph;
	private PassengerRideTime PRideTime;
	
	public enum Method{
		UpdateLog, SetupConnection, UpdateElevatorStatus, UpdateElevatorPosition, Start;
	}
	
	public Website(Method m, String inputi){	//UpdateLog
		method = m;
		input = inputi;

	}
	public Website(Controller c, Safety s, Method m, int PORTi, int elevatorsi, int floorsi, PassengerWaitGraph a, ElevatorDistanceGraph b, ElevatorPositionGraph cc, PassengerRideTime d){ //Start
		controller = c;
		method = m;
		PORT = PORTi;
		elevators = elevatorsi;
		floors = floorsi;
		safety = s;
		PWaitGraph = a;
		EDistanceGraph = b;
		EPositionGraph = cc;
		PRideTime = d;
	}
	
	public Website(Controller c, Safety s, Method m, int PORTi, int elevatorsi, int floorsi){ //Start
		controller = c;
		method = m;
		PORT = PORTi;
		elevators = elevatorsi;
		floors = floorsi;
		safety = s;
		
	}
	
	public Website(Controller c, Safety s, Method m, String inputi, int elevatorsi, int floorsi){ //SetupConnection
		controller = c;
		method = m;
		input = inputi;
		elevators = elevatorsi;
		floors = floorsi;
		safety = s;
	}
	
	public Website(Safety s, Method m, String[] dataarrayi){	//UpdateElevatorStatus/Position
		method = m;
		dataarray = dataarrayi;
		safety = s;
	}
	
	public Website(Method m, String[] dataarrayi){	//UpdateElevatorStatus/Position
		method = m;
		dataarray = dataarrayi;
	}

	
	public void run(){

			switch (method){
			case UpdateLog:	UpdateLog(input); break;
			case SetupConnection:	SetupConnection(input, elevators, floors); break;
			case UpdateElevatorStatus:	UpdateElevatorStatus(dataarray); break;
			case UpdateElevatorPosition:	UpdateElevatorPosition(dataarray); break;
			case Start: Start(PORT, elevators, floors); break;
			default: break;
			}

	}

    public void setController(Controller c) {
    	controller = c;
    }
    
   /* public static void main(String[] args) throws Exception {
    	int PORT;
    	String ADDRESS;
    	/*
    	//Arguments for connection
    	PORT = 8093;
    	//ADDRESS = "http://pod7.no-ip.org";
    	//ADDRESS = GetIP();
    	ADDRESS = GetLocalIP();
    	
    	//Sync with the website
    	SetupConnection("http://" + ADDRESS + ":" + PORT, 5, 35);	//http:// is required
    	
    	
    	
    	//Starting the Listener
    	String[] data = new String[2];
    	data[0] = "127.0.0.1";
    	data[1] = String.valueOf(PORT);
    	ElementalHttpServer.main(data);
    	
    	
    	//Transmit("TEST200");
    	
    	
    	
    	String[] Status = new String[10];
    	for(int i=0; i<10; i++)
    		Status[i] = "ON";
    	UpdateElevatorStatus(Status);

    	String[] Position = new String[10];
    	Position[0] = "0";
    	Position[1] = "5";
    	Position[2] = "3";
    	Position[3] = "2";
    	Position[4] = "7";
    	Position[5] = "10";
    	Position[6] = "18";
    	Position[7] = "15";
    	Position[8] = "11";
    	Position[9] = "9";
    	UpdateElevatorPosition(Position);
    	
    	for(int k=0; k<100; k++)
    	{
    		Position[0] = String.valueOf(Integer.parseInt(Position[0]) + 1);
    		Position[1] = String.valueOf(Integer.parseInt(Position[1]) - 1);
    		Position[2] = String.valueOf(Integer.parseInt(Position[2]) - 1);
    		Position[3] = String.valueOf(Integer.parseInt(Position[3]) + 1);
    		Position[4] = String.valueOf(Integer.parseInt(Position[4]) + 1);
    		Position[5] = String.valueOf(Integer.parseInt(Position[5]) - 1);
    		Position[6] = String.valueOf(Integer.parseInt(Position[6]) + 1);
    		Position[7] = String.valueOf(Integer.parseInt(Position[7]) - 1);
    		Position[8] = String.valueOf(Integer.parseInt(Position[8]) + 1);
    		Position[9] = String.valueOf(Integer.parseInt(Position[9]) + 1);
        	
    		for(int i=0; i<9; i++)
    		{
    			if(Integer.parseInt(Position[i]) < 0)
    				Position[i] = "34";
    			if(Integer.parseInt(Position[i]) > 34)
    				Position[i] = "0";
    		}
    				
        	
        	UpdateElevatorPosition(Position);
        	Thread.currentThread();
			Thread.sleep(500);
        	
    	}
    	
    	
    //}
    
    public void setController(Controller c) {
    	controller = c;
    }
    
 */
    
    public String GetIP() throws IOException {
	    try{
    	  URL whatismyip = new URL("http://automation.whatismyip.com/n09230945.asp");
    	  BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

    	  String ip = in.readLine(); //you get the IP as a String
    	  //System.out.println(ip);
		
    	  return ip;
    	  
		} catch (IOException e) {
			return "Error";
		}
    }
    
    public String GetLocalIP() throws IOException
    {
    	try{
    		
    		InetAddress ownIP=InetAddress.getLocalHost();
    		//System.out.println("IP of my system is := "+ownIP.getHostAddress());
    		return ownIP.getHostAddress();
    	} catch (IOException e) {
    		return "Error";
		}
    }

    
    public void Start(int PORT, int elevators, int floors) {

    	try {
    		
    	//Arguments for connection

    	//ADDRESS = "http://pod7.no-ip.org";
    	String ADDRESS;
    	ADDRESS = GetIP();
    	//ADDRESS = GetLocalIP();
    	
    	//Sync with the website
    	SetupConnection("http://" + ADDRESS + ":" + PORT, elevators, floors);	//http:// is required
    	
    	//Starting the Listener
    	String[] data = new String[2];
    	data[0] = "/";
    	data[1] = String.valueOf(PORT);
    	
    	
    	ElementalHttpServer.setController(controller);
    	ElementalHttpServer.setSafety(safety);
    	
    	try {
			ElementalHttpServer.Start(data, PWaitGraph, EDistanceGraph, EPositionGraph, PRideTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//Transmit("TEST200");
    	
    	} catch (IOException e) {
		}
    }
    
    
	public void UpdateLog(String input)	//UpdateLog
	{
		  try {
			    // Construct data
			    String data = URLEncoder.encode("request", "UTF-8") + "=" + URLEncoder.encode(input, "UTF-8");
			    //data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

			    // Send data
			    URL url = new URL("http://pod7.alwaysdata.net/bosslift/rxrequest/");
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(data);
			    wr.flush();

			    
			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String line;
			    while ((line = rd.readLine()) != null) {
			        // Process line...
			    	//System.out.printf("%s\n", line);
			    }
			    wr.close();
			    rd.close();
			} catch (Exception e) {
			}
	}
	
	public void UpdateElevatorStatus(String[] dataarray)
	{
		  try {
			    // Construct data
			    String data = URLEncoder.encode("E0", "UTF-8") + "=" + URLEncoder.encode(dataarray[0], "UTF-8");
			    data += "&" + URLEncoder.encode("E1", "UTF-8") + "=" + URLEncoder.encode(dataarray[1], "UTF-8");
			    data += "&" + URLEncoder.encode("E2", "UTF-8") + "=" + URLEncoder.encode(dataarray[2], "UTF-8");
			    data += "&" + URLEncoder.encode("E3", "UTF-8") + "=" + URLEncoder.encode(dataarray[3], "UTF-8");
			    data += "&" + URLEncoder.encode("E4", "UTF-8") + "=" + URLEncoder.encode(dataarray[4], "UTF-8");
			    data += "&" + URLEncoder.encode("E5", "UTF-8") + "=" + URLEncoder.encode(dataarray[5], "UTF-8");
			    data += "&" + URLEncoder.encode("E6", "UTF-8") + "=" + URLEncoder.encode(dataarray[6], "UTF-8");
			    data += "&" + URLEncoder.encode("E7", "UTF-8") + "=" + URLEncoder.encode(dataarray[7], "UTF-8");
			    data += "&" + URLEncoder.encode("E8", "UTF-8") + "=" + URLEncoder.encode(dataarray[8], "UTF-8");
			    data += "&" + URLEncoder.encode("E9", "UTF-8") + "=" + URLEncoder.encode(dataarray[9], "UTF-8");

			    // Send data
			    URL url = new URL("http://pod7.alwaysdata.net/bosslift/elevatorstatusdb/");
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(data);
			    wr.flush();

			    
			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String line;
			    while ((line = rd.readLine()) != null) {
			        // Process line...
			    	//System.out.printf("%s\n", line);
			    }
			    wr.close();
			    rd.close();
			} catch (Exception e) {
			}
	}

	
	public void UpdateElevatorPosition(String[] dataarray)
	{
		  try {
			    // Construct data
			    String data = URLEncoder.encode("E0", "UTF-8") + "=" + URLEncoder.encode(dataarray[0], "UTF-8");
			    data += "&" + URLEncoder.encode("E1", "UTF-8") + "=" + URLEncoder.encode(dataarray[1], "UTF-8");
			    data += "&" + URLEncoder.encode("E2", "UTF-8") + "=" + URLEncoder.encode(dataarray[2], "UTF-8");
			    data += "&" + URLEncoder.encode("E3", "UTF-8") + "=" + URLEncoder.encode(dataarray[3], "UTF-8");
			    data += "&" + URLEncoder.encode("E4", "UTF-8") + "=" + URLEncoder.encode(dataarray[4], "UTF-8");
			    data += "&" + URLEncoder.encode("E5", "UTF-8") + "=" + URLEncoder.encode(dataarray[5], "UTF-8");
			    data += "&" + URLEncoder.encode("E6", "UTF-8") + "=" + URLEncoder.encode(dataarray[6], "UTF-8");
			    data += "&" + URLEncoder.encode("E7", "UTF-8") + "=" + URLEncoder.encode(dataarray[7], "UTF-8");
			    data += "&" + URLEncoder.encode("E8", "UTF-8") + "=" + URLEncoder.encode(dataarray[8], "UTF-8");
			    data += "&" + URLEncoder.encode("E9", "UTF-8") + "=" + URLEncoder.encode(dataarray[9], "UTF-8");

			    // Send data
			    URL url = new URL("http://pod7.alwaysdata.net/bosslift/elevatorpositiondb/");
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(data);
			    wr.flush();

			    
			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String line;
			    while ((line = rd.readLine()) != null) {
			        // Process line...
			    	//System.out.printf("%s\n", line);
			    }
			    wr.close();
			    rd.close();
			} catch (Exception e) {
			}
	}

	
	public void SetupConnection(String input, int elevators, int floors)
	{
		  try {
			  String s_elevators = String.valueOf(elevators);
			  String s_floors = String.valueOf(floors);
			  
			    // Construct data
			    String data = URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(input, "UTF-8");
			    data += "&" + URLEncoder.encode("elevators", "UTF-8") + "=" + URLEncoder.encode(s_elevators, "UTF-8");
			    data += "&" + URLEncoder.encode("floors", "UTF-8") + "=" + URLEncoder.encode(s_floors, "UTF-8");
			  //data += "&" + URLEncoder.encode("key2", "UTF-8") + "=" + URLEncoder.encode("value2", "UTF-8");

			    // Send data
			    URL url = new URL("http://pod7.alwaysdata.net/bosslift/connection/");
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(data);
			    wr.flush();

			    //System.out.println("Connection setup has been sent");
			    
			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String line;
			    while ((line = rd.readLine()) != null) {
			        // Process line...
			    	//System.out.printf("%s\n", line);
			    }
			    wr.close();
			    rd.close();
			} catch (Exception e) {
			}
	}

}
