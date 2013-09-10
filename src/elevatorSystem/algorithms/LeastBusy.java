package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Passenger;

public class LeastBusy extends Algorithm{

	// Keeping state of next elevator to assign
	private int[] queueSizeAry;
	private boolean firstTime = true;
	private int nextElevator = 0;

	@Override
	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p) {
		
		/* Initialize the array the first time this method is called.
		 */
		if (firstTime == true) {
			queueSizeAry = new int[e.length];
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

		/* Get size of queue for each elevator
		 */
		for (int i = 0; i < e.length; i++) {
			queueSizeAry[i] = e[i].getQueueSize();
		}

		for(int i=0; i < waitingPass.length; i++)
		{
			//check if there is passenger waiting on the current floor
			if(waitingPass[i] == 1){
				int minIdx = findMin(queueSizeAry);
				// This elevator can access the floor AND it is in-service
				if	((canAccessFloor(e, minIdx, i)) && (isInService(e, minIdx)) ) 
				{	
					// Loop through all the passengers on the floor
					for (int pass=0; pass<p[i].size(); pass++) { 
						// If the passenger is not serviced AND the elevator can go to his/her destination
						if (!p[i].get(pass).serviced() && canAccessFloor(e, minIdx, p[i].get(pass).getDestFloor()) )
						{		
							//System.out.println("LB: Sending elevator " + minIdx + " to floor " + i);
							p[i].get(pass).servicePassenger(minIdx);
							e[minIdx].addToQueue(i);	//add the floor to the elevators queue to stop at
							queueSizeAry[minIdx]++ ;
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
									//System.out.println("LB: Sending elevator " + nextElevator + " to floor " + i);
									p[i].get(pass).servicePassenger(nextElevator);
									e[nextElevator].addToQueue(i);			//add the floor to the elevators queue to stop at
									queueSizeAry[nextElevator]++;
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
	 * Returns the index of the elevator with the least number of floors to stop at in its queue.
	 */
	private int findMin (int[] queueSizeAry) {
		int minIdx = 0;
		for (int i = 0; i < queueSizeAry.length; i++) {
			if (queueSizeAry[i] < queueSizeAry[minIdx]) {
				minIdx = i;
			}
		}
		return minIdx;
	}

	@Override
	public String getName() {
		return "Least Busy";
	}

	@Override
	public String getDescription() {
		return "The Least Busy algorithm tries to assign the passenger" +
			   " request to the elevator with the least number of stops in its queue.";
	}

}
