package tests_cases;

import java.util.Vector;

import mainUI.mainWindow;
import elevatorSystem.*;
import elevatorSystem.RandomEventGenerator.Probability;

public class main_tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Controller controller = null;
		
		System.out.println("BOSS LIFT ACCEPTANCE TESTS\n[EECE 419 -- POD 7]\n\n");
		
		// Check controller 
		System.out.println("PROGRAM START-UP\n");
		System.out.println("\n----> [SU-01] INIT ELEVATORS");
		try {
			controller = new Controller();
			controller.setFloors(5);
			controller.createElevators(0);
			controller.destroy();
			System.out.println("Zero elevators: FAILED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Zero elevators: PASSED");
		}
		try {
			controller.createElevators(-1);
			controller.destroy();
			System.out.println("Negative elevators: FAILED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Negative elevators: PASSED");
		}
		try {
			controller.createElevators(11);
			controller.destroy();
			System.out.println("11 elevators: FAILED");
		} catch (BossLiftGeneralException e) {
			System.out.println("11 elevators: PASSED");
		}
		try {
			controller.createElevators(10);
			controller.destroy();
			System.out.println("10 elevators: PASSED");
		} catch (BossLiftGeneralException e) {
			System.out.println("10 elevators: FAILED");
			e.printStackTrace();
		}
		try {
			controller.createElevators(6);
			controller.destroy();
			System.out.println("6 elevators: PASSED");
		} catch (BossLiftGeneralException e) {
			System.out.println("6 elevators: FAILED");
		}
		
		
		System.out.println("\n----> [SU-02] INIT FLOORS");
		// floor numbers
		try {
			controller = new Controller();
			controller.setFloors(-1);
			System.out.println("Negative number of floors: FAILED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Negative number of floors: PASSED");
		}
		try {
			controller = new Controller();
			controller.setFloors(101);
			System.out.println("Too many floors: FAILED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Too many floors: PASSED");
		}
		try {
			controller = new Controller();
			controller.setFloors(1);
			System.out.println("Only one floor: FAILED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Only one floor: PASSED");
		}
		try {
			controller = new Controller();
			controller.setFloors(2);
			System.out.println("Two floors: PASSED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Two floors: FAILED");
		}
		try {
			controller = new Controller();
			controller.setFloors(50);
			System.out.println("Fifty floors: PASSED");
		} catch (BossLiftGeneralException e) {
			System.out.println("Fifty floors: FAILED");
		}
		
		RandomEventGenerator rand = null;
		System.out.println("\n----> [SU-03] RANDOM EVENT GEN");
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),0f,0f,0f);
				System.out.println("Start Random Event Gen with zero probabilities: PASSED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with zero probabilities: FAILED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),-0.2f,0f,0f);
				System.out.println("Start Random Event Gen with neg probabilities (1): FAILED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with neg probabilities (1): PASSED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),0f,-0.2f,0f);
				System.out.println("Start Random Event Gen with neg probabilities (2): FAILED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with neg probabilities (2): PASSED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),0f,0f,-0.2f);
				System.out.println("Start Random Event Gen with neg probabilities (3): FAILED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with neg probabilities (3): PASSED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),1.1f,0f,0f);
				System.out.println("Start Random Event Gen with large probabilities (1): FAILED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with large probabilities (1): PASSED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),0f,1.1f,0f);
				System.out.println("Start Random Event Gen with large probabilities (2): FAILED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with large probabilities (2): PASSED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),0f,0f,1.1f);
				System.out.println("Start Random Event Gen with large probabilities (3): FAILED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with large probabilities (3): PASSED");
			}
			try {
				rand = new RandomEventGenerator(new Controller(),new Safety(controller),0.2f,0.5f,0.9f);
				System.out.println("Start Random Event Gen with good probabilities: PASSED");
			} catch (ProbabilityOutofBoundsException e4) {
				System.out.println("Start Random Event Gen with good probabilities: FAILED");
			}
			
		
		
			
			System.out.println("\n----> [SU-04] LOCKING FLOORS IN ELEVATOR");
			Elevator e = null;
			controller = new Controller();
			try {
				controller.setFloors(20);
			} catch (BossLiftGeneralException e2) {
				System.out.println("Test setup failed");
			}
			e = new Elevator(controller, 20, 0);
			e.start();
			e.addLockedFloor(2);
			if (e.isFloorLocked(2))
				System.out.println("Elevator floor lock test: PASSED");
			else
				System.out.println("Elevator floor lock test: FAILED");
			if (controller.validFloorReq(2, 10))
				System.out.println("Valid floor request test: FAILED");
			else 
				System.out.println("Valid floor request test: PASSED");
			e.addToQueue(2);
			int[] q = e.getQueue();
			String s = "";
			for (int i=0; i<q.length; i++)
				s = s+q[i]+",";
			
			System.out.println("Add locked floor to queue test: {"+s+"} -- should be empty");
			e.interrupt();
			
			
			System.out.println("\n----> [SU-05] ADDING PENTHOUSE TO SYSTEM");
			controller = new Controller();
			try {
				controller.setFloors(20);
				controller.createElevators(1);
			} catch (BossLiftGeneralException e2) {
				System.out.println("Test setup failed");
			}
			controller.addPenthouse();
			if (controller.hasPenthouse())
				System.out.println("Penthouse added test: PASSED");
			else
				System.out.println("Penthouse added test: FAILED");
			boolean[] b = controller.getElevatorBounds(0);
			if (b[19])
				System.out.println("Penthouse locked in elevator test: PASSED");
			else
				System.out.println("Penthouse locked in elevator test: FAILED");
			controller.destroy();
			
			
		// Check controller 
		System.out.println("\n\nEVENTS\n");
		System.out.println("----> [ME-02] MANUAL MAINTENANCE");
		try {
			controller.setFloors(20);
			controller.createElevators(1);
		} catch (BossLiftGeneralException e3) {
			System.out.println("Failure during test initialization");
		}
		Safety safety = new Safety(controller);
		safety.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e4) {
		}
		safety.autoMaintenanceRequest(0);
		if (controller.checkForMaintenance(0))
			System.out.println("Maintenance request test: PASSED");
		else
			System.out.println("Maintenance request test: FAILED");
		safety.interrupt();
		controller.destroy();
		
		
		// passenger object tests
		System.out.println("\n----> [PE-01] PASSENGER CREATION]");
		try {
			controller = new Controller();
			controller.setFloors(20);
			controller.createElevators(1);
		} catch (BossLiftGeneralException e3) {
			System.out.println("Failure during test initialization");
		}
		
		try {
			Passenger p = new Passenger(0, 19, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with dest floor just inside upper bounds: PASSED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with dest floor just inside upper bounds: FAILED");
		}
		try {
			Passenger p = new Passenger(6, 2, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with normal properties: PASSED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with normal properties: FAILED");
		}
		
		System.out.println("\n----> [PE-02] PASSENGER WEIGHT");
		try {
			Passenger p = new Passenger(0, 5, -100 , false);
			System.out.println("Passenger with negative weight: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with negative weight: PASSED");
		}
		try {
			Passenger p = new Passenger(0, 5, 0 , false);
			System.out.println("Passenger with zero weight: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with zero weight: PASSED");
		}
		
		System.out.println("\n----> [PE-03] PASSENGER FLOORS OUT OF SYSTEM");
		try {
			Passenger p = new Passenger(-1, 5, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with negative arrival floor: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with negative arrival floor: PASSED");
		}
		try {
			Passenger p = new Passenger(0, -3, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with negative dest floor: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with negative dest floor: PASSED");
		}
		try {
			Passenger p = new Passenger(22, 5, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with arrival floor out of bounds: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with arrival floor out of bounds: PASSED");
		}
		try {
			Passenger p = new Passenger(6, 99, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with dest floor out of bounds: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with dest floor out of bounds: PASSED");
		}
		try {
			Passenger p = new Passenger(0, 20, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with dest floor just outside upper bounds: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with dest floor just outside upper bounds: PASSED");
		}
		
		System.out.println("\n----> [PE-04] PASSENGER WITH EQUAL ARR. & DEST. FLOORS");
		try {
			Passenger p = new Passenger(9, 9, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("PASSED");
		}
		
		System.out.println("\n----> [PE-05] PASSENGER WITH LOCKED DESTINATION FLOOR");
		try {
			controller.lockFloor(0, 11);
			Passenger p = new Passenger(8, 11, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with locked arrival floor: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with locked arrival floor: PASSED");
		}
		
		System.out.println("\n----> [PE-06] PASSENGER WITH LOCKED ARRIVAL FLOOR");
		try {
			controller.lockFloor(0, 6);
			Passenger p = new Passenger(6, 10, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with locked arrival floor: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with locked arrival floor: PASSED");
		}
		
		System.out.println("\n----> [PE-07] PASSENGER WITH PENTHOUSE");
		try {
			controller.addPenthouse();
			Passenger p = new Passenger(0, 19, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with dest floor of penthouse, no VIP: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with dest floor of penthouse, no VIP: PASSED");
		}
		try {
			Passenger p = new Passenger(0, 19, 100 , true);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with dest floor of penthouse, VIP: PASSED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with dest floor of penthouse, VIP: FAILED");
			e2.printStackTrace();
		}
		
		try {
			Passenger p = new Passenger(19, 0, 100 , true);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with arrival floor of penthouse, VIP: PASSED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with arrival floor of penthouse, VIP: FAILED");
			e2.printStackTrace();
		}
		try {
			Passenger p = new Passenger(19, 1, 100 , false);
			controller.newPassengerRequest(p);
			System.out.println("Passenger with arrival floor of penthouse, no VIP: FAILED");
		} catch (BossLiftGeneralException e2) {
			System.out.println("Passenger with arrival floor of penthouse, no VIP: PASSED");
		}
		controller.destroy();
		
		System.out.println("\n----> [PE-08] PASSENGER EMERGENCY");
		try {
			controller = new Controller();
			controller.setFloors(20);
			controller.createElevators(1);
			safety = new Safety(controller);
			safety.start();
			Passenger p = new Passenger(1,5,100,false);
			controller.newPassengerRequest(p);
			controller.start();
			controller.runSim();
			long t = System.currentTimeMillis();
			while(System.currentTimeMillis() - t < 3000) {}
			safety.passengerEmergencyRequest(0);
		} catch (BossLiftGeneralException e3) {
			System.out.println("Failure during test initialization");
			e3.printStackTrace();
		}
		if (controller.checkForEmergency(0))
			System.out.println("Maintenance request test: PASSED");
		else
			System.out.println("Maintenance request test: FAILED");
		safety.interrupt();
		controller.destroy();
		controller.interrupt();
		
		
		/*****************************************************/
		System.out.println("\n\nSAFETY");
		System.out.println("\n----> [SA-01] EMERGENCY ALERT WHILE IN MAINTENANCE");
		
		
		/*****************************************************/
		System.out.println("\n\nELEVATOR MOVEMENT");
		System.out.println("\n----> [EM-01] ADD TO QUEUE");
		e = new Elevator(controller, 20, 0);
		e.addToQueue(4);
		e.addToQueue(22);
		q = e.getQueue();
		System.out.println("Elevator queue: ");
		s = "";
		for (int i=0; i<q.length; i++)
			s = s+q[i]+",";
		System.out.println(s+"   --  should be {4}");
		
		e = new Elevator(controller, 20, 0);
		e.addToQueue(-1);
		e.addToQueue(2);
		e.addToQueue(5);
		q = e.getQueue();
		System.out.println("Elevator queue: ");
		s = "";
		for (int i=0; i<q.length; i++)
			s = s+q[i]+",";
		System.out.println(s+"   --  should be {2,5}");
		
		System.out.println("\n----> [EM-02] ELEVATOR STATUS");
		e.setMaintenance();
		if (e.isInMaintenance())
			System.out.println("Elevator in maintenance mode: PASSED");
		else
			System.out.println("Elevator in maintenance mode: FAILED");
		e.setEmergency();
		if (e.isInEmergency())
			System.out.println("Elevator in emergency mode: PASSED");
		else
			System.out.println("Elevator in emergency mode: FAILED");
		e.setHalted();
		if (e.isHalted())
			System.out.println("Elevator in halted mode: PASSED");
		else
			System.out.println("Elevator in halted mode: FAILED");
		e.setActive();
		if (e.isInMaintenance() || e.isInEmergency() || e.isHalted())
			System.out.println("Elevator in active mode: FAILED");
		else
			System.out.println("Elevator in active mode: PASSED");
		
		System.out.println("\n----> [EM-03] MOVE ELEVATOR TO FLOOR 5");
		controller = new Controller();
		try {
			controller.setFloors(20);
		} catch (BossLiftGeneralException e2) {
			System.out.println("Test setup failed");
		}
		e = new Elevator(controller, 20, 0);
		e.start();
		e.addToQueue(5);
		e.startSim();
		long t = System.currentTimeMillis();
		boolean pass = false;
		int lastf = e.getCurrentFloor();
		int lastpf = -1;
		while (System.currentTimeMillis() - t < 20000) {
			if (lastpf != lastf){
				lastpf = lastf;
				System.out.println("Floor "+lastpf);
			}
			lastf = e.getCurrentFloor();
			if (e.getCurrentFloor() == 5){
				System.out.println("Elevator has reached destination.");
				pass = true;
				break;
			}
		}
		if (pass) {
			System.out.println("Checking to make sure it doesn't move past....");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (e.getCurrentFloor() == 5)
				System.out.println("Elevator movement: PASSED");
			else 
				System.out.println("Elevator movement: FAILED, moved past destination floor");
		}
		else 
			System.out.println("Elevator movement: FAILED, movement timed out");
		e.interrupt();
		
		
		
		System.out.println("\n----> [EM-04] PICK UP ONE PASSENGER");
		controller = new Controller();
		try {
			controller.setFloors(20);
			controller.createElevators(1);
			
			e = new Elevator(controller, 20, 0);
			e.start();
			Passenger p = new Passenger(4, 19, 100 , false);
			p.servicePassenger(0);
			controller.newPassengerRequest(p);
			
			e.addToQueue(4);
			e.startSim();
			
			t = System.currentTimeMillis();
			pass = false;
			while (System.currentTimeMillis() - t < 20000) {
				if (e.isOccupied()){
					System.out.println("Elevator has picked up the passenger.");
					pass = true;
					break;
				}
			}
			if (pass) {
					System.out.println("Passenger pick up: PASSED");
			}
			else 
				System.out.println("Passenger pick up: FAILED");
			e.interrupt();
			controller.destroy();
		} catch (BossLiftGeneralException e2) {
			System.out.println("Test setup failed");
		}
		
		System.out.println("\n----> [EM-05] ELEVATOR WEIGHT LIMIT");
		controller = new Controller();
		try {
			controller.setFloors(20);
			controller.createElevators(1);
			
			e = new Elevator(controller, 20, 0);
			e.start();
			Passenger p = new Passenger(4, 19, Elevator.MAX_LOAD_WEIGHT+50 , false);
			p.servicePassenger(0);
			controller.newPassengerRequest(p);
			
			e.addToQueue(4);
			e.startSim();
			
			t = System.currentTimeMillis();
			pass = true;
			while (System.currentTimeMillis() - t < 20000) {
				if (e.isOccupied()){
					System.out.println("Elevator has picked up the passenger.");
					pass = false;
					break;
				}
			}
			if (pass) {
					System.out.println("Heavy passenger: PASSED");
			}
			else 
				System.out.println("Heavy passenger: FAILED");
			e.interrupt();
			controller.destroy();
		} catch (BossLiftGeneralException e2) {
			System.out.println("Test setup failed");
		}
		
		
		
		
		// timing tests
		System.out.println("\n[SYSTEM PAUSING]");
		try {
			controller.createElevators(1);
		} catch (BossLiftGeneralException e1) {
			e1.printStackTrace();
		}
		controller.start();
		controller.runSim();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException j) {
			j.printStackTrace();
		}
		controller.pauseSim();
		System.out.println("Sim ran for 5 seconds. Actual sim time: "+controller.getSimRunTime());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException j) {
			j.printStackTrace();
		}
		System.out.println("Sim is still paused, sim time should be the same: "+controller.getSimRunTime());
		
		controller.destroy();
		controller.interrupt();
		
	}

}
