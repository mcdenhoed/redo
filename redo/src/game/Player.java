package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;

/**
 * This class serves as the class for the player character. It is abstracted
 * from Actor because Actor's movement frameworks will be used to play back the
 * player's motions.
 * 
 * @author Mark DenHoed
 * 
 */
public class Player extends Actor {
	
	/*
	 * public Player(GameState initial) { super(initial);
	 * 
	 * // TODO Auto-generated constructor stub }
	 */
	/**
	 * Default Constructor
	 */
	public Player() {
		super();
		setImage(imgString);
	}

	/**
	 * Default constructor specifying an initial point
	 * 
	 * @param p
	 *            An initial position for the player.
	 */
	public Player(Point p) {
		super(p);
		setImage(imgString);
	}
	
	/**
	 * Draws the player as a blue rectangle
	 */
	@Override
	public void draw(Graphics2D g) {
		// System.out.println(currentPos.toString());

		if (image == null) { // the sprite has no image
			g.setColor(Color.yellow); // draw a yellow circle instead
			g.fillOval((int) currentPos.x, (int) currentPos.y, width, height);
			g.setColor(Color.black);
		} else{
			AffineTransform original = g.getTransform();
			Graphics2D imageGraph = (Graphics2D) image.getGraphics();
			imageGraph.rotate(Math.toRadians(theta), boundRect.getCenterX(), boundRect.getCenterY());

			g.setTransform(imageGraph.getTransform());
			g.drawImage(image, (int) currentPos.x, (int) currentPos.y, null);
			g.setTransform(original);
		}

		// g.fill(avatar);
	}

}
