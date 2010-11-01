/**
 * 
 */
package effects;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * @author skarn
 *
 */
public class SobelJAI extends ImageEffect {
	/**
	 * 
	 */
	public SobelJAI() {
	}

	public RenderedOp getRenderedOp(RenderedOp op) {
		KernelJAI sobelVertKernel = KernelJAI.GRADIENT_MASK_SOBEL_VERTICAL;
		KernelJAI sobelHorizKernel = KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL;
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(op);
		pb.add(sobelHorizKernel);
		pb.add(sobelVertKernel);
		return JAI.create("gradientmagnitude", pb);
		}
	
	@Override
	public BufferedImage getBufferedImage(BufferedImage img){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(img);
		RenderedOp op = JAI.create("addconst", pb);
		return this.getRenderedOp(op).getAsBufferedImage();
	}
	
	@Override
	public JPanel getSidebar(){
		sidebar = new JPanel();
		JLabel text = new JLabel(""); 
		
		sidebar.add(text);
		
		return sidebar;
	}

	@Override
	public String getName() {
		return "Rilevamento bordi";
	}
}
