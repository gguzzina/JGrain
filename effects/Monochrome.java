/**
 * 
 */
package effects;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author Giulio Guzzinati
 *
 */
public class Monochrome extends ImageEffect {
	JSlider rthresh, gthresh, bthresh;
	
	
	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage applyEffect(BufferedImage img) {
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				Color pxl = new Color(img.getRGB(i,j));
				if (rthresh.getValue() > pxl.getRed() && gthresh.getValue() > pxl.getGreen() && bthresh.getValue() > pxl.getBlue()) {
					img.setRGB(i, j, Color.BLACK.getRGB());
				} else {
					img.setRGB(i, j, Color.WHITE.getRGB());
				}
			}
		}
		
		
		return img;
	}

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar(ActionListener engine) {
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new GridLayout(6,1));
		
		rthresh = new JSlider(0, 255);
		rthresh.setBorder(new TitledBorder("Soglia per il rosso"));
		rthresh.setPaintTicks(true);
		rthresh.setMajorTickSpacing(32);
		sidebar.add(rthresh);
		
		gthresh = new JSlider(0, 255);
		gthresh.setBorder(new TitledBorder("Soglia per il verde"));
		gthresh.setPaintTicks(true);
		gthresh.setMajorTickSpacing(32);
		sidebar.add(gthresh);
		
		bthresh = new JSlider(0, 255);
		sidebar.add(bthresh);
		bthresh.setBorder(new TitledBorder("Soglia per il blu"));
		bthresh.setPaintTicks(true);
		bthresh.setMajorTickSpacing(32);
		

		return sidebar;
	}

}
