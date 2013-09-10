package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Passenger;

public class BalancedService extends Algorithm{

	// Keeping state of next elevator to assign
	private int[] numAssignments;
	private boolean firstTime = true;
	private int nextElevator = 0;

	@Override
	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p) {
		
		/* Initialize the array the first time this method is called.
		 */
		if (firstTime == true) {
			numAssignments = new int[e.length];
			for (int i = 0; i < e.length; i++) {
				numAssignments[i] = 0;
			}
			firstTime = false;
		}
	
		/* Get passenger waiting for each floor.
		 */
		int [] waitingPass = new int [p.length];

		//check if there is a passenger on the floor
		for (int floors=0; floors<p.length; floors++){
			waitingPass[floors] = 0;
			p[floors].trimToSize();
			for (int pass=0; pass<p[floors].size(); pass++) {
				if (!p[floors].get(pass).serviced())
				{
					//PASSENGER WAITING ON floor = floors
					waitingPass[floors] = 1;

				}	
			}		
		}

		for(int i=0; i < waitingPass.length; i++)
		{
			//check if there is passenger waiting on the current floor
			if(waitingPass[i] == 1){
				int minIdx = findMin(numAssignments);
				// This elevator can access the floor AND it is in-service
				if	((canAccessFloor(e, minIdx, i)) && (isInService(e, minIdx)) ) 
				{	
					// Loop through all the passengers on the floor
					for (int pass=0; pass<p[i].size(); pass++) { 
						// If the passenger is not serviced AND the elevator can go to his/her destination
						if (!p[i].get(pass).serviced() && canAccessFloor(e, minIdx, p[i].get(pass).getDestFloor()) )
						{		
							//System.out.println("Balanced: Sending elevator " + minIdx + " to floor " + i);
							p[i].get(pass).servicePassenger(minIdx);
							e[minIdx].addToQueue(i);	//add the floor to the elevators queue to stop at
							numAssignments[minIdx]++ ;
						}
					}
				} else {
					for (int j=0 ; j<e.length; j++) {
						// The next elevator that can access the floor AND is in-service
						if	((canAccessFloor(e, nextElevator, i)) && (isInService(e, nextElevator)) ) 
						{
							// Loop through all the passengers on the floor
							for (int pass=0; pass<p[i].size(); pass++) { 
								// If the passenger is not serviced AND the elevator can go to his/her destination
								if (!p[i].get(pass).serviced() && canAccessFloor(e, nextElevator, p[i].get(pass).getDestFloor()) )
								{		
									//System.out.println("Balanced: Sending elevator " + nextElevator + " to floor " + i);
									p[i].get(pass).servicePassenger(nextElevator);
									e[nextElevator].addToQueue(i);			//add the floor to the elevators queue to stop at
									numAssignments[nextElevator]++;
								}
							}
							nextElevator++;
							nextElevator = nextElevator % e.length;
							break;
						}
						else
						{
							nextElevator++;
							nextElevator = nextElevator % e.length;
						}
					}
					
				}
			}
		}

	}
	
	/* findMin()
	 * Returns the index of the elevator with the lowest number of passenger request assignments.
	 */
	private int findMin (int[] numAssignments) {
		int minIdx = 0;
		for (int i = 0; i < numAssignments.length; i++) {
			if (numAssignments[i] < numAssignments[minIdx]) {
				minIdx = i;
			}
		}
		return minIdx;
	}

	@Override
	public String getName() {
		return "Balanced Service";
	}

	@Override
	public String getDescription() {
		return "The Balanced Service algorithm tries to prevent any one elevator" + 
			   " from being overworked. Instead, the algorithm assigns the passenger" +
			   " request to the elevator with the least amount of assignments over its lifetime. \n\n" +
			   "This algorithm is useful when elevator service is split among floors. When all floors" +
			   " are accessible, this algorithm behaves like RoundRobin.";
	}

}
