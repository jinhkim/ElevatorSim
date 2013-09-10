package tests_cases;

import java.util.Vector;
import elevatorSystem.Log.MsgType;

import elevatorSystem.BossLiftGeneralException;
import elevatorSystem.Controller;
import elevatorSystem.Elevator;
import elevatorSystem.Log;
import elevatorSystem.Passenger;
import elevatorSystem.Log.MsgType;

public class LogTester {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Controller c = new Controller();
		Elevator[] e = new Elevator[6];
		Vector<Passenger>[] passenger = new Vector[5];
		
		for(int i=0; i < passenger.length; i++){
			passenger[i] = new Vector();
		}
		
		Passenger p1, p2, p3, p4, p5, p6;
		
		try{
			p1 = new Passenger(12, 1, 2,  true);
			p2 = new Passenger(34, 4, 111,  true);
			p3 = new Passenger(54, 1, 55, true);
			p4 = new Passenger(11, 6, 77,  true);
			p5 = new Passenger(543, 8, 45,  true);
			p6 = new Passenger(654, 7, 12, true);
			
			passenger[0].add(p1);
			passenger[0].add(p2);
			passenger[1].add(p3);
			passenger[2].add(p4);
			passenger[2].add(p5);
			passenger[3].add(p6);
			
		}catch(BossLiftGeneralException bb){
			bb.printStackTrace();
		} 
		
		
		e[0] = new Elevator(c, 20, 6);
		e[1] = new Elevator(c, 12, 5);
		e[2] = new Elevator(c, 33, 1);
		e[3] = new Elevator(c, 55, 3);
		e[4] = new Elevator(c, 88, 5);
		e[5] = new Elevator(c, 23, 7);
		
		Log.addElevatorLog(e);
		System.out.println("\n\n"+"status is: "+Log.getLogElement(MsgType.ELEVATOR, "status", 0)+
							"\tmax floor is: "+Log.getLogElement(MsgType.ELEVATOR, "max floors", 5));
		Log.addElevatorLog(e);
		Log.addPassengerLog(passenger);
		Log.printLog(MsgType.ELEVATOR);
		Log.printLog(MsgType.PASSENGER);
		
		Log.cleanLogs();
		
		Log.addElevatorLog(e);
		Log.printLog(MsgType.ELEVATOR);
		//Log.addPassengerLog(passenger);
		
		
	}

}
