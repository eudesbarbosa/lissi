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
package dk.sdu.imada.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.methods.ValidateInput;


/** 
 * Class creates and configures the aplication's 
 * main frame.
 * 
 * @author Eudes Barbosa
 */
public class MainFrame extends JFrame {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = -8840721299088738075L;

	private static final Logger logger = LogManager.getLogger(MainFrame.class.getCanonicalName());
	
	/** Global parameters. */
	protected static ValidateInput parameters;

	//------  Declaration end  ------//


	/**
	 * Create and initializes the components associated with 
	 * the main frame of LiSSI.
	 * @param parameters Object containing global parameters.
	 */
	public MainFrame(ValidateInput parameters) {
		super("LifeStyle-Specific-Islands");
		// Log
		logger.info("Running LiSSI..."); 
		// Set global parameters
		MainFrame.parameters = parameters;
		// Configure frame
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Close frame
				close();
			}
		});
		// Set icon
		//ClassLoader cl = MainFrame.class.getClassLoader();
		//ImageIcon img = new ImageIcon(cl.getClass().getResource("/images/lissi_icon.png"));
		ImageIcon img = new ImageIcon(ClassLoader.getSystemResource("images/lissi_icon.png"));
		setIconImage(img.getImage());

		// Set dimensions
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
		setPreferredSize(new Dimension(1400,820));

		/* Set the Nimbus look and feel 
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException 
				| UnsupportedLookAndFeelException ex) {
			logger.error(java.util.logging.Level.SEVERE, ex);
		}	
		// Initialize components
		initiComponents();
	}

	/** Initialize swing components of the main frame. */
	protected void initiComponents() {
		// Configures menus
		JMenuBar jMenuBar = new JMenuBar();
		jMenuBar.add(new CreateMenuBar("File"));
		//jMenuBar.add(new CreateMenuBar("Edit"));
		jMenuBar.add(new CreateMenuBar("Help"));		
		setJMenuBar(jMenuBar);	
		// Add MultiSplitPanel
		getContentPane().add(new CreateMultiSplitPane());
	}

	/** Close application and clean up */
	public static void close() {
		int confirm = JOptionPane.showOptionDialog(null, "Close Application?",
				"Exit Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (confirm == 0) {
			System.exit(0);
		}		
	}

	/** Set main frame visible. */
	public void display() {
		pack();
		setVisible(true);
	}
	
	/** @return Returns global parameters. */
	public static ValidateInput getGlobalParameters() {
		return MainFrame.parameters;
	}
	
}
