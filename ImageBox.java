import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.*;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * Componente che visualizza un'immagine {@link BufferedImage} dotato di features avanzate.
 * 
 * 
 * Un ImageBox è costutito da un {@link JPanel} contenente la toolbar per lo zoom dell'immagine e l'immagine stessa.
 * Se necessario (l'immagine è più grossa dell'area disponibile) compaiono le barre di scorrimento verticali e orizzontali.
 * Dato che utilizza il widget {@link DisplayJAI} per l'effettiva visualizzazione, questa classe dipende dalle librerie JAI.
 * 
 * @author Giulio Guzzinati
 * @version 0.3
 */

@SuppressWarnings("serial")
public class ImageBox extends JPanel {
	/**
	 * l'immagine che il componente deve visualizzare
	 */
	protected BufferedImage img;
	 /**
	  * una copia di una volta applicato lo zoom, ha dimensione diversa dall'originale
	  */
	protected BufferedImage zimg;
	protected DisplayJAI display;
	protected JScrollPane pane;
	protected float zoom = 1f;
	protected Dimension dim;
	protected JPanel toolbar;
	protected JTextField zm;
	
	/**
	 * metodo privato che inizializza la toolbar per lo zoom, con 3 {@link JButton} per zoom in, zoom out e per reimpostare lo zoom a 1, e un <code>JTextField</code> che mostra il fattore di zoom percentuale
	 * L'operazione di effettivo zoom è effettuata dal metodo <code>zoom</code>
	 * 
	 * @return la toolbar creata
	 */
	private JPanel toolbar(){
		JPanel tb = new JPanel();
		tb.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton zmin = new JButton("Z+");
		zmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(0.1f);}});
		
		JButton zmout = new JButton("Z-");
		zmout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(-0.1f);}});
		
		JButton zm0 = new JButton("Z=1");
		zm0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomReset();}});
		
		zm = new JTextField(3);
		zm.setEditable(false);
		
		tb.add(zmin);
		tb.add(zmout);
		tb.add(zm);
		tb.add(zm0);
		return tb;
	}
	
	/**
	 * crea l'oggetto, inizializzando tutti i componenti dell'interfaccia, non inizializza l'immagine <code>img</code>
	 */
	
	public ImageBox(){
		setLayout(new BorderLayout());
		display = new DisplayJAI();
		toolbar = toolbar();
		pane = new JScrollPane(display);
		add(toolbar, BorderLayout.NORTH);
		add(pane);
		}
	
	/**
	 * Sostituisce l'immagine fornita a quella precedentemente visualizzata, la mostra senza alterare il fattore di zoom precedentemente impostato
	 * 
	 * 
	 * @param img l'immagine da visualizzare, sotto forma di {@link BufferedImage}
	 */
	public void set(BufferedImage img){
		this.img = img;
		zoom(0);
	}
	
	/**
	 * incrementa il fattore di zoom della quantità <code>inc</code>  
	 * 
	 * lo zoom è da intendersi come fattore e non come percentuale (ossia il valore neutro è 1 e non 100)
	 * 
	 * @param inc
	 */
	public void zoom(float inc){
		zoom = zoom+inc;
		ParameterBlock params = new ParameterBlock();
		params.addSource(img);
		params.add(zoom);         // zoom X
		params.add(zoom);		  // zoom Y
		params.add(0.0f);         // traslazione X
		params.add(0.0f);         // traslazione Y
		params.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));        
		RenderedOp img = JAI.create("scale", params);
		zimg = img.getAsBufferedImage();
		zm.setText(Integer.toString((int)(zoom*100)));
		zm.setEditable(false);
		toolbar.repaint();
		display.set(zimg);
	}
	
	/**
	 * reimposta il fattore di zoom a 1 (100%)
	 */
	
	public void zoomReset(){
		zoom=1;
		zoom(0);
	}
}
