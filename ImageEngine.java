import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import effects.*;

/**
 * 
 */

/**
 * @author Giulio Guzzinati
 *
 */
public class ImageEngine implements ActionListener{
	protected ImageEffect effect;
	protected int neft = 0;
	protected ImageEffect[] eftlist = new ImageEffect[10];
	protected Sidebar sidebar;
//	protected BufferedImage immagine = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB );
	protected BufferedImage immagine = null;
	protected File file;
	protected ImageBox box;
	protected RenderedOp op;
	protected RenderedOp[] oplist = new RenderedOp[10];
	
	
	public ImageEngine(ImageBox imgbx){
		try { file = new File(this.getClass().getResource("files/Dot_Blot.jpg").toURI());
		} catch (URISyntaxException e1) { e1.printStackTrace(); }
//		file = new File("/home/skarn/Documenti/Universita/V Anno/Java/Eclipse/jgrain/src/Dot_Blot.jpg");
//		file = new File("/home/skarn/Documenti/Universita/V Anno/Java/Eclipse/jgrain/src/garbage_collector.jpg");
		{try {	immagine = ImageIO.read(file);	} catch (IOException e) {System.out.println("errore!!");}}
		Graphics2D g = immagine.createGraphics();
		g.setColor(new Color ( 0, 0, 0, 0 ));
		g.fillRect(0, 0, 300, 300);
		g.dispose();
		box = imgbx;
		box.set(immagine);
	}
	
	public void openImage(File file){
		this.file = file;
		try {
			immagine = ImageIO.read(file);
		} catch (IOException e) {}
		box.set(immagine);
	}
	
	public void reload(){
		this.openImage(file);
	}
	
	public void setEffect(ImageEffect eft){
		effect = eft;
		sidebar.setEffect(effect);
	}
	
	public void addEffect(ImageEffect eft){
		eftlist[neft] = eft;
		sidebar.addEffect(eft);
		neft = neft + 1;
	}
	
	public void removeEffect(int del){
		neft = neft-1;
	    System.arraycopy(eftlist,del+1,eftlist,del,eftlist.length-1-del);
	    sidebar.removeEffect(del);
	}
	
	public int getNum(){
		return neft;
	}
	
	public void sidebar(Sidebar sidebar){
		this.sidebar = sidebar;
	}

	public void chainBuild(){
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(immagine);
		double[] cost = new double[1];
		cost[0]=0;
		pb.add(cost);
		oplist[0] = JAI.create("addconst", pb);
		for (int j = 0; j < neft; j++) {
			oplist[j+1] = eftlist[j].applyEffectJAI(oplist[j]);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		reload();
		for (int i = 0; i < neft; i++) {
			immagine = eftlist[i].applyEffect(immagine);
			box.set(immagine);
			chainBuild();
		}
	}
	
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		immagine = effect.applyEffect(immagine);
//		box.set(immagine);
//	}
	
	
	
}