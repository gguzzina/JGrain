import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	protected Sidebar sidebar;
	protected BufferedImage immagine = null;
	protected File file;
	protected ImageBox box;
	
	public ImageEngine(ImageBox imgbx){
		file = new File("/home/skarn/Documenti/Universita/V Anno/Java/Eclipse/jgrain/src/Dot_Blot.jpg");
//		file = new File("/home/skarn/Documenti/Universita/V Anno/Java/Eclipse/jgrain/src/garbage_collector.jpg");
			{try {	immagine = ImageIO.read(file);	} catch (IOException e) {}}
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
	
	public void sidebar(Sidebar sidebar){
		this.sidebar = sidebar;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		immagine = effect.applyEffect(immagine);
		box.set(immagine);
	}
}
