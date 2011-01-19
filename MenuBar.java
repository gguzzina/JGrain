import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;


/**
 * Una classe contenente una menubar per il programma JGrain
 * @author Giulio Guzzinati
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	
	protected JMenu file, modifica, zoom;
	protected JMenuItem apri, salva, chiudi;
	protected JMenuItem ricarica, applica, batch;
	protected JMenuItem zin, zout, z0;
	protected ImageEngine engine;
	
	/**Crea la menubar, che comunica con l' {@link ImageEngine} dato.
	 * 
	 * @param engine l'{@link ImageEngine} a cui far compiere le operazioni
	 */
	public MenuBar(ImageEngine engine){
		this.engine = engine;
		buildMenu();
	}
	
	private void buildMenu(){
	
	file = new JMenu("File");
	modifica = new JMenu("Modifica");
	zoom = new JMenu("Zoom");
		this.add(file);
		this.add(modifica);
		this.add(zoom);
		
		
	apri = new JMenuItem("Apri");
		apri.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		apri.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.openImage();}});
		
	salva = new JMenuItem("Salva");
		salva.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		salva.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.saveImage();}});
		
	chiudi = new JMenuItem("Chiudi");
		chiudi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
		chiudi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);}});
		file.add(apri);
		file.add(salva);
		file.add(chiudi);
		
		
	applica = new JMenuItem("Applica effetti");
		applica.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		applica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.chooseEffect();}});
		
	ricarica = new JMenuItem("Ricarica immagine");
		ricarica.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
		ricarica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.reload();}});
		
	batch = new JMenuItem("Applica a molti file...");
		batch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, Event.CTRL_MASK));
		batch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				engine.batchProcess();
	                	}});
		
		modifica.add(applica);
		modifica.add(ricarica);
		modifica.add(batch);
		
		zin = new JMenuItem("Zoom +");
			zin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, Event.CTRL_MASK));
			zin.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					engine.zoom(0.1);}});
		
		zout = new JMenuItem("Zoom -");
			zout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, Event.CTRL_MASK));
			zout.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					engine.zoom(-0.1);}});
		
		z0 = new JMenuItem("Zoom 100%");
			z0.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, Event.CTRL_MASK));
			z0.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					engine.zoomReset();}});
		 
		zoom.add(zin);
		zoom.add(zout);
		zoom.add(z0);
	}
}
	
	
