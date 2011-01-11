/**
 * 
 */
package effects;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**Inverte l'immagine. È indipendente dal tipo di immagine
 * (binaria, grigio, colore)
 * 
 * @author Giulio Guzzinati
 *
 */
public class Invert extends ImageEffect {

	/**Inverte l'immagine utilizzando l'effetto di inversione
	 * incluso nelle librerie {@link JAI}
	 * 
	 * @param op Una {@link RenderedOp} contenente l'immagine
	 * @return Una {@link RenderedOp} per l'immagine invertita
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	protected RenderedOp getRenderedOp(RenderedOp op) {
		return JAI.create("invert", op);
	}
	
	
	
	/**Inverte l'immagine utilizzando. Chiama in realtà il metodo getRenderedOp
	 * che sfrutta l'effetto di inversione incluso nelle librerie {@link JAI}
	 * 
	 * @param img L'immagine da invertire in forma di {@link BufferedImage}
	 * @return una {@link BufferedImage} con dell'immagine invertita
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(img);
		return this.getRenderedOp(JAI.create("addconst", pb)).getAsBufferedImage();
	}

	/**Restituisce il {@link JPanel} per l'effetto, che è
	 * privo di controlli interattivi  
	 * 
	 *  
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar() {
		sidebar = new JPanel();
		JLabel label = new JLabel("Inverti l'immagine!");
		sidebar.add(label);
		return sidebar;
	}

	/* 
	 * @see effects.ImageEffect#getName()
	 */
	@Override
	public String getName() {
		return "Inverti";
	}

}
