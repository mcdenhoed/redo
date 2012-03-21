package game;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 * Handles user input, levels, game states.
 * @author Mark DenHoed
 *
 */
public class World implements KeyListener {
	private LevelManager levelManager;
	int yscreen;
	int xscreen;
	private Player player;
	private Camera camera;
	public boolean gameOver;
	private Background background;

	
	World(int ID, int height, int width) {
 
		yscreen = height;
		xscreen = width;
		camera = new Camera(xscreen, yscreen);
		levelManager = new LevelManager(0, yscreen);
		background = new Background(xscreen, yscreen);
		player = new Player(levelManager.getInitialPlayer());
		playMusic();
	}
/**
 * Draws all the components of the game
 * @param g
 */
	public synchronized void draw(Graphics2D g) {
		background.draw(g);
		player.draw(g);
		levelManager.draw(g);
		
	}
	
	  public synchronized void playMusic() {
		    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
		      public void run() {
		        try {
		        
		          Clip clip = AudioSystem.getClip();
		          AudioInputStream inputStream = AudioSystem.getAudioInputStream(World.class.getResourceAsStream("/assets/sounds/background.wav"));
		          clip.open(inputStream);
		          clip.start(); 
		          clip.loop(Clip.LOOP_CONTINUOUSLY);
		        } catch (Exception e) {
		          System.err.println(e.getMessage());
		        }
		      }
		    }).start();
		  }
	
	  public synchronized void playSound(final String name) {
		    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
		      public void run() {
		        try {
		        
		          Clip clip = AudioSystem.getClip();
		          AudioInputStream inputStream = AudioSystem.getAudioInputStream(World.class.getResourceAsStream("/assets/sounds/" + name));
		          clip.open(inputStream);
		          clip.start(); 
		        } catch (Exception e) {
		          System.err.println(e.getMessage());
		        }
		      }
		    }).start();
		  }
	  
/**
 * Updates all the components of the game
 */
	public void update() {
		// Check for win
		if (levelManager.getExit().box.intersects(player.boundRect)) {
			playSound("level2.wav");
			if(levelManager.getCurrentLevel()<levelManager.getNumberOfLevels()){
				int next = levelManager.getCurrentLevel() + 1;
				levelManager = new LevelManager(next, yscreen);
				player.setLocation(levelManager.getInitialPlayer().getX(),levelManager.getInitialPlayer().getY());
				player.currentPos.setLocation(levelManager.getInitialPlayer());
			}else{
				gameOver=true;
			}
		}
		// Check for falling off the screen
		if (!levelManager.getGameArea().contains(player.boundRect)){
			playSound("gameover2.wav");
			resetWorld();
		}
		// Update the camera (move everything opposite of the camera)
		Point.Double offset = camera.updateCamera(player.currentPos);
		levelManager.moveLevel(offset);
		player.offset(offset);
		background.update(offset);
		// Update the player
		player.update();
		for (Platform r : levelManager.getComponents()) {
			// Later this might be a nested loop for player as well as
			// recordings

			//if the player intersects the level
			if (player.boundRect.intersects(r.getBounds2D())) {
				//Handles intersections
				player.rest(r);
			}
		}

		// Update the recordings?
		// Update any states of buttons
		// Update any states of doors/obstacles

	}

	/**
	 * Resets player and level to their original positions
	 */
	public void resetWorld(){
		camera = new Camera(xscreen, yscreen);
		levelManager = new LevelManager(levelManager.getCurrentLevel(), yscreen);
		background = new Background(xscreen, yscreen);
		player = new Player(levelManager.getInitialPlayer());
	}
	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() == KeyEvent.VK_W) {
			if(player.onGround)
				playSound("jump2.wav");
			player.jumpPress();
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			player.leftPress();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			player.rightPress();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_A) {
			player.leftRelease();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			player.rightRelease();
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
