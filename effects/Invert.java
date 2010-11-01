/**
 * 
 */
package effects;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author Giulio Guzzinati
 *
 */
public class Invert extends ImageEffect {

	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public RenderedOp getRenderedOp(RenderedOp op) {
		return JAI.create("invert", op);
	}
	
	
	
	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(img);
		return this.getRenderedOp(JAI.create("addconst", pb)).getAsBufferedImage();
	}

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar() {
		sidebar = new JPanel();
		JLabel label = new JLabel("Inverti l'immagine!");
		sidebar.add(label);
		return sidebar;
	}

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getName()
	 */
	@Override
	public String getName() {
		return "Inverti";
	}

}
