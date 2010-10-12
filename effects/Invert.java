/**
 * 
 */
package effects;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

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
	public RenderedOp applyEffectJAI(RenderedOp op) {
		return JAI.create("invert", op);
	}
	
	
	
	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage applyEffect(BufferedImage img) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar(ActionListener engine) {
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
