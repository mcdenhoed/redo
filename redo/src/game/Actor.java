package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * Represents a player or a recording of a player
 * 
 * @author Mark DenHoed
 * 
 */
public abstract class Actor {
	protected Point2D.Double currentPos;
	protected Point2D.Double currentVel;
	protected Point2D.Double currentAcc;
	protected boolean onGround;
	protected double maxVelx = 70;
	protected double theta = 0;
	protected double dtheta = 0;
	protected int width = 30;
	protected int height = 30;
	protected boolean left = false;
	protected boolean right = false;
	protected final double velDamp = .1;
	protected final double accDamp = .35;
	protected final double acc = 3;
	protected final double grav = 2.9;
	protected Rectangle boundRect;
	protected BufferedImage image;
	String imgString = "/assets/images/coolball.png";

	/**
	 * Constructor
	 * 
	 * @param initial
	 *            the initial position of the actor
	 */
	public Actor(Point initial) {
		currentPos = new Point2D.Double(initial.x, initial.y);
		currentVel = new Point2D.Double(0, 0);
		currentAcc = new Point2D.Double();
		boundRect = new Rectangle((int) currentPos.x, (int) currentPos.y,
				width, height);
		currentAcc.setLocation(0, grav);
	}

	/**
	 * Default constructor
	 */
	public Actor() {
		currentPos = new Point2D.Double(100, 100);
		currentVel = new Point2D.Double(0, 0);
		currentAcc = new Point2D.Double();
		currentAcc.setLocation(0, grav);
		// avatar = new ActorSprite(currentPos.x, currentPos.y, 30, 30, null);
	}

	/**
	 * Handles the left key being pressed
	 */
	protected void leftPress() {
		if (onGround)
			currentAcc.x = -2.8 * acc;
		else
			currentAcc.x = -.9 * acc;
		left = true;
	}

	/**
	 * Handles the left key being released
	 */
	protected void leftRelease() {
		left = false;
	}

	/**
	 * Handles the right key being pressed
	 */
	protected void rightPress() {
		if (onGround)
			currentAcc.x = 2.8 * acc;
		else
			currentAcc.x = .9 * acc;
		right = true;
	}

	/**
	 * Handles the right key being released
	 */
	protected void rightRelease() {
		right = false;
	}

	/**
	 * Handles the jump key betting pressed
	 */
	protected void jumpPress() {
		if (onGround) {
			currentVel.y = -80;
			onGround = false;
		}
	}

	/**
	 * Updates the actor
	 */
	public void update() {
		// Update the position
		currentPos.x += velDamp * currentVel.x;
		currentPos.y += velDamp * currentVel.y;
		// Cap the velocity
		if (currentVel.distance(0, currentVel.y) > maxVelx
				&& currentVel.x * currentAcc.x > 0) {
			currentAcc.x = 0;
		}
		// Update the velocity
		currentVel.x += accDamp * currentAcc.x;
		currentVel.y += accDamp * currentAcc.y;

		// Handles making the player slow down when there's no input
		if (!(left ^ right)) {
			if (onGround)
				currentAcc.x = -.2 * (currentVel.x);
			else
				currentAcc.x = -.12 * (currentVel.x);
		}

		boundRect.setLocation((int) currentPos.x, (int) currentPos.y);

		if (left) {
			dtheta = -5;
		} else if (right) {
			dtheta = 5;
		} else {
			dtheta *= .95;
		}
		
		theta = (theta+dtheta)%360;
		// Update the player's drawing position
		// avatar.updateSprite(currentPos.x, currentPos.y, rotate);

	}

	/**
	 * Move the actor based on camera motion
	 * 
	 * @param offset
	 *            the camera's offset
	 */
	public void offset(Point2D.Double offset) {
		double newX = currentPos.x + (int) offset.x;
		double newY = currentPos.y + (int) offset.y;
		currentPos.setLocation(newX, newY);
		// avatar.updateSprite(newX, newY);

	}

	public void setImage(String name)
	// assign the name image to the sprite
	{
		URL imageURL = Actor.class.getResource(name);
		System.out.println(imageURL.toString());
		try {
			image = ImageIO.read(imageURL);
			width = image.getWidth();
			height = image.getHeight();
			boundRect.width = width;
			boundRect.height = height;
		} catch (IOException e) {
			System.out.println("No such Image!");
		}
	} // end of setImage()

	public void setLocation(double x, double y) {
		currentPos.x = x;
		currentPos.y = y;
		boundRect.setLocation((int) x, (int) y);
	}

	/**
	 * Handles collisions between the player and the level
	 * 
	 * @param r
	 *            the rectangle in question
	 */
	public void rest(Rectangle r) {
		while (boundRect.intersects(r)) {
			// Get the intersection

			Rectangle inter = boundRect.intersection(r);
			// Setting this to false will turn off wall jumps
			onGround = true;
			// If on the bottom right (meaning bottom or right side)
			if (boundRect.getX() == inter.x && boundRect.getY() == inter.y) {
				// If on the right
				if (inter.height >= inter.width) {
					// Move right
					// avatar.translateX(1);
					currentPos.x += 1;
					// if (!left)
					currentVel.x = -.7 * currentVel.x;

					// If on the left
				} else if (inter.height < inter.width) {
					// Move down
					// avatar.translateY(1);
					currentPos.y += 1;
					currentVel.y = 0;
				} else {
					// Nothing
				}
				// If on the top or the left
			} else {
				// If on the left
				if (inter.height > inter.width) {
					// Move left
					// avatar.translateX(-1);
					currentPos.x -= 1;
					// if (!right)
					currentVel.x = -.7 * currentVel.x;
					// If on the top
				} else {
					// avatar.translateY(-1);
					currentPos.y -= 1;
					currentVel.y = 0;
					// onGround = true;
				}
			}
			boundRect.setLocation((int) currentPos.x, (int) currentPos.y);

		}
	}

	/**
	 * Draws the actor
	 * 
	 * @param g
	 *            Graphics2D object
	 */
	public abstract void draw(Graphics2D g);
}
