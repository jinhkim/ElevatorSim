package elevatorSystem;

import j3dSimView.SimViewPassenger;


public class Passenger {
	
	private int weight;
	private int currentFloor;
	private int destFloor;
	private long timeOfArrival;// is this the time the passenger enters the building?
	private int elevatorID;
	private boolean servicedFlag;
	private boolean upFlag;


	private boolean VIP;// may be used for Penthouse access
	
	private String name;

	private SimViewPassenger simView;
	
	private PassengerStatus status;
	private long ridingTime;
	private long waitingTime;
	private long prevWait = 0;
	private long prevRide = 0;
	
	private static String[] passengerNames = {"Sally","Billy","Jason","Frank","Hubert","Dilbert","Francis","Edward","Jack","Simon","Rachel","Susan"};
	private static int passengerNumber = 0;
	
	public enum PassengerStatus {
		WAITING,SERVICED,RIDING
	}
	
	public Passenger(int arrive, int dest, int passengerWeight, boolean VIPstatus) throws BossLiftGeneralException
	{		
		if(arrive == dest)
			throw new BossLiftGeneralException("Passenger destination floor is the same as the passenger's current floor");
		if (passengerWeight <= 0)
			throw new BossLiftGeneralException("Passenger must have a weight > 0");
		
		currentFloor = arrive;
		destFloor = dest;
		
		status = PassengerStatus.WAITING;
		name = passengerNames[passengerNumber % passengerNames.length]+passengerNumber;
		passengerNumber++; 

		servicedFlag = false;
		
		if(dest - arrive > 0) 
		{
			upFlag = true;
		}
		else
			upFlag = false;

		weight = passengerWeight;
		
		VIP = VIPstatus;
		
		simView = null;
		
		elevatorID = -1;	
	}
	
	public void setArrivalTime(long t){
		timeOfArrival = t;
	}
	
	public void setWaitingTime(long t) {
		long LoggedWait;
		waitingTime = t - timeOfArrival;
		LoggedWait = waitingTime - prevWait;
		prevWait = waitingTime;
		Log.avgPassengerWaitTime(LoggedWait,0);
	}
	
	public void setRidingTime(long t) {
		long LoggedRide;
		ridingTime = t - waitingTime;
		LoggedRide = ridingTime - prevRide;
		prevRide = ridingTime;
		Log.avgPassengerWaitTime(0,LoggedRide);
	}
	
	public long getWaitingTime() {
		return waitingTime;
	}
	
	public long getRidingTime() {
		return ridingTime;
	}
	
	public boolean isVIP() {
		return VIP;
	}
	
	public void setSimView(SimViewPassenger s) {
		simView = s;
	}
	
	public boolean serviced()
	{
		return servicedFlag;
	}
	public void servicePassenger(int ID)
	{
		elevatorID = ID;
		servicedFlag = true;
		status = PassengerStatus.SERVICED;
	}
	public void unservicePassenger() {
		servicedFlag = false;
		status = PassengerStatus.WAITING;
		elevatorID = -1;
	}
	
	public void requestFloor(int floor)
	{		
		destFloor = floor;
	}
	
	public boolean getUpFlag(){
		return upFlag;
	}
	public int getWeight()
	{
		return weight;
	}
	
	public int getCurrentFloor()
	{
		return currentFloor;
	}
	
	public int getDestFloor()
	{
		return destFloor;
	}
	
	public long getTimeOfArrival()
	{
		return timeOfArrival;
	}
	
	public void pickedUp(int id) {
		elevatorID = id;
		status = PassengerStatus.RIDING;
	}
	
	public int getElevatorID()
	{
		return elevatorID;
	}

	public void removeSimView() {
		if (simView != null)
		simView.remove();		
	}
	

	public void moveSimView(float elePos, float yPos, float f) {
		if (simView != null)
		simView.move(elePos, yPos, f);		
	}
	
	public String getName() {
		return name;
	}
	
	public String getStatus() {
		if (status == PassengerStatus.RIDING) 
			return "RIDING "+elevatorID;
		else
			return status.toString();
	}
	
	public void setStatus(PassengerStatus s) {
		status = s;
	}

	
}
