package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/**
 * This class handles level selection and initialization.
 * 
 * @author Mark DenHoed
 * 
 */
public class LevelManager {

	private static final String IMS_INFO = "imgs.txt";
	private ArrayList<Button> buttons;
	private ArrayList<Platform> components;
	private int currentLevel;
	private Exit exit;
	private Rectangle gameArea;
	private Point initialPlayer;
	private int numberOfLevels = 3;
	private int screen;
	private int textX = 300;
	private BufferedImage image;
	private String imgString = "/assets/images/tiles.png";
	private int textY = 400;
	private String msg;
	/**
	 * This constructor allows specification of level, and takes a screen height
	 * To aid in the initial camera configuration, all levels are 'built' around
	 * the bottom left of the screen (which requires knowledge of the height).
	 * 
	 * @param levelNum
	 *            Level number
	 * @param height
	 *            the Screen height
	 */
	public LevelManager(int levelNum, int height) {
		// ImagesLoader imsLoader = new ImagesLoader(IMS_INFO, );
		setImage(imgString);
		screen = height;
		components = new ArrayList<Platform>();
		initialPlayer = new Point();
		buttons = new ArrayList<Button>();
		// Select level
		switch (levelNum) {
		case 0:
			currentLevel = 0;
			level0();
			break;
		case 1:
			currentLevel = 1;
			level1();
			break;
		case 2:
			currentLevel = 2;
			level2();
			break;
		case 3:
			currentLevel = 3;
			level3();
			break;
		}
	}

	private void setImage(String img) {
		URL imageURL = Actor.class.getResource(img);
		System.out.println(imageURL.toString());
		try {
			image = ImageIO.read(imageURL);
		} catch (IOException e) {
			System.out.println("No such Image!");
		}
	}

	/**
	 * This method draws all the rectangles of the level.
	 * 
	 * @param g
	 *            the Graphics2D object
	 */
	public synchronized void draw(Graphics2D g) {
		exit.draw(g);

		g.setColor(Color.BLACK);
		for (Platform r : components) {
			// if (r.isVisible())
			g.setPaint(new TexturePaint(image, new Rectangle(r.x, r.y, image
					.getWidth(), image.getHeight())));
			g.fill(r);
		}
		drawTitle(g);
	}

	/**
	 * Draws the level's title "Level x" onto the starting location of the
	 * level.
	 * 
	 * @param g
	 *            Graphics2D object
	 */
	protected void drawTitle(Graphics2D g)
	// center the game-over message in the panel
	{
		
		g.setColor(Color.ORANGE);
		g.setFont(new Font("Monospaced", 1, 60));
		g.drawString(msg, textX, textY);
	} // end of gameOverMessage()

	/**
	 * @return the buttons
	 */
	public ArrayList<Button> getButtons() {
		return buttons;
	}

	/**
	 * @return the components
	 */
	public ArrayList<Platform> getComponents() {
		return components;
	}

	/**
	 * @return the currentLevel
	 */
	public int getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * @return the exit
	 */
	public Exit getExit() {
		return exit;
	}

	/**
	 * @return the gameArea
	 */
	public Rectangle getGameArea() {
		return gameArea;
	}

	/**
	 * @return the initialPlayer
	 */
	public Point getInitialPlayer() {
		return initialPlayer;
	}

	/**
	 * @return the numberOfLevels
	 */
	public int getNumberOfLevels() {
		return numberOfLevels;
	}

	/**
	 * Creates a level
	 */
	private void level0() {
		components = new ArrayList<Platform>();
		components.add(new Platform(0, screen - 200, 1920, 200, null));
		components.add(new Platform(2300, screen - 50, 50, 500, null));
		components.add(new Platform(2800, screen - 200, 500, 1200, null));
		components.add(new Platform(4000, screen - 200, 1500, 500, null));
		components.add(new Platform(3600, -1000, 50, screen + 600, null));
		exit = new Exit(4500, screen - 300, 100, 100);
		initialPlayer.setLocation(100, screen - 300);
		gameArea = new Rectangle(-500, -500, 8000, 10000);
		msg = "Level " + currentLevel;
	}

	/**
	 * Creates a level
	 */
	private void level1() {
		components = new ArrayList<Platform>();
		Platform ground = new Platform(0, 880, 3000, 1200, null);

		components.add(ground);
		components.add(new Platform(800, 800, 100, 100, null));
		components.add(new Platform(1200, 800, 100, 100, null));
		components.add(new Platform(1800, 400, 100, 100, null));
		components.add(new Platform(1000, 500, 100, 100, null));
		components.add(new Platform(100, 700, 100, 100, null));
		components.add(new Platform(600, 600, 100, 100, null));
		components.add(new Platform(900, 200, 100, 100, null));
		components.add(new Platform(1500, 550, 100, 100, null));
		exit = new Exit(2000, 50, 100, 100);
		initialPlayer.setLocation(100, 100);
		gameArea = new Rectangle(-2000, -3000, 8000, 10000);
		msg = "Level " + currentLevel;
	}

	/**
	 * Creates a level
	 */
	private void level2() {
		components = new ArrayList<Platform>();
		components.add(new Platform(0, screen - 200, 1920, 200, null));
		components.add(new Platform(0, screen - 600, 200, 200, null));
		components.add(new Platform(screen + 800, -200, 200, screen, null));
		components.add(new Platform(screen, 400, 200, 200, null));
		components.add(new Platform(screen - 200, 600, 100, 100, null));
		components.add(new Platform(screen + 100, 100, 200, 200, null));
		components.add(new Platform(screen - 300, 0, 200, 200, null));
		components.add(new Platform(screen + 200, -200, 200, 200, null));
		components.add(new Platform(screen - 300, -400, 200, 200, null));
		components.add(new Platform(screen , -800, 200, 200, null));
		components.add(new Platform(screen - 500, -1000, 200, 200, null));
		components.add(new Platform(screen - 900, -1200, 200, 200, null));

		exit = new Exit(500, -1500, 100, 100);
		initialPlayer.setLocation(100, screen - 300);
		gameArea = new Rectangle(-1000, -3000, 3000, 6000);
		msg = "Level " + currentLevel;
	}

	/**
	 * Creates a level (unused)
	 */
	private void level3() {
		components = new ArrayList<Platform>();
		components.add(new Platform(0, screen - 200, 4500, 200, null));
		exit = new Exit(4500, screen - 300, 100, 100);
		initialPlayer.setLocation(100, screen - 300);
		gameArea = new Rectangle(-500, -500, 8000, 10000);
		msg = "All art by Mark DenHoed.\n All sounds by Blake Burkhart.\n Music by homestarrunner.com.";
	}

	/**
	 * This method offsets the level based on the camera's motion.
	 * 
	 * @param off
	 *            the offset from the camera
	 */
	public void moveLevel(Point2D.Double off) {
		// rootNode.setLocation(rootNode.getX()-offset.getX(),
		// rootNode.getY()-offset.getY());
		for (Rectangle r : components) {
			r.translate((int) off.x, (int) off.y);
		}
		exit.box.translate((int) off.x, (int) off.y);
		gameArea.translate((int) off.x, (int) off.y);
		textX += (int) off.x;
		textY += (int) off.y;
		// Moves buttons
		// Move obstacles
	}

	/**
	 * @param buttons
	 *            the buttons to set
	 */
	public void setButtons(ArrayList<Button> buttons) {
		this.buttons = buttons;
	}

	/**
	 * @param components
	 *            the components to set
	 */
	public void setComponents(ArrayList<Platform> components) {
		this.components = components;
	}

	/**
	 * @param currentLevel
	 *            the currentLevel to set
	 */
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	/**
	 * @param exit
	 *            the exit to set
	 */
	public void setExit(Exit exit) {
		this.exit = exit;
	}

	/**
	 * @param gameArea
	 *            the gameArea to set
	 */
	public void setGameArea(Rectangle gameArea) {
		this.gameArea = gameArea;
	}

	/**
	 * @param initialPlayer
	 *            the initialPlayer to set
	 */
	public void setInitialPlayer(Point initialPlayer) {
		this.initialPlayer = initialPlayer;
	}

	/**
	 * @param numberOfLevels
	 *            the numberOfLevels to set
	 */
	public void setNumberOfLevels(int numberOfLevels) {
		this.numberOfLevels = numberOfLevels;
	}
}
