import java.awt.*;
//import java.awt.event.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.net.URL;

import javax.media.jai.*;
import javax.swing.*;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * Componente che visualizza un'immagine {@link BufferedImage}
 * dotato di features avanzate.
 * 
 * 
 * Un ImageBox è costutito da un {@link JPanel} contenente la toolbar 
 * e l'immagine.
 * Se necessario (l'immagine è più grossa dell'area disponibile)
 * compaiono le barre di scorrimento verticali e orizzontali.
 * Dato che utilizza il widget {@link DisplayJAI} per l'effettiva
 * visualizzazione, questa classe dipende dalle librerie JAI.
 * 
 * @author Giulio Guzzinati
 */

@SuppressWarnings("serial")
public class ImageBox extends JPanel {
	/**
	 * l'immagine che il componente deve visualizzare
	 */
	protected BufferedImage img;
	 /**
	  * una copia di <code>img</code> una volta applicato lo zoom,
	  * ha dimensione diversa dall'originale
	  */
	protected BufferedImage zimg;
	protected DisplayJAI display;
	protected JScrollPane pane;
	protected float zoom = 1f;
	protected Dimension dim;
	protected JPanel toolbar;
	protected JTextField zm = new JTextField(3);
	private ImageEngine engine;
	
	/**
	 * metodo privato che inizializza la toolbar per lo zoom,
	 * con 3 {@link JButton} per zoom in, zoom out e per reimpostare lo zoom a 1,
	 * e un <code>JTextField</code> che mostra il fattore di zoom percentuale
	 * L'operazione di effettivo zoom è effettuata dal metodo <code>zoom</code>
	 * 
	 * @return la toolbar creata
	 */
	private JPanel toolbar(){
		URL uo = JGrain.class.getResource("icons/open.png");
		ImageIcon io = new ImageIcon(uo);
		JButton open = new JButton(io);
		open.setToolTipText("Apri File... [crtl]+[o]");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.openImage();
			}});
		
		URL us = JGrain.class.getResource("icons/save.png");
		ImageIcon is = new ImageIcon(us);
		JButton save = new JButton(is);
		save.setToolTipText("Salva File... [crtl]+[s]");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.saveImage();
			}});
		
		URL ub = JGrain.class.getResource("icons/batch.png");
		ImageIcon ib = new ImageIcon(ub);
		JButton batch = new JButton(ib);
		batch.setToolTipText("Applica a molti file... [ctrl]+[b]");
		batch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.batchProcess();
			}});
		
		URL uin = JGrain.class.getResource("icons/zoom-in.png");
		ImageIcon iin = new ImageIcon(uin);
		JButton zmin = new JButton(iin);
		zmin.setToolTipText("Aumenta lo zoom [ctrl]+[.]");
		zmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(0.1);}});
		
		URL uout = JGrain.class.getResource("icons/zoom-out.png");
		ImageIcon iout = new ImageIcon(uout);
		JButton zmout = new JButton(iout);
		zmout.setToolTipText("Riduci lo zoom [ctrl]+[,]");
		zmout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoom(-0.1);}});
		
		URL u0 = JGrain.class.getResource("icons/zoom-100.png");
		ImageIcon i0 = new ImageIcon(u0);
		JButton zm0 = new JButton(i0);
		zm0.setToolTipText("[ctrl]+[=]");
		zm0.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomReset();}});
		
		
		zm.setEditable(false);
		
		JPanel tb = new JPanel();
		tb.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(getBackground()),
				BorderFactory.createLineBorder(Color.GRAY)));
		tb.setLayout(new BorderLayout());
		
		JPanel left = new JPanel();
		left.setLayout(new BoxLayout(left, BoxLayout.LINE_AXIS));
		left.add(open);
		left.add(save);
		left.add(batch);
		
		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.LINE_AXIS));
		right.add(zmin);
		right.add(zmout);
		right.add(zm);
		right.add(zm0);
		
		tb.add(left, BorderLayout.WEST);
		tb.add(right, BorderLayout.EAST);
		
		
		return tb;
	}
	
	/**
	 * crea l'oggetto, inizializzando tutti i componenti dell'interfaccia,
	 * non inizializza l'immagine <code>img</code>
	 */
	
	public ImageBox(){
		setLayout(new BorderLayout());
		display = new DisplayJAI();
		toolbar = toolbar();
		display.setAutoscrolls(true);
		pane = new JScrollPane(display);
		pane.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				center();
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
			});
		
		add(toolbar, BorderLayout.NORTH);
		//JViewport viewp = pane.getViewport();
		//viewp.setLayout(new BoxLayout(viewp, BoxLayout.Y_AXIS));
		pane.getVerticalScrollBar().setUnitIncrement(10);
		pane.getHorizontalScrollBar().setUnitIncrement(10);
		
		add(pane);
		}
	
	/**
	 * Sostituisce l'immagine fornita a quella precedentemente visualizzata,
	 * la mostra senza alterare il fattore di zoom precedentemente impostato
	 * 
	 * 
	 * @param img l'immagine da visualizzare, sotto forma di {@link BufferedImage}
	 */
	public void set(BufferedImage img){
		this.img = img;
		zoom(0);
	}
	
	
	/**
	 * incrementa il fattore di zoom con cui viene visualizzata l'immagine
	 * della quantità <code>inc</code>.  
	 * 
	 * Lo zoom è da intendersi come fattore e non come percentuale
	 * (ossia il valore neutro del fattore di zoome è 1 e non 100), quindi 
	 * per ottenere un incremento del 10% dello si deve usare zoom(0.1)
	 * 
	 * @param inc
	 */
	public void zoom(double inc){
		zoom = zoom+((float) inc);
		ParameterBlock params = new ParameterBlock();
		params.addSource(img);
		params.add(zoom);         // zoom X
		params.add(zoom);		  // zoom Y
		params.add(0.0f);         // traslazione X
		params.add(0.0f);         // traslazione Y
		params.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));        
		try {
			RenderedOp img = JAI.create("scale", params);
			zimg = img.getAsBufferedImage();
		} catch (Exception e) {
			e.printStackTrace();
		}

		zm.setText(Integer.toString((int)(zoom*100)));
		zm.setEditable(false);
		
		display.set(zimg);
		center();
		//pane.validate();
		//pane.repaint();
	}
	
	/**
	 * Centra l'immagine all'interno del widget {@link DisplayJAI},
	 * individuando la nuova origine con il metodo <code>findOrigin()</code>
	 */
	public void center(){
		int[] origin = findOrigin();
		display.setOrigin(origin[0], origin[1]);
	}
	
	/**calcola il punto da usare come origine per centrare l'immagine
	 * 
	 * 
	 * @return una coppia di interi. L'elemento [0] è la <code>x</code>
	 * 				della nuova origine, l'elemento [1] la <code>y</code>.
	 */
	public int[] findOrigin(){
		Dimension dd = display.getSize();
		Dimension di = new Dimension(zimg.getWidth(),zimg.getHeight());
		int[] origin = new int[2];
		origin[0] = (int) (dd.getWidth()-di.getWidth())/2;
		origin[1] = (int) (dd.getHeight()-di.getHeight())/2;
		return origin;
	}
	
	
	/**
	 * reimposta il fattore di zoom a 1 (100%)
	 */
	public void zoomReset(){
		zoom=1;
		zoom(0);
	}
	
	/**Comunica alla classe qual'è l'{@link ImageEngine} con cui interagire.
	 * Questo è necessario per il funzionamento dei pulsanti della toolbar. 
	 * 
	 * @param engine
	 */
	public void engine(ImageEngine engine){
		this.engine = engine;
	}
	
}
