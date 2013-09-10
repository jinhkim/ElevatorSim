package elevatorSystem;

import j3dSimView.ElevatorSimView;
import j3dSimView.SimViewEmergencyAlert;
import j3dSimView.SimViewMaintenanceAlertBox;

import java.util.Vector;

import elevatorSystem.Log.MsgType;

import mainUI.mainWindow;

import elevatorSystem.algorithms.*;

import remoteUI.Website;
import remoteUI.Website.Method;

public class Controller extends Thread{

	// algorithms
	private int algorithm;
	private Algorithm[] algorithmList;

	//passenger manager
	private PassengerManager passengerManager;

	// elevators
	private Elevator[] elevators;
	private SimViewMaintenanceAlertBox[] maintenanceAlerts;
	private SimViewEmergencyAlert[] emergAlerts;
	
	// simulation view and ui
	private mainWindow mainGUI;
	private ElevatorSimView simView;

	// elevators
	private int numElevators;
	private int numFloors;
	private boolean penthouse;

	// time keeping
	private long simRunTime;
	private Boolean simRunning;
	
	//Website Update
	private int[] ElevatorPosition;
	private String[] ElevatorStatus;
	private boolean newerPosition;
	private boolean newerStatus;
	

	/*
	 * CONTROLLER CONSTRUCTOR
	 * 
	 * INPUT: a int for the number of floors in the building
	 * EFFECTS: if floor number is greater than 100 or less than 2, BossLiftGeneralException thrown
	 * 			otherwise, initializes the controller, including setup of algorithm and passenger vector
	 */
	public Controller()
	{
		algorithmList = new Algorithm[7];
		algorithmList[0] = new newRoundRobin();
		algorithmList[1] = new ClosestElevator();
		algorithmList[2] = new PassengerModE();
		algorithmList[3] = new RandomElevator();
		algorithmList[4] = new BalancedService();
		algorithmList[5] = new LeastBusy();
		algorithmList[6] = new LeastTraveled();
		algorithm = 0;

		simView = null;
		mainGUI = null;

		numElevators = 0;
		numFloors = 0;
		penthouse = false;

		simRunning = false;
		
		ElevatorPosition = new int[10];
		ElevatorStatus = new String[10];
		
		passengerManager = new PassengerManager(this);

	}

	/*
	 * createElevators()
	 * 
	 * INPUT: the number of elevators to control in the simulation
	 * EFFECTS: if the number of elevators isn't between 1 and 10, BossLiftGeneralException is thrown
	 * 			otherwise, creates elevator threads and stores them in elevators[]
	 */
	public void createElevators(int numElevators) throws BossLiftGeneralException
	{
		if (numElevators < 1 || numElevators > 10)
			throw new BossLiftGeneralException("Number of elevators not acceptable (must be between 1 and 10)");
		if (numFloors == 0)
			throw new BossLiftGeneralException("Floors have not been intialized yet!");

		this.numElevators = numElevators;
		elevators = new Elevator[numElevators];

		for(int i = 0; i < numElevators; i++){
			elevators[i] = new Elevator(this,numFloors, i); 
			if (penthouse) {
				elevators[i].addLockedFloor(getMaxFloor());
				elevators[i].setPenthouse();
			}
			elevators[i].start();
		}
		
		maintenanceAlerts = new SimViewMaintenanceAlertBox[numElevators];
		emergAlerts = new SimViewEmergencyAlert[numElevators];
		Log.addElevatorLog(elevators);
		Log.addEventLog(this.numElevators + " elevators created!", MsgType.ELEVATOR);
	}

	/*
	 * destroyElevators()
	 * 
	 * EFFECTS:	interrupts all the elevator threads which signals them to stop
	 * 			resets the elevator array. sets the UI pointers to null.
	 */
	public void destroy() {
		for(int i = 0; i < numElevators; i++){
			elevators[i].interrupt();
		}
		this.numElevators = 0;
		elevators = new Elevator[numElevators];
		
		mainGUI = null;
		simView = null;
	}

	/*
	 * setFloors()
	 * 
	 * EFFECTS: Adds f floors to the simulation and informs the passengerMananger object.
	 * 			If f is less than 2 or greater than 100, new BossLiftGeneralException is thrown.
	 */
	public void setFloors(int f) throws BossLiftGeneralException {
		if (f > 100 || f < 2)
			throw new BossLiftGeneralException("Number of floors not acceptable (must be greater than 2, no more than 100)");

		numFloors = f;

		passengerManager.setFloors(f);
	}
	
	/*
	 * setSimView()
	 * 
	 * EFFECTS: Sets the UI component references to mainWindow object and 3DCanvas class ElevatorSimView
	 */
	public void setSimView(mainWindow w, ElevatorSimView v) {
		simView = v;
		mainGUI = w;

		passengerManager.setSimView(simView, numFloors);
		for (int i=0; i<numElevators; i++) {
			maintenanceAlerts[i] = simView.createMaintenanceAlertBox(i);
			emergAlerts[i] = simView.createEmergencyAlertBox(i);
		}
	}
	
	
	/*
	 * newPassengerRequest()
	 * 
	 * EFFECTS: Injects a new passenger object into the elevator system and informs the passengerManager and simulation view
	 * 			objects.  If the passenger's arrival floor or destination is outside the floor bounds of the simulation, a 
	 * 			new BossLiftGeneralException is thrown.
	 */
	public void newPassengerRequest(Passenger p) throws BossLiftGeneralException {
		// Throw exception if the floor request is out of bounds
		if (p.getCurrentFloor() < 0 || p.getCurrentFloor() >= numFloors || p.getDestFloor() < 0 || p.getDestFloor() >= numFloors)
			throw new BossLiftGeneralException("Passenger input is not acceptable. Both current and destination floors must be in range.");
		// Throw exception if the passenger's request is not serviceable (going to or from locked floors)
		// Throw exception if the passenger wants to go to the penthouse but does not have the VIP key
		if (!validFloorReq(p.getCurrentFloor(), p.getDestFloor()) && !((p.getDestFloor() == getMaxFloor() || p.getCurrentFloor() == getMaxFloor()) && hasPenthouse() && p.isVIP()))
			throw new BossLiftGeneralException("Passenger request cannot be serviced. ");
	
		passengerManager.newPassengerWaiting(p);
		if (simView != null)
			passengerManager.setPassengerAlert(p.getCurrentFloor());
		Log.addEventLog("Passenger request at floor " + p.getCurrentFloor() +
						"; Destination floor " + p.getDestFloor(), MsgType.PASSENGER);
	}
	
	/* 
	 * Check that there is an elevator that can go to both floors
	 * Returns: true if an elevator that can service both floors exists
	 * 			false otherwise
	 */
	public boolean validFloorReq(int arrivalFl, int destFl) 
	{
		// Loop through elevators until we find one that can service both floors.
		for (int el = 0; el < getElevNum(); el++) {
			if (!isFloorLocked(el, arrivalFl) && !isFloorLocked(el, destFl)) 
			{
				return true;
			}
		}
		// If we can't find an elevator that can service both floors, return false.
		return false;
	}
	
	/*
	 * checkForPassengers()
	 * 
	 * EFFECTS: Called by the Elevator threads when they arrive at a floor and wish to pick up passengers
	 * 			The passengerManager populates ret with the passengers waiting on that floor and passes them
	 * 			to the elevator.
	 */
	public Vector<Passenger> checkForPassengers(int currentFloor, int currentWeight, int currentPassengers) {

		Vector<Passenger> ret = passengerManager.elevatorArrives(currentFloor,currentWeight,currentPassengers);
		return ret;
	}
	
	/*
	 * getPassengersInSystem()
	 * 
	 * EFFECTS: Returns the total number of passengers within the elevator system, whether waiting or riding.
	 */
	public int getPassengersInSystem() {
		return passengerManager.passInSystem();
	}
	
	/*
	 * removePassengerFromSystem()
	 * 
	 * EFFECTS: Informs passengerManager that the passenger has been dropped off and is no longer in the system.
	 */
	public void removePassengerFromSystem(Passenger p) {
		passengerManager.removeFromSystem(p);
		Log.incPassengersServiced();
	}
	
	
	/******************
	 * MAINTENANCE, FAULTS
	 */
	/*
	 * setElevatorActive()
	 * 
	 * EFFECTS: Returns elevator ID to active mode and removes emergency and maintenance alerts from simView
	 */
	public void setElevatorActive(int ID){
		elevators[ID].setActive();
		if (simView != null)
			emergAlerts[ID].remove();
		if (simView != null)
			maintenanceAlerts[ID].remove();
	}

	/*
	 * Inject and resolve emergencies
	 */
	public void setElevatorEmergency(int ID){
		elevators[ID].setEmergency();
		if (simView != null) {
			maintenanceAlerts[ID].remove();
			emergAlerts[ID].create();
		}
	}
	
	public void resolveEmergency(int ID) {
		setElevatorActive(ID);
	}
	
	/*
	 * Set and return from elevator maintenance
	 */
	public void setElevatorMaintenance(int ID){
		elevators[ID].setMaintenance();
		
		passengerManager.elevatorMaintenance(ID);
		
		if (simView != null)
			maintenanceAlerts[ID].create();
	}
	
	public void returnElevatorToService(int ID) {
		setElevatorActive(ID);
	}
	
	public void returnAllToService() {
		for (int i=0; i<elevators.length; i++)
			returnElevatorToService(i);
	}
	
	/*
	 * Inject and resolve floor hardware faults
	 */
	public void injectFloorFault(int f) {
		if (simView != null)
			simView.setFloorFault(f);
	}
	
	public void resolveFloorFault(int f) {
		if (simView != null)
			simView.resolveFloorFault(f);
	}
	
	/*
	 * Maintenance and Emergency Checkers
	 */
	public boolean checkForMaintenance(int ID){
		return elevators[ID].isInMaintenance();
	}
	
	public boolean checkForEmergency(int ID){
		return elevators[ID].isInEmergency();
	}
	
	/************
	 * Locked floors and penthouse functionality
	 */
	public boolean isFloorLocked( int elevator, int floor){
		if(elevators[elevator].isFloorLocked(floor)){
			return true;
		}
		else
			return false;
	}

	public void lockFloor(int elevator, int floor){
		elevators[elevator].addLockedFloor(floor);
	}

	public void unlockFloor(int elevator, int floor){
		elevators[elevator].removeLockedFloor(floor);
	}
	public void addPenthouse(){
		penthouse = true;
		for(int e = 0; e < elevators.length; e++){
			elevators[e].addLockedFloor(elevators[e].getMaxFloors());
			elevators[e].setPenthouse();
		}
	}
	public boolean hasPenthouse() {
		return penthouse;
	}


	/*
	 * THREAD RUN
	 */
	public void run()
	{			
		long last = System.currentTimeMillis(); // inc simRunTime
		long lastGUpdate = 0; // allow for fps calc
		simRunTime = 0; // main time keeper

		while(true){
			try {
				// check to see if user wants to stop this thread
				Thread.yield(); // let another thread have some time perhaps to stop this one.
				if (Thread.currentThread().isInterrupted()) {
					break;
				}

				if (simRunning) {
					// control elevators
					algorithmList[algorithm].doAlgorithm(elevators, passengerManager.getWaitingPassengers());

					lastGUpdate += System.currentTimeMillis() - last;
					last = System.currentTimeMillis();

					if (lastGUpdate > 30) { // if > ~30 ms since last ui update
						simRunTime += lastGUpdate;
						lastGUpdate = 0;
						
						passengerManager.incrementWaitTime(simRunTime);

						if (mainGUI != null) {	
							mainGUI.setSimTime(simRunTime);
							mainGUI.updateTotalPassengersInSystem(passengerManager.getTotalPassengers());
							mainGUI.updatePassengersOnFloor(passengerManager.getWaitingPassengers());
							mainGUI.updateElevatorTabs(elevators);
						}

						if (simView != null) {
							for (int ele=0; ele < numElevators; ele++) {
								simView.moveElevator(ele, elevators[ele].getYpos());
							}
						}
					} else
						Log.addElevatorLog(elevators);
						
					
					//Updating the website (Elevator Position)
				    newerPosition = false;
				    
					for(int i=0; i<elevators.length; i++)
					{
						if(ElevatorPosition[i] != elevators[i].getCurrentFloor())
						{
							ElevatorPosition[i] = elevators[i].getCurrentFloor();
							newerPosition = true;
							//break;
						}							
					}
					
					if(newerPosition)
					{
						String[] Position = new String[10];
						for(int i=0; i<elevators.length; i++)
							Position[i] = String.valueOf(ElevatorPosition[i]);
						for(int i=elevators.length; i<10; i++)
							Position[i] = String.valueOf(0);
						Website A = new Website(Method.UpdateElevatorPosition, Position);
						A.start();
			        }
					
					//Updating the website (Elevator Status)
				    newerStatus = false;
				    
					for(int i=0; i<elevators.length; i++)
					{
						if(ElevatorStatus[i] != elevators[i].getStatus().toString())
						{
							ElevatorStatus[i] = elevators[i].getStatus().toString();
							newerStatus = true;
						}							
					}
					
					if(newerStatus)
					{
						String[] Status = new String[10];
						for(int i=0; i<elevators.length; i++)
							Status[i] = ElevatorStatus[i];
						for(int i=elevators.length; i<10; i++)
							Status[i] = "OFF";
						Website A = new Website(Method.UpdateElevatorStatus, Status);
						A.start();
			        }
				

				} else {
					last = System.currentTimeMillis(); // continue to update last even when paused to keep simRunTime accurate

					// prevent CPU usage when sim not running
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						break;
					}

				}
			}catch (Exception e) {
				if (mainGUI != null)
					mainGUI.displayError(e.toString());
					e.printStackTrace();
			}
		}
	}

	


	/*
	 * Simulation timing, pausing and un-pausing
	 */
	public Boolean isRunning() {
		return simRunning;
	}

	public void runSim() {
		simRunning = true;
		for(int i = 0; i < numElevators; i++){
			elevators[i].startSim();
		}
		Log.addEventLog("Simulator running!", MsgType.GENERAL);
	}

	public void pauseSim() {
		simRunning = false;
		for(int i = 0; i < numElevators; i++){
			elevators[i].pauseSim();
		}
		Log.addEventLog("Simulator Paused", MsgType.GENERAL);
	}

	public long getSimRunTime() {
		return simRunTime;
	}
	
	/*********
	 * Algorithm controls
	 */
	public String[] getAlgorithms() {
		String[] ret = new String[algorithmList.length];
		for (int i=0 ; i < ret.length; i++)
			ret[i] = algorithmList[i].getName();

		return ret;
	}

	public String getAlgorithmDesc(int a) {
		return algorithmList[a].getDescription();
	}

	public int getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(int a) {
		algorithm = a;
		Log.addEventLog("The system is now using the algorithm: "+algorithmList[algorithm].getName(), MsgType.GENERAL);		
	}

	
	/****************
	 * Elevator Information
	 */
	public Boolean passengerInElev(int ID) {
		return elevators[ID].isOccupied();
	}

	public float getElePos(int ID) {
		if (simView == null)
			return 0f;
		return simView.getElePos(ID);
	}

	public int getElevNum() {
		return numElevators;
	}

	public int getMaxFloor() {
		return numFloors-1;
	}
	
		public int getCurrentFloor(int elevID) {
		return elevators[elevID].getCurrentFloor();
	}
	public Elevator.ElevatorStatus getElevStatus(int elevID) {
		return elevators[elevID].getStatus();
	}
	
	public int elevatorDistance(int elevatorID){
		
		return elevators[elevatorID].getTotalFloors();
	
	}

	public boolean[] getElevatorBounds(int i) {
		return elevators[i].getBounds();
	}

	
}