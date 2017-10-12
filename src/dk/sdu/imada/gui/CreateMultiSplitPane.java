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


import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



/**
 * Class configures a MultiSplitPane. It is divided into three panels, holding: 
 * an Accordion Menu (left side), a Layered Pane (right side), and a panel 
 * that contains the progress bar (bottom). 
 * 
 * @author Eudes Barbosa
 */
public class CreateMultiSplitPane extends MultiSplitPane {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = 581852896362196585L;

	private static final Logger logger = LogManager.getLogger(CreateMultiSplitPane.class.getName());

	/** 
	 * JPanel using card layout. All future results 
	 * will be added to this panel.
	 */
	protected static JPanel layeredPane;
	
	//------  Declaration end  ------//

	
	/**
	 * Creates and configures a MultiSplitPane. It is divided into three panels, 
	 * holding:  an Accordion Menu (left side), a Layered Pane (right side), and  
	 * a panel that contains the progress bar (bottom). 
	 */
	public CreateMultiSplitPane() {
		super();
		// Configure MultiSplitPane
		String layoutDef = "(COLUMN (ROW (LEAF name=left weight=0.2) (LEAF name=right weight=0.8)) bottom)";
		MultiSplitLayout.Node modelRoot = MultiSplitLayout.parseModel(layoutDef);
		//setDividerSize(5);
		getMultiSplitLayout().setModel(modelRoot);

		// Create and add Accordion Menu (left)
		JPanel menu = new CreateAccordionMenu();
		menu.setBorder(BorderFactory.createTitledBorder(""));
		//menu.setPreferredSize(new Dimension(200, 800));
		//
		add(menu, "left");
		revalidate();

		// Create and add LayeredPanel (Right)
		layeredPane = new JPanel(new CardLayout());
		//layeredPane.setBorder(BorderFactory.createTitledBorder(""));
		layeredPane.add(new WizardPanel(), "Wizard");
		layeredPane.setPreferredSize(new Dimension(600, 750));
		//
		add(new JScrollPane(layeredPane), "right");
		revalidate();

		// Create and add LayeredPnel Progress Panel (Bottom)
		JPanel progressPane = new ProgressPanel();
		progressPane.setBorder(BorderFactory.createTitledBorder(""));
		//
		add(progressPane, "bottom");
		revalidate();
	}

	/**
	 * Sets which of the panels inside the LayeredPane should be visible.
	 * @param panel			The name of the panel that should visible. Names
	 *  					where defined when panels where created and added 
	 * 						to LayeredPane. 
	 */
	public static void setPanel(String panel) {
		logger.debug("Switching to panel : " + panel);
		CardLayout cardLayout = (CardLayout) layeredPane.getLayout();
		cardLayout.show(layeredPane, panel);
	}

	/**
	 * Adds a new panel to currently in used LayeredPane.
	 * 
	 * @param reference			An object expressing layout contraints 
	 * 							for the old panel (description).
	 * 
	 * @param panel				The new panel to substitute the recently 
	 * 							deleted one.
	 */
	public static void updateLayeredPane(JPanel panel, String reference) {
		/*
		JPanel newPanel = new JPanel();
		newPanel.setLayout(new MigLayout());
		JScrollPane scroll = new JScrollPane(panel);
		newPanel.add(scroll, "top, align left, grow, push, wrap");
		*/
		logger.debug("Creating panel : " + reference);
		layeredPane.add(panel, reference);
		layeredPane.revalidate();
		layeredPane.repaint();
	}
}
