
package effects;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
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
	protected JPanel sidebar;
	protected String name = new String("orchidea");
	
	public ImageEffect(){}
	
	public abstract BufferedImage applyEffect(BufferedImage img);
	
	public RenderedOp applyEffectJAI(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(op);
		return JAI.create("addconst", pb);
	}
	
	public abstract JPanel getSidebar(ActionListener engine);
	
	public abstract String getName();

}
