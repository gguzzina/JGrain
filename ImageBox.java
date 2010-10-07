import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.*;
import javax.swing.*;

import com.sun.media.jai.widget.DisplayJAI;

/**
 * 
 */

/**
 * @author Giulio Guzzinati
 *
 */
@SuppressWarnings("serial")
public class ImageBox extends JPanel {
	protected BufferedImage immagine, zimmagine;
	protected DisplayJAI display;
	protected JScrollPane pane;
	protected float zoom = 1f;
	protected Dimension dim;
	protected JPanel toolbar;
	protected JTextField zm;
	
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
	 * 
	 */
	
	public ImageBox(){
		setLayout(new BorderLayout());
		display = new DisplayJAI();
		toolbar = toolbar();
		pane = new JScrollPane(display);
		add(toolbar, BorderLayout.NORTH);
		add(pane);
		}
	
	public void set(BufferedImage img){
		immagine = img;
		zoom(0);
	}
	
	public void zoom(float z){
		zoom = zoom+z;
		ParameterBlock params = new ParameterBlock();
		params.addSource(immagine);
		params.add(zoom);         // zoom X
		params.add(zoom);			// zoom Y
		params.add(0.0f);         // traslazione X
		params.add(0.0f);         // traslazione Y
		params.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));        
		RenderedOp img = JAI.create("scale", params);
		zimmagine = img.getAsBufferedImage();
		zm.setText(Integer.toString((int)(zoom*100)));
		zm.setEditable(false);
		toolbar.repaint();
		display.set(zimmagine);
	}
	
	public void zoomReset(){
		zoom=1;
		zoom(0);
	}
}
