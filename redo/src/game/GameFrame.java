package game;

// GameFrame.java
// Roger Mailler, January 2009, adapted from
// 		Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.ImageCapabilities;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;

import javax.swing.JFrame;

public abstract class GameFrame extends JFrame implements Runnable {
	private static final long serialVersionUID = 1863596360846514344L;
	private static final int NUM_BUFFERS = 2; // used for page flipping

	private static long MAX_STATS_INTERVAL = 1000000000L;
	// record stats every 1 second (roughly)

	private static final int NO_DELAYS_PER_YIELD = 16;

	/*
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads.
	 */
	private static int MAX_FRAME_SKIPS = 5; // was 2;
	// no. of frames that can be skipped in any one animation loop
	// i.e the games state is updated but not rendered

	private static int NUM_FPS = 10;
	// number of FPS values stored to get an average

	protected int pWidth, pHeight; // panel dimensions

	private Thread animator; // the thread that performs the animation
	protected boolean running = false; // used to stop the animation thread
	protected boolean isPaused = false;
	private boolean finishedOff = false;

	protected long period; // period between drawing in _nanosecs_

	// used for gathering statistics
	private long statsInterval = 0L; // in ns
	private long prevStatsTime;
	private long totalElapsedTime = 0L;
	private long gameStartTime;
	protected int timeSpentInGame = 0; // in seconds

	private long frameCount = 0;
	private double fpsStore[];
	private long statsCount = 0;
	protected double averageFPS = 0.0;

	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;
	private double upsStore[];
	protected double averageUPS = 0.0;
	private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp


	// used at game termination
	protected boolean gameOver = false;

	// used for full-screen exclusive mode
	private GraphicsDevice gd;
	private Graphics2D gScr;
	private BufferStrategy bufferStrategy;

	public GameFrame(long period) {
		this.period = period;

		initFullScreen();

		readyForTermination();

		simpleInitialize();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				mousePress(e.getX(), e.getY());
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouseMove(e.getX(), e.getY());
			}
		});

		// Initialize timing elements
		fpsStore = new double[NUM_FPS];
		upsStore = new double[NUM_FPS];
		for (int i = 0; i < NUM_FPS; i++) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}

		gameStart();

	} // end of GamePanel()

	private void initFullScreen() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		gd = ge.getDefaultScreenDevice();

		setUndecorated(true); // no menu bar, borders, etc. or Swing components
		setIgnoreRepaint(true); // turn off all paint events since doing active
		// rendering
		setResizable(false);

		if (!gd.isFullScreenSupported()) {
			System.out.println("Full-screen exclusive mode not supported");
			System.exit(0);
		}
		gd.setFullScreenWindow(this); // switch on full-screen exclusive mode

		// we can now adjust the display modes, if we wish
		showCurrentMode();

		// setDisplayMode(800, 600, 8); // or try 8 bits

		// setDisplayMode(1280,720, 32);

		reportCapabilities();

		pWidth = getBounds().width;
		pHeight = getBounds().height;

		setBufferStrategy();
	} // end of initFullScreen()

	private void reportCapabilities() {
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		// Image Capabilities
		ImageCapabilities imageCaps = gc.getImageCapabilities();
		System.out.println("Image Caps. isAccelerated: "
				+ imageCaps.isAccelerated());
		System.out.println("Image Caps. isTrueVolatile: "
				+ imageCaps.isTrueVolatile());

		// Buffer Capabilities
		BufferCapabilities bufferCaps = gc.getBufferCapabilities();
		System.out.println("Buffer Caps. isPageFlipping: "
				+ bufferCaps.isPageFlipping());
		System.out.println("Buffer Caps. Flip Contents: "
				+ getFlipText(bufferCaps.getFlipContents()));
		System.out.println("Buffer Caps. Full-screen Required: "
				+ bufferCaps.isFullScreenRequired());
		System.out.println("Buffer Caps. MultiBuffers: "
				+ bufferCaps.isMultiBufferAvailable());
	} // end of reportCapabilities()

	private String getFlipText(BufferCapabilities.FlipContents flip) {
		if (flip == null)
			return "false";
		else if (flip == BufferCapabilities.FlipContents.UNDEFINED)
			return "Undefined";
		else if (flip == BufferCapabilities.FlipContents.BACKGROUND)
			return "Background";
		else if (flip == BufferCapabilities.FlipContents.PRIOR)
			return "Prior";
		else
			// if (flip == BufferCapabilities.FlipContents.COPIED)
			return "Copied";
	} // end of getFlipTest()

	private void setBufferStrategy() {
		createBufferStrategy(NUM_BUFFERS);
		bufferStrategy = getBufferStrategy(); // store for later
	} // end of setBufferStrategy()

	private void readyForTermination() {
		addKeyListener(new KeyAdapter() {
			// listen for esc, q, end, ctrl-c on the canvas to
			// allow a convenient exit from the full screen configuration
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if ((keyCode == KeyEvent.VK_ESCAPE)
						|| (keyCode == KeyEvent.VK_Q)
						|| (keyCode == KeyEvent.VK_END)
						|| ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
					running = false;
				}
			}
		});

		// for shutdown tasks
		// a shutdown may not only come from the program
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				running = false;
				finishOff();
			}
		});
	} // end of readyForTermination()

	/**
	 * Starts the animation loop
	 */
	private void gameStart()
	// initialise and start the thread
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()

	// ------------- game life cycle methods ------------
	public void resumeGame() {
		isPaused = false;
	}

	public void pauseGame() {
		isPaused = true;
	}

	public void stopGame() {
		running = false;
	}

	// ----------------------------------------------

	public void run()
	/* The frames of the animation are drawn inside the while loop. */
	{
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;

		gameStartTime = System.nanoTime();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;

		running = true;

		while (running) {
			// MAGIC
			gameUpdate();
			// MORE MAGIC
			screenUpdate();

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else { // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the
			 * required FPS.
			 */
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate(); // update state but don't render
				skips++;
			}
			framesSkipped += skips;
			storeStats();
		}
		finishOff();
		System.exit(0); // so window disappears
	} // end of run()

	/**
	 * Renders to the backbuffer
	 */
	private void gameRender(Graphics2D gScr) {
		// clear the background

		gScr.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		simpleRender(gScr);

		if (gameOver)
			gameOverMessage(gScr);
	} // end of gameRender()

	private void screenUpdate() {
		// use active rendering
		try {
			// I'm guessing this is ok because Graphics2D extends Graphics
			gScr = (Graphics2D) bufferStrategy.getDrawGraphics();

			gameRender(gScr);
			gScr.dispose();
			if (!bufferStrategy.contentsLost())
				bufferStrategy.show();
			else
				System.out.println("Contents Lost");
		} catch (Exception e) {
			e.printStackTrace();
			running = false;
		}
	} // end of screenUpdate()

	/**
	 * Should be update the game state
	 */
	private void gameUpdate() {
		if (!isPaused && !gameOver)
			// MAGIC
			simpleUpdate();

	} // end of gameUpdate()

	private void storeStats()
	/*
	 * The statistics: - the summed periods for all the iterations in this
	 * interval (period is the amount of time a single frame iteration should
	 * take), the actual elapsed time in this interval, the error between these
	 * two numbers;
	 * 
	 * - the total frame count, which is the total number of calls to run();
	 * 
	 * - the frames skipped in this interval, the total number of frames
	 * skipped. A frame skip is a game update without a corresponding render;
	 * 
	 * - the FPS (frames/sec) and UPS (updates/sec) for this interval, the
	 * average FPS & UPS over the last NUM_FPSs intervals.
	 * 
	 * The data is collected every MAX_STATS_INTERVAL (1 sec).
	 */
	{
		frameCount++;
		statsInterval += period;

		if (statsInterval >= MAX_STATS_INTERVAL) { // record stats every
			// MAX_STATS_INTERVAL
			long timeNow = System.nanoTime();
			timeSpentInGame = (int) ((timeNow - gameStartTime) / 1000000000L); // ns
			// --
			// >
			// secs

			long realElapsedTime = timeNow - prevStatsTime; // time since last
			// stats collection
			totalElapsedTime += realElapsedTime;

			totalFramesSkipped += framesSkipped;

			double actualFPS = 0; // calculate the latest FPS and UPS
			double actualUPS = 0;
			if (totalElapsedTime > 0) {
				actualFPS = (((double) frameCount / totalElapsedTime) * 1000000000L);
				actualUPS = (((double) (frameCount + totalFramesSkipped) / totalElapsedTime) * 1000000000L);
			}

			// store the latest FPS and UPS
			fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
			upsStore[(int) statsCount % NUM_FPS] = actualUPS;
			statsCount = statsCount + 1;

			double totalFPS = 0.0; // total the stored FPSs and UPSs
			double totalUPS = 0.0;
			for (int i = 0; i < NUM_FPS; i++) {
				totalFPS += fpsStore[i];
				totalUPS += upsStore[i];
			}

			if (statsCount < NUM_FPS) { // obtain the average FPS and UPS
				averageFPS = totalFPS / statsCount;
				averageUPS = totalUPS / statsCount;
			} else {
				averageFPS = totalFPS / NUM_FPS;
				averageUPS = totalUPS / NUM_FPS;
			}
			/*
			 * System.out.println(timedf.format( (double)
			 * statsInterval/1000000000L) + " " + timedf.format((double)
			 * realElapsedTime/1000000000L) + "s " + df.format(timingError) +
			 * "% " + frameCount + "c " + framesSkipped + "/" +
			 * totalFramesSkipped + " skip; " + df.format(actualFPS) + " " +
			 * df.format(averageFPS) + " afps; " + df.format(actualUPS) + " " +
			 * df.format(averageUPS) + " aups" );
			 */
			framesSkipped = 0;
			prevStatsTime = timeNow;
			statsInterval = 0L; // reset
		}
	} // end of storeStats()

	private void finishOff()
	/*
	 * Tasks to do before terminating. Called at end of run() and via the
	 * shutdown hook in readyForTermination().
	 * 
	 * The call at the end of run() is not really necessary, but included for
	 * safety. The flag stops the code being called twice.
	 */
	{ // System.out.println("finishOff");
		if (!finishedOff) {
			finishedOff = true;
			printStats();
			restoreScreen();
			System.exit(0);
		}
	} // end of finishedOff()

	private void printStats() {
		System.out.println("Frame Count/Loss: " + frameCount + " / "
				+ totalFramesSkipped);
		System.out.println("Average FPS: " + df.format(averageFPS));
		System.out.println("Average UPS: " + df.format(averageUPS));
		System.out.println("Time Spent: " + timeSpentInGame + " secs");
	} // end of printStats()

	private void restoreScreen()
	/*
	 * Switch off full screen mode. This also resets the display mode if it's
	 * been changed.
	 */
	{
		Window w = gd.getFullScreenWindow();
		if (w != null)
			w.dispose();
		gd.setFullScreenWindow(null);
	} // end of restoreScreen()

	// ------------------ display mode methods -------------------

	private void setDisplayMode(int width, int height, int bitDepth)
	// attempt to set the display mode to the given width, height, and bit depth
	{
		if (!gd.isDisplayChangeSupported()) {
			System.out.println("Display mode changing not supported");
			return;
		}

		if (!isDisplayModeAvailable(width, height, bitDepth)) {
			System.out.println("Display mode (" + width + "," + height + ","
					+ bitDepth + ") not available");
			return;
		}

		DisplayMode dm = new DisplayMode(width, height, bitDepth,
				DisplayMode.REFRESH_RATE_UNKNOWN); // any refresh rate
		try {
			gd.setDisplayMode(dm);
			System.out.println("Display mode set to: (" + width + "," + height
					+ "," + bitDepth + ")");
		} catch (IllegalArgumentException e) {
			System.out.println("Error setting Display mode (" + width + ","
					+ height + "," + bitDepth + ")");
		}

		try { // sleep to give time for the display to be changed
			Thread.sleep(1000); // 1 sec
		} catch (InterruptedException ex) {
		}
	} // end of setDisplayMode()

	private boolean isDisplayModeAvailable(int width, int height, int bitDepth)
	/*
	 * Check that a displayMode with this width, height, bit depth is available.
	 * We don't care about the refresh rate, which is probably
	 * REFRESH_RATE_UNKNOWN anyway.
	 */
	{
		DisplayMode[] modes = gd.getDisplayModes();
		showModes(modes);

		for (int i = 0; i < modes.length; i++) {
			if (width == modes[i].getWidth() && height == modes[i].getHeight()
					&& bitDepth == modes[i].getBitDepth())
				return true;
		}
		return false;
	} // end of isDisplayModeAvailable()

	private void showModes(DisplayMode[] modes)
	// pretty print the display mode information in modes
	{
		System.out.println("Modes");
		for (int i = 0; i < modes.length; i++) {
			System.out.print("(" + modes[i].getWidth() + ","
					+ modes[i].getHeight() + "," + modes[i].getBitDepth() + ","
					+ modes[i].getRefreshRate() + ")  ");
			if ((i + 1) % 4 == 0)
				System.out.println();
		}
		System.out.println();
	} // end of showModes()

	private void showCurrentMode()
	// print the display mode details for the graphics device
	{
		DisplayMode dm = gd.getDisplayMode();
		System.out.println("Current Display Mode: (" + dm.getWidth() + ","
				+ dm.getHeight() + "," + dm.getBitDepth() + ","
				+ dm.getRefreshRate() + ")  ");
	}

	// -----------------------------------------

	/**
	 * Should implement game specific rendering
	 * 
	 * @param g
	 */
	protected abstract void simpleRender(Graphics2D g);

	/**
	 * Should display a game specific game over message
	 * 
	 * @param g
	 */
	protected abstract void gameOverMessage(Graphics g);

	protected abstract void simpleUpdate();

	/**
	 * This just gets called when a click occurs, no default behavior
	 */
	protected abstract void mousePress(int x, int y);

	/**
	 * This just gets called when a click occurs, no default behavior
	 */
	protected abstract void mouseMove(int x, int y);

	/**
	 * Should be overridden to initialize the game specific components
	 */
	protected abstract void simpleInitialize();

} // end of GamePanel class

