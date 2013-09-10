package mainUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorEmergActionListener implements ActionListener {

	private mainWindow win;
	private int eleID;
	private Boolean resolve;
	
	public ElevatorEmergActionListener(mainWindow w, int id, Boolean r) {
		win = w;
		eleID = id;
		resolve = r;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (resolve)
			win.resolveEmerg(eleID);
		else
			win.elevatorEmerg(eleID);
	}

}
