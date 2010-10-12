/**
 * 
 */
package effects;

import java.awt.event.ActionListener;
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
public class Fourier extends ImageEffect {

	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	
	@Override
	public RenderedOp applyEffectJAI(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(op);
		return JAI.create("dft", pb);
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

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar(ActionListener engine) {
		JLabel lab = new JLabel("Discrete Fourier Transform!!");
		sidebar = new JPanel();
		sidebar.add(lab);
		return sidebar;
	}

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getName()
	 */
	@Override
	public String getName() {
		return "FFT";
	}

}
