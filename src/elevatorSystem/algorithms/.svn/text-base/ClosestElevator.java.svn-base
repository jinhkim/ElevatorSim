package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Passenger;



public class ClosestElevator extends Algorithm{

	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p)
	{
		int [] waitingPass = new int [p.length];
		int [] eles = new int [p.length];
		
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
		
		//initializes the array to -1
		for (int j=0; j<eles.length; j++)
			eles[j] = -1;
		
		//puts the floor the elevator is on in the array  eles[floor] = elevator id
		for (int el=0; el<e.length; el++) {
			eles[e[el].getCurrentFloor()] = el;
		}
		
		
		
		for(int i=0; i < waitingPass.length; i++)
		{
			//check if there is passenger waiting on the current floor
			if(waitingPass[i] == 1){
				for(int j = 0; j < (eles.length); j++) {
					
					//check if there is an elevator above the passenger
					if (i+j < eles.length) {
						if (eles[i+j] >= 0 && canAccessFloor(e, eles[i+j], i) && isInService(e, eles[i+j]) ) { //there is an elevator on the floor
							
							if( !e[eles[i+j]].getDirection() ){ //check if the elevator above is goign down and passenger is going down add to queue
								
								for (int pass=0; pass<p[i].size(); pass++) { 							//loop through all the passengers on the floor
									if (!p[i].get(pass).serviced() && !p[i].get(pass).getUpFlag()){		//if the passenger is not serviced and is going down service passenger
										p[i].get(pass).servicePassenger(eles[i+j]);
										e[eles[i+j]].addToQueue(i);										//add the floor to the elevators queue to stop at
									}
								}
								break;
							}
						}
					} 
					
					//check if there is an elevator below the passenger
					if(i-j >= 0) {
						if (eles[i-j] >= 0 && canAccessFloor(e, eles[i-j], i) && isInService(e, eles[i-j])) {
							
							
							if( e[eles[i-j]].getDirection() ){ //check if the elevator below is goign up and passenger is going up add to queue
								
								for (int pass=0; pass<p[i].size(); pass++) { 							//loop through all the passengers on the floor
									if (!p[i].get(pass).serviced() && p[i].get(pass).getUpFlag()){		//if the passenger is not serviced and is going up service passenger
										p[i].get(pass).servicePassenger(eles[i-j]);
										e[eles[i-j]].addToQueue(i);										//add the floor to the elevators queue to stop at
									}
								}
								break;
							}

						}
					}
				}
			}
		}

		
	}

	@Override
	public String getName() {
		return "Closest Elevator";
	}

	@Override
	public String getDescription() {
		return "The Closest Elevator algorithm assigns the elevator "
				+ "closest to the passenger to service the request.";
	}
}
