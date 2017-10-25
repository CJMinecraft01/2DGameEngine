package cjminecraft.engine.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Keyboard implements KeyListener {

	private static Keyboard instance = new Keyboard();
	
	private static HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
	
	@Override
	public void keyPressed(KeyEvent e) {
		keys.put(e.getKeyCode(), true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys.put(e.getKeyCode(), false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	public static boolean isKeyDown(int key) {
		return keys.getOrDefault(key, false);
	}
	
	public static Keyboard getInstance() {
		return instance;
	}

}
