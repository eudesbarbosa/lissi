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
package dk.sdu.imada.gui.statistics;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.miginfocom.swing.MigLayout;

/**
 * Class creates a panel with the Random Forest 
 * plots. It also adds an additional label with 
 * the location of the graph.
 * 
 * @author Eudes Barbosa
 */
public class RandomForestROCPlot extends JPanel {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = -7368383337810207399L;	

	private static final Logger logger = LogManager.getLogger(RandomForestROCPlot.class.getName());

	/** Label will be used to display the ROC plot. */
	protected JLabel plot = new JLabel();
	
	/** Label will be used to indicate local path to figure (png). */
	protected JLabel help;
	
	//------  Declaration end  ------//


	/**
	 * Creates a panel with the Random Forest 
	 * plots. It also adds an aditional label with 
	 * the location of the graph.
	 * 
	 * @param path		Path to plot (png).
	 * @param title		Panel title.
	 */
	public RandomForestROCPlot(String path, String title) {
		//
		initComponents(path);
		configureLayout(title);			
	}	

	/**
	 * Configures the layout of the components
	 * within the panel.
	 * @param title		Panel title.
	 */
	protected void configureLayout(String title) {
		// Configure panel
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder(title));
		setBackground(Color.WHITE);
		// Add
		add(plot,"split 2, top, align center, grow");
		add(help, "span, top, align right");			
	}

	/** Initializes all objects associated with the panel. */
	protected void initComponents(final String path) {
		// Load graph
		BufferedImage img;			
		try {
			File imgFile = new File(path);
			img = ImageIO.read(imgFile);
			plot.setIcon(new ImageIcon(img));	
			logger.debug("Path to figure: " + imgFile.getAbsolutePath().toString());
		} catch (Throwable e) {
			logger.error("Problem loading figure: " + path);
			//e.printStackTrace();
		}

		// Create label with path
		String text = "<html><FONT color=\"#000099\">(?)</FONT></html>";
		help = new JLabel(text);
		help.addMouseListener(new MouseAdapter() {				
			@Override
			public void mouseClicked(MouseEvent me) {
				super.mouseClicked(me);
				// Send error message
				String msg = "Graph was saved in: " + path;
				JOptionPane.showMessageDialog(null, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
				return;						
			}
		});
	}
}
