package mainUI;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;

public class KeyDispatcher implements KeyEventDispatcher {

	private mainWindow win;
	public KeyDispatcher(mainWindow w) {
		win = w;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent e) {

		if(e.getID() == KeyEvent.KEY_PRESSED) {
			if (e.getModifiers() == KeyEvent.CTRL_MASK) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT){
					win.rotateSimViewLeft();
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					win.rotateSimViewRight();
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					win.zoomInSimView();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					win.zoomOutSimView();
				}
			}
		
		}
		return false;
	}

}
