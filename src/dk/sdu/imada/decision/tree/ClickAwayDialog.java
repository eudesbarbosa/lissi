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
package dk.sdu.imada.decision.tree;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Class extends JDialog mainly to add a new functionality: 
 * it will close the visualization if the window loses focus. 
 * 
 * @author Eudes Barbosa
 */
public class ClickAwayDialog extends JDialog {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = -1443896392647591673L;
	
	//------  Declaration end  ------//


	/**
	 * Creates a JDialog will close the visualization if the window 
	 * loses focus. 
	 * 
	 * @param owner		JFrame owner.
	 * @param v			Object.
	 */
	public ClickAwayDialog(final JFrame owner, Object v) {
		super(owner);
		// Set basic dimension 
		this.setPreferredSize(new Dimension(200, 75));
		this.setMinimumSize(new Dimension(140, 40));
		//
		this.setUndecorated(true);
		this.pack();
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		// Set location
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height/8);
		//Point newLocation = new Point(middle.x - (owner.getWidth() / 2), 
		//                              middle.y - (owner.getHeight() / 2));
		this.setLocation(middle);
		this.setAlwaysOnTop(true);
		this.addWindowFocusListener(new WindowFocusListener() {

			/** Closes visualization if window loses focus. */
			public void windowLostFocus(WindowEvent e) { 
				ClickAwayDialog.this.setVisible(false);
			}

			public void windowGainedFocus(WindowEvent e) { }

		});
	}
}