

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import effects.*;


/**
 * 
 */

/**
 * @author Giulio Guzzinati
 *
 */
@SuppressWarnings("serial")
public class Sidebar extends JPanel implements ActionListener{

	/**
	 * 
	 */
	protected JPanel center;
	protected ImageEngine engine;
	protected JComboBox combo = new JComboBox();
	protected Section[] secs = new Section[10]; 

	private void comboBuild(){
		String[] names = {"Rilevamento bordi",
							"Monocromatizza",
							"Monocromatizza JAI"};
		for (int i = 0; i < names.length; i++) {
			combo.addItem(names[i]);
		}
		combo.setSelectedIndex(1);
	}
	
	
	
	public Sidebar(final ImageEngine engine){
		//Creo la parte superiore della sidebar
		JPanel top = new JPanel(new FlowLayout());
		JButton add = new JButton("+");
			add.addActionListener(this);
			comboBuild();
			top.add(combo);
			top.add(add);
		setLayout(new BorderLayout());
		JButton applica = new JButton("Applica");
		applica.addActionListener(engine);
		//creo la parte inferiore
		JPanel bottom = new JPanel(new GridLayout(2,1));
		bottom.add(applica);
		bottom.add(new JPanel());
		//creo il centro, vuoto, inizializzo le sezioni
		centerinit();
		//aggiungo tutto
		add(bottom, BorderLayout.SOUTH);
		add(top, BorderLayout.NORTH);
		add(center);
		this.engine = engine;
		
		
	}
	
	public void setEffect(effects.ImageEffect effect){
		remove(center);
		center = effect.getSidebar(engine);
		add(center);
		validate();
		repaint();
	}
	
	public void addEffect(ImageEffect eft){
		int n = (engine.getNum());
		secs[n] = new Section(eft, engine); 
		center.add(secs[n]);
		validate();
		repaint();
	}
	
	public void removeEffect(int del){
		center.remove(secs[del]);
	    System.arraycopy(secs,del+1,secs,del,secs.length-1-del);
	    for (int i = 0; i < engine.getNum(); i++) {
			secs[i].setN(i);
		}
		validate();
		repaint();
	}
	
	private void centerinit() {
		center = new JPanel();
		center.setLayout(new FlowLayout());
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		int idx = combo.getSelectedIndex();
		ImageEffect tmpeft = new SobelJAI();
		if (idx == 0) {
			tmpeft = new SobelJAI();
		} else if (idx == 1) {
			tmpeft = new Monochrome();
		} else if (idx == 2) {
			tmpeft = new MonochromeJAI();
		}
		engine.addEffect(tmpeft);
	}
		
}
	@SuppressWarnings("serial")
	class Section extends JPanel implements ActionListener{
		ImageEngine engine;
		int num;
		
		public Section(ImageEffect eft, ImageEngine engine){
			this.engine = engine;
			this.num = engine.getNum();
			
			setLayout(new BorderLayout());
			JPanel south = new JPanel(new FlowLayout());
			setBorder(new TitledBorder(eft.getName()));
			JButton app = new JButton("Applica");
			JButton rmv = new JButton("Elimina");
				rmv.addActionListener(this);
				
				
			south.add(app);
			south.add(rmv);
			add(south, BorderLayout.SOUTH);
			add(eft.getSidebar(engine));
		}
		
		public void setN(int n){
			num=n;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			engine.removeEffect(num);
		}

	}
