

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import effects.*;


/**
 * Classe che crea la sidebar del programma JGrain.
 * 
 * Permette di sceglere gli effetti da aggiungere o eliminare dalla catena,
 * carica ed elimina sezioni corrispondenti ai vari effetti.
 * 
 * la selezione degli effetti avviene tramite una {@link JComboBox}
 * che ottiene i nomi degli effetti tramite il metodo getName degli effetti stessi 
 *  
 * @author Giulio Guzzinati
 */
@SuppressWarnings("serial")
public class Sidebar extends JPanel{

	protected JPanel center;
	protected ImageEngine engine;
	protected JComboBox combo = new JComboBox();
	protected Section[] secs = new Section[10]; 
	
	private void comboBuild(){
		String[] names = {new SobelJAI().getName(),
							new BinarizeGray().getName(),
							new BinarizeColor().getName(),
							new GrayScale().getName(),
							new Invert().getName(),
							new Counter().getName()};
		for (int i = 0; i < names.length; i++) {
			combo.addItem(names[i]);
		}
		combo.setSelectedIndex(0);
	}
	
	
	/**
	 * @param engine l'{@link ImageEngine} a cui la
	 * sidebar comunica le operazioni da eseguire
	 */
	public Sidebar(final ImageEngine engine){
		//Creo la parte superiore della sidebar
		JPanel top = new JPanel(new FlowLayout());
		URL url = JGrain.class.getResource("icons/add.png");
		ImageIcon icon = new ImageIcon(url);
		JButton add = new JButton(icon); 
			add.addActionListener(new ActionListener() {@Override
				public void actionPerformed(ActionEvent e) {
					int idx = combo.getSelectedIndex();
						   if (idx == 0) {
						engine.addEffect(new SobelJAI());
					} else if (idx == 1) {
						engine.addEffect(new BinarizeGray());
					} else if (idx == 2) {
						engine.addEffect(new BinarizeColor());
					} else if (idx == 3) {
						engine.addEffect(new GrayScale());
					} else if (idx == 4) {
						engine.addEffect(new Invert());
					} else if (idx == 5) {
						engine.addEffect(new Counter());
					}
				}});
			comboBuild();
			top.add(combo);
			top.add(add);
		setLayout(new BorderLayout());
		JButton applica = new JButton("Applica");
		applica.addActionListener(new ActionListener() {@Override
			public void actionPerformed(ActionEvent arg0) {
			engine.chooseEffect();
				}});
		//creo la parte inferiore
		JPanel bottom = new JPanel();
		bottom.add(applica);
//		bottom.add(new JPanel());
		//creo il centro, vuoto, inizializzo le sezioni
		center = new JPanel();
//		center.setMaximumSize(new Dimension(240, 800));
		BoxLayout layout = new BoxLayout(center, BoxLayout.Y_AXIS);
		center.setLayout(layout);
		JScrollPane pane = new JScrollPane(center);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//aggiungo tutto
		add(bottom, BorderLayout.SOUTH);
		add(top, BorderLayout.NORTH);
		add(pane);
		this.engine = engine;
		
		
	}
	
	
	
	/**Aggiunge alla sidebar la sezione corrispondente ad un {@link ImageEffect}.
	 * 
	 * Il contorno e i pulsanti <code>applica</code> ed <code>elimina</code>
	 * sono presenti in maniera predefinita, il "contenuto"
	 * è ottenuto dall'{@link ImageEffect}
	 * tramite il metodo <code>getSidebar</code>
	 * 
	 * @param eft l'effetto da aggiungere
	 */
	public void addEffect(ImageEffect eft){
		int n = (engine.getNum());
		secs[n] = new Section(eft, engine); 
		center.add(secs[n]);
		validate();
		repaint();
	}
	
	/**Rimuove dalla sidebar la sezione corrispondete
	 * ad uno degli {@link ImageEffect} caricati
	 * 
	 * @param del l'indice della sezione da eliminare
	 */
	public void removeEffect(int del){
		center.remove(secs[del]);
	    System.arraycopy(secs,del+1,secs,del,secs.length-1-del);
	    for (int i = 0; i < engine.getNum(); i++) {
			secs[i].setN(i);
		}
		validate();
		repaint();
	}
}

	@SuppressWarnings("serial")
	class Section extends JPanel implements ActionListener{
		ImageEngine engine;
		int num;
		JButton rmv;
		JButton app;
		
		public Section(ImageEffect eft, ImageEngine engine){
			this.engine = engine;
			this.num = engine.getNum();
			URL url1 = JGrain.class.getResource("icons/apply.png");
			ImageIcon icon1 = new ImageIcon(url1);
			URL url2 = JGrain.class.getResource("icons/close.png");
			ImageIcon icon2 = new ImageIcon(url2);
			
			setLayout(new BorderLayout());
			JPanel south = new JPanel(new FlowLayout());
			setBorder(new TitledBorder(eft.getName()));
			app = new JButton("Applica",icon1);
				app.addActionListener(this);
			rmv = new JButton("Elimina",icon2);
				rmv.addActionListener(this);
				
				
			south.add(app);
			south.add(rmv);
			add(south, BorderLayout.SOUTH);
			add(eft.getSidebar());
			this.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) this.getMinimumSize().getHeight()));
		}
		
		public void setN(int n){
			num=n;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == rmv) {
				engine.removeEffect(num);
			} else if (e.getSource() == app) {
				engine.chooseEffect(num);
			}
		}

	}
