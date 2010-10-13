

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
		String[] names = {new SobelJAI().getName(),
							new MonochromeJAI().getName(),
							new GrayScale().getName(),
							new Invert().getName(),
							new Counter().getName(),
							new CounterLegacy().getName()};
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
		center = new JPanel();
//		center.setMaximumSize(new Dimension(240, 800));
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		JScrollPane pane = new JScrollPane(center);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//aggiungo tutto
		add(bottom, BorderLayout.SOUTH);
		add(top, BorderLayout.NORTH);
		add(pane);
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



	@Override
	public void actionPerformed(ActionEvent e) {
		int idx = combo.getSelectedIndex();
		if (idx == 0) {
			engine.addEffect(new SobelJAI());
		} else if (idx == 1) {
			engine.addEffect(new MonochromeJAI());
		} else if (idx == 2) {
			engine.addEffect(new GrayScale());
		} else if (idx == 3) {
			engine.addEffect(new Invert());
		} else if (idx == 4) {
			engine.addEffect(new Counter());
		} else if (idx == 5) {
			engine.addEffect(new CounterLegacy());
		}
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
			
			setLayout(new BorderLayout());
			JPanel south = new JPanel(new FlowLayout());
			setBorder(new TitledBorder(eft.getName()));
			app = new JButton("Applica");
				app.addActionListener(this);
			rmv = new JButton("Elimina");
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
			if (e.getSource() == rmv) {
				engine.removeEffect(num);
			} else if (e.getSource() == app) {
				engine.chooseEffect(num);
			}
		}

	}
