/*
 *            	Life-Style-Specific-Islands
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public License.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *      
 * This material was developed as part of a research project at 
 * the University of Southern Denmark (SDU - Odense, Denmark) 
 * and the Federal University of Minas Gerais (UFMG - Belo 
 * Horizonte, Brazil). For more information please access:
 * 
 *      	https://lissi.compbio.sdu.dk/ 
 */
package dk.sdu.imada.main;


import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import dk.sdu.imada.gui.MainFrame;
import dk.sdu.imada.methods.ValidateInput;


/** 
 * Class starts application. <br>
 * LiSSI stands for Life-Style-Specific-Islands. It was developed 
 * to identify islands mainly associated with a given life-style. 
 * LiSSI is divided into three subsequencial modules: <br>
 * - Evolutionary Sequence Analysis; <br>
 * - Island Detection; <br>
 * - Statistical Learning Methods. <br>
 * Optionally, the tool can be used without the islands detection. 
 * In that case, it will report putative homologous genes that are 
 * mainly associated with a given life-style.
 * 
 * @author Eudes Barbosa
 */
public class Main {

	//------  Variable declaration  ------//

	/**
	 * Application avoid running multiple instances by binding 
	 * to a local host adapter with a zero connection queue.
	 */
	protected static ServerSocket socket;

	//------  Declaration end  ------//


	/** Initializes the application.
	 * @param args		The command line arguments.	The application 
	 * 					only requires a configuration file.
	 */
	public static void main(String[] args) {
		// Avoid running multiple instances of the application
		try {
			// Bind to local host adapter with a zero connection queue 
			socket = new ServerSocket(1986, 0, InetAddress.getByAddress(new byte[] {127,0,0,1}));
		} catch (BindException e) {
			JOptionPane.showMessageDialog(null, "Application is already running.");
			System.exit(1);		    
		} catch (Exception e) {
			System.err.println("Unexpected error.");
			e.printStackTrace();			
			System.exit(2);
		}		

		// Create runnable to display frame
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Validate input
				ValidateInput valid = new ValidateInput(args);
				// Start frame
				new MainFrame(valid).display();				
			}
		});				
	}		
}
