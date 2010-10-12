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
	boolean testing = false;
	protected ImageEffect[] eftlist = new ImageEffect[10];
	protected Sidebar sidebar;
	protected BufferedImage immagine = null;
	protected File file;
	protected ImageBox box;
	protected RenderedOp op;
	protected RenderedOp[] oplist = new RenderedOp[10];
	
	
	public ImageEngine(ImageBox imgbx){
		if (testing == true){try { file = new File(this.getClass().getResource("files/Dot_Blot.jpg").toURI());
		} catch (URISyntaxException e1) { e1.printStackTrace(); }
		{try {	immagine = ImageIO.read(file);	} catch (IOException e) {System.out.println("errore!!");}}}
		if (testing==false) {immagine = new BufferedImage(500, 400, BufferedImage.TYPE_INT_ARGB );}
		Graphics2D g = immagine.createGraphics();
		g.setColor(new Color ( 0, 0, 0, 0 ));
		g.fillRect(0, 0, 500, 400);
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
		chainBuild();
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
	
	public void chooseEffect(int num){
		reload();
		chainBuild();
		immagine = oplist[num + 1].getAsBufferedImage();
		box.set(immagine);
	}
	
	public void chainBuild(){
		reload();
		ParameterBlock pb = new ParameterBlock();
		pb.addSource(immagine);
		oplist[0] = JAI.create("addconst", pb);
		for (int j = 0; j < neft; j++) {
			oplist[j+1] = eftlist[j].applyEffectJAI(oplist[j]);
		}
		box.set(oplist[neft].getAsBufferedImage());
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
//		chainBuild();
//		for (int i = 0; i < neft; i++) {
//			immagine = eftlist[i].applyEffect(immagine);}
		immagine = oplist[neft].getAsBufferedImage();
		box.set(immagine);
	}
	
	
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		immagine = effect.applyEffect(immagine);
//		box.set(immagine);
//	}
	
	
	
}
