import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
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
 
public class ImageEngine /*implements ActionListener*/{
	/** il numero di {@link ImageEffect} attualmente caricati
	 */
	protected int neft = 0;
	protected boolean testing = true;
	
	/**Il vettore contenente gli {@link ImageEffect} da applicare alle immagini.
	 * La dimensione del vettore è fissata a 10,
	 * ritenendolo un numero sufficentemente grande per gli scopi del software.
	 */
	protected ImageEffect[] eftlist = new ImageEffect[10];
	
	protected Sidebar sidebar;
	protected JFrame frame;
	protected File file = new File("~");
	protected ImageBox box;
	
	protected BufferedImage dummy = new BufferedImage(500, 400, BufferedImage.TYPE_INT_ARGB );
					{Graphics2D g = dummy.createGraphics();
						g.setColor(new Color ( 0, 0, 0, 0 ));
						g.fillRect(0, 0, 500, 400);
						g.dispose();}
	
	protected BufferedImage[] imglist = new BufferedImage[10];
	
	/**
	 * Crea un ImageEngine che visualizza le immagini generate
	 * attraverso l'{@link ImageBox} fornito come parametro 
	 * 
	 * @param imgbx {@link ImageBox} in cui visualizzare le immagini 
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
			imglist[0] = ImageIO.read(file); 
		} catch (IOException e) {
			showError("Impossibile aprire il file "+fl.getName() + "forse non è un'immagine?");
			}
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
			showError("Impossibile salvare il file");
			e.printStackTrace();
		}
	}
	
	/**Controlla una cartella e applica le gli effetti selezionati
	 * a tutti i file contenuti.
	 * Nella seconda cartella include un file di log 
	 * 
	 * @param dir1  la cartella dei file sorgente
	 * @param dir2  la cartella in cui salvare i file
	 */
	public void batchProcess(File dir1, File dir2){
		File[] files = dir1.listFiles(); //array dei contenuti di dir1
		//Creo il file di log, il logger e il file handler 
		File logFile = new File(dir2, "log.txt");
		Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 
		
		try{
			log.addHandler(new FileHandler(logFile.toString(), false));
		} catch (IOException e) {
			//TODO: gestire l'eccezione 
		}
		
		
		for (File fl : files) {
			System.out.println("apro" + fl.toString());
			//carica un'immagine ed applica gli effetti attivi
			openImage(fl);
			//TODO: loggare l'apertura
			reload();
			applyEffects();
			//salva il file
			File newFl = new File(dir2, "mod_" + fl.getName());
			System.out.println(newFl.toString());
			update();
			     try {
					  Thread.currentThread();
					  Thread.sleep(1000);
			       }
			     catch (InterruptedException e) {
			       e.printStackTrace();
			       }
			saveImage(newFl);
		}
	}
	
	/**
	 * ridisegna l'immagine per riflettere eventuali cambiamenti
	 */
	public void update(){
		box.update();
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
	
	/**
	 * visualizza l'immagine corrispondente all'applicazione dei primi
	 * <b>num</b> effetti 
	 * @param num l'indice dell'ultimo effetto da applicare 
	 */
	public void chooseEffect(int num){
		chainBuild();
		box.set(imglist[num + 1]);
	}
	
	/**
	 * applica tutti gli effetti caricati all'immagine
	 */
	public void applyEffects(){
		chooseEffect(neft-1);
	}
	
	/**
	 * rimuove un elemento dal vettore <code>eftlist</code>.
	 * 
	 * L'intero <code>neft</code> viene decrementato,
	 * e gli elementi successivi a quello eliminato vengono spostati,
	 * in modo da avere <code>neft </code> elementi consecutivi.
	 * Dalla sidebar viene eliminata la sezione corrispondente all'effetto.
	 * 
	 * @param del l'indice dell'ImageEffect da eliminare
	 * 
	 */
	
	public void removeEffect(int del){
		neft = neft-1;
	    System.arraycopy(eftlist,del+1,eftlist,del,eftlist.length-1-del);
	    sidebar.removeEffect(del);
	    chainBuild();
	}
	
	/**
	 * @return il numero di effetti caricati 
	 */
	public int getNum(){
		return neft;
	}
	
	/**
	 * passa all'oggetto {@link ImageEngine} la {@link Sidebar}
	 * del programma, in modo che esso possa caricarvi le sezioni
	 * corrispondenti ai vari {@link ImageEffect}
	 * @param sidebar la {@link Sidebar} del programma
	 */
	public void sidebar(Sidebar sidebar){
		this.sidebar = sidebar;
	}
	
	/**
	 * passa all'oggetto {@link ImageEngine} la {@link JFrame}
	 * del programma
	 */
	public void frame(JFrame frame){
		this.frame = frame;
	}
	
	/**
	 * Ricostruisce i primi <b>n</b> elementi del vettore contente
	 * le immagini che costuiscono i singoli punti
	 * della catena di applicazione degli effetti.
	 * @param n numero degli elementi da ricreare
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
	
	/**
	 * mostra una finestra di dialogo modale,
	 * che quindi mette in secondo piano la finestra
	 * principale finchè non viene chiusa, che mostra un messaggio d'errore
	 * @param err il messaggio d'errore da visualizzare
	 */
	private void showError(String err) {
		JOptionPane.showMessageDialog(frame, err, "Errore!",JOptionPane.ERROR_MESSAGE); 
	}

	/**
	 * Aggiorna la catena degli effetti a partire dalla posizione data 
	 * 
	 * @param n effetto da cui iniziare ad aggiornare
	 * il vettore delle immagini calcolate.
	 */
	
	public void chainUpdateFrom(int n){
		for (int j=n; j < neft; j++) {
			try {	imglist[j+1] = eftlist[j].getBufferedImage(imglist[j]); 
			} catch (IllegalArgumentException e) {
				imglist[j+1] = imglist[0];
				showError(eftlist[j].getArgumentError());
			}}
	}
	
	/**
	 * Ricostruisce tutti gli elementi dell'array <code>eftlist</code>
	 */
	
	public void chainBuild(){
		chainBuild(neft);
	}
	
	
	/**
	 * Incrementa del valore indicato lo zoom dell'immagine
	 * all'interno dell'ImageBox <code>box</code>.
	 * 
	 * @param z l'incremento da applicare allo zoom
	 */
	public void zoom(double z){
		box.zoom(z);
	} 
	
	/**
	 * Reimposta a 1 il fattore di zoom dell'ImageBox <code>box</code>.
	 */
	public void zoomReset(){
		box.zoomReset();
	}
}
