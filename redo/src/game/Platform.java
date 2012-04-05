package game;

import java.awt.Rectangle;

/**
 * Extends Rectangle to add in a visibility flag (which will be useful later)
 * 
 * @author Mark DenHoed
 * 
 */
@SuppressWarnings("serial")
public class Platform extends Rectangle {

	// private boolean isFloor;
	private boolean isVisible;
	private String group = null;

	/**
	 * Constructor
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public Platform(int x, int y, int width, int height, String groupName) {
		super(x, y, width, height);
		if (groupName != null)
			group = groupName;
	}

	/**
	 * @return the isVisible
	 */
	public boolean isVisible() {
		return isVisible;
	}

	/**
	 * @param isVisible
	 *            the isVisible to set
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
