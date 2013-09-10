package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Elevator.ElevatorStatus;
import elevatorSystem.Passenger;

/* Abstract class: Algorithm
 * Description: This is the superclass of all implemented algorithms, 
 * 				but is never to be instantiated.
 */
public abstract class Algorithm {
	
	/* Abstract methods - to be implemented in all subclasses */
	public abstract void doAlgorithm(Elevator[] e, Vector<Passenger>[] p);
	public abstract String getName();
	public abstract String getDescription();
	
	/* Methods - may be used by all subclasses */
	
	/* Function: isInService()
	 * Parameters: e - array of elevators in the system
	 * 			   elevID - the elevator we're interested in
	 * Returns: true, if elevator status is ACTIVE or HALTED
	 * 		    false, if elevator status is EMERGENCY, MAINTENANCE, ...
	 */
	boolean isInService (Elevator[] e, int elevID) {
		if (e[elevID].getStatus() == ElevatorStatus.ACTIVE || e[elevID].getStatus() == ElevatorStatus.HALTED) {
			return true;
		}
		return false;
	}
	
	/* Function: canServiceFloor()
	 * Parameters: e - array of elevators in the system
	 * 			   elevID - the elevator we're interested in
	 * 			   floor - the floor we're interested in
	 * Returns: true, if elevator can service "floor"
	 * 		    false, otherwise
	 */
	boolean canAccessFloor (Elevator[] e, int elevID, int floor) {
		if ( !e[elevID].isFloorLocked(floor) || (e[elevID].isFloorLocked(floor) && floor == e[elevID].getMaxFloors() && e[elevID].penthouseAccess())) {
			return true;
		}
		return false;
	}
	
}
