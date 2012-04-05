package game;
/**
 * This will make recordings of the player.
 * It will be used for puzzles. And be awesome.
 * @author Mark DenHoed
 *
 */
public class Recorder extends Button{
	/**
	 * Constructor!
	 * @param x position in x
	 * @param y position in y
	 * @param width width of recorder
	 * @param height height of recorder
	 */
	public Recorder(int x, int y, int width, int height) {
		super(x, y, width, height);
		// TODO Auto-generated constructor stub
	}
	//TO BE USED LATER:
	private GameState initialState;
	private GameState finalState;
	private GameState currentState;
	private Recording bot;
}
