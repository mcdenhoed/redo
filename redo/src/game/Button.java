package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;
/**
 * This class is currently unused. It will allow players to trigger events,
 * open doors and all that kind of stuff.
 * @author Mark DenHoed
 *
 */
public class Button {
	protected boolean triggered = false;
	protected Rectangle box;
	protected String groupName;
	/**
	 * Constructor
	 * @param x The x coordinate of the button
	 * @param y The y coordinate of the button
	 * @param width The width of the button
	 * @param height The height of the button
	 */
	public Button(int x, int y, int width, int height) {
		box = new Rectangle(x, y, width, height);
	}

	/**
	 * Draw (unused)
	 * @param g
	 */
	public synchronized void draw(Graphics2D g) {
		// TODO Auto-generated method stub

	}
	/**
	 * Update (unused)
	 */
	public void update(){
		
	}
}
