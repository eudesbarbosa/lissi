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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.miginfocom.swing.MigLayout;

import dk.sdu.imada.methods.statistics.RandomForest;


/**
 * Class configures the objects associated with the Random Forest 
 * Panel. It contains a JFormattedTextField for each of the RF 
 * parameters, namely: k-fold, number of runs, number of trees
 * per run.
 *
 * @author Eudes Barbosa
 */
public class RandomForestPanel extends RandomForestInstructions {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = 5617069885330520000L;

	private static final Logger logger = LogManager.getLogger(RandomForestPanel.class.getName());

	/** Button to run process. */
	protected JButton jButtonRun;
	
	/**/
	//protected JButton loadFilesButton;
	//protected JFileChooser jFileChooser;
	
	/** Formatted Text Field for k-fold cross validation. */
	protected JFormattedTextField jTFkfold;
	
	/** Formatted Text Field for number of runs. */
	protected JFormattedTextField jTFRuns;
	
	/** Formatted Text Field for number of trees. */
	protected JFormattedTextField jTFTrees;
	
	/** Label associated with k-fold cross validation. */
	protected JLabel jLabelKfold;
	
	/** Label associated with number of runs. */
	protected JLabel jLabelRuns;
	
	/** Label associated with number of trees. */
	protected JLabel jLabelTrees;
	
	/** Help label associated with k-fold cross validation. */
	protected JLabel kfoldHelpLabel;
	
	/** Help label associated with number of trees. */
	protected JLabel treesHelpLabel;
	
	/** Help label associated with number of runs. */
	protected JLabel runsHelpLabel;
	
	/** Path to indicator matrix. */
	protected String indicatorMatrix = ""; 
	// Matrix test = "/home/eudesbarbosa/LISSI/Saved/IndicatorMatrix.csv";

	//------  Declaration end  ------//

	/** Configures the objects associated with the Random forest panel. */
	public RandomForestPanel(){
		// Initialize components
		initComponents();
		// Configure layout
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		// Configure panel	
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		setLayout(new MigLayout());
		// Configure panel
		add(jLabelKfold,"split 3, span, align left, gaptop 2");
		add(jTFkfold,"span");
		add(kfoldHelpLabel,"span,wrap");
		//
		add(jLabelRuns,"split 3, gaptop 5");
		add(jTFRuns,"span");
		add(runsHelpLabel,"span, wrap");
		//
		add(jLabelTrees,"split 4, gaptop 5");
		add(jTFTrees,"span");
		add(treesHelpLabel,"span");
		//add(loadFilesButton,"span, align right");
		//add(jButtonRun, "align right, gaptop 5");
	}

	@Override
	protected void initComponents() {
		// Configure labels
		jLabelKfold = new JLabel("Set k-fold: ");
		jLabelKfold.setFont(LabelFontPlain);
		jLabelRuns = new JLabel("Number of runs: ");
		jLabelRuns.setFont(LabelFontPlain);
		jLabelTrees = new JLabel("Number of trees per run: ");
		jLabelTrees.setFont(LabelFontPlain);

		// Configure FormattedTextField
		jTFkfold = new CreateFormattedTextField(1,10).getNewFormattedTextField();
		jTFkfold.setColumns(3);
		jTFkfold.setText("5");
		jTFkfold.setEditable(true);
		//
		jTFRuns = new CreateFormattedTextField(1,20).getNewFormattedTextField();
		jTFRuns.setColumns(3);
		jTFRuns.setText("10");
		jTFRuns.setEditable(true);
		//
		jTFTrees = new CreateFormattedTextField(1,10000).getNewFormattedTextField();
		jTFTrees.setColumns(5);
		jTFTrees.setText("500");
		jTFTrees.setEditable(true);	

		// Configure help buttons
		kfoldHelpLabel = createHelpLabel(null, KFOLD_INST, PACK_MANUAL, PAPER_ONLINE);
		runsHelpLabel = createHelpLabel(null, NRUNS_INST, PACK_MANUAL, PAPER_ONLINE);
		treesHelpLabel = createHelpLabel(null, NTREES_INST, PACK_MANUAL, PAPER_ONLINE);

		// Configure Run button
		jButtonRun = new CreateRunButton();

		
		/*
		// Configure Load files button
		// Configure file chooser
		jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		
		// Configure Load files button
		loadFilesButton = new CreateLoadFilesButton();
		loadFilesButton.setToolTipText("Load Indicator matrix");
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
						userIndicatorMatrix = file.getAbsolutePath();
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
						me.getClickCount() == 1 && !userIndicatorMatrix.equals("")) {
					// Change load button icon
					try {
						((CreateLoadFilesButton) loadFilesButton).changeLoadIcon("Load");
						repaint();
					} catch (Exception e) {
						logger.debug("It wasn't possible to change load file button icon.");
						e.printStackTrace();
					}
					// Ignores previously provided path
					userIndicatorMatrix = "";
				}				
			}
			// Ignore...
			@Override
			public void mouseEntered(MouseEvent me) {
				// Do nothing				
			}
			@Override
			public void mouseExited(MouseEvent me) {
				// Do nothing				
			}
			@Override
			public void mousePressed(MouseEvent me) {
				// Do nothing				
			}
			@Override
			public void mouseReleased(MouseEvent me) {
				// Do nothing				
			}
		}); */
	}

	@Override
	public void resetValues() {
		// Set default values
		jTFkfold.setText("5");
		jTFRuns.setText("10");
		jTFTrees.setText("1000");
	}

	/**
	 * @return		Returns Random Forest object based 
	 * 				on user's parameters.
	 */
	public RandomForest getParameters() {
		RandomForest rf = new RandomForest();
		// Test if threshold was provided	
		int kfold;
		int runs;
		int trees;

		try {			
			// Get parameters
			kfold = Integer.parseInt(jTFkfold.getValue().toString());	
			runs = Integer.parseInt(jTFRuns.getValue().toString());	
			trees = Integer.parseInt(jTFTrees.getValue().toString());	

			// Configure RF object
			rf.setKfold(kfold);
			rf.setRuns(runs);
			rf.setTrees(trees);
			rf.setUserMatrix(indicatorMatrix);

		} catch (Exception e) {
			String errorMessage = "Please assign a value for all parameters. ";
			JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
			// Log
			logger.warn("Attempting to run Machine Learning methods without assinging all parameters. ");
		}
		//
		return rf;
	}

	/**
	 * InnerClass configures Run button. It will call Random Forest for
	 * classification and feature selection using the given parameters.
	 */
	protected class CreateRunButton extends JButton {

		private static final long serialVersionUID = -1066353881323061324L;

		/**
		 *  Sole constructor
		 */
		public CreateRunButton() {
			// Set label
			setText("Run");
			setFont(ButtonFont);
			setToolTipText("Will start call Random Forest (classification and feature selection).");
			// Add action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Test if threshold was provided	
					int kfold;
					int runs;
					int trees;
					try {
						// Get parameters
						kfold = Integer.parseInt(jTFkfold.getValue().toString());	
						runs = Integer.parseInt(jTFRuns.getValue().toString());	
						trees = Integer.parseInt(jTFTrees.getValue().toString());	

						// Log
						logger.info("Calling Random Forest with the following parameter: "+
								"K-fold = "+ kfold +"; \n" +
								"Number of runs = "+ runs +"; \n" +
								"Number of trees per run = "+ trees +"; \n");


						// TODO: Essa porra toda que é a Bahêa. Mim beija.


					} catch (Exception e) {
						String errorMessage = "Please assign a value for all parameters. ";
						JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
						// Log
						logger.warn("Attempting to run Machine Learning methods without assinging all parameters. ");
					}
				}
			});    

		}

	}

}


