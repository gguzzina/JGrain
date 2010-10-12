
package effects;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.media.jai.*;
import javax.swing.*;

/**
 * @author skarn
 *
 */
public abstract class ImageEffect {
	
	/**
	 * Classe astratta per gli effetti.
	 * Andrà implementata negli effetti specifici.
	 */
	
	protected JPanel sidebar;
	
	public ImageEffect(){}
		
	public abstract RenderedOp applyEffectJAI(RenderedOp op);
	
	public abstract BufferedImage applyEffect(BufferedImage img);
	
	public abstract JPanel getSidebar(ActionListener engine);
	
	public abstract String getName();

}
