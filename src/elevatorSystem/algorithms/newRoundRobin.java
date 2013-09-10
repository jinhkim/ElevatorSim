package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Passenger;

public class newRoundRobin extends Algorithm{

	// Keeping state of next elevator to assign
	private int nextElevator=0;

	@Override
	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p) {
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
				for (int j=0 ; j<e.length; j++) {
					// The next elevator can access the floor AND is in-service
					if	((canAccessFloor(e, nextElevator, i)) && (isInService(e, nextElevator)) ) 
					{	
						// Loop through all the passengers on the floor
						for (int pass=0; pass<p[i].size(); pass++) { 
							// If the passenger is not serviced AND the elevator can go to his/her destination
							if (!p[i].get(pass).serviced() && canAccessFloor(e, nextElevator, p[i].get(pass).getDestFloor()) )
							{		
								//System.out.println("RR: Sending elevator " + eles[j] + " to floor " + i);
								p[i].get(pass).servicePassenger(nextElevator);
								e[nextElevator].addToQueue(i);			//add the floor to the elevators queue to stop at
							}
						}
						nextElevator++;
						nextElevator = nextElevator % e.length;
						break;
					} else {
						nextElevator++;
						nextElevator = nextElevator % e.length;
					}
				}
			}
		}

	}

	@Override
	public String getName() {
		return "Round Robin";
	}

	@Override
	public String getDescription() {
		return "Passenger requests are serviced by the next elevator in line." + 
			   " When the last elevator is reached, the first elevator is next.";
	}

}
