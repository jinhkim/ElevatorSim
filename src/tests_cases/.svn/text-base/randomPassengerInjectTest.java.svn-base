package tests_cases;

import elevatorSystem.BossLiftGeneralException;
import elevatorSystem.Controller;
import elevatorSystem.ProbabilityOutofBoundsException;
import elevatorSystem.RandomEventGenerator;
import elevatorSystem.Safety;

public class randomPassengerInjectTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Controller controller = new Controller();
		
		Safety safety = new Safety(controller);
		
		RandomEventGenerator randomEventGen = null;
		float prob = 0.2f;
		
		System.out.println("BOSS LIFT ACCEPTANCE TESTS\n[EECE 419 -- POD 7]\n\n");
		System.out.println("EVENTS\n[E-01] CHECK PASSENGER FREQUENCY\nRunning...");
		try {
			randomEventGen = new RandomEventGenerator(controller, safety, 0.0f, prob, 0.0f);
		} catch (ProbabilityOutofBoundsException e) {
			e.printStackTrace();
		}
		
		try {
			controller.setFloors(20);
			controller.createElevators(1);
			randomEventGen.start();
			randomEventGen.runEvents();
		} catch (BossLiftGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int pass = controller.getPassengersInSystem();
		randomEventGen.interrupt();
		controller.destroy();
		controller.interrupt();
		
		System.out.println("With probability of " +prob+ ", "+pass+" passengers per minute.");
	}

}
