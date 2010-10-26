import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;
import javax.media.jai.*;
import javax.swing.*;

import effects.*;

/**
 * Classe pensata per costituire il backend di un sofware di manipolazione di immagini.
 * Implementa metodi per le operazioni di I/O di
 * immagini e applicazione di {@link ImageEffect}s alle immagini caricate. 
 * 
 * 
 * Internamente la classe lavora su un'immagine caricata in memoria,
 * all'interno di una {@link BufferedImage},
 * e su un array di {@link ImageEffect} che ad essa vengono applicati in sequenza.
 * Si legga in particolare del metodo <code>ChainBuild</code>.
 * 
 * 
 *
 * @author Giulio Guzzinati 
 * 
 * @version 0.3
 */
 
public class ImageEngine implements ActionListener{
	/** il numero di {@link ImageEffect} attualmente caricati
	 */
	protected int neft = 0;
	protected boolean testing = false;
	
	/**Il vettore contenente gli {@link ImageEffect} da applicare alle immagini.
	 * La dimensione del vettore è fissata a 10,
	 * ritenendolo un numero sufficentemente grande per gli scopi del software.
	 */
	protected ImageEffect[] eftlist = new ImageEffect[10];
	
	protected Sidebar sidebar;
	protected File file = new File("~");
	protected ImageBox box;
	
	
	protected BufferedImage[] imglist = new BufferedImage[10];
	
	/**
	 * Crea un ImageEngine che visualizza le immagini generate
	 * attraverso l'{@link ImageBox} fornito come parametro 
	 * 
	 * @param imgbx {@link ImageBox} in cui visualizzare le immagini 
	 * @return l'ImageEngine creato.
	 */	
	public ImageEngine(ImageBox imgbx){
		
		
		if (testing == true){try { file = new File(this.getClass().getResource("files/Cell_Colony.jpg").toURI());
		} catch (URISyntaxException e1) { e1.printStackTrace(); }
		{try {	imglist[0] = ImageIO.read(file);	} catch (IOException e) {System.out.println("errore!!");}}}
		if (testing==false) {imglist[0] = new BufferedImage(500, 400, BufferedImage.TYPE_INT_ARGB );}
		Graphics2D g = imglist[0].createGraphics();
		g.setColor(new Color ( 0, 0, 0, 0 ));
		g.fillRect(0, 0, 500, 400);
		g.dispose();
		box = imgbx;
		box.set(imglist[0]);
	}
	
	/**
	 * carica l'immagine contenuta in un {@link File} 
	 * 
	 * @param fl il {@link File} da caricare
	 */
	public void openImage(File fl){
		file = fl;
		try {
			ParameterBlock pb = new ParameterBlock();
			pb.addSource(ImageIO.read(file));
			imglist[0] = JAI.create("addconst", pb).getAsBufferedImage();
		} catch (IOException e) {}
		box.set(imglist[0]);
	}
	
	/**
	 * salva l'ultima modifica dell'immagine in un
	 * {@link File} in formato <code>jpeg</code>
	 * 
	 * @param fl il {@link File} da caricare
	 */
	public void saveImage(File fl){
		try {
			ImageIO.write(imglist[neft], "jpeg", fl);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return il {@link File} corrispondente all'immagine aperta
	 */
	public File getFile(){
		return file;
	}
	
	/**
	 * ricarica l'immagine originale dal file
	 */
	public void reload(){
		this.openImage(file);
	}
	
	/**
	 * aggiunge un effetto all'array <code>eftlist</code>
	 * @param eft
	 */
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
	    chainBuild();
	}
	
	public int getNum(){
		return neft;
	}
	
	public void sidebar(Sidebar sidebar){
		this.sidebar = sidebar;
	}
	
	public void chooseEffect(int num){
		box.set(imglist[num + 1]);
	}
	
	/**
	 * Ricostruisce il vettore contente le immagini che costuiscono i singoli punti
	 * della catena di applicazione degli effetti.
	 */
	public void chainBuild(int n){
		reload();
		imglist[0] = imglist[0];
		for (int j = 0; j < n; j++) {
			try {	imglist[j+1] = eftlist[j].getBufferedImage(imglist[j]); 
			} catch (IllegalArgumentException e) {
				imglist[j+1] = imglist[0];
				showError(eftlist[j].getArgumentError());
			}}
		box.set(imglist[n]);
	}
	
	public void chainBuild(){
		chainBuild(neft);
	}
	
	
	public void showError(String err){
		JFrame errWin = new JFrame("Errore!");
		JTextArea errText = new JTextArea(err);
		errText.setEditable(false);
		errText.setFocusable(false);
		errWin.add(errText);
		errWin.pack();
		errWin.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		imglist[0] = imglist[neft];
		box.set(imglist[0]);
	}	
}
