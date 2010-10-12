package effects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class CounterLegacy extends ImageEffect {

	int[][] bklist;
	int numpart, srcw, w, h;
	Color[] clrs = {Color.BLUE,Color.RED,Color.CYAN,Color.GREEN,Color.LIGHT_GRAY,
			Color.MAGENTA,Color.ORANGE,Color.PINK,Color.YELLOW,};
	JSlider dst;
	
	public RenderedOp applyEffectJAI(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		BufferedImage img = op.getAsBufferedImage();
		pb.addSource(applyEffect(img));
		return JAI.create("addconst", pb);
	}
	
	
	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage applyEffect(BufferedImage img) {
		numpart = 0;
		w = img.getWidth();
		h = img.getHeight();
		bklist = new int[w][h];
		srcw = dst.getValue();
		for (int x = 0; x < w; x++){ for (int y = 0; y < h; y++) {
			bklist[x][y] = 0;}}
		for (int x = srcw; x < w-srcw; x++) { for (int y = srcw; y < h-srcw; y++) {
			int col = img.getRGB(x, y);
//			System.out.println("applycol"+col);
			if (col > -100){
				Point thisp = new Point(x,y);
			if ( bklist[x][y] == 0){
//				System.out.println("x="+x+",y="+y);
				for (Point p : scanAround(thisp, img)) if(p != null) {
//					System.out.println("p="+p.getX()+","+p.getY());
					for (Point pt : scanAround(p, img)) if (pt != null) {
//						System.out.println("pt="+pt.getX()+","+pt.getY());
						for (Point pnt : scanAround(pt, img)) if (pnt != null){
//							System.out.println("pnt="+pnt.getX()+","+pnt.getY());
							for (Point point : scanAround(pnt, img)) if( point != null ) {
//								System.out.println("point="+point.getX()+","+point.getY());
								for (Point pts : scanAround(point, img)) if (pts != null) {
									for (Point punto : scanAround(pts, img)) if (punto != null) {
										scanAround(punto, img);
									}}}}}}
				numpart = numpart + 1;
			}}}
		}
		
		Font fnt = new Font("Bookman Old Style",Font.BOLD,14);
		Graphics2D g2d = img.createGraphics();
		g2d.setFont(fnt);
        g2d.setColor(Color.RED);
        g2d.setBackground(Color.WHITE);
        g2d.drawString(new String(numpart+" "), 10, 30);
		
		return img;
	}

	/* (non-Javadoc)
	 * @see effects.ImageEffect#getSidebar(java.awt.event.ActionListener)
	 */
	@Override
	public JPanel getSidebar(ActionListener engine) {
		sidebar = new JPanel();
		
		final JTextField text = new JTextField(2);
			text.setText("5");
			text.setEditable(false);
		
		dst = new JSlider(1, 20);
		dst.setValue(5);
		dst.setBorder(new TitledBorder("Distanza dei vicini"));
		dst.setPaintTicks(true);
		dst.setMajorTickSpacing(1);
		dst.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				text.setText(Integer.toString(dst.getValue())); }});
		sidebar.add(dst);
		sidebar.add(text);
		return sidebar;
	}

	@Override
	public String getName() {
		return "Conta oggetti (old)";
	}
	
	Point[] scanAround(Point p, BufferedImage img){
		int x = (int) p.getX();
		int y = (int) p.getY();
		int col;
		Point[] nxtpoints = new Point[(2*srcw+1)*(2*srcw+1)];
		int numnxtp = 0;
		for (int i = x-srcw; i <= x+srcw ; i++) {for (int j = y-srcw; j <= y+srcw; j++) {
//			System.out.println(i+","+j);
			if (i >= 0 && j >= 0 && i < w && j < h){
				col = img.getRGB(i,j);
//			System.out.println("scancol"+col);
			Point thisp = new Point(i,j);
			if (col > -100) {
				img.setRGB(i, j, clrs[numpart%clrs.length].getRGB());
				if (bklist[i][j] == 0) {
					bklist[i][j] = 1;
					nxtpoints[numnxtp]= thisp;
					numnxtp = numnxtp+1;}
			}}}
		}
		System.arraycopy(nxtpoints, 0, new Point[numnxtp], 0, numnxtp);
		return nxtpoints;
	}
	

		

}	