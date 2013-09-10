package mainUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import elevatorSystem.Log;

public class mainWindowListener implements WindowListener{

	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}


	@Override
	public void windowClosing(WindowEvent arg0) {
		//PROGRAM ENDS
		Log.printStatusReport();
	}

	
}
