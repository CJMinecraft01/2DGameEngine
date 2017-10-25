package cjminecraft.engine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.lang.model.SourceVersion;
import javax.swing.Timer;

import cjminecraft.engine.loaders.LanguageLoader;
import cjminecraft.engine.managers.RenderManager;
import cjminecraft.engine.managers.WindowManager;
import cjminecraft.engine.util.ILaunchClass;
import cjminecraft.engine.util.IManager;
import cjminecraft.engine.util.IRenderManager;

public class Engine implements ILaunchClass, IRenderManager {

	public static String LAUNCH_PATH_FILE = "launch.txt";

	private static Engine instance = new Engine();

	private List<IManager> managers = new ArrayList<IManager>();
	private List<IRenderManager> renderManagers = new ArrayList<IRenderManager>();
	private HashMap<String, String> options = new HashMap<String, String>();
	
	private Timer renderTimer;
	private Timer logicTimer;

	public static void main(String[] args) throws Exception {
		LanguageLoader.loadLanguage("en_UK");
		for (String line : Files.readAllLines(Paths.get(LAUNCH_PATH_FILE))) {
			if (!line.startsWith("##") && !line.isEmpty()) {
				if (SourceVersion.isName(line) && !SourceVersion.isKeyword(line)) {
					if (ILaunchClass.class.isAssignableFrom(Class.forName(line))) {
						System.out.println("Found launch class: " + line);
						ILaunchClass launch = (ILaunchClass) Class.forName(line).newInstance();
						launch.addManagers();
					}
				} else {
					String[] details = line.split(":");
					if (details.length < 2)
						continue;
					String value = line.substring(details[0].length() + 1);
					if (value.contains("#format")) { // Format
						value = value.substring("#format".length() + 1);
						value = value.substring(0, value.length() - 1);
						String[] formatArgs = value.split(",");
						List<Object> formatArgsList = new ArrayList<Object>();
						for (String arg : formatArgs)
							formatArgsList.add(arg);
						formatArgsList.remove(0);
						value = LanguageLoader.format(formatArgs[0], formatArgsList.toArray());
					}
					instance.options.put(details[0], value);
					System.out.println(String.format("Loading option: %s = %s", details[0], value));
				}
			}
		}
		instance.preInit();
		instance.init();
		instance.postInit();
	}

	@Override
	public void addManagers() {
		System.out.println("Adding Managers");
		addManager(WindowManager.getInstance());
		addManager(RenderManager.getInstance());
	}

	@Override
	public void preInit() throws Exception {
		this.renderTimer = new Timer(1000 / Integer.valueOf(getOption("fps")), e -> {
			try {
				renderLoop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		this.logicTimer = new Timer(1000 / Integer.valueOf(getOption("tps")), e -> {
			try {
				loop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		for (IManager manager : this.managers) {
			System.out.println(manager.getClass().getSimpleName() + " pre init");
			manager.preInit();
		}
	}

	@Override
	public void init() throws Exception {
		for (IManager manager : this.managers) {
			System.out.println(manager.getClass().getSimpleName() + " init");
			manager.init();
		}
	}

	@Override
	public void postInit() throws Exception {
		for (IManager manager : this.managers) {
			System.out.println(manager.getClass().getSimpleName() + " post init");
			manager.postInit();
		}
		this.renderTimer.start();
		this.logicTimer.start();
	}

	@Override
	public void loop() throws Exception {
		for (IManager manager : this.managers) {
			manager.loop();
		}
	}
	
	@Override
	public void renderLoop() throws Exception {
		for(IRenderManager manager : this.renderManagers) {
			manager.renderLoop();
		}
	}

	@Override
	public void cleanUp() throws Exception {
		for (IManager manager : this.managers) {
			System.out.println(manager.getClass().getSimpleName() + " clean up");
			manager.cleanUp();
		}
	}

	/**
	 * Add a manager to be handled automatically
	 * 
	 * @param manager
	 *            The manager to add
	 * @return The engine instance
	 */
	public static Engine addManager(IManager manager) {
		instance.managers.add(manager);
		return instance;
	}
	
	/**
	 * Add a render manager to be handled automatically
	 * 
	 * @param manager
	 *            The render manager to add
	 * @return The engine instance
	 */
	public static Engine addManager(IRenderManager manager) {
		instance.managers.add(manager);
		instance.renderManagers.add(manager);
		return instance;
	}

	/**
	 * Get the value of any launch option
	 * 
	 * @param key
	 *            The name of the variable to get the value of
	 * @return The value of the launch option
	 */
	public static String getOption(String key) {
		if (!instance.options.containsKey(key)) {
			System.out.println("Cannot find key in launch file: " + key);
			System.exit(-1);
		}
		return instance.options.getOrDefault(key, "");
	}

	/**
	 * @return the {@link Engine} instance
	 */
	public static Engine getInstance() {
		return instance;
	}

}
