package elevatorSystem;

import elevatorSystem.Log.MsgType;




/*
Changes made: 
Oct 29 2011
	make every method static so that Safety thread is not initialized more than once

	added passengerEmergencyRequest() and randomMaintenanceReqest()

Nov 6 2011

	added logging events
	
	signals to Controller an event has occurred 

	autoMaintenanceRequest() calls in "public void run()" for elevators that travel a certain distance
*/


public class Safety extends Thread{
	

	private int timer = 0;
	private int maxMaintenanceTime = 10;
	private int maxEmergencyTime = 5;
	private int durability = 100;
	private int threshold = 99;
	private int[] numberOfFloorsMoved;
	private Controller controller;
	private int[] lastFloor;
	private Boolean[] FloorSensor;
	private Boolean[] elevatorEmergency;
	private Boolean[] elevatorMaintenance;
	private int numberOfElevators;
	private int numberOfFloors;
	private Boolean[][] elevatorFloorLog;
	private Boolean fire = false;
	private Boolean earthquake = false;
	private int earthquakeTimer = 0;
	
	
	private int[] emergencyTimer;
	
	private int[] elevatorDurability;//the number of floors travelled before an Elevator fails
	
	private int[] durabilityThreshold;//the number of floors remaining in elevatorDurability 
												//in which autoMaintenanceRequest() can be called
	
	private int elevatorInMaintenance = 0;	//the current elevator in auto maintenance, only one 
												//elevator can be in "auto maintenance" at a time
	
	public Safety(Controller c)
	{
		controller = c;
		
	}
	
	private void addLog()//takes in int elevatorID parameter, need to make changes in Architecture Doc
	{
		//sends safety log to Log.java
		Log.addSafetyLog(numberOfFloorsMoved, lastFloor, 
				elevatorDurability, durabilityThreshold, elevatorInMaintenance, false);
	}
	
	public void passengerEmergencyRequest(int elevatorID) throws BossLiftGeneralException
	{
		if(!controller.passengerInElev(elevatorID))
		{
		throw new BossLiftGeneralException("Emergency Request Exception: There are no passengers in Elevator " + elevatorID);
		}
		else if(!elevatorEmergency[elevatorID] && !fire && !earthquake)
		{
		//log event and send to controller to handle request
		Log.addEventLog("Emergency Passenger Request in Elevator"  , elevatorID, MsgType.EMERGENCY);
		//System.out.println("eID3: "+ elevatorID);
		elevatorEmergency[elevatorID] = true;
		controller.setElevatorEmergency(elevatorID);
		}
	}
	
	public void earthquakeEmergency()
	{
		Log.addEventLog("EARTHQUAKE!!"  , MsgType.EMERGENCY);
		for(int i = 0; i < controller.getElevNum(); i++){
		controller.setElevatorEmergency(i);
		}
		earthquakeTimer = 0;
		earthquake = true;
	}
	
	public void resolveEarthquake()
	{
		//log event and send to controller to handle request
				Log.addEventLog("Building is safe"  , MsgType.EMERGENCY);
				for(int i = 0; i < controller.getElevNum(); i++){
					controller.returnElevatorToService(i);
				}
				earthquakeTimer = 0;
				earthquake = false;
	}
	
	
	public void fireEmergency()
	{
		//log event and send to controller to handle request
		Log.addEventLog("BUILDING IS ON FIRE!!"  , MsgType.EMERGENCY);
		for(int i = 0; i < controller.getElevNum(); i++){
		controller.setElevatorMaintenance(i);
		
		}
		fire = true;
	}
	
	public void resolveFire()
	{
	
		Log.addEventLog("Building is safe"  , MsgType.EMERGENCY);
		for(int i = 0; i < controller.getElevNum(); i++){
			controller.returnElevatorToService(i);
		}
		fire = false;
	}
	
	public void autoMaintenanceRequest(int elevatorID)
	{
		if(!fire && !earthquake){
		//log event and send to controller to handle request
		if(!elevatorEmergency[elevatorID]){
			Log.addEventLog("Maintenance Request in Elevator"  , elevatorID, MsgType.MAINTENANCE);
			controller.setElevatorMaintenance(elevatorID);
		}
		elevatorMaintenance[elevatorID] = true;
		}
		
	}
	
	
	public void SendSafetyStatus(int elevatorID, boolean sensor)
	{
		//if an elevator sensor for a given floor fails, stop the elevator
		if(sensor && !controller.checkForEmergency(elevatorID))
		{
			elevatorEmergency[elevatorID] = true;
			Log.addEventLog("Fault in Elevator" , elevatorID, MsgType.EMERGENCY);
		controller.setElevatorEmergency(elevatorID);
		}
	}
	

	public void finishElevatorMaintenance(int elevatorID)
	{
		Log.addEventLog("Finished Maintenance Request in Elevator"  , elevatorID, MsgType.MAINTENANCE);
		controller.returnElevatorToService(elevatorID);
		//timer = 0;
		resetElevatorDurability(elevatorID);
		elevatorMaintenance[elevatorID] = false;
	}
	
	public void finishElevatorEmergency(int elevatorID)
	{
		Log.addEventLog("Finished Emergency Request in Elevator"  , elevatorID, MsgType.EMERGENCY);
		
	
		controller.resolveEmergency(elevatorID);
		elevatorEmergency[elevatorID] = false;
		emergencyTimer[elevatorID] = 0;

		if(elevatorMaintenance[elevatorID])
		{
			autoMaintenanceRequest(elevatorID);
		}
		
	}
	public void resetElevatorDurability(int elevatorID)
	{
		//if an Elevator is repaired, reset the durability of the elevator to 100%
		elevatorDurability[elevatorID] = durability;
		Log.addSafetyLog(numberOfFloorsMoved, lastFloor, 
				elevatorDurability, durabilityThreshold, elevatorInMaintenance, true);
	}

	public synchronized void  injectFloorFault(int floor){
		//Log Elevators Floor bounds
		for(int i = 0; i < controller.getElevNum(); i++)
		{
			for(int j =0 ; j < controller.getMaxFloor(); j++ )
				elevatorFloorLog[i][j] = controller.isFloorLocked(i, j);
		}
		FloorSensor[floor] = false;
		controller.injectFloorFault(floor);
		Log.addEventLog("Fault at floor" + floor , MsgType.EMERGENCY);

	}
	public synchronized void resolveFloorFault(int floor){
		if(! FloorSensor[floor])
		{
			for(int i = 0 ; i < controller.getElevNum() ; i++){
				for(int j = 0 ; j < controller.getMaxFloor(); j++){
					if(!elevatorFloorLog[i][j])
						controller.unlockFloor(i, j);			
				}
			}
		 FloorSensor[floor] = true;
		 controller.resolveFloorFault(floor);
			Log.addEventLog("Fault resolved at floor" + floor , MsgType.EMERGENCY);

		}
	}
	public void floorUpdate( int elevatorID, int currentFloor)
	{
		numberOfFloorsMoved[elevatorID] += java.lang.Math.abs(lastFloor[elevatorID] - currentFloor);
		elevatorDurability[elevatorID] = elevatorDurability[elevatorID] - 
				java.lang.Math.abs(lastFloor[elevatorID] - currentFloor);
		lastFloor[elevatorID] = currentFloor;
	}
	
	public void setThreshold(int ID, int i)throws BossLiftGeneralException{
		if(i>elevatorDurability[ID])
		{
			throw new BossLiftGeneralException("Maintenance threshold > elevator durability");
		}
		durabilityThreshold[ID] = i;
	}
	
	public void setMaxDurability(int ID, int i)throws BossLiftGeneralException
	{
		if(i<durabilityThreshold[ID])
		{
			throw new BossLiftGeneralException("Maintenance threshold > elevator durability");

		}
		elevatorDurability[ID] = i;
	}
	
	public int[] getLastFloor()
	{
		return lastFloor;
	}
	
	public int[] getNumberOfFloors()
	{
		return numberOfFloorsMoved;
	}
	

	public void run()
	{
				//initialize safety arrays
				numberOfFloorsMoved = new int[controller.getElevNum()];
				lastFloor = new int[controller.getElevNum()];
				elevatorDurability = new int[controller.getElevNum()];
				durabilityThreshold = new int[controller.getElevNum()];
				FloorSensor = new Boolean[controller.getMaxFloor()+1];
				elevatorEmergency = new Boolean[controller.getElevNum()];
				numberOfElevators = controller.getElevNum();
				numberOfFloors = controller.getMaxFloor();
				elevatorFloorLog = new Boolean[controller.getElevNum()][controller.getMaxFloor()];
				int temp = durability;
				int temp2 = -1;
				emergencyTimer = new int[controller.getElevNum()];
				elevatorMaintenance = new Boolean[controller.getElevNum()];
				
				durability = controller.getMaxFloor() * 8;
				threshold = controller.getMaxFloor() * 3;
				
				for (int i = 0; i<controller.getElevNum(); i++)
				{
				numberOfFloorsMoved[i] = 0;
				lastFloor[i] = 0;
				elevatorDurability[i] = durability;
				durabilityThreshold[i] = threshold;
				emergencyTimer[i] = 0;
				
				elevatorEmergency[i] = false;
				elevatorMaintenance[i] = false;
				}
				
				for(int j = 0; j < controller.getMaxFloor(); j++){
					FloorSensor[j] = true;
				}
				// Initialize Sensors to True 
				
				
		while(true)
		{
			// check to see if user wants to stop this thread
			Thread.yield(); // let another thread have some time perhaps to stop this one.
		    if (Thread.currentThread().isInterrupted()) {
		      break;
		    }
		    // check to see if Elevator Sensors are working properly
		    // If there is a floor sensor problem lock out all elevators from that floor
		    for(int j=0;  j < controller.getMaxFloor(); j++){
		        	if(FloorSensor[j] == false){
		        		for(int k = 0; k < numberOfElevators; k++)
		        			controller.lockFloor(k,j);	
		        	}
		        		
		        		
		    }
		  //  System.out.println("pause safety");
		    try {
				Thread.sleep(1000);	//1 second delay (~between executions)
			} catch (InterruptedException e) {
				break;
			}
		    
		    for(int c = 0; c< controller.getElevNum(); c++)
		    {
		    	floorUpdate(c, controller.getCurrentFloor(c));
		    }
		    
		    addLog();
		 //   System.out.println("eid durability: " + elevatorDurability[0]);
		    
		    if(!controller.checkForMaintenance(elevatorInMaintenance) && !controller.checkForEmergency(elevatorInMaintenance))//if no elevators are in maintenance
		    {
		    	temp = durability;
		    	temp2 = -1;
			    for (int i = 0; i<controller.getElevNum(); i++)
			    {
			    	if (elevatorDurability[i] <= durabilityThreshold[i])
			    	{
			    		if(elevatorDurability[i] < temp){
			    			temp = elevatorDurability[i];
			    			temp2 = i;
			    		}
			    	}
			    }
			    if(temp2 >= 0)
			    {
			    elevatorInMaintenance = temp2;
	    		autoMaintenanceRequest(temp2);
	    		timer = 0;
			    }
	    		
		    }
		    else if (controller.checkForMaintenance(elevatorInMaintenance) && lastFloor[elevatorInMaintenance] == 0 && controller.isRunning() && !fire)
		    {
		    	if(timer >= maxMaintenanceTime )
		    	{
		    		//controller.setElevatorActive(elevatorInMaintenance);
		    		finishElevatorMaintenance(elevatorInMaintenance);
		    		timer = 0;
		    	}
		    	timer++;
		    }
		    
		 /*resets elevator emergency status to active when emergency is completed*/   
		    for(int a = 0; a <controller.getElevNum(); a++)
		    {
		    	if(elevatorEmergency[a] && controller.isRunning() && !earthquake)
		    	{
		    		emergencyTimer[a]++;
		    	}
		    	if(controller.isRunning() && earthquake)
		    	{
		    		earthquakeTimer++;
		    	}
		    	if(emergencyTimer[a] >= maxEmergencyTime)
		    	{
		    		//System.out.println("suck it");
		    		//controller.setElevatorActive(a);
		    		finishElevatorEmergency(a);
		    		emergencyTimer[a] = 0;
		    	}
		    	if(earthquakeTimer >= (maxEmergencyTime + 5))
		    	{
		    		resolveEarthquake();
		    	}
		    }
		}
	}
}