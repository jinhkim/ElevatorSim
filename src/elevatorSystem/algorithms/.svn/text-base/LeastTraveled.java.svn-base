package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Passenger;

public class LeastTraveled extends Algorithm {

	@Override
	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p) {
		//e[1].getTotalFloors();

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

		int elvID[] = new int[e.length];
		int leastTraveled[] = new int[e.length];

		int shit[][] = new int[e.length][2];






		for(int f=0; f < waitingPass.length; f++)
		{
			//check if there is passenger waiting on the current floor
			if(waitingPass[f] == 1){
				for(int i = 0; i < e.length; i++){

					elvID[i] = i;
					leastTraveled[i] = Math.abs(f - e[i].getCurrentFloor()) + e[i].getTotalFloors();
					if (i>0 && leastTraveled[i] < leastTraveled[0]){
						leastTraveled[0] = leastTraveled[i];
						elvID[0] = i;
					}

				}


				int least = leastTraveled[0];

				//bubble sort
				/*int n = e.length;
				for(int pass = 1; pass < n; pass++){
					for(int i = 0; i < n-pass; i++){

						if(leastTraveled[i] < leastTraveled[i+1]){
							int temp = leastTraveled[i];
							leastTraveled[i] = leastTraveled[i+1];
							leastTraveled[i+1] = temp;

							int tempid = elvID[i];
							elvID[i] = elvID[i+1];
							elvID[i+1] = tempid;
						}
					}
				}*/

				for (int j=0; j<e.length; j++) {
					if	((canAccessFloor(e, elvID[j], f)) && (isInService(e, elvID[j])) ) 
					{
						// Loop through all the passengers on the floor
						for (int pass=0; pass<p[f].size(); pass++) { 
							// If the passenger is not serviced AND the elevator can go to his/her destination
							if (!p[f].get(pass).serviced() && canAccessFloor(e, elvID[j], p[f].get(pass).getDestFloor()) )
							{		
								p[f].get(pass).servicePassenger(elvID[j]);
								e[elvID[j]].addToQueue(f);			//add the floor to the elevators queue to stop at
							}
						}
					}
				}
			}
		}






	}

	@Override
	public String getName() {

		return "Least Traveled Floors";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "New passenger requests are serviced by the elevator who has traveled the least amount of floors at that time.";
	}


}
