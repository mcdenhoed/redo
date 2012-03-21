package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * A parallax background for the game (written BEFORE Dr. Mailler mentioned the
 * possibility in lecture).
 * 
 * @author Mark DenHoed
 * 
 */
public class Background {
	private int screenX;
	private int screenY;
	private int numBackLevel = 50;
	private int numMidLevel = 15;
	private int numFrontLevel = 6;
	private int maxRectSize;
	private Point.Double frontPos;
	private Point.Double midPos;
	private Point.Double backPos;
	private ArrayList<Point> frontRect;
	private ArrayList<Point> middleRect;
	private ArrayList<Point> backRect;
	private double damper = .75;
	private Random ran;
	private String img1 = "/assets/images/bigcloud.png";
	private String img2 = "/assets/images/cloud2.png";
	private String img3 = "/assets/images/cloud3.png";
	private BufferedImage image1;
	private BufferedImage image2;
	private BufferedImage image3;
	/**
	 * Constructor
	 * 
	 * @param screenX
	 *            screen width
	 * @param screenY
	 *            screen height
	 */
	public Background(int screenX, int screenY) {
		this.screenX = screenX;
		this.screenY = screenY;
		// front layer
		frontRect = new ArrayList<Point>();
		// middle layer
		middleRect = new ArrayList<Point>();
		// back layer
		backRect = new ArrayList<Point>();
		frontPos = new Point.Double();
		midPos = new Point.Double();
		backPos = new Point.Double();
		ran = new Random();
		getImages();

		// Determine the maximum size of the rectangles
		// Generate three layers of rectangles
		for (int i = 0; i < numFrontLevel; i++) {
			frontRect.add(new Point(ran.nextInt(2*screenX), ran
					.nextInt(2*screenY)));
		}
		for(int i = 0;i < numMidLevel; i++){
			middleRect.add(new Point(ran.nextInt(2*screenX), ran
					.nextInt(2*screenY)));
		}
		for(int i = 0; i < numBackLevel;i++){
			backRect.add(new Point(ran.nextInt(2*screenX), ran
					.nextInt(2*screenY)));
		}
	}

	public void getImages(){
		URL image1URL = Actor.class.getResource(img1);
		URL image2URL = Actor.class.getResource(img2);
		URL image3URL = Actor.class.getResource(img3);
		try {
			image1 = ImageIO.read(image1URL);
			image2 = ImageIO.read(image2URL);
			image3 = ImageIO.read(image3URL);
			int maxWidth = Math.max(image1.getWidth(), Math.max(image2.getWidth(), image3.getWidth()));
			int maxHeight = Math.max(image1.getHeight(), Math.max(image2.getHeight(), image3.getHeight()));
			maxRectSize = Math.max(maxWidth, maxHeight);
		} catch (IOException e) {
			System.out.println("No such Image!");
		}
	}
	/**
	 * Moves the background and implements the parallax effect.
	 * 
	 * @param off
	 *            the camera's offset
	 */
	public void update(Point.Double off) {
		// Offset the layers different amounts
		frontPos.setLocation(damper * off.x, damper * off.y);
		midPos.setLocation(off.x * Math.pow(damper, 2),
				off.y * Math.pow(damper, 2));
		backPos.setLocation(off.x * Math.pow(damper, 3),
				off.y * Math.pow(damper, 3));
		// Wrap the rectangles around the screen
		for (int i = 0; i < numFrontLevel; i++) {
			Point front = frontRect.get(i);
			// Props to Blake Burkhart, who helped me to reduce this from if
			// statements to a concise mathematical modulus statement.
			// The shifts are all necessary due to the fact that rectangles have
			// width.
			front.x = (front.x + (int) frontPos.x + screenX + 2 * maxRectSize)
					% (screenX + maxRectSize) - maxRectSize;
			front.y = (front.y + (int) frontPos.y + screenY + 2 * maxRectSize)
					% (screenY + maxRectSize) - maxRectSize;
		}
		for(int i=0;i<numMidLevel;i++){
			
			// Wrap middle
			Point middle = middleRect.get(i);
			middle.x = (middle.x + (int) midPos.x + screenX + 2 * maxRectSize)
					% (screenX + maxRectSize) - maxRectSize;
			middle.y = (middle.y + (int) midPos.y + screenY + 2 * maxRectSize)
					% (screenY + maxRectSize) - maxRectSize;
		}
			// Wrap back
		for(int i = 0; i<numBackLevel;i++){
			Point back = backRect.get(i);
			back.x = (back.x + (int) backPos.x + screenX + 2 * maxRectSize)
					% (screenX + maxRectSize) - maxRectSize;
			back.y = (back.y + (int) backPos.y + screenY + 2 * maxRectSize)
					% (screenY + maxRectSize) - maxRectSize;
		}
		// CHange to do ONLY the modulus stuff
	}

	/**
	 * Draw the background
	 * 
	 * @param g
	 *           Graphics2D object
	 */
	public void draw(Graphics2D g) {
		g.setColor(new Color(.05f,.1f,.2f));
		g.fillRect(0, 0, screenX, screenY);
		// g.setColor(Color.gray.darker());
		//g.setColor(new Color(.4f, .4f, .4f));
		for (int i = 0; i < numBackLevel; i++) {
			g.drawImage(image3, backRect.get(i).x, backRect.get(i).y, null);
		}
		// g.setColor(Color.gray.darker().darker());
		//g.setColor(new Color(.2f, .2f, .2f, .8f));
		for (int i = 0; i < numMidLevel; i++) {
			g.drawImage(image2, middleRect.get(i).x, middleRect.get(i).y, null);
		}
		// g.setColor(Color.gray.darker().darker().darker());
		//g.setColor(new Color(.12f, .12f, .12f, .8f));
		for (int i = 0; i < numFrontLevel; i++) {
			g.drawImage(image1, frontRect.get(i).x, frontRect.get(i).y, null);
		}
	}
}
