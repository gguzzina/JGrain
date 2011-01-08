import java.awt.*;
import javax.swing.*;

import effects.*;

/**
 * @author Giulio Guzzinati
// * @version 0.3.0
 */
public class JGrain {
	/**
	 * @param args
	 */


	/*
	* Elimino l'eccezione per l'assenza di accelerazione nativa
	*/
	static 
	{ 
	System.setProperty("com.sun.media.jai.disableMediaLib", "true"); 
	} 
	
	public static void main(String[] args) {
		boolean testing = false;
		JFrame mainWin = new JFrame("JGrain");
		Container mainCont = mainWin.getContentPane();
		ImageBox box = new ImageBox();
		final ImageEngine engine = new ImageEngine(box);
		MenuBar barra = new MenuBar(engine);
		Sidebar sidebar = new Sidebar(engine);
		
		engine.sidebar(sidebar);
		engine.frame(mainWin);
		mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mainWin.setSize(1000,700);
		mainWin.setJMenuBar(barra);
		BorderLayout layout = new BorderLayout();
		mainCont.setLayout(layout);
		mainCont.add(box);
		mainCont.add(sidebar, BorderLayout.EAST);
		mainWin.pack();
		mainWin.setExtendedState(mainWin.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		mainWin.setVisible(true);
		
		
		if (testing==true) {
			engine.addEffect(new GrayScale());
			engine.addEffect(new BinarizeGray());
//			engine.addEffect(new Invert());
			engine.addEffect(new Counter());
		}
	}
}
