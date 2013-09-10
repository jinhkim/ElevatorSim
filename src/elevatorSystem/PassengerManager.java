package elevatorSystem;

import j3dSimView.ElevatorSimView;
import j3dSimView.SimViewPassengerAlertBox;

import java.util.Vector;

public class PassengerManager {

	// all passengers within the system
	private Vector<Passenger> totalPassengers = new Vector<Passenger>();
	
	// all passengers waiting for an elevator
	private Vector<Passenger>[] waitingPassengers;
	
	// simulation view waiting passenger alert boxes
	private SimViewPassengerAlertBox[] passengerAlerts;
	
	// 3DCanvas
	private ElevatorSimView simView = null;
	
	private Controller controller;
	
	public PassengerManager(Controller c) {
		controller = c;
	}
	
	/*
	 * Initialization methods:
	 * setFloors(): sets the number of floors in the simulation
	 * setSimView(): sets the pointer to the 3DCanvas which the passenger alerts will be displayed
	 */
	public void setFloors(int f) {
		waitingPassengers = new Vector[f];
		for (int i=0; i<f; i++) {
			waitingPassengers[i] = new Vector<Passenger>();
		}

		passengerAlerts = new SimViewPassengerAlertBox[f];
	}
	
	public void setSimView(ElevatorSimView v, int numFloors) {
		simView  = v;
		for (int i=0; i<numFloors; i++) {
			passengerAlerts[i] = simView.createPassengerAlert(i);
		}
	}

	
	public synchronized void newPassengerWaiting(Passenger p) {
		p.setArrivalTime(controller.getSimRunTime());
		waitingPassengers[p.getCurrentFloor()].add(p);
		totalPassengers.add(p);
	}

	public synchronized void setPassengerAlert(int currentFloor) {
		passengerAlerts[currentFloor].addPassenger();
	}

	public synchronized void removeFromSystem(Passenger p) {
		totalPassengers.remove(p);
		p.setRidingTime(controller.getSimRunTime());
	}
	

	public synchronized Vector<Passenger> elevatorArrives(int currentFloor, int currentWeight, int currentPassengers) {
		Vector<Passenger> ret = new Vector<Passenger>();
		for (int i=(waitingPassengers[currentFloor].size()-1); i >=0 ; i--) {
			if (waitingPassengers[currentFloor].get(i).serviced()) {
				// if adding passenger will break weight or people limit, stop adding people
				if (waitingPassengers[currentFloor].get(i).getWeight()+currentWeight > Elevator.MAX_LOAD_WEIGHT || currentPassengers+1 > Elevator.MAX_PEOPLE )
					break;
				
				if (simView != null) {
					passengerAlerts[currentFloor].passengerExit();
					simView.createPassenger(waitingPassengers[currentFloor].get(i));
				}
				
				ret.add(waitingPassengers[currentFloor].get(i));
				currentWeight += waitingPassengers[currentFloor].get(i).getWeight();
				currentPassengers++;
				waitingPassengers[currentFloor].get(i).setWaitingTime(controller.getSimRunTime());
				Log.incPassengersWaiting();
				waitingPassengers[currentFloor].remove(i);	///////// changes size here
				waitingPassengers[currentFloor].trimToSize();
				
			}
		}
		
		// unservice passenger not picked up
		for (int i=0; i<waitingPassengers[currentFloor].size(); i++)
			waitingPassengers[currentFloor].get(i).unservicePassenger();
		
		return ret;
	}

	public synchronized Vector<Passenger>[] getWaitingPassengers() {
		Vector<Passenger>[] tmp = waitingPassengers.clone();
		for (int i=0; i < tmp.length; i++) {
			tmp[i] = (Vector<Passenger>) waitingPassengers[i].clone();
		}		
		return tmp;
	}
	
	public synchronized Vector<Passenger> getTotalPassengers() {
		Vector<Passenger> tmp = (Vector<Passenger>) totalPassengers.clone();	
		tmp.trimToSize();
		return tmp;
	}

	public void elevatorMaintenance(int iD) {
		for (int i=0; i<waitingPassengers.length; i++) {
			for (int j=0; j<waitingPassengers[i].size(); j++) {
				Passenger p = waitingPassengers[i].get(j);
				if (p.serviced() && p.getElevatorID() == iD)
					p.unservicePassenger();
			}
		}
	}

	public int passInSystem() {
		return totalPassengers.size();
	}
	
	public synchronized void incrementWaitTime(long t) {
		for (int i=0; i<waitingPassengers.length; i++) {
			for (int j=0; j<waitingPassengers[i].size(); j++) {
				waitingPassengers[i].get(j).setWaitingTime(t);
			}
		}
	}
	
	
}
