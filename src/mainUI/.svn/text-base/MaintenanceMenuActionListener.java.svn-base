package mainUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MaintenanceMenuActionListener implements ActionListener{

	private mainWindow win;
	private int eleID;
	private Boolean returntoActive;
	
	public MaintenanceMenuActionListener(mainWindow w, int id, Boolean toActive) {
		win = w;
		eleID = id;
		returntoActive = toActive;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (returntoActive) 
			win.returnElevatortoActiveMode(eleID);
		else
			win.setToMaintenanceMode(eleID);
	}

}
