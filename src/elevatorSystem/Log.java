package elevatorSystem;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import java.util.Date;
import java.util.Iterator;

import remoteUI.Website.Method;
import remoteUI.Website;

import mainUI.mainWindow;





public class Log {
	
	private static boolean writeElevatorFile = true,
	writePassengerFile = true,
	writeControllerFile = true,
	writeSafetyFile = true,
	writeEventFile = true,
	writePassengerEmergencyFile = true,
	writeElevatorQueueFile = true,
	writeFloorsMovedFile = true,
	writeLastFloorFile = true,
	writeFloorsMovedSinceFaultFile = true;
	
	static Controller controllin;

	private static Vector<Vector<String>> elevatorLog = new Vector(new Vector()),
		 								  passengerLog = new Vector(new Vector()),
										  controllerLog = new Vector(new Vector()),
										  safetyLog = new Vector(new Vector()),
										  eventLog = new Vector(new Vector()),
										  passengerEmergencyLog = new Vector(new Vector());
	
	private static Vector<Vector<Integer>> elevatorQueue = new Vector(new Vector());
	private static Vector<int[]> numberOfFloorsMoved = new Vector(),
									 lastFloor = new Vector();
	private static Vector<String> floorsMovedSinceLastFault = new Vector(); 
	
	private static Date currentTime = new Date();
	private static Calendar cal = Calendar.getInstance();
	private static DateFormat dfm = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
	private static DateFormat fileDfm = new SimpleDateFormat("[yyyy-MM-dd HH_mm_ss_mmm]");
	private static boolean elevatorRunOnce = false, 
						   passengerRunOnce = false, 
						   controllerRunOnce = false,
						   safetyRunOnce = false, 
						   eventRunOnce = false,
						   emergencyRunOnce = false,
						   reportRunOnce = false;
	private static float[] oldFloors;
	//private static float avgPassWaitTime;
	
	private static mainWindow mainUI = null;
	
	private static FileWriter elevatorLogStream, passengerLogStream, controllerLogStream, 
							  safetyLogStream, eventLogStream, elevatorQueueLogStream, 
							  floorsMovedLogStream, lastFloorLogStream,
							  passengerEmergencyLogStream;

	private static BufferedWriter elevatorLogFile, passengerLogFile, 
								  controllerLogFile, safetyLogFile, 
								  eventLogFile, elevatorQueueLogFile, 
								  floorsMovedLogFile, lastFloorLogFile,
								  passengerEmergencyLogFile,report;
	private static FileOutputStream stufftemp;
	
	private static int LogFileCount = 0, maintenanceCount = 0;
	private static int numPassengersServiced = 0;
	private static int numPassengersWaiting = 0;
	private static float[] totalFloorsTravelled;
	private static long avgPassWait = 0, avgTotalWait = 0, avgRidingTime = 0,
						totalWaitTime = 0, totalRidingTime = 0, 
						oldTime = 0;
	private static String elevatorLogTime, passengerLogTime, controllerLogTime,safetyLogTime,
				   eventLogTime, pemergencyLogTime, elevatorQueueLogTime, nfloorsLogTime,
				   lastFloorLogTime, faultLogTime, reportLogTime;
	
	/*frequency of File writes in milliseconds (i.e. every X milliseconds)*/
	private static long elevatorWriteFrequency = 1000;
	

	
	private static long oldElevatorTime = 0, oldPassengerTime = 0, 
						oldControllerTime = 0, oldSafetyTime = 0,
						oldEventTime = 0, oldPEmergencyTime = 0, oldElevatorQueueTime = 0, 
						oldNFloorsTime = 0, oldLastFloorTime = 0;
	
	//Graph class needs: floor distance, current floors, passenger wait time.
	//Log needs: Iterator, floors travelled between maintenance checks, logging wait time
	//TEST ALL THE METHODS
	
	//Log still needs: log Passenger wait time, 
	//update GET methods: make one general GET method that takes a string argument specifying 
	//  				  what specific parameter you want
	
	//at the end of the simulation (i.e. when the simulation closes) write a progress report
	//which records the avg passenger wait time, distance travelled by each elevator, emergencies,
	//riding time for each elevators, etc.
	
	public Log(){
		
	}
	
	public static void setController(Controller c){
		controllin = c;
	}
	
	public static void setMainUI(mainWindow mainUI){
		Log.mainUI = mainUI;
	}
	
	public enum EmergencyStatus {
		RESOLVED, ALERT;
	}
	
	public enum MsgType{
		GENERAL, PASSENGER, ELEVATOR, EMERGENCY, FAULT,
		CONTROLLER,SAFETY,EVENT,
		ELEVATORQUEUE, FLOORSMOVED, LASTFLOOR, MAINTENANCE;
	}
	
	public static void addPassengerEmergencyLog(int elevatorID, EmergencyStatus state){
		Vector<String> temp = new Vector();
		
		if(!emergencyRunOnce){
			temp.add("System Time");
			temp.add("Elevator ID");
			temp.add("State");
			passengerEmergencyLog.add((Vector<String>)temp.clone());
			pemergencyLogTime = getCurrentFileTime();
		}
		emergencyRunOnce = true;
		temp.clear();
		temp.add(Integer.toString(elevatorID));
		temp.add(state.toString());
		passengerEmergencyLog.add((Vector<String>)temp.clone());
		if(writePassengerEmergencyFile){
			try {
				passengerEmergencyLogFile = new BufferedWriter(
							new FileWriter("passengerEmergencyLog "+pemergencyLogTime+".txt", true));
				if(passengerEmergencyLog.lastElement() != null){
					passengerEmergencyLogFile.write("\r\n"+passengerEmergencyLog.lastElement().toString()+"\r\n");
					passengerEmergencyLogFile.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
/*********************ELEVATOR**********************/
	public static void addElevatorLog(Elevator[] elevators){//, Vector<Integer> e_queue){
		
		int numElevators = elevators.length;
		assert(numElevators > 0);
		if(elevatorLog.size() != numElevators){
			elevatorLog.setSize(numElevators+1);
		}
		
		Vector<String> temp = new Vector();
		
		try {
		//elevatorLog will have up to 10 static elements: one for each elevator
		for(int i = 0; i < (numElevators+1); i++){
			if(!elevatorRunOnce){//should run only once
				totalFloorsTravelled = new float[numElevators];
				temp.add("System Time");
				temp.add("Status");
				temp.add("Current Floor");
				temp.add("Distance Travelled");
				temp.add("Thread ID");
				temp.add("Current Passengers");
				temp.add("Total Passengers");
				temp.add("Ypos");
				temp.add("Going Up?");
				elevatorLog.set(0,(Vector<String>)temp.clone());
				elevatorLogTime = getCurrentFileTime();
				elevatorRunOnce=true;
			} else { //the below should only run from i=1 to i=numElevators
				if(i==0)continue;
				temp.clear();
				temp.add(getCurrentTime());
				temp.add((elevators[i-1].getStatus().toString()));
				temp.add(Integer.toString(elevators[i-1].getCurrentFloor()));
				temp.add(String.valueOf(elevators[i-1].getTotalFloors()));
				temp.add(Integer.toString(elevators[i-1].getID()));
				temp.add(Integer.toString(elevators[i-1].getCurrentPassengerCount()));
				temp.add(Integer.toString(elevators[i-1].getTotalPassengerCount()));
				temp.add(Float.toString(elevators[i-1].getYpos()));
				temp.add(String.valueOf(elevators[i-1].getDirection()));
				elevatorLog.set(i,(Vector<String>)temp.clone());
				totalFloorsTravelled[i-1] = elevators[i-1].getTotalFloorsF();
				//elevatorQueue.add((Vector<Integer>)e_queue.clone());
			}
		}
		} catch (ArrayIndexOutOfBoundsException e) {}
		
//		if(writeFrequency > 0){
//			long tempTime = cal.getTimeInMillis(); //time now
//			
//			//compare with the last time write was called
//			if((tempTime - oldTime) >= writeFrequency){  
//				writeElevatorFile = true;
//				oldTime = cal.getTimeInMillis();
//			} else {
//				writeElevatorFile = false;
//			}
//		}
		
		long timeDifference = cal.getInstance().getTimeInMillis() - oldElevatorTime;
		
		if(timeDifference > elevatorWriteFrequency){
			writeElevatorFile = true;
			oldElevatorTime = cal.getInstance().getTimeInMillis();
		}
		else writeElevatorFile = false;

		
		if(writeElevatorFile){
			try {
				elevatorLogFile = new BufferedWriter(new FileWriter("elevatorLog "+elevatorLogTime+".txt", true));
				if(writeElevatorQueueFile)elevatorQueueLogFile = new BufferedWriter(new FileWriter("elevatorQueueLog "+elevatorLogTime+".txt", true));
	
				if(elevatorLog.lastElement() != null){
					for(int g = 0; g < elevatorLog.size(); g++){
						elevatorLogFile.write("\r\n"+elevatorLog.get(g).toString()+"\r\n");
					}
				}
				//elevatorLogFile.write("\n\n\n");
				elevatorLogFile.close();
				
	//			if(elevatorQueue.size() > 200){
	//				elevatorQueueLogFile.write(elevatorQueue.get(0).toString()+"\n");
	//				elevatorQueueLogFile.close();
	//				elevatorQueue.remove(0);
	//			} 
			} catch (IOException e) {
//				e.printStackTrace();
			} 
		}
		//Log.printLog(MsgType.ELEVATOR);
	}
	
/*********************PASSENGER**********************/
//	public static void addPassengerLog(int weight, int currentFloor, int destFloor, long timeOfArrival,
//									   int elevatorID, boolean servicedFlag, boolean upFlag, boolean VIP)
	public static void addPassengerLog(Vector<Passenger>[] passengers)
	{
		//number of passengers on each floor
		Vector<Integer> numPassengers = new Vector(passengers.length); 
		
		if(passengerLog.size() <= 0){
			passengerLog.setSize(1);
		}
		
		Vector<String> temp = new Vector();	
		
		//passengerLog will store passengers in each element
		for(int i = 0; i < (passengers.length-1); i++){//for each floor, add passengers
			if(!passengerRunOnce){
				temp.add("System Time");
				temp.add("Weight");
				temp.add("Current Floor");
				temp.add("Dest. Floor");
				temp.add("Time of Arrival");
				temp.add("Elevator ID");
				temp.add("Serviced");
				temp.add("Going Up?");
				temp.add("VIP");
				passengerLog.set(0,(Vector<String>)temp.clone());
				passengerLogTime = getCurrentFileTime();
			} 
			passengerRunOnce = true;
			for(int j = 0; j < passengers[i].size(); j++){ //for each passenger on a floor, add info
				temp.clear();
				temp.add(getCurrentTime());
				temp.add(Integer.toString(passengers[i].get(j).getWeight()));
				temp.add(Integer.toString(passengers[i].get(j).getCurrentFloor()));
				temp.add(Integer.toString(passengers[i].get(j).getDestFloor()));
				temp.add(Long.toString(passengers[i].get(j).getTimeOfArrival()));
				temp.add(Integer.toString(passengers[i].get(j).getElevatorID()));
				temp.add(Boolean.toString(passengers[i].get(j).serviced()));//servicedflag
				temp.add(Boolean.toString(passengers[i].get(j).getUpFlag()));//upflag
				temp.add(Boolean.toString(passengers[i].get(j).isVIP()));//VIP
				passengerLog.add((Vector<String>)temp.clone());
			}
		}
		
		long timeDifference = cal.getInstance().getTimeInMillis() - oldPassengerTime;
		
		if(timeDifference > 0){
			writePassengerFile = true;
			oldPassengerTime = cal.getInstance().getTimeInMillis();
		}
		else writePassengerFile = false;
		
		if(writePassengerFile){
			try {
				passengerLogFile = new BufferedWriter(
						new FileWriter("passengerLog "+passengerLogTime+".txt", true));
				
				if(passengerLog.lastElement() != null)
					passengerLogFile.write("\r\n"+passengerLog.lastElement().toString()+"\r\n");
				//passengerLogFile.write("\n\n\n");
				passengerLogFile.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
		}
	}
	
/*********************CONTROLLER**********************/	
	public static void addControllerLog(String algorithm, int numPassengers, int numElevators, int numFloors)
	{
		Vector<String> temp = new Vector();	
		
		
		if(!controllerRunOnce){
			temp.add("System Time");
			temp.add("Algorithm");
			temp.add("numPassengers");
			temp.add("numElevators");
			temp.add("numFloors");
			controllerLog.add((Vector<String>)temp.clone());
			controllerLogTime = getCurrentFileTime();
		}
		controllerRunOnce = true;
		temp.clear();
		temp.add(getCurrentTime());
		temp.add(algorithm);
		temp.add(Integer.toString(numPassengers));
		temp.add(Integer.toString(numElevators));
		temp.add(Integer.toString(numFloors));
		controllerLog.add((Vector<String>)temp.clone());

		long timeDifference = cal.getInstance().getTimeInMillis() - oldControllerTime;
		
		if(timeDifference > 0){
			writeControllerFile = true;
			oldControllerTime = cal.getInstance().getTimeInMillis();
		}
		else writeControllerFile = false;
		
		
		if(writeControllerFile){
			try {
				controllerLogFile = new BufferedWriter(
						new FileWriter("controllerLog "+controllerLogTime+".txt", true));
				if(controllerLog.lastElement() != null){
					controllerLogFile.write("\r\n"+controllerLog.lastElement().toString()+"\r\n");
					controllerLogFile.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
		}
	}
	
/*********************SAFETY**********************/
	public static void addSafetyLog(int[] floorsMoved, int[] lastFloor, int[] elevatorDurability, 
									int[] durabilityThreshold, int elevatorInMaintenance, 
									boolean maintenanceCheck)
	{
		Vector<String> temp = new Vector();
		Vector<Integer> tempDurability = new Vector(elevatorDurability.length);
		Vector<Integer> tempThreshold = new Vector(durabilityThreshold.length);
		if(!safetyRunOnce){
			temp.add("System Time");
			temp.add("Durability");
			temp.add("Durability Thresh.");
			temp.add("Maintenance");
			safetyLog.add((Vector<String>)temp.clone());
			safetyLogTime = getCurrentFileTime();
		}
		for(int i = 0; i < elevatorDurability.length; i++){
			tempDurability.add(elevatorDurability[i]);
		}
		for(int i = 0; i < durabilityThreshold.length; i++){
			tempThreshold.add(durabilityThreshold[i]);
		}
		safetyRunOnce = true;
		temp.clear();
		temp.add(getCurrentTime());
		temp.add(tempDurability.toString());
		temp.add(tempThreshold.toString());
		temp.add(Integer.toString(elevatorInMaintenance));
		safetyLog.add((Vector<String>)temp.clone());
		numberOfFloorsMoved.add((int[])floorsMoved.clone());
		Log.lastFloor.add((int[])lastFloor.clone());
		updateFloorsSinceLastMaintenance(elevatorDurability);


		long timeDifference = cal.getInstance().getTimeInMillis() - oldSafetyTime;
		
		if(timeDifference > 0){
			writeSafetyFile = true;
			oldSafetyTime = cal.getInstance().getTimeInMillis();
		}
		else writeSafetyFile = false;
		
		Vector tempMoved = new Vector(numberOfFloorsMoved.size());
		Vector tempLastFloor = new Vector(Log.lastFloor.size());
		if(writeSafetyFile){
			if(numberOfFloorsMoved != null){
				for(int k = 0; k < numberOfFloorsMoved.lastElement().length; k++){
					tempMoved.add(numberOfFloorsMoved.lastElement()[k]);
				}
			}
			if(Log.lastFloor != null){
				for(int k = 0; k < Log.lastFloor.lastElement().length; k++){
					tempLastFloor.add(Log.lastFloor.lastElement()[k]);
				}
			}
			try {
				safetyLogFile = new BufferedWriter(new FileWriter("safetyLog "+safetyLogTime+".txt", true));
				floorsMovedLogFile = new BufferedWriter(new FileWriter("floorsMovedLog "+safetyLogTime+".txt", true));
				lastFloorLogFile = new BufferedWriter(new FileWriter("lastFloorLog "+safetyLogTime+".txt", true));
				
				if(safetyLog.lastElement() != null){
					safetyLogFile.write("\r\n"+safetyLog.lastElement().toString()+"\r\n");
					safetyLogFile.close();
				}
				if(numberOfFloorsMoved.lastElement() != null){
					floorsMovedLogFile.write("\r\n"+tempMoved.toString()+"\r\n");
					floorsMovedLogFile.close();
				} 
				if(Log.lastFloor.lastElement() != null){
					lastFloorLogFile.write("\r\n"+tempLastFloor.toString()+"\r\n");
					lastFloorLogFile.close();
				} 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} 
		}
		if(maintenanceCheck){//once an elevator has been fixed, set the distance travelled to 0
			totalFloorsTravelled[elevatorInMaintenance] = 0;
			maintenanceCount++;
		}
	}
	
/*********************EVENT**********************/
	public static void addEventLog(String message, int elevatorID, MsgType t)
	{
		Vector<String> temp = new Vector();	
		if(!eventRunOnce){
			temp.add("System Time");
			temp.add("Message");
			temp.add("elevatorID");
			eventLog.add((Vector<String>)temp.clone());
			eventLogTime = getCurrentFileTime();
		}
		eventRunOnce = true;
		temp.clear();
		temp.add(getCurrentTime());
		temp.add(message);
		temp.add(Integer.toString(elevatorID));
		eventLog.add((Vector<String>)temp.clone());
		
		long timeDifference = cal.getInstance().getTimeInMillis() - oldEventTime;
		
		if(timeDifference > 0){
			writeEventFile = true;
			oldEventTime = cal.getInstance().getTimeInMillis();
		}
		else writeEventFile = false;
		
		if(writeEventFile){
			try {
				eventLogFile = new BufferedWriter(new FileWriter("eventLog "+eventLogTime+".txt", true));
				
				
				if(eventLog.lastElement() != null){
					eventLogFile.write("\r\n"+eventLog.lastElement().toString()+"\r\n");
					eventLogFile.close();
				}
				
			} catch (IOException e) {
				//e.printStackTrace();
			} 
		}
		
		switch(t){
		case GENERAL:
			break;
		case PASSENGER:
			postToUI(passengerLog.lastElement().toString(), t);
			break;
		case ELEVATOR:
			postToUI(eventLog.lastElement().get(0) +" "+ eventLog.lastElement().get(1) + 
					" elevatorID: " + eventLog.lastElement().get(2), t);
			break;
		case EMERGENCY:
			postToUI(eventLog.lastElement().toString(), t);
			break;
		case FAULT:
			break;
		case MAINTENANCE:
			postToUI(eventLog.lastElement().toString(), t);
			break;
		}
		
		Website A = new Website(Method.UpdateLog, getCurrentTime() + message);
		A.start();
	}
/*********************EVENT ALTERNATE**********************/
	public static void addEventLog(String message, MsgType t)
	{	
		Vector<String> temp = new Vector();	
		if(!eventRunOnce){
			temp.add("System Time");
			temp.add("Message");
			//temp.add("elevatorID");
			eventLog.add((Vector<String>)temp.clone());
			eventLogTime = getCurrentFileTime();
		}
		eventRunOnce = true;
		temp.clear();
		temp.add(getCurrentTime());
		temp.add(message);
		temp.add("");
		eventLog.add((Vector<String>)temp.clone());
		
		long timeDifference = cal.getInstance().getTimeInMillis() - oldEventTime;
		
		if(timeDifference > 0){
			writeEventFile = true;
			oldEventTime = cal.getInstance().getTimeInMillis();
		}
		else writeEventFile = false;

		
		if(writeEventFile){
			try {
				eventLogFile = new BufferedWriter(new FileWriter("eventLog "+eventLogTime+".txt", true));
				
				
				if(eventLog.lastElement() != null){
					eventLogFile.write("\r\n"+eventLog.lastElement().toString()+"\r\n");
					eventLogFile.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
		}
		
		postToUI(eventLog.lastElement().toString(), t);
		Website B = new Website(Method.UpdateLog, getCurrentTime() + message);
		B.start();
	}
	
	private static void postToUI(String msg, MsgType t) {
		if (mainUI != null) {
			switch (t) {
			case GENERAL:
				mainUI.postGeneralEventMsg(msg);
				break;
			case PASSENGER:
				mainUI.postPassengerEventMsg(msg);
				break;
			case ELEVATOR:
				mainUI.postElevatorEventMsg(msg);
				break;
			case EMERGENCY:
				mainUI.postEmergEventMsg(msg);
				break;
			case FAULT:
				mainUI.postFaultEventMsg(msg);
				break;
			case MAINTENANCE:
				mainUI.postMaintenanceEventMsg(msg);
				break;
			}
		}
	}
	
	public static void cleanLogs(){
		//10 vectors
		elevatorLog.clear();
		passengerLog.clear();
		controllerLog.clear();
		safetyLog.clear();
		eventLog.clear();
		passengerEmergencyLog.clear();
		elevatorQueue.clear();
		numberOfFloorsMoved.clear();
		lastFloor.clear();
		floorsMovedSinceLastFault.clear(); 
		
		elevatorRunOnce = false;
		passengerRunOnce = false;
		controllerRunOnce = false;
		safetyRunOnce = false;
		eventRunOnce = false;
		emergencyRunOnce = false;
		
	}
	
	
	public static void printLog(MsgType type){
		
		Vector<Vector<String>> tempString = new Vector(new Vector());
		Vector<Vector<Integer>> tempInt = new Vector(new Vector());
		Vector<int[]> tempIntArr = new Vector();
		
		System.out.println("--------------------\n" +
				   "Printing "+type.toString()+" log:\n" +
				   "--------------------\n\n");
		
		switch(type){
		case ELEVATOR:tempString = elevatorLog; break;
		case PASSENGER:tempString = passengerLog; break;
		case CONTROLLER:tempString = controllerLog; break;
		case EMERGENCY:tempString = passengerEmergencyLog; break;
		case SAFETY:tempString = safetyLog; break;
		case EVENT:tempString = eventLog; break;
		case ELEVATORQUEUE:tempInt = elevatorQueue; break;
		case FLOORSMOVED:tempIntArr = numberOfFloorsMoved; break;
		case LASTFLOOR:tempIntArr = lastFloor; break;
		default: break;
		}
		
		if(tempString.size() > 0){
			for(int i = 0; i < tempString.size(); i++){
				for(int j = 0; j < (tempString.get(i).size()-1); j++){
					System.out.print(tempString.get(i).get(j) + "\t");
				}
				System.out.println("");
			}
		} else if(tempInt.size() > 0){
			for(int i = 0; i < tempInt.size(); i++){
				for(int j = 0; j < (tempInt.get(i).size()-1); j++){
					System.out.println(tempInt.get(i).get(j) + "\t");
				}
				System.out.println("\n");
			}
		} else {
			for(int i = 0; i < tempIntArr.size(); i++){
				for(int j = 0; j < (tempIntArr.get(i).length-1); j++){
					System.out.println(tempIntArr.get(i)[j] + "\t");
				}
				System.out.println("\n");
			}
		}
	}
	
	
	/*track the distance of floors travelled:
	 *Controller will send float array of elevator floors,
	 *store previous floors find difference with current floors
	 *to calculate difference*/
//	public static float[] updateFloors(float currentFloors[]){
//		float[] temp = currentFloors;
//		oldFloors = new float[temp.length];
//		for(int i = 0; i < currentFloors.length; i++){
//			temp[i] = currentFloors[i] - oldFloors[i];
//			oldFloors[i] = currentFloors[i];
//		}
//		return temp;
//	}
	
	public static int[] updateFloorsSinceLastMaintenance(int[] durability){
		//this is called at each maintenance check; keeps track of how many floors have 
		//been travelled between maintenance checks
		int[] temp = new int[durability.length];
		
		for(int i = 0; i < durability.length; i++){
			temp[i] = 100 - durability[i];
		}
		
		floorsMovedSinceLastFault.add(getCurrentTime() + " " + temp.toString());
		
		return temp;
	}
	
	public static float avgPassengerWaitTime(long waitingTime, long ridingTime){
		//keep track of waiting time of each passenger
		//total time per passenger = waitingTime + ridingTime
		//avg time per passenger = (waitingTime for each passenger)/numPassengers
		//avg ridingTime = (ridingTime for each elevator)/numElevators
		//number of passengers serviced
		
		totalWaitTime += waitingTime; //total wait time for ALL passengers so far
		totalRidingTime += ridingTime; //total riding time for ALL passengers so far
		
		//moving average passenger wait time
		if (numPassengersServiced == 0) {
			avgTotalWait = (totalWaitTime + totalRidingTime)/1;
			avgRidingTime = totalRidingTime/1;
		} else {
			avgTotalWait = (totalWaitTime + totalRidingTime)/numPassengersServiced;
			avgRidingTime = totalRidingTime/numPassengersServiced;
		}
		if (numPassengersWaiting == 0)
			avgPassWait = totalWaitTime/1;
		else
			avgPassWait = totalWaitTime/numPassengersWaiting;
		
		
		//average wait time per elevator
		
		return avgPassWait;
	}
	
	public static void incPassengersWaiting() {
		numPassengersWaiting++;
	}
	
	public static void incPassengersServiced(){
		numPassengersServiced++;
	}
	
	public static float[] getElevatorPos(){
		float[] temp = new float[elevatorLog.size()-1];
		for(int i = 1; i < elevatorLog.size(); i++)
			temp[i-1] = Float.parseFloat(Log.getLogElement(MsgType.ELEVATOR, "ypos",i));
		return temp;
		
	}

	public static String getLogElement(MsgType typeOfLog, String logParameter, int elevatorID){
		
		Vector<Vector<String>> tempString = null;
		
		switch(typeOfLog){
		case ELEVATOR:tempString = elevatorLog; break;
		case PASSENGER:tempString = passengerLog; break;
		case CONTROLLER:tempString = controllerLog; break;
		case EMERGENCY:tempString = passengerEmergencyLog; break;
		case SAFETY:tempString = safetyLog; break;
		case EVENT:tempString = eventLog; break;
		default: break;
		}
		
		int count = 0, index,i=0;
		
		if(tempString != null){
			while(count < tempString.get(0).size()){ 
				if(tempString.get(0).get(count).toLowerCase().equals(logParameter)){
					index = count;
					if(elevatorID > -1 && elevatorID < elevatorLog.size())
						return tempString.get(elevatorID).get(count);
					else
						return tempString.lastElement().get(count);
				}
				count++;
			} 
		} 
		return null;
	}
	
	public static String getLogElement(MsgType typeOfLog, String logParameter){
		return getLogElement(typeOfLog, logParameter, -1);
	}
	
	public static Vector<Vector<String>> getLog(MsgType typeOfLog, String logParameter){
		
		Vector<Vector<String>> tempString = null;
		
		switch(typeOfLog){
		case ELEVATOR:tempString = elevatorLog; break;
		case PASSENGER:tempString = passengerLog; break;
		case CONTROLLER:tempString = controllerLog; break;
		case EMERGENCY:tempString = passengerEmergencyLog; break;
		case SAFETY:tempString = safetyLog; break;
		case EVENT:tempString = eventLog; break;
		default: break;
		}
	
		if(tempString != null){
			return tempString;
		} 
		return null;
	}
	
	//write status report
	//avg 
	public static void printStatusReport(){
		Vector temp;
		if(totalFloorsTravelled != null)
			temp = new Vector(totalFloorsTravelled.length);
		else
			temp = new Vector(1);
		if(!reportRunOnce){
			reportLogTime = getCurrentFileTime();
			reportRunOnce = true;
		}
		for(int i = 0; i < temp.size(); i++)
		{
			temp.add(totalFloorsTravelled[i]);
		}
		try{
			report = new BufferedWriter(new FileWriter("Summary Report "+reportLogTime+".txt", true));
			report.write("\r\n------------------------------\r\n" +
				   "SUMMARY REPORT\r\n" +
				   "------------------------------\r\n\r\n");
			report.write("NumElevators = " + (elevatorLog.size()-1)
						 +"\r\nAverage Passenger Wait Time: "+avgPassWait
						 +"\r\nAverage Passenger Riding Time: "+avgRidingTime
						 +"\r\nTotal Distance Travelled for each elevator: "+ temp.toString()
						 +"\r\nNumber of maintenance checks: "+maintenanceCount);
			report.close();
		} catch (IOException io){
			io.printStackTrace();
		}
		
	}
	
	/*get methods*/

	public static Vector<Vector<String>> getElevatorLog(){return (Vector<Vector<String>>) Log.elevatorLog.clone();}
	public static Vector<Vector<String>> getPassengerLog(){return (Vector<Vector<String>>) Log.passengerLog.clone();}
	public static Vector<Vector<String>> getControllerLog(){return (Vector<Vector<String>>)Log.controllerLog.clone();}
	public static Vector<Vector<String>> getSafetyLog(){return (Vector<Vector<String>>)Log.safetyLog.clone();}
	public static Vector<Vector<String>> getEventLog(){return (Vector<Vector<String>>)Log.eventLog.clone();}
	public static Vector<Vector<String>> getPassengerEmergencyLog(){return (Vector<Vector<String>>)Log.passengerEmergencyLog.clone();}
	public static Vector<Vector<Integer>> getElevatorQueue(){return (Vector<Vector<Integer>>)Log.elevatorQueue.clone();}
	public static Vector<int[]> getFloorsMoved(){return (Vector<int[]>)Log.numberOfFloorsMoved.clone();}
	public static Vector<int[]> getLastFloor(){return (Vector<int[]>)Log.lastFloor.clone();}
	public static long getAvgPassWait(){return avgPassWait;}
	public static long getAvgRidingTime(){return avgRidingTime;}
	public static float[] getTotalFloorsTravelled(){return totalFloorsTravelled;}
	
	
	
	/**********Iterator**********/
	public static Iterator getIterator(String type){return new LogIterator(type);}
	
	private static class LogIterator implements Iterator{
		int position = 0;
		String type;
		
		public LogIterator(String LogType){
			this.type = LogType;
		}
		
		public boolean hasNext(){
			if(type == "elevator") 
				if(elevatorLog.get(position) == null) return false;
				else return true;
			else if(type == "passenger") 
				if(passengerLog.get(position) == null) return false;
				else return true;
			else if(type == "controller") 
				if(controllerLog.get(position) == null) return false;
				else return true;
			else if(type == "safety")
				if(safetyLog.get(position) == null) return false;
				else return true;
			else if(type == "event")
				if(eventLog.get(position) == null) return false;
				else return true;
			else if(type == "passengeremergency") 
				if(passengerEmergencyLog.get(position) == null) return false;
				else return true;
			else if(type == "elevatorqueue") 
				if(elevatorQueue.get(position) == null) return false;
				else return true;
			else if(type == "floorsmoved") 
				if(numberOfFloorsMoved.get(position) == null) return false;
				else return true;
			else if(type == "lastfloor") 
				if(lastFloor.get(position) == null) return false;
				else return true;
			else return false;
		}
		
		
		public Vector<String> next() {
			if(hasNext()){
				if(type == "elevator") return elevatorLog.get(position++);
				else if(type == "passenger") return passengerLog.get(position++); 
				else if(type == "controller") return controllerLog.get(position++); 
				else if(type == "safety") return safetyLog.get(position++); 
				else if(type == "event") return eventLog.get(position++); 
				else if(type == "passengeremergency") return passengerEmergencyLog.get(position++);
				else return null;
			} else return null;
		}
		
		public Vector<Integer> nextInt(){
			if(hasNext()){
				if(type == "elevatorqueue") return elevatorQueue.get(position++);
				else return null;
			} else return null;
		}
		
		public int[] nextIntArr(){
			if(hasNext()){
				if(type == "floorsmoved") return numberOfFloorsMoved.get(position++);
				else if(type == "lastfloor") return lastFloor.get(position++);
				else return null;
			} else return null;
		}
		
		public void remove(){
			
		}
	}
	
	
	private static String getCurrentTime(){
		return dfm.format(cal.getInstance().getTime());
	}
	
	private static String getCurrentFileTime(){
		return fileDfm.format(cal.getInstance().getTime());
	}
	
	
	
}
