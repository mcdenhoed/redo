package game;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
/**
 * Extends button. Signals that the user has completed the level
 * @author Mark
 *
 */
public class Exit extends Button {
	/**
	 * Constructor
	 * @param x x position
	 * @param y y position
	 * @param width width of the exit
	 * @param height height of the exit
	 */
	String imageName = "/assets/images/exit.png";
	BufferedImage image;
	private ConvolveOp blurOp; // image blurring
	public Exit(int x, int y, int width, int height) {
		super(x, y, width, height);
		setImage(imageName);
		float ninth = 1.0f / 9.0f;
		float[] blurKernel = {ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth };
		blurOp = new ConvolveOp(new Kernel(3, 3, blurKernel),
				ConvolveOp.EDGE_NO_OP, null);
	}

	public void drawBlurredImage(Graphics2D g2d, BufferedImage im, int x, int y) {
		// blurring with a fixed convolution kernel
		if (im == null) {
			System.out.println("getBlurredImage: input image is null");
			return;
		}
		g2d.drawImage(im, blurOp, x, y); // use the predefined ConvolveOp
	} // end of drawBlurredImage()
	
	public void setImage(String name){
		URL imageURL = Exit.class.getResource(name);
		System.out.println(imageURL.toString());
		try {
			image = ImageIO.read(imageURL);

		} catch (IOException e) {
			System.out.println("No such Image!");
		}
	}

	@Override
	public synchronized void draw(Graphics2D g) {
		//Color color = new Color(1f,1f,1f,.75f);
		//g.setColor(color);
		//g.fill(box);
		drawBlurredImage(g, image, box.x, box.y);
	}
	


}
