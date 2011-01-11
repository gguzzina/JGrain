package effects;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**{@link ImageEffect} che conta le particelle distinguibili in un'immagine binaria.
 * 
 * Il conteggio avviene scorrendo i pixel dell'immagine per colonne,
 * quando trova un pixel bianco, controlla i pixel adiacenti,
 * quindi quelli adiacenti a questi. Quando non è possibile trovare nuovi pixel
 * bianchi si considera di avere mappato tutta la particella e si incrementa il
 * contatore delle particelle, poi si prosegue nella scansione dell'immagine.
 * 
 * Il controllo di quali pixel sono stati contati oppure no viene effettuato
 * con una blacklist costituita da un array di booleani delle stesse dimensioni
 * dell'immagine, dove ogni volta che un pixel bianco viene rilevato, l'algoritmo
 * imposta il corrispondente valore a true.
 * 
 * 
 * 
 * @author Giulio Guzzinati
 */
public class Counter extends ImageEffect {
	protected boolean[][] bklist;
	protected int numpart, srcw, w, h, size;
	protected Color[] clrsTrue = {Color.BLUE,Color.RED,Color.CYAN,Color.GREEN,
								Color.LIGHT_GRAY,Color.MAGENTA,Color.ORANGE,
								Color.PINK,Color.YELLOW,Color.DARK_GRAY,
								new Color(115, 0, 85)};
	protected Color[] clrsFalseFalse = {Color.BLACK};
	protected Color[] clrsFalseTrue = {Color.WHITE};
	protected Color[] clrs;
	protected Color backGdClr;
	protected JSlider dst, minsize;
	protected boolean chbColor = false, chbDark = false;
	protected JCheckBox chb1, chb2;
	
	protected RenderedOp getRenderedOp(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		BufferedImage img = op.getAsBufferedImage();
		pb.addSource(getBufferedImage(img));
		return JAI.create("addconst", pb);
	}
	
	
	/* 
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
		
		if (img.getType() == BufferedImage.TYPE_BYTE_BINARY){
		
		{BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(),  
			    BufferedImage.TYPE_INT_RGB);  
			Graphics g = image.getGraphics();  
			g.drawImage(img, 0, 0, null);  
			g.dispose();
		img = image;}
		
		if (chbColor == false && chbDark == true) {
			clrs = clrsFalseTrue;
		}else if (chbColor == false && chbDark == false) {
			clrs = clrsFalseFalse;
		}else if (chbColor == true){
			clrs = clrsTrue;
		}
		
		if (chbDark == true) {
			backGdClr = Color.BLACK;
		} else {backGdClr =  Color.WHITE;}
		
		numpart = 0;
		w = img.getWidth();
		h = img.getHeight();
		bklist = new boolean[w][h];
		srcw = dst.getValue();
		int minsz = minsize.getValue();
		System.out.println("dimnensione minima "+minsz);
		
		for (int x = 0; x < w; x++){ for (int y = 0; y < h; y++) {
			bklist[x][y] = false;}}
		for (int x = srcw; x < w-srcw; x++) { for (int y = srcw; y < h-srcw; y++) {
			int col = img.getRGB(x, y);
			if (col != backGdClr.getRGB()){
				Point thisp = new Point(x,y);
			if ( bklist[x][y] == false){
				size = 0; 
				//System.out.println("inizializzato"+size);
				Point[] points = scanAround(thisp, img);
				while (points.length > 0) {
					size = size + points.length;
					//System.out.println("aumentato"+size);
					points = scanAround(points, img);
				}
				if (size >= minsz){
				numpart = numpart + 1;
				System.out.println("incrementato" + size);
				}
			}
			size = 0;
			//System.out.println("resettato"+size);
			}}
		}
		
		Font fnt = new Font("Bookman Old Style",Font.BOLD,14);
		{Graphics2D g = img.createGraphics();
		g.setFont(fnt);
        g.setColor(Color.RED);
        g.drawString(new String(numpart+""), 10, 30);
		g.dispose();}
        
		return img;
		} else { throw new IllegalArgumentException(); }
	}

	/*
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar() {
		sidebar = new JPanel();		
		dst = new JSlider(1, 17);
		dst.setValue(1);
		dst.setBorder(new TitledBorder("Distanza dei vicini"));
		dst.setPaintTicks(true);
		dst.setPaintLabels(true);
		dst.setMajorTickSpacing(4);
		dst.setMinorTickSpacing(2);
		/*sidebar.add(dst);*/
		
		minsize = new JSlider(1, 41);
		minsize.setValue(1);
		minsize.setBorder(new TitledBorder("Dimensione minima"));
		minsize.setPaintTicks(true);
		minsize.setPaintLabels(true);
		minsize.setMajorTickSpacing(8);
		minsize.setMinorTickSpacing(4);
		
		chb1 = new JCheckBox("Colora le particelle", chbColor);
		chb1.addItemListener(new ItemListener() {@Override
			public void itemStateChanged(ItemEvent arg0) {
			chbColor = !chbColor;}});
		
		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chbDark = Boolean.getBoolean(e.getActionCommand());
			}
		}; 
		
		
		ButtonGroup bg = new ButtonGroup();
		JPanel radio = new JPanel();
		radio.setBorder(new TitledBorder("Colore sfondo"));
		
		JRadioButton rb1 = new JRadioButton("Sfondo bianco");
		rb1.setActionCommand("false");
		rb1.addActionListener(al);
		rb1.setSelected(!chbDark);
		bg.add(rb1);
		radio.add(rb1);
		
		JRadioButton rb2 = new JRadioButton("Sfondo nero");
		rb2.setActionCommand("true");
		rb2.addActionListener(al);
		bg.add(rb2);
		rb2.setSelected(chbDark);
		radio.add(rb2);
		
		
		
		//chb2.addItemListener(new ItemListener() {@Override
		//	public void itemStateChanged(ItemEvent arg0) {
		//	chbDark = !chbDark;}});
		
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS)); 
		box.add(chb1);
		box.add(radio);
		box.add(minsize);
		sidebar.add(box);
		
		
		return sidebar;
	}

	@Override
	public String getName() {
		return "Conta oggetti";
	}
	
	@Override
	public String getArgumentError(){
		return "L'immagine non è binaria";
		}
	
	@Override
	public String getLogMessage(){
		return ("Contate " + numpart + " particelle");
	}
	
	
	Point[] scanAround(Point p, BufferedImage img){
		int x = (int) p.getX();
		int y = (int) p.getY();
		int col;
		Point[] nxtpoints = new Point[(int) Math.pow((3*srcw+3),2)];
		int numnxtp = 0;
		for (int i = x-srcw; i <= x+srcw; i++){for (int j = y-srcw; j <= y+srcw; j++){
//			System.out.println(i+","+j);
			if (i >= 0 && j >= 0 && i < w && j < h){
				col = img.getRGB(i,j);
//			System.out.println("scancol"+col);
			Point thisp = new Point(i,j);
			if (col != backGdClr.getRGB() && bklist[i][j] == false) {
					img.setRGB(i, j, clrs[numpart%clrs.length].getRGB());
					bklist[i][j] = true;
					nxtpoints[numnxtp]= thisp;
					numnxtp = numnxtp+1;
			}}}
		}
		Point[] results = new Point[numnxtp];
		System.arraycopy(nxtpoints, 0, results, 0, numnxtp);
		return results;
	}
	
	Point[] scanAround(Point[] pts, BufferedImage img){
		int numnxtp = 0;
		Point[] nxtpoints = new Point[(int) (pts.length*Math.pow((3*srcw+3),2))];
		for (Point pnt : pts) {
			Point[] tmppoints = scanAround(pnt, img);
//			System.out.println("numnxtp ="+numnxtp+" length " + tmppoints.length);
			System.arraycopy(tmppoints, 0, nxtpoints, numnxtp, tmppoints.length);
			numnxtp = numnxtp + tmppoints.length;
		}
		Point[] results = new Point[numnxtp];
		System.arraycopy(nxtpoints, 0, results, 0, numnxtp);
		return results;
	}
}