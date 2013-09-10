package elevatorSystem.algorithms;

import elevatorSystem.Elevator;
import elevatorSystem.Passenger;
import java.util.Vector;

public class PassengerModE extends Algorithm{

	@Override
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
				
				for (int j=0; j< e.length; j++) {
					int jojo =(i+j)%e.length;
				
				if(canAccessFloor(e, jojo, i) && isInService(e, jojo)){
				
					//add the floor to the queue of the random elevator
					for (int pass=0; pass<p[i].size(); pass++) { 							//loop through all the passengers on the floor
						if (!p[i].get(pass).serviced()){									//if the passenger is not serviced and is going down service passenger
							p[i].get(pass).servicePassenger(jojo);
							e[jojo].addToQueue(i);//add the floor to the elevators queue to stop at

						}
					}
					break;
				}
				}
			}
		}
		
	}

	@Override
	public String getName() {
		return "PassengerModE";
	}

	@Override
	public String getDescription() {
		return "The PassengerModE algorithm assigns certain floors to each elevator. Passengers arriving"
				+ " on a specific floor will always be assigned to the same elevator. "
				+ "\n\n NOTE: This algorithm will not work with only one elevator.";
	}

}
