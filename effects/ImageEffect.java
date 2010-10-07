
package effects;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * @author skarn
 *
 */
public abstract class ImageEffect {
	
	/**
	 * Classe astratta per gli effetti.
	 * Andrà implementata negli effetti specifici.
	 */
	
	protected BufferedImage img;
	JPanel sidebar;
	
	public ImageEffect(){}
	
	public abstract BufferedImage applyEffect(BufferedImage img);
	
	
	public abstract JPanel getSidebar(ActionListener engine);
}
