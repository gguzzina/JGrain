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
 * L'utilizzo di immagini già in scala di grigi,
 * per quanto superfluo, non genera errori.
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
	
	/**Restituisce un nome per l'effetto
	 * da mostrare nell'interfaccia utente e nei log.
	 * In questo caso "Scala di grigi"
	 * 
	 * @return "Scala di grigi"
	 */
	@Override
	public String getName() {
		return "Scala di grigi";
	}
}
