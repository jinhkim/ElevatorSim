package elevatorSystem;


import java.util.Random;
import elevatorSystem.Controller;

public class RandomEventGenerator extends Thread
{
	// Types of probability
	public enum Probability 
	{	
		PASSENGER,
		EMERGENCY,
		FAULT
	}
	
	private Boolean isRunning = false;

	private float probFault;
	private float probEmergency;
	private float probPassenger;

	private Controller controller;
	private Safety safety;

	private boolean paused = true;

	// Constructor and initialization
	public RandomEventGenerator (Controller c, Safety s, float emergencyProb, float passengerProb, float faultProb) 
	throws ProbabilityOutofBoundsException 
	{
		// Check that probabilities are valid
		if (0.0 > emergencyProb || 1.0 < emergencyProb
				|| 0.0 > passengerProb || 1.0 < passengerProb
				|| 0.0 > faultProb || 1.0 < faultProb)
		{
			throw new ProbabilityOutofBoundsException("Probability must be between 0.0 and 1.0");
		}

		controller = c;
		safety = s; 
		probEmergency = emergencyProb;
		probPassenger = passengerProb;
		probFault = faultProb;
		return;
	}

	// Set different probabilities
	public void setProbability (Probability e, float prob) 
	throws ProbabilityOutofBoundsException 
	{
		if (0.0 > prob || 1.0 < prob)
		{
			throw new ProbabilityOutofBoundsException("Probability must be between 0.0 and 1.0");
		}
		switch (e) {
		case PASSENGER:
			probPassenger = prob;
			break;
		case EMERGENCY:
			probEmergency = prob;
			break;
		case FAULT:
			probFault = prob;
			break;
		default:
			return;
		}

		return;
	}

	public float getFaultProb()
	{
		return probFault;
	}

	public float getEmergencyProb()
	{
		return probEmergency;
	}

	public float getPassengerProb()
	{
		return probPassenger;
	}
	
	public Boolean isRunning() {
		return isRunning;
	}
	
	public void pauseEvents() {
		paused = true;
	}

	public void runEvents() {
		paused = false;
	}
	
	public void run ()
	{
		// Test floor lock-out with RandomEventGen
//		for (int fl = 2; fl < 12; fl++) {
//			controller.lockFloor(0, fl);
//			controller.lockFloor(1, fl+10);
//		}
		
		Random random = new Random();
		isRunning = true;
		while (true) {
			
			// check to see if user wants to stop this thread
			Thread.yield(); // let another thread have some time perhaps to stop this one.
		    if (Thread.currentThread().isInterrupted()) {
		      break;
		    }
			
		    /* Generate a random float between 0.0 - 1.0
			 * every 0.5 seconds
			 */
			try {
				sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		    
			if (!paused) {
				
				float randomFloat = random.nextFloat();

				/* Create the event if randomFloat is less 
				 * than the specified probability.
				 */
				if (probPassenger > randomFloat) {
					int arrivalFl = 0;
					int destFl = 0;
					
					// Loop until we get a valid floor request
					do {
						arrivalFl = random.nextInt(controller.getMaxFloor()+1);
						destFl = random.nextInt(controller.getMaxFloor()+1);
					} while (!controller.validFloorReq(arrivalFl, destFl));
					
					// Create Passenger
					try {
						Passenger passenger = new Passenger(arrivalFl, destFl,
															40 + random.nextInt(70),
															// if building has penthouse and dest is penthouse -> VIP = true
															(destFl == controller.getMaxFloor() && controller.hasPenthouse()) ? true : false);	 

						// Pass passenger to the Controller
						controller.newPassengerRequest(passenger);
					} catch (BossLiftGeneralException e) {//do nothing
						
					}
				}

				// Passenger will request emergency
				// if randomFloat is less than probEmergency.
				if (probEmergency > randomFloat) {
					// Only send passenger emergency request to elevators with passengers
					// First check to see if there are passengers in a random elevator
					int elevID = random.nextInt(controller.getElevNum());
					
					// Avoid potential infinite loop if there are no passengers in the system.
					for (int i = 0; i < 10; i++)
					{

						if (controller.passengerInElev(elevID))
						{
							try {
								safety.passengerEmergencyRequest(elevID);
							} catch (BossLiftGeneralException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						}
						// if not - check next elevator that has passenger -> send emergency to safety
						elevID = (elevID += 1) % controller.getElevNum();
					}				
				}

				// Make maintenance event probability exclusive 
				// from passenger emergency probability
				if (probFault > (1.0f - randomFloat)) {
					// Create random integer for specific floor and pass it to safety
					//safety.randomMaintenanceRequest(random.nextInt());
					safety.injectFloorFault(random.nextInt(controller.getMaxFloor()+1));
				}
			} else {
				try {
				Thread.sleep(500);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
		isRunning = false;


	}
}
