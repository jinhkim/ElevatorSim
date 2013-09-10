package tests_cases;

import java.util.Random;

import elevatorSystem.BossLiftGeneralException;
import elevatorSystem.Controller;
import elevatorSystem.Passenger;
import elevatorSystem.ProbabilityOutofBoundsException;
import elevatorSystem.RandomEventGenerator;
import elevatorSystem.Safety;

public class algorithmEfficiencyTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		Random rand = new Random();
		Controller controller = new Controller();
		
		for (int a=0; a<controller.getAlgorithms().length; a++) {
			
		
		long start = System.currentTimeMillis();
		try {
			controller.setFloors(20);
			controller.createElevators(3);
			controller.start();
			controller.setAlgorithm(a);
			System.out.println("20 floors, 3 elevators, algorithm -- "+controller.getAlgorithms()[controller.getAlgorithm()]);
			
			try {
				for (int i=0;i<3;i++)
					controller.newPassengerRequest(new Passenger(rand.nextInt(controller.getMaxFloor()),rand.nextInt(controller.getMaxFloor()),100,false));
			} catch (BossLiftGeneralException e) {}
			System.out.println("3 random passengers added.");
			controller.runSim();
			System.out.println("Running sim....");
		} catch (BossLiftGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		while (controller.getPassengersInSystem() != 0) {
			if (System.currentTimeMillis() - start > 120000)
				break;
			
		}

		System.out.println("Algorithm: "+controller.getAlgorithms()[controller.getAlgorithm()]+" -- "+(System.currentTimeMillis() - start)+"\n\n");
		controller.destroy();
		controller.interrupt();
		controller = new Controller();
		}
	}

}
