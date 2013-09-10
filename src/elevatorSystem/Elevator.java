package elevatorSystem;

import java.text.DecimalFormat;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import elevatorSystem.Log.MsgType;


public class Elevator extends Thread{

	public static int MAX_LOAD_WEIGHT = 2000;
	public static int MAX_PEOPLE = 20;
	private static double SPEED = 0.00625; // m/ms = 2.5 m/s
	private static double DISTANCE_BETWEEN_FLOORS = 1;

	//simulation pause
	private Boolean simRunning;

	//passenger data
	private Vector<Passenger> passengers;
	private int totalPassengersServiced;

	//thread data
	private int ID;
	private ElevatorStatus status;

	//floor data
	private int maxFloors;
	private int currentFloor;
	private float yPos;
	private float yPosOld;
	private float totalFloorsTraveledF;
	private Vector<Integer> queue;
	private boolean [] lockedFloors;
	private int totalFloorsTraveled;

	//elevator physics
	private int loadWeight;

	//elevator state
	private boolean betweenFloors;

	Semaphore mutex = new Semaphore(1, false);

	public enum ElevatorStatus{
		ACTIVE, HALTED, MAINTENANCE, EMERGENCY;
	}

	private boolean upFlag;


	private Controller controller;
	private boolean penthouse = false;


	public Elevator(Controller c, int numFloors, int id)
	{
		controller = c;

		ID = id;

		status = ElevatorStatus.ACTIVE;

		maxFloors = numFloors;
		currentFloor = 0;
		yPos = 0;
		yPosOld = 0;
		upFlag = true;
		queue = new Vector<Integer>();
		lockedFloors = new boolean[maxFloors];
		totalFloorsTraveled = 0;

		for(int i = 0; i < maxFloors; i++){
			lockedFloors[i] = false;
		}

		betweenFloors = false;

		loadWeight = 0;

		passengers = new Vector<Passenger>();
		totalPassengersServiced = 0;

		simRunning = false;
	}

	public void addLockedFloor(int floor)
	{
		lockedFloors[floor] = true;
	}

	public void removeLockedFloor(int floor)
	{
		lockedFloors[floor] = false;
	}

	public Boolean isOccupied() {
		if (passengers.size() > 0)
			return true;
		else 
			return false;
	}

	public boolean isHalted()
	{
		if(status == ElevatorStatus.HALTED){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isFloorLocked( int floor )
	{
		if(lockedFloors[floor] == true){
			return true;
		}
		else 
			return false;
	}

	public boolean isInMaintenance()
	{	
		if(status == ElevatorStatus.MAINTENANCE){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean isInEmergency()
	{	
		if(status == ElevatorStatus.EMERGENCY){
			return true;
		}
		else{
			return false;
		}
	}
	public float getYpos()
	{
		for(int j = 0; j < passengers.size(); j++){
			passengers.get(j).moveSimView(controller.getElePos(ID),yPos, -0.05f);
		}
		return yPos;
	}
	public int getID()
	{
		return ID;
	}

	public boolean getDirection()
	{
		return upFlag;
	}
	public int getMaxFloors()
	{
		return maxFloors-1;
	}

	public int getHighestFloorFromQ(){
		int largest = 0;
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).intValue() > largest)
				largest = queue.get(i).intValue();
		}
		return largest;
	}

	public int getLowestFloorFromQ(){
		int lowest = maxFloors;
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).intValue() < lowest)
				lowest = queue.get(i).intValue();
		}
		return lowest;
	}

	public int getCurrentFloor()
	{
		return currentFloor;
	}
	public ElevatorStatus getStatus() 
	{
		return status;
	}

	public void setActive(){
		status = ElevatorStatus.ACTIVE;
	}
	public void setHalted(){
		status = ElevatorStatus.HALTED;
	}
	public void setMaintenance(){
		status = ElevatorStatus.MAINTENANCE;
	}
	public void setEmergency(){
		status = ElevatorStatus.EMERGENCY;
	}

	public void changeDirection()
	{
		upFlag = !upFlag;
	}
	public void moveUp()
	{
		upFlag = true;
	}
	public void moveDown()
	{
		upFlag = false;
	}

	/*
	 * addToQueue
	 * 
	 * EFFECTS: Adds the floor to the elevator's destination list.  If the floor is outside the bounds of the building, 
	 * 			it is not added.  If the floor is locked, it is not added except when it is the top floor and there is 
	 * 			a penthouse in the system or when it is the bottom floor and the elevator is in maintenance mode.  No
	 * 			duplicate entries are added to the queue.
	 */
	public void addToQueue(int floor) {
		if (floor < 0 || floor >= maxFloors)
			return;

		if ((lockedFloors[floor] && !(floor == 0 && isInMaintenance())) && !(floor == maxFloors-1 && controller.hasPenthouse()))
			return;

		synchronized(this)
		{
			for (int i=0; i<queue.size(); i++) {
				if (queue.get(i).intValue() == floor)
					return;
			}

			queue.add(new Integer(floor));
		}
	}

	public void pauseSim() {
		simRunning = false;
	}

	public void startSim() {
		simRunning = true;
	}

	public void run()
	{
		int lastFloor = currentFloor;
		yPosOld = yPos;
		while(true){

			// check to see if controller wants to stop this thread
			Thread.yield(); // let another thread have some time perhaps to stop this one.
			if (Thread.currentThread().isInterrupted()) {
				break;
			}

			if (simRunning) {

				for (int i=0; i<passengers.size(); i++)
					passengers.get(i).setRidingTime(controller.getSimRunTime());
				synchronized(this)
				{
					if( queue.contains(new Integer(currentFloor)) && !betweenFloors  ){ //if the queue contains the current floor


						queue.remove(new Integer(currentFloor)); //remove the current floor from the queue
						//else Floor Sensor Is not Working we need to decide what to do here






						//if someone wants to get off the elevator
						int c = 0;
						for(int j =  (passengers.size() - 1); j >= 0; j--){
							if(passengers.get(j).getDestFloor() == currentFloor ){
								passengers.get(j).removeSimView();
								controller.removePassengerFromSystem(passengers.get(j));
								loadWeight -= passengers.get(j).getWeight();
								passengers.remove(j);
								c++; // keep count of passengers leaving
							}
						}
						totalPassengersServiced += c;

						int pickup = 0;
						//if someone wants to get on the elevator
						if (status != ElevatorStatus.MAINTENANCE){
							Vector<Passenger> tmp = controller.checkForPassengers(currentFloor,loadWeight,passengers.size());
							for (int k=0; k<tmp.size(); k++) {
								addToQueue(tmp.get(k).getDestFloor()); //adds the destination floor the the queue
								tmp.get(k).pickedUp(ID);
								passengers.add(tmp.get(k));			   //adds the passenger object to the elevators passenger vector	
								loadWeight += tmp.get(k).getWeight();  // add weight of passenger to loadWeight
								pickup++;
							}
						}
						//}

						//print the log
						Log.addEventLog("At floor "+currentFloor+": Pickup - "+pickup+" Dropoff - "+c, ID, MsgType.ELEVATOR);
						
						//pause the elevator for 2 seconds on the floor
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							break;
						}
					}



				}//end of if current floor check

				//change the dir of the elevator
				if( getHighestFloorFromQ() <= currentFloor){
					upFlag = false;
				}
				if( getLowestFloorFromQ() >= currentFloor){
					upFlag = true;
				}

				if (status == ElevatorStatus.MAINTENANCE && currentFloor != 0 && queue.isEmpty()) 
					addToQueue(0);

				if((queue.isEmpty() && status != ElevatorStatus.MAINTENANCE) && status != ElevatorStatus.EMERGENCY){
					status = ElevatorStatus.HALTED;
				}
				else if(status == ElevatorStatus.HALTED) {
					status = ElevatorStatus.ACTIVE;
				}

				if(status != ElevatorStatus.EMERGENCY){
					if (status != ElevatorStatus.HALTED && !(status == ElevatorStatus.MAINTENANCE && queue.isEmpty())) {
						if(upFlag){
							yPos += SPEED;
							if(yPos > DISTANCE_BETWEEN_FLOORS * maxFloors-1){
								yPos = (float) (DISTANCE_BETWEEN_FLOORS * maxFloors-1);
								upFlag = false;
							}
						}
						else if(!upFlag){
							yPos -= SPEED;

							if(yPos < 0){
								yPos = 0;
								upFlag = true;
							}
						}
					}
				}
				

				//the floor the elevator is on
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				float po = Float.valueOf(twoDForm.format(yPos));
				if(upFlag || (!upFlag && (po*100 % 100) == 0))
					currentFloor = (int) (po / DISTANCE_BETWEEN_FLOORS); 

				//whether or not elevator is between floors
				if ((((int)(Math.round(po*100))) % 100) != 0)
					betweenFloors = true;
				else
					betweenFloors = false;

				// keep track of total floors travelled
				if (lastFloor != currentFloor) {
					totalFloorsTraveled += Math.abs(lastFloor - currentFloor);
					lastFloor = currentFloor;
				}

				if ( yPosOld != yPos) {
					totalFloorsTraveledF += Math.abs(yPosOld - yPos);
					yPosOld = yPos;

				}

				try {
					Thread.sleep(13);
				} catch (InterruptedException e) {
					break;
				}

			}
		}//end of while
	}//end of run

	public int getTotalFloors() {
		return totalFloorsTraveled;
	}

	public float getTotalFloorsF(){
		return totalFloorsTraveledF;
	}

	public int getCurrentPassengerCount() {
		return passengers.size();
	}

	public int getTotalPassengerCount() {
		return totalPassengersServiced;
	}

	public int[] getQueue() {
		int[] ret = new int[queue.size()];
		for (int i=0 ; i<queue.size(); i++) 
			ret[i] = queue.get(i);
		return ret;
	}

	public int getQueueSize() {
		return queue.size();
	}

	public int getCurrentWeight() {
		return loadWeight;
	}

	public boolean[] getBounds() {
		return lockedFloors;
	}

	public void setPenthouse() {
		penthouse = true;
	}
	public boolean penthouseAccess() {
		return penthouse;
	}
}//end of Elevator