/**
 * 
 */
package effects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**{@link ImageEffect} che binarizza un'immagine a colore sulla
 * base di 3 soglie rispettivamente per i canali RGB impostate
 * nella GUI attraverso 3 {@link JSlider} visualizzati nella
 * {@link Sidebar}
 * 
 * @author Giulio Guzzinati
 */
public class BinarizeColor extends ImageEffect {
	JSlider rthresh, gthresh, bthresh;
	String name = "Monocromatizza";
	
	
	/*
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
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
		
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(),  
			    BufferedImage.TYPE_BYTE_BINARY);  
			Graphics g = image.getGraphics();  
			g.drawImage(img, 0, 0, null);  
			g.dispose();
		img = image;
		
		return img;
	}

	/* 
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar() {
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new GridLayout(3,1));
		
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
	
	
	@Override
	public String getName() {
		return "Binarizza, colori";
	}
	
	
	@Override
	protected RenderedOp getRenderedOp(RenderedOp op) {
		ParameterBlock pb = new ParameterBlock();
		BufferedImage img = op.getAsBufferedImage();
		pb.addSource(getBufferedImage(img));
		return JAI.create("addconst", pb);
	}

}