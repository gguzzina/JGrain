import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

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
	protected boolean testing = false;
	
	/**Il vettore contenente gli {@link ImageEffect} da applicare alle immagini.
	 * La dimensione del vettore è fissata a 10,
	 * ritenendolo un numero sufficentemente grande per gli scopi del software.
	 */
	protected ImageEffect[] eftlist = new ImageEffect[10];
	
	protected Sidebar sidebar;
	protected JFrame frame;
	protected File file = new File("~");
	protected File dir1, dir2;
	protected ImageBox box;
	
	protected BufferedImage dummy = new BufferedImage(500, 400, BufferedImage.TYPE_INT_ARGB );
					{Graphics2D g = dummy.createGraphics();
						g.setColor(new Color ( 0, 0, 0, 0 ));
						g.fillRect(0, 0, 500, 400);
						g.dispose();}
	
	protected BufferedImage[] imglist = new BufferedImage[10];
	protected boolean batch = false;
	
	Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); 

	
	
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
		box.engine(this);
		box.set(imglist[0]);
		
		log.addHandler(new ConsoleHandler());
		
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
			log.log(new LogRecord(Level.INFO, "apro " + fl.toString()));
		} catch (IOException e) {
			String s = ("Impossibile aprire il file "+fl.getName() + "forse non è un'immagine?");
			log.log(new LogRecord(Level.SEVERE, s));
			if (batch==false){ showError(s);}
		}
		box.set(imglist[0]);
	}
	
	/**carica l'immagine contenuta in un file esterno, che viene selezionato
	 * dall'utente attraverso un {@link JFileChooser}
	 */
	public void openImage(){
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(getFile().getParentFile());
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        openImage(file);}
    }
	
	/**
	 * salva l'ultima modifica dell'immagine in un
	 * {@link File} in formato <code>jpeg</code>
	 * 
	 * @param fl il {@link File} da salvare
	 */
	public void saveImage(File fl){
		try {
			ImageIO.write(imglist[neft], "jpeg", fl);
			log.log(new LogRecord(Level.INFO, "salvo " + fl.toString()));
		} catch (IOException e) {
			String s = "Impossibile salvare il file";
			log.log(new LogRecord(Level.INFO, s));
			if (batch==false) {showError(s);}
			e.printStackTrace();
		}
	}
	
	/** 
	 * salva l'ultima modifica dell'immagine  in formato <code>jpeg</code> in un
	 * {@link File} esterno che viene selezionato
	 * dall'utente attraverso un {@link JFileChooser}
	 */
	public void saveImage(){
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(getFile().getParentFile());
		int returnVal = fc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			saveImage(file);}
	}
	
	
	/**Controlla una cartella e applica le gli effetti selezionati
	 * a tutti i file contenuti, salvando le immagini risultanti in una
	 * seconda cartella. Nella seconda cartella include un file di log 
	 * nel formato indicato (txt o xml)
	 * 
	 * @param dir1  la cartella dei file sorgente
	 * @param dir2  la cartella in cui salvare i file
	 * @param logType il formato del log, le opzioni
	 * valide sono <code>"txt"</code> e <code>"xml"</code>
	 */
	public void batchProcess(File dir1, File dir2, String logType){
		batch = true;
		File[] files = dir1.listFiles();//array dei contenuti di dir1
		//Creo il file di log, il file handler, e lo aggiungo al logger.
		File logFile = null;
		if (logType == "txt"){logFile = new File(dir2, "log.txt");}
		else if (logType == "xml"){logFile = new File(dir2, "log.xml");}
		else{showError("Impossibile identificare il tipo di log. " +
				"Che hai combinato???");
				logFile = null;}
		FileHandler hnd = null;
		
		try {
			hnd = new FileHandler(logFile.toString(), false);
			if (logType == "txt"){hnd.setFormatter(new SimpleFormatter());}
			else if (logType == "xml"){hnd.setFormatter(new XMLFormatter());}
			log.addHandler(hnd);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (File fl : files) {
			//carica un'immagine ed applica gli effetti attivi
			openImage(fl);
			//reload();
			chooseEffect();
			//salva il file
			File newFl = new File(dir2, "mod_" + fl.getName());
			saveImage(newFl);
		}
		
		hnd.close();
		log.removeHandler(hnd);
		batch = false;
	}
	
	
	/**Controlla una cartella e applica le gli effetti selezionati
	 * a tutti i file contenuti, salvando le immagini risultanti in una
	 * seconda cartella. Nella seconda cartella include un file di
	 * log in formato xml
	 * 
	 * @param dir1  la cartella dei file sorgente
	 * @param dir2  la cartella in cui salvare i file
	 */
	public void batchProcess(File dir1, File dir2){
		batchProcess(dir1, dir2, "xml");
	}

	
	
	/** Controlla una cartella e applica le gli effetti selezionati
	 * a tutti i file contenuti, salvando le immagini risultanti in una
	 * seconda cartella. Nella seconda cartella include un file di log
	 * 
	 * Interroga l'utente attraverso una finestra di dialogo modale su
	 * quali cartelle utilizzare
	 * 
	 */
	public void batchProcess(){
		JPanel pn = new JPanel();
		pn.setLayout(new BoxLayout(pn, BoxLayout.PAGE_AXIS));
		JLabel lbl = new JLabel("Formato del file di log:");
		
		dir1 = getFile().getParentFile();
		dir2 = getFile().getParentFile();
		
		//creo il primo campo di testo
		JPanel pn1 = new JPanel();
		Border bd1 = BorderFactory.createTitledBorder("Cartella di origine");
		final JTextField tf1 = new JTextField(getFile().toString(), 40);
		tf1.setEditable(false);
		
		//creo il bottone sfoglia per il primo campo,
		//con actionlistener e JFileChooser
		JButton btn1 = new JButton("Sfoglia...");
		btn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc1 = new JFileChooser();
				fc1.setCurrentDirectory(getFile().getParentFile());
				fc1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fc1.setDialogTitle("Cartella sorgente");
				int returnVal1 = fc1.showOpenDialog(null);
		        if (returnVal1 == JFileChooser.APPROVE_OPTION) {
		            dir1 = fc1.getSelectedFile();
		            tf1.setText(dir1.toString());}
			}
		});
		//creo il secondo campo di testo, bordo
		JPanel pn2 = new JPanel();
		Border bd2 = BorderFactory.createTitledBorder("Cartella di destinazione");
		final JTextField tf2 = new JTextField(getFile().toString(), 40);
		tf2.setEditable(false);
		
		//creo il bottone sfoglia per il secondo campo,
		//con actionlistener e JFileChooser
		JButton btn2 = new JButton("Sfoglia...");
		btn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc2 = new JFileChooser();
		        fc2.setCurrentDirectory(getFile().getParentFile());
		        fc2.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		        fc2.setDialogTitle("Cartella di destinazione");
		        int returnVal2 = fc2.showOpenDialog(null);
		        if (returnVal2 == JFileChooser.APPROVE_OPTION) {
		        	dir2 = fc2.getSelectedFile();
		        	tf2.setText(dir2.toString());}
			}});
		
		//aggiungo tutto a tutto...
		pn1.add(tf1);
		pn1.add(btn1);
		pn1.setBorder(bd1);
		
		pn2.add(tf2);
		pn2.add(btn2);
		pn2.setBorder(bd2);
		
		pn.add(pn1);
		pn.add(pn2);
		pn.add(lbl);
		
		
		
		// il metodo chiamato, restituisce lo stesso oggetto pn che gli ho dato io
		// in caso di conferma, altrimenti un null
		String[] opts = {"xml","txt"};
		Object o = JOptionPane.showInputDialog(frame, pn, "Applica a molti file...",
				JOptionPane.QUESTION_MESSAGE,
				UIManager.getIcon("OptionPane.questionIcon"),
				opts, opts[0]);
		if (o != null) {
			batchProcess(dir1 ,dir2, o.toString());
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
	
	/**
	 * visualizza l'immagine corrispondente all'applicazione dei primi
	 * <b>num</b> effetti 
	 * @param num l'indice dell'ultimo effetto da applicare 
	 */
	//TODO: chiarificare tutti i casini con gli indici
	public void chooseEffect(int num){
		chainBuild();
		box.set(imglist[num + 1]);
	}
	
	/**
	 * applica tutti gli effetti caricati all'immagine
	 */
	public void chooseEffect(){
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
	    chooseEffect();
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
		for (int j = 0; j < n; j++) {
			try {	imglist[j+1] = eftlist[j].getBufferedImage(imglist[j]); 
					if (eftlist[j].getLogMessage() != null){
						// se l'effetto prevede un messaggio informativo, loggarlo 
						log.log(new LogRecord(Level.INFO, eftlist[j].getLogMessage()));
						}
			} catch (IllegalArgumentException e) {
				//loggare, e nel caso mostrare in popup, un eventuale errore
				imglist[j+1] = imglist[0];
				String s = eftlist[j].getArgumentError();
				log.log(new LogRecord(Level.SEVERE, s));
				if (batch==false) {showError(s);}
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
