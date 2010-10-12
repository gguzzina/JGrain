/**
 * 
 */
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
/**
 * @author Giulio Guzzinati
 *
 */
public class Counter extends ImageEffect {
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
				Point[] points = scanAround(thisp, img);
				while (points.length > 0) {
//					System.out.println(points[0].getX() + "," + points[0].getY());
					points = scanAround(points, img);
				}
				
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
		return "Conta oggetti";
	}
	
	Point[] scanAround(Point p, BufferedImage img){
		int x = (int) p.getX();
		int y = (int) p.getY();
		int col;
		Point[] nxtpoints = new Point[(int) Math.pow((3*srcw+3),2)];
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
		Point[] results = new Point[numnxtp];
		System.arraycopy(nxtpoints, 0, results, 0, numnxtp);
		return results;
	}
	
	Point[] scanAround(Point[] pts, BufferedImage img){
		int numnxtp = 0;
		Point[] nxtpoints = new Point[(int) Math.pow((3*srcw+3),4)];
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