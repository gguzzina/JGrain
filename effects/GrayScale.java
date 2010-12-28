package effects;

import java.awt.Graphics;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.*;

/**Converte l'immagine a colori in un'immagine in scala di grigi,
 * compreso un cambio dello spazio di colori.
 * Questo può essere utile nel caso l'immagine sia già costituita da toni di grigio,
 * ma utilizzi uno spazio di colori RGB, per cambiare spazio di colori.
 * 
 * @author Giulio Guzzinati
 *
 */
public class GrayScale extends ImageEffect {



	
	protected RenderedOp getRenderedOp(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(getBufferedImage(op.getAsBufferedImage()));
		return JAI.create("absolute", pb);
	}
	
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(),  
			    BufferedImage.TYPE_BYTE_GRAY);  
			Graphics g = image.getGraphics();  
			g.drawImage(img, 0, 0, null);  
			g.dispose();
		return image;
	}

	@Override
	public JPanel getSidebar() {
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
