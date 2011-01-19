package effects;

import java.awt.image.BufferedImage;
import javax.media.jai.*;
import javax.swing.*;

/**Classe astratta per gli effetti.
 * Andrà implementata negli effetti specifici.
 * 
 * L'effetto viene utilizzato chiamando il metodo <code>getBufferedImage</code>
 * che prende una {@link BufferedImage} come argomento e ne restituisce una.
 * 
 * Sono forniti i metodi <code>getSidebar</code> e <code>getName</code>
 * per l'utilizzo con la classe {@link Sidebar} e il
 * metodo <code>getArgumentError</code> per permettere alla
 * classe {@link ImageEngine} la visualizzazione di messaggi d'errore personalizzati
 * 
 * @author Giulio Guzzinati
 */
public abstract class ImageEffect {	
	protected JPanel sidebar;
	
	public ImageEffect(){}
	
	
	protected abstract RenderedOp getRenderedOp(RenderedOp op);
	
	/**applica l'effetto ad un'immagine
	 * @param img l'immagine a cui applicare l'effetto
	 * @return l'immagine, risultato dell'effetto
	 */
	public abstract BufferedImage getBufferedImage(BufferedImage img);
	
	/**restituisce un {@link JPanel} con i controlli personalizzati per l'effetto
	 * @return il JPanel da inserire nella sezione della sidebar 
	 */
	public abstract JPanel getSidebar();
	
	/**Restituisce un nome per l'effetto, da mostrare nell'interfaccia utente e nei log
	 * 
	 * @return il nome del'effetto
	 */
	public abstract String getName();
	
	/**Restituisce un messaggio d'errore personalizzato
	 * per la comunicazione all'utente di eccezioni
	 * {@link IllegalArgumentException} 
	 * 
	 * @return un stringa messaggio d'errore
	 */
	public String getArgumentError(){
		return "Errore, argomento non valido";
	}
	
	
	/**Fornisce un messaggio con informazioni sull'applicazione dell'effetto.
	 * Se l'effetto non prevede ci possano essere informazioni rilevanti,
	 * viene restituito un <code>null</code>.
	 * 
	 * 
	 * @return un messaggio informativo sull'esito dell'applicazione dell'effetto.
	 */
	public String getLogMessage(){
	return null;
	}
}
