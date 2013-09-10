package mainUI;


import com.apple.eawt.Application;
import elevatorSystem.Controller;
import elevatorSystem.Log;
import elevatorSystem.ProbabilityOutofBoundsException;
import elevatorSystem.RandomEventGenerator;
import elevatorSystem.Safety;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Boss Lift");
		
		String os = System.getProperty("os.name");
		if (os.startsWith("Mac")) {
			Application a = new Application();
			a.addApplicationListener(new mainWindowMacListener()); 
		}
		
		
		Controller controller = new Controller();
		
		Safety safety = new Safety(controller);
		
		RandomEventGenerator randomEventGen = null;
		try {
			randomEventGen = new RandomEventGenerator(controller, safety, 0.0f, 0.0f, 0.0f);
		} catch (ProbabilityOutofBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mainWindow win = new mainWindow(controller, randomEventGen, safety);
		win.setVisible(true);
		Log.setMainUI(win);
		
	}
}
