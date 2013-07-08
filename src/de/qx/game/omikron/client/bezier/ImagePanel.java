package de.qx.game.omikron.client.bezier;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JPanel;

@SuppressWarnings("serial") 
public class ImagePanel extends JPanel{
	private BufferedImage image;

	/**
	 * C'tor.
	 */
	public ImagePanel(Dimension dimension) {
		super(true);
		
		image = new BufferedImage((int) dimension.getWidth(),
				(int) dimension.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		setPreferredSize(new Dimension((int)dimension.getWidth(), (int)dimension.getHeight()));
		setSize(dimension);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	public int[] getImageData() {
		return ((DataBufferInt) image.getRaster().getDataBuffer())
				.getData();
	}
}