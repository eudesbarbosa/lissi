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

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.gui.ClickAwayDialog;


/**
 * Class creates a visualization for the parameters 
 * instructions. 
 * 	 
 * @author Eudes Barbosa	
 */
public class ViewHelp implements MouseListener {
	
	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(ViewHelp.class);	

	/** Component's parent JFrame. */
	protected JFrame parent;

	/** Instruction. */
	protected String instruction = null;

	/** Online manual (http). */
	protected String manual = null;

	/** Paper access (http). */
	protected String paper = null;

	/** Button font. */
	protected final Font ButtonFont = new Font("Tahoma", Font.PLAIN, 10);
	
	//------  Declaration end  ------//



	/**
	 * Creates a visualization for the parameters 
	 * instructions.
	 * 
	 * @param owner			Owner JFrame (can be null).
	 * @param instruction	Instructions on how to set the 
	 * 						parameter.
	 * @param manual		Link to online manual (can be null).
	 * @param paper			Link to online paper (can be null).
	 */
	public ViewHelp(JFrame owner, String instruction, String manual, String paper) {
		this.parent = owner;
		this.instruction = Objects.requireNonNull(instruction, "Instruction cannot be null");
		this.manual = manual;
		this.paper = paper;
	}

	@Override
	public void mouseClicked(MouseEvent me) {
		//
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		//
		if (me.getButton() == MouseEvent.BUTTON1) {
			//
			ClickAwayDialog dialog = new ClickAwayDialog(parent, new Object());
			dialog.setPreferredSize(new Dimension(120, dialog.getHeight()+35));
			dialog.setMinimumSize(new Dimension(120, dialog.getHeight()+35));
			//--- Instructions ---//
			StringBuilder sb = new StringBuilder(64);
			sb.append(instruction);
			panel.add(new JLabel(sb.toString()), "al center center, top, wrap");
			//--- Online Manual ---//
			if (manual != null) {
				JButton manualButton = new JButtonOpenUrl("Manual", manual);
				panel.add(manualButton, "split 2, grow, al center center");
			}
			//--- Online Paper ---//
			if (paper != null) {
				JButton paperButton = new JButtonOpenUrl("Paper", paper);
				panel.add(paperButton, "grow, al center center");
			}
			dialog.add(panel);
			int x = me.getXOnScreen();
			int y = me.getYOnScreen();
			dialog.setLocation(x,y);
			dialog.setVisible(true);
		}
		me.consume();		
	}

	@Override
	public void mouseEntered(MouseEvent me) { }

	@Override
	public void mouseExited(MouseEvent me) { }

	@Override
	public void mousePressed(MouseEvent me) { }

	@Override
	public void mouseReleased(MouseEvent me) { }	


	/**
	 * Inner class add action to help button. It opens 
	 * and URL with specific instructions on how to use 
	 * a given parameter. 
	 */
	protected class JButtonOpenUrl extends JButton {

		private static final long serialVersionUID = 1L;

		/** 
		 * Specifies which URL shall be open when 
		 * button is pressed by the user.
		 *
		 * @param url		Path to HTML file that shall  
		 * 					be open.
		 */
		public JButtonOpenUrl(String label, final String url) {
			// Set label
			setText(label);
			setFont(ButtonFont);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					open(url);
				}
			});
		}

		/**
		 * Opens URL.
		 * @param uri	The URL that shall be open.
		 */
		private void open(String url) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(URI.create(url));
				} catch (IOException e) { 
					logger.debug("Problem while loading HTML.");
					e.printStackTrace();
				}
			} else {
				logger.debug("Browser option is not available.");
				// Send error message
				String errorMessage = "Browser is not available...";
				JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
