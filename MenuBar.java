import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;


/**
 * Una classe contenente una menubar per il programma JGrain
 * @author Giulio Guzzinati
 */
@SuppressWarnings("serial")
public class MenuBar extends JMenuBar {
	
	protected JMenu file, modifica, visualizza, preferenze;
	protected JMenuItem apri, salva, chiudi;
	protected JMenuItem ricarica, applica;
	protected JMenuItem zoom0;
	protected JMenuItem configura;
	protected ImageEngine engine;
	
	/**
	 * @param engine l'{@link ImageEngine} a cui far compiere le operazioni
	 */
	public MenuBar(ImageEngine engine){
		this.engine = engine;
		buildMenu();
	}
	
	private void buildMenu(){
	
	file = new JMenu("File");
	modifica = new JMenu("Modifica");
	visualizza = new JMenu("Visualizza");
	preferenze = new JMenu("Preferenze");
		this.add(file);
		this.add(modifica);
		this.add(visualizza);
		this.add(preferenze);
		
		
	apri = new JMenuItem("Apri");
		apri.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(engine.getFile().getParentFile());
				int returnVal = fc.showOpenDialog(null);
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                engine.openImage(file);}}});
	salva = new JMenuItem("Salva");
		salva.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(engine.getFile().getParentFile());
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					engine.saveImage(file);}}});
	chiudi = new JMenuItem("Chiudi");
		chiudi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);}});
		file.add(apri);
		file.add(salva);
		file.add(chiudi);
		
	applica = new JMenuItem("Applica effetti");
		applica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.applyEffect();}});
	ricarica = new JMenuItem("Ricarica immagine");
		ricarica.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				engine.reload();}});
		modifica.add(applica);
		modifica.add(ricarica);
	}
}
	
	
