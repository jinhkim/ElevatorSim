package elevatorSystem.algorithms;

import java.util.Vector;

import elevatorSystem.Elevator;
import elevatorSystem.Elevator.ElevatorStatus;
import elevatorSystem.Passenger;

public class BetterBalancedService extends Algorithm {

	
	
	public class ElevatorQueue {
		private int ElevatorID;
		private int distanceTravelled;
		private int[] Destination;
		private ElevatorStatus status;
		private int currentfloor;
		private ElevatorQueue tail;
		
		//constructor
		public ElevatorQueue(int ID, int DT, int[] D, ElevatorStatus S, int CF, ElevatorQueue next){
			
			ElevatorID = ID;
			distanceTravelled = DT;
			Destination = D;
			status = S;
			currentfloor = CF;
			tail = next;
			
		}
	}
		
	public class PassengerQueue{

		private int passengerNumber;
		private int passengerFloor;
		private int passengerDestination;
		private Boolean direction; // 1 for Up, 0 for Down
		private Boolean VIP;
		private PassengerQueue tail;
		
		public PassengerQueue(int pN, int pF, int pD, Boolean d,Boolean v, PassengerQueue next){
			
			passengerNumber = pN;
			passengerFloor = pF;
			passengerDestination = pD;
			direction = d;
			VIP = v;
			tail = next;
		}
		
	}
	
	
	public void doAlgorithm(Elevator[] e, Vector<Passenger>[] p) {
		//Initialize 
		PassengerQueue passengerList = null;
		ElevatorQueue elevatorList = null;
		int [] waitingPass = new int [p.length];
		
		// Find a Unserviced Passenger on a floor and updates waitingPass
		for (int floors=0; floors<p.length; floors++){
			waitingPass[floors] = 0;
			p[floors].trimToSize();
			for (int pass=0; pass<p[floors].size(); pass++) {
				if (!p[floors].get(pass).serviced())
				{
					//PASSENGER WAITING ON floor = floors
					waitingPass[floors]++;

				}	
			}		
		}		
		
		
		// Fill PassengerQueue and ElevatorQueue 
		for(int i = 0 ; i < e.length ; i++)
		{
			elevatorList = new ElevatorQueue( i , e[i].getTotalFloors(),e[i].getQueue(), e[i].getStatus(),   
					                          e[i].getCurrentFloor() ,elevatorList );
		}
		
		for(int k = 0 ; k < p.length ; k++ ){
			if(waitingPass[k] > 1){
				
			}
		
	    }
	}

	
	@Override
	public String getName() {
		return "Better Balanced Service";
	}

	@Override
	public String getDescription() {
		return "To be done";
	}
	

	

}//BetterBalanceService