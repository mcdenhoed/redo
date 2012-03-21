package game;

//WormChase.java
//Roger Mailler, January 2009, adapted from
//Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* A worm moves around the screen and the user must
click (press) on its head to complete the game.

If the user misses the worm's head then a blue box
will be added to the screen (if the worm's body was
not clicked upon).

A worm cannot move over a box, so the added obstacles
*may* make it easier to catch the worm.

A worm starts at 0 length and increases to a maximum
length which it keeps from then on.

A score is displayed on screen at the end, calculated
from the number of boxes used and the time taken. Less
boxes and less time mean a higher score.

-------------

Uses full-screen exclusive mode, active rendering,
and double buffering/page flipping.

On-screen pause and quit buttons.

Using Java 3D's timer: J3DTimer.getValue()
*  nanosecs rather than millisecs for the period

Average FPS / UPS
20			50			80			100
Win 98:         20/20       50/50       81/83       84/100
Win 2000:       20/20       50/50       60/83       60/100
Win XP (1):     20/20       50/50       74/83       76/100
Win XP (2):     20/20       50/50       83/83       85/100

Located in /WormFSEM

Updated: 12th Feb 2004
* added extra sleep to the end of our setDisplayMode();

* moved createBufferStrategy() call to a separate
setBufferStrategy() method, with added extras
----
*/

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;
/**
 * Originally WormChase
 * @author Andrew Davison (modified by Mark DenHoed)
 *
 */
public class RedoGame extends GameFrame {

	private static final long serialVersionUID = -2450477630768116721L;

	private static int DEFAULT_FPS = 100;
	private World world;
	private Font font;
	private FontMetrics metrics;
	// used by quit 'button'
	private volatile boolean isOverQuitButton = false;
	private Rectangle quitArea;

	// used by the pause 'button'
	private volatile boolean isOverPauseButton = false;
	private Rectangle pauseArea;
	
 private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp


	public RedoGame(long period) {
		super(period);
	}

	@Override
	protected void simpleInitialize() {
		// create game componenths
		world = new World(0, pHeight, pWidth);
		//player = new Player();
		addKeyListener(world);
		// set up message font
		font = new Font("Monospaced", Font.BOLD, 30);
		metrics = this.getFontMetrics(font);

		// specify screen areas for the buttons
		pauseArea = new Rectangle(pWidth - 150, pHeight - 80, 70, 15);
		quitArea = new Rectangle(pWidth - 150, pHeight - 40, 70, 15);
	}

	@Override
	protected void mousePress(int x, int y) {
		if (isOverPauseButton)
			isPaused = !isPaused; // toggle pausing
		else if (isOverQuitButton)
			running = false;

	} // end of testPress()

	@Override
	protected void mouseMove(int x, int y) {
		if (running) { // stops problems with a rapid move after pressing 'quit'
			isOverPauseButton = pauseArea.contains(x, y) ? true : false;
			isOverQuitButton = quitArea.contains(x, y) ? true : false;
		}
	}


	@Override
	protected void simpleRender(Graphics2D gScr) {
		
		world.draw(gScr);
		gScr.setColor(new Color(1f,1f,1f,.5f));
		gScr.setFont(font);

	    // report frame count & average FPS and UPS at top left
		gScr.drawString("Average FPS/UPS: " + df.format(averageFPS) + ", " +
	                                df.format(averageUPS), 20, 25);  // was (10,55)
		
		// report time used and boxes used at bottom left
		gScr.drawString("Time Spent: " + timeSpentInGame + " secs", 10,
				pHeight - 15);
		/*gScr.drawString("Boxes used: " + boxesUsed, 260, pHeight - 15);*/

		// draw the pause and quit 'buttons'

		drawButtons(gScr);

	} // end of simpleRender()

	private void drawButtons(Graphics g) {
		g.setColor(new Color(1f,1f,1f,.5f));

		// draw the pause 'button'
		if (isOverPauseButton)
			g.setColor(new Color(0f,1f,0f,.5f));

		if (isPaused)
			g.drawString("Paused", pauseArea.x, pauseArea.y + 10);
		else
			g.drawString("Pause", pauseArea.x + 5, pauseArea.y + 10);

		if (isOverPauseButton)
			g.setColor(new Color(1f,1f,1f,.5f));

		// draw the quit 'button'
		if (isOverQuitButton)
			g.setColor(new Color(0f,1f,0f,.5f));

		g.drawString("Quit", quitArea.x + 15, quitArea.y + 10);

	} // drawButtons()

	@Override
	protected void gameOverMessage(Graphics g)
	// center the game-over message in the panel
	{
		metrics = this.getFontMetrics(new Font("Monospaced", 1, 80));
		String msg = "Congratulations!";
		int x = (pWidth - metrics.stringWidth(msg)) / 2;
		int y = (pHeight - metrics.getHeight()) / 2;
		g.setColor(new Color(1f,1f,1f,.5f));
		g.setFont(new Font("Monospaced", 1, 80));
		g.drawString(msg, x, y);
	} // end of gameOverMessage()

	@Override
	protected void simpleUpdate() {
		//Move the camera
		world.update();
		if(world.gameOver){
			gameOver = true;
		}
	}

	public static void main(String args[]) {
		int fps = DEFAULT_FPS;
		if (args.length != 0)
			fps = Integer.parseInt(args[0]);

		long period = (long) 1000.0 / fps;
		System.out.println("fps: " + fps + "; period: " + period + " ms");
		new RedoGame(period * 1000000L); // ms --> nanosecs
	} // end of main()

} // end of WormChase class

