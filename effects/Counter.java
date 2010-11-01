/**
 * 
 */
package effects;

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * @author Giulio Guzzinati
 *
 */
public class Counter extends ImageEffect {
	int[][] bklist;
	int numpart, srcw, w, h;
	Color[] clrs = {Color.BLUE,Color.RED,Color.CYAN,Color.GREEN,Color.LIGHT_GRAY,
			Color.MAGENTA,Color.ORANGE,Color.PINK,Color.YELLOW,Color.DARK_GRAY,new Color(115, 0, 85)};
	JSlider dst;
	
	public RenderedOp getRenderedOp(RenderedOp op){
		ParameterBlock pb = new ParameterBlock();
		BufferedImage img = op.getAsBufferedImage();
		pb.addSource(getBufferedImage(img));
		return JAI.create("addconst", pb);
	}
	
	
	/* (non-Javadoc)
	 * @see effects.ImageEffect#applyEffect(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage getBufferedImage(BufferedImage img) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight(),  
			    BufferedImage.TYPE_INT_RGB);  
			Graphics g = image.getGraphics();  
			g.drawImage(img, 0, 0, null);  
			g.dispose();
		img = image;
		
		
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
//				bklist[x][y] = 1;
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
        g2d.drawString(new String(numpart+""), 10, 30);
		
		return img;
	}

	/* (non-Javadoc)
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
		sidebar.add(dst);
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
			if (col > -100 && bklist[i][j] == 0) {
					img.setRGB(i, j, clrs[numpart%clrs.length].getRGB());
					bklist[i][j] = 1;
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