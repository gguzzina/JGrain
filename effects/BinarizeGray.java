/**
 * 
 */
package effects;

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
public class BinarizeGray extends ImageEffect {
	protected JSlider threshold;

	/**
	 * 
	 */

	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	public RenderedOp getRenderedOp(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(op);
		pb.add((double)threshold.getValue());
		return JAI.create("binarize", pb);
	}
	
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(img);
		RenderedOp op = JAI.create("addconst", pb);
		op = getRenderedOp(op);
		img = op.getAsBufferedImage();
		return img;
	}

	@Override
	public JPanel getSidebar() {
		sidebar = new JPanel();
		
		threshold = new JSlider(0, 255);
		threshold.setValue(160);
		threshold.setBorder(new TitledBorder("Soglia"));
		threshold.setPaintTicks(true);
		threshold.setMajorTickSpacing(32);
		sidebar.add(threshold);
		
		return sidebar;
	}

	@Override
	public String getName() {
		return "Binarizza, scala di grigi";
	}
	
	@Override
	public String getArgumentError() {
		return "Questo effetto lavora su immagini in scala di grigi,\n converti l'immagine o utilizza l'effetto 'Binarizza, Colore'"; 	
	}
	
}
