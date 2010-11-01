
package effects;

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
		
	public abstract RenderedOp getRenderedOp(RenderedOp op);
	
	public abstract BufferedImage getBufferedImage(BufferedImage img);
	
	public abstract JPanel getSidebar();
	
	public abstract String getName();
	
	public String getArgumentError(){
		return "Errore, argomento non valido";
	}
}
