/**
 * 
 */
package effects;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

/**
 * @author Giulio Guzzinati
 *
 */
public class MonochromeJAI extends ImageEffect {
	protected JSlider threshold;

	/**
	 * 
	 */

	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	public RenderedOp applyEffectJAI(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(op);
		pb.add((double)threshold.getValue());
		return JAI.create("binarize", pb);
	}
	
	@Override
	public BufferedImage applyEffect(BufferedImage img) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(img);
		RenderedOp op = JAI.create("addconst", pb);
		op = applyEffectJAI(op);
		img = op.getAsBufferedImage();
		return img;
	}

	@Override
	public JPanel getSidebar(ActionListener engine) {
		JPanel sidebar = new JPanel();
		
		threshold = new JSlider(0, 255);
		threshold.setBorder(new TitledBorder("Soglia"));
		threshold.setPaintTicks(true);
		threshold.setMajorTickSpacing(32);
		sidebar.add(threshold);
		
		return sidebar;
	}

	@Override
	public String getName() {
		return "Monocromatizza JAI";
	}
	
}
