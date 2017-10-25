package cjminecraft.engine.managers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import cjminecraft.engine.util.IRender;
import cjminecraft.engine.util.IRenderManager;
import cjminecraft.engine.util.Keyboard;

public class RenderManager extends JPanel implements IRenderManager {

	private static final long serialVersionUID = 1L;
	
	private static RenderManager instance = new RenderManager();
	
	private List<IRender> renderQueue = new ArrayList<IRender>();
	
	private int x, y;
	
	@Override
	public void preInit() throws Exception {
	}

	@Override
	public void init() throws Exception {
	}

	@Override
	public void postInit() throws Exception {
	}

	@Override
	public void loop() throws Exception {
		if(Keyboard.isKeyDown(KeyEvent.VK_W))
			y -= 2;
		if(Keyboard.isKeyDown(KeyEvent.VK_S))
			y += 2;
		if(Keyboard.isKeyDown(KeyEvent.VK_A))
			x -= 2;
		if(Keyboard.isKeyDown(KeyEvent.VK_D))
			x += 2;
	}
	
	@Override
	public void renderLoop() throws Exception {
		repaint();
		addRenderToQueue(g -> {
			g.fillRect(x, y, 250, 250);
		});
	}

	@Override
	public void cleanUp() throws Exception {
		this.renderQueue.clear();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(IRender render : this.renderQueue)
			render.render((Graphics2D) g);
		this.renderQueue.clear();
	}
	
	public RenderManager addRenderToQueue(IRender render) {
		this.renderQueue.add(render);
		return this;
	}
	
	public static RenderManager getInstance() {
		return instance;
	}
	
}
