/**
 * 
 */
package effects;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.*;

/**
 * @author Giulio Guzzinati
 *
 */
public class GrayScale extends ImageEffect {



	
	public RenderedOp applyEffectJAI(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(applyEffect(op.getAsBufferedImage()));
		return JAI.create("absolute", pb);
	}
	
	@Override
	public BufferedImage applyEffect(BufferedImage img) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(),  
			    BufferedImage.TYPE_BYTE_GRAY);  
			Graphics g = image.getGraphics();  
			g.drawImage(img, 0, 0, null);  
			g.dispose(); 
		return image;
	}

	@Override
	public JPanel getSidebar(ActionListener engine) {
		sidebar = new JPanel();
		JLabel label = new JLabel("Passa in scala di grigi");
		sidebar.add(label);
		return sidebar;
	}

	@Override
	public String getName() {
		return "Scala di grigi";
	}
}
