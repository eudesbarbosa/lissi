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

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Class creates a generic Cancel Button. It allows the stop of each activity.
 * <br><br>
 * 'Stop icon' <br>
 * - Artist: Matte White <br>
 * - Link: http://icons.mysitemyway.com/matte-white-square-icons-signs/ <br>
 * - License: Free (http://icons.mysitemyway.com/terms-of-use/)
 * 
 * @author Eudes Barbosa
 */
public class JButtonCancel extends JButton {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = -8462853789469830556L;
	
	//------  Declaration end  ------//


	/** Creates a generic Cancel Button.*/
	public JButtonCancel() {
		super();
		// Configure
		setEnabled(false);
		setToolTipText("Cancels the current process.");
		//	
		BufferedImage img;
		try {
			// Set enable icon
			img = ImageIO.read(getClass().getResource("/images/stop_grey.png"));
			BufferedImage ret = new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
			ret.getGraphics().drawImage(img,0,0,50,50,null);
			setIcon(new ImageIcon(ret));

			// Set disable icon
			img = ImageIO.read(getClass().getResource("/images/stop_grey_disable.png"));
			ret = new BufferedImage(50,50,BufferedImage.TYPE_INT_ARGB);
			ret.getGraphics().drawImage(img,0,0,50,50,null);
			setDisabledIcon(new ImageIcon(ret));

			// Configure appearance 
			setContentAreaFilled(false);
			setBorderPainted(true); 
			setFocusPainted(true); 
			setOpaque(false);

		} catch (IOException e) {
			System.out.println("It was not possible to load Transitivity Clustering icon.");
			e.printStackTrace();
		}
	}

}
