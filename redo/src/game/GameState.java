package game;

import java.awt.Point;
/**
 * This will keep track of what buttons are pressed
 * And possibly by which actors. It is unused at the moment
 * But shows where the game is headed
 * @author Mark DenHoed
 *
 */
public class GameState {
	private Point initialPos;
	private Point initialVel;
	private boolean buttonsPressed[];
	
	public Point getInitialPos() {
		return initialPos;
	}
	public void setInitialPos(Point initialPos) {
		this.initialPos = initialPos;
	}
	public Point getInitialVel() {
		return initialVel;
	}
	public void setInitialVel(Point initialVel) {
		this.initialVel = initialVel;
	}
	public boolean[] getButtonsPressed() {
		return buttonsPressed;
	}
	public void setButtonsPressed(boolean[] buttonsPressed) {
		this.buttonsPressed = buttonsPressed;
	}
}
