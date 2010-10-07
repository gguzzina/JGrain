/**
 * 
 */
package effects;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.JButton;
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

	public RenderedOp sobelGradientMagnitude(PlanarImage image) {
		KernelJAI sobelVertKernel = KernelJAI.GRADIENT_MASK_SOBEL_VERTICAL;
		KernelJAI sobelHorizKernel = KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL;
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(image);
		pb.add(sobelHorizKernel);
		pb.add(sobelVertKernel);
		return JAI.create("gradientmagnitude", pb);
		}
	
	public BufferedImage applyEffect(BufferedImage img){
		PlanarImage pimg = PlanarImage.wrapRenderedImage(img);
		RenderedOp op = sobelGradientMagnitude(pimg);
		img = op.getAsBufferedImage();
		return img;
	}
	
	public JPanel getSidebar(ActionListener al){
		sidebar = new JPanel();
		
		JButton btn = new JButton("Applica");
		btn.addActionListener(al);
		sidebar.add(btn);
		
		return sidebar;
	}
}
