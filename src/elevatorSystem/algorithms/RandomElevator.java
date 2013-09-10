package elevatorSystem.algorithms;

import java.util.Random;
import elevatorSystem.Elevator;
import elevatorSystem.Passenger;
import java.util.Vector;

public class RandomElevator extends Algorithm{

	
	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p) {
		
		
		int [] waitingPass = new int [p.length];
		
		//check if there is a passenger on the floor
		for (int floors=0; floors<p.length; floors++){
			waitingPass[floors] = 0;
			if (p[floors].size() > 0) {
				for (int pass=0; pass<p[floors].size(); pass++) {
					if (!p[floors].get(pass).serviced())
					{
						//PASSENGER WAITING ON floor = floors
						waitingPass[floors] = 1;
						
					}	
				}
			}
		}
		
		
		for(int i=0; i < waitingPass.length; i++)
		{
			//check if there is passenger waiting on the current floor
			if(waitingPass[i] == 1){
				
				//pick random elevator
				Random randomGen = new Random();
				int elevator = randomGen.nextInt(e.length);
				if(canAccessFloor(e, elevator, i) && isInService(e, elevator)){
				
					//add the floor to the queue of the random elevator
					for (int pass=0; pass<p[i].size(); pass++) { 							//loop through all the passengers on the floor
						if (!p[i].get(pass).serviced()){									//if the passenger is not serviced and is going down service passenger
							p[i].get(pass).servicePassenger(elevator);
							e[elevator].addToQueue(i);										//add the floor to the elevators queue to stop at
						}
					}
				}
			}
		}
		
		
	}

	@Override
	public String getName() {
		return "Random Elevator";
	}

	@Override
	public String getDescription() {
		return "In this algorithm, a random elevator will be assigned to service a passenger request.";
	}
}
