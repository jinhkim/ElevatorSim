package mainUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ElevatorFocusActionListener implements ActionListener{

	private mainWindow win;
	private int eleID;
	
	public ElevatorFocusActionListener(mainWindow w, int id) {
		win = w;
		eleID = id;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		win.focusViewOnElevator(eleID);
	}

}
