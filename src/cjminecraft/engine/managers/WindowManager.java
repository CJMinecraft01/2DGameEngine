package cjminecraft.engine.managers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import cjminecraft.engine.Engine;
import cjminecraft.engine.util.IManager;
import cjminecraft.engine.util.Keyboard;

public class WindowManager implements IManager {

	private static WindowManager instance = new WindowManager();

	public JFrame frame;

	@Override
	public void preInit() throws Exception {
		this.frame = new JFrame(Engine.getOption("title"));
		this.frame.setSize(Integer.valueOf(Engine.getOption("width")), Integer.valueOf(Engine.getOption("height")));
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocationRelativeTo(null);
		this.frame.setContentPane(RenderManager.getInstance());
		this.frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {

			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Engine.getInstance().cleanUp();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		this.frame.addKeyListener(Keyboard.getInstance());
		this.frame.setVisible(true);
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void postInit() throws Exception {
	}

	@Override
	public void loop() throws Exception {
	}

	@Override
	public void cleanUp() throws Exception {
	}

	public static WindowManager getInstance() {
		return instance;
	}

}
