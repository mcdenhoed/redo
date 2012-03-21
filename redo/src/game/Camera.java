package game;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Handles the game's camera motions
 * 
 * @author Mark
 * 
 */
public class Camera {
	private Point.Double camPos;
	private Point.Double camVel;
	private double damper = .0003;// .0005;
	private int screenWidth;
	private int screenHeight;
	private int minWidth, maxWidth;
	private int minHeight, maxHeight;
	private Rectangle allowableArea;

	/**
	 * Constructor
	 * 
	 * @param screenWidth
	 *            width of the screen
	 * @param screenHeight
	 *            height of the screen
	 */
	Camera(int screenWidth, int screenHeight) {
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		minWidth = screenWidth / 3;
		minHeight = screenHeight / 2;
		maxWidth = screenWidth;
		maxHeight = screenHeight;
		int width = minWidth;
		int height = minHeight;
		System.out.println(width + " " + height);
		camPos = new Point.Double(0, 0);
		camVel = new Point.Double(0, 0);
		// Rectangle that the player will be 'pushed' into
		allowableArea = new Rectangle(width, height);
		int xChange = (int) (.5 * screenWidth - .5 * width) - screenWidth / 2;
		int yChange = (int) (.5 * screenHeight - .5 * height) - screenHeight/ 2;
		allowableArea.translate(xChange, yChange);
	}

	/**
	 * Updates the camera (to move it towards the player)
	 * 
	 * @param player
	 *            the location of the player
	 * @return the offset of the camera (to apply to both level and player)
	 */
	public synchronized Point.Double updateCamera(Point.Double player) {
		// If the player is too far to the edge of the screen
		if (!allowableArea.contains(player)) {
			// Determine the camera's next setp
			camVel.x = allowableArea.getCenterX() - player.x;
			camVel.y = (allowableArea.getCenterY() - player.y);
			// Because it is fairly easy for the camera to get stuck in 'orbit'
			// I restrained it to either move up or move left.
			// This is by no means a perfect fix, but it does help some
			// In most cases
			//if (camVel.distance(0, camVel.y) >= camVel.distance(camVel.x, 0))
				camPos.x += damper * camVel.x;
			//else
				camPos.y += damper * camVel.y;
			//if (allowableArea.width <= 2*maxWidth
				//	&& allowableArea.height <= 2*maxHeight)
				biggerArea();
		} else {
			// I didn't want the camera to abruptly stop,
			// So it slows down... slowly
			// I should probably redirect it towards the center once again (just
			// to make sure)
			camPos.x = .95 * camPos.x;
			camPos.y = .95 * camPos.y;
			if (allowableArea.width > minWidth
					&& allowableArea.height > minHeight)
				smallerArea();

		}

		return camPos;
	}

	/**
	 * @return the camPos
	 */
	public Point.Double getCamPos() {
		return camPos;
	}

	private void biggerArea() {
		allowableArea.height += 16;
		allowableArea.width += 16;
		recenter();
	}

	private void smallerArea() {
		allowableArea.height -= 64;
		allowableArea.width -= 64;
		recenter();
	}

	private void recenter() {
		int xChange = (int) (.5 * screenWidth - .5 * allowableArea.width)
				- screenWidth / 4;
		int yChange = (int) (.5 * screenHeight - .5 * allowableArea.height)
				+ screenHeight / 8;
		allowableArea.setLocation(xChange, yChange);
	}

	/**
	 * @param camPos
	 *            the camPos to set
	 */
	public void setCamPos(Point.Double camPos) {
		this.camPos = camPos;
	}

}
