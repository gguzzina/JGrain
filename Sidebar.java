

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
	protected JPanel content = new JPanel();
	protected ImageEngine engine;
	protected JComboBox eftchooser = new JComboBox();

	private void comboBuild(){
		String[] names = {"Rilevamento bordi","Monocromatizza"};
		for (int i = 0; i < names.length; i++) {
			eftchooser.addItem(names[i]);
		}
		eftchooser.setSelectedIndex(1);
	}
	
	
	
	public Sidebar(ImageEngine engine){
		JPanel top = new JPanel(new GridLayout(2,1));
		JPanel bottom = new JPanel(new GridLayout(2,1));
		comboBuild();
//		top.add(new JPanel());
		top.add(eftchooser);
		setLayout(new BorderLayout());
		eftchooser.addActionListener(this);
		add(top, BorderLayout.NORTH);
		add(content);
		this.engine = engine;
		
		JButton applica = new JButton("Applica");
		applica.addActionListener(engine);
		bottom.add(applica);
		bottom.add(new JPanel());
		add(bottom, BorderLayout.SOUTH);
	}
	
	public void setEffect(effects.ImageEffect effect){
		remove(content);
		content = effect.getSidebar(engine);
		add(content);
		validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = ((JComboBox)e.getSource());
		int idx = cb.getSelectedIndex();
		if (idx == 0) {
			engine.setEffect(new SobelJAI());
		} else if (idx == 1) {
			engine.setEffect(new Monochrome());
		}
		
	}	
		
}
	
