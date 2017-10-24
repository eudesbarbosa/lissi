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
package dk.sdu.imada.gui.gecko;


import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.gui.genome.GenomeSelection;
import dk.sdu.imada.methods.gecko.Gecko;

import net.miginfocom.swing.MigLayout;


/**
 * Class configures the objects associated 
 * with the Gecko panel. 
 * 
 * @author Eudes Barbosa
 */
public class GeckoPanel extends GeckoInstructions {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = 8818235581215388060L;

	private static final Logger logger = LogManager.getLogger(GeckoPanel.class.getName());

	/** Checkbox - with or without island detection. */
	protected JCheckBox checkbox;
	
	/** Label associated with distance between islands (Indels). */
	protected JLabel jLabelDistance;
	
	/** Label associated with the size of the islands. */
	protected JLabel jLabelSize;
	
	/** Label associated with the minimum number of genomes. */
	protected JLabel jLabelGenomes;
	
	/** 
	 * Formatted Text Field associated with distance 
	 * between islands (indels). 
	 */
	protected JFormattedTextField jTFDistance;
	
	/** Formatted Text Field associated with size. */
	protected JFormattedTextField jTFSize;
	
	/** 
	 * Formatted Text Field associated with the 
	 * minimum number of genomes. 
	 */
	protected JFormattedTextField jTFGenomes;
	
	/** 
	 * Help label associated with distance between 
	 * islands (indels).
	 */
	protected JLabel distanceHelpLabel;
	
	/** Help associated with size of island. */
	protected JLabel sizeHelpLabel;
	
	/** Help associated with minimum number of genomes. */
	protected JLabel genomesHelpLabel;
	
	/** Filer chooser - select previously generated results.*/
	protected JFileChooser jFileChooser;
	
	/** Load button - load previously generated results. */
	protected JButton loadFilesButton;
	
	/** Path to Gecko file provided by the user. */
	protected String userGeckoFile  = "";
	//test gecko file = "/home/eudes/LISSI/testDir/testSet/GeckoOut.gck"; 

	//------  Declaration end  ------//


	/** Configures the objects associated with the Gecko panel. */
	public GeckoPanel() {
		// Create and configure panel components
		initComponents();
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		// Configure panel	
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		setLayout(new MigLayout());
		// Configure panel
		add(jLabelDistance, "split 3, span, align left, gaptop 2");
		add(jTFDistance,"span");
		add(distanceHelpLabel,"span, wrap");
		//
		add(jLabelSize, "split 3, gaptop 5");
		add(jTFSize, "span");
		add(sizeHelpLabel,"span, wrap");
		//
		add(jLabelGenomes, "split 3, gaptop 5");
		add(jTFGenomes, "span");
		add(genomesHelpLabel,"span,wrap");
		//
		add(checkbox, "split 2, span, gaptop 5");
		add(loadFilesButton, "span, align right");
	}

	@Override
	protected void initComponents() {
		// Configure Checkbox
		checkbox = new JCheckBox("Use Gecko (search for islands).");
		checkbox.setMnemonic(KeyEvent.VK_C); 
		checkbox.setSelected(true);
		checkbox.setFont(LabelFontPlain);
		checkbox.setEnabled(true);

		// Configure labels
		jLabelDistance = new JLabel("Maximum distance: ");
		jLabelDistance.setFont(LabelFontPlain);
		//
		jLabelSize = new JLabel("Minimum island size: ");
		jLabelSize.setFont(LabelFontPlain);
		//
		jLabelGenomes = new JLabel("Minimum covered genomes: ");
		jLabelGenomes.setFont(LabelFontPlain);

		// Configure FormattedTextField
		jTFDistance = new CreateFormattedTextField(0,10).getNewFormattedTextField();
		jTFDistance.setColumns(3);
		jTFDistance.setText("2");
		jTFDistance.setEditable(true);
		//
		jTFSize = new CreateFormattedTextField(2,250).getNewFormattedTextField();
		jTFSize.setColumns(4);
		jTFSize.setText("8");
		jTFSize.setEditable(true);
		//
		jTFGenomes = new CreateFormattedTextField(2,10000).getNewFormattedTextField();
		jTFGenomes.setColumns(6);
		jTFGenomes.setText("2");
		jTFGenomes.setEditable(true);

		// Configure help labels
		distanceHelpLabel = createHelpLabel(null, DISTANCE_INST, ONLINE_MANUAL, ONLINE_PAPER);
		sizeHelpLabel = createHelpLabel(null, SIZE_INST, ONLINE_MANUAL, ONLINE_PAPER);
		genomesHelpLabel = createHelpLabel(null, COVERAGE_INST, ONLINE_MANUAL, ONLINE_PAPER);

		// Configure file chooser
		jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Configure Load files button
		loadFilesButton = new CreateLoadFilesButton();
		loadFilesButton.setToolTipText("Load Gecko file");
		loadFilesButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent me) {
				// One click to open select file view
				// Two clicks to remove and selection
				if (me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 1) {
					int returnVal = jFileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = jFileChooser.getSelectedFile();
						// Set path
						userGeckoFile = file.getAbsolutePath();
						// Change load button icon
						try {
							((CreateLoadFilesButton) loadFilesButton).changeLoadIcon("Loaded");
							repaint();
						} catch (Exception e) {
							logger.debug("It wasn't possible to change load file button icon.");
							e.printStackTrace();
						}
					}
				} else if (me.getButton() == MouseEvent.BUTTON3 && 
						me.getClickCount() == 1 && !userGeckoFile.equals("")) {
					// Change load button icon
					try {
						((CreateLoadFilesButton) loadFilesButton).changeLoadIcon("Load");
						repaint();
					} catch (Exception e) {
						logger.debug("It wasn't possible to change load file button icon.");
						e.printStackTrace();
					}
					// Ignores previously provided path
					userGeckoFile = "";
				}				
			}
			@Override
			public void mouseEntered(MouseEvent me)  {}
			@Override
			public void mouseExited(MouseEvent me)   {}
			@Override
			public void mousePressed(MouseEvent me)  {}
			@Override
			public void mouseReleased(MouseEvent me) {}
		}); //end-mouse click

	}

	@Override
	public void resetValues() {
		// Set default values
		jTFDistance.setText("3");
		jTFSize.setText("8");
		jTFGenomes.setText("2");
	}

	/** @return Returns	Gecko object based on user's parameters. */
	public Gecko getParameters() {
		// Initialize variables
		Gecko gecko = new Gecko();		
		int distance;
		int size;
		int genomes = -1;
		int total = -1;

		// Test if all values were provided	
		try {			
			// Get parameters
			distance = Integer.parseInt(jTFDistance.getValue().toString());	
			size = Integer.parseInt(jTFSize.getValue().toString());	
			genomes = Integer.parseInt(jTFGenomes.getValue().toString());	

			// Verify if requested genome 
			// number makes sense
			total = getGenomesTotal();
			if (genomes > total) {
				throw new Exception();
			}			

			// Configure Gecko object
			gecko.setDistance(distance);
			gecko.setSize(size);
			gecko.setGenomes(genomes);
			gecko.setUserGeckoFile(userGeckoFile);

		} catch (Exception e) {
			if (genomes > total) {
				String errorMessage = "Requested minimum covered genomes is bigger than the " +
						"number of genomes under analysis. ";
				JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
				// Log
				logger.warn("Attempting to run Gecko with bad parameters. ");

			} else {		
				String errorMessage = "Please assign a value for all parameters. ";
				JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
				// Log
				logger.warn("Attempting to run Machine Learning methods without assinging all parameters. ");
			}
		}
		//
		return gecko;
	}

	/** @return	Returns number of chromosomes under	analysis. */
	private int getGenomesTotal() {
		int total = GenomeSelection.getLifestyleOneTable().getRowCount() +
				GenomeSelection.getLifestyleTwoTable().getRowCount();
		//
		return total;
	}

	/**
	 * @return	Checks if user wants to include Gecko in 
	 * the analysis pipeline. Returns False if BFG pipeline, 
	 * True if LiSSI's.	
	 */
	public boolean useGecko(){
		return checkbox.isSelected();

	}
}