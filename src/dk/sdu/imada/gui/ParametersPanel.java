/**
 * 
 */
package dk.sdu.imada.gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dk.sdu.imada.gui.gecko.GeckoPanel;
import dk.sdu.imada.gui.statistics.RandomForestPanel;
import dk.sdu.imada.gui.transclust.TransClustPanel;
import dk.sdu.imada.methods.CommomStaticMethods;
import dk.sdu.imada.methods.PipelineSync;
import dk.sdu.imada.methods.gecko.Gecko;
import dk.sdu.imada.methods.statistics.RandomForest;
import dk.sdu.imada.methods.transclust.TransClust;


import net.miginfocom.swing.MigLayout;

/**
 * Class configures panels associated with the differerent 
 * modules of the application: Evolutionary Sequence Analysis 
 * (Transitivity Clustering); Island Detection (Gecko); and 
 * Statistical Learning Methods (Random Forest). It allows 
 * user to specify all parameters necessary to run LiSSI.
 * <br><br>
 * 'Question mark' icon <br>
 * Artist: Chromatix <br>
 * Link: http://www.iconarchive.com/show/keyboard-keys-icons-by-chromatix/ <br>
 * License: CC Attribution-Noncommercial-No Derivate 4.0 <br>
 * 		(http://creativecommons.org/licenses/by-nc-nd/4.0/)  <br>
 *<br>
 * 'Reset' and 'Start' icons <br>
 * Artist: Icons-Land (Available for custom work) <br>
 * Link: http://www.iconarchive.com/show/vista-multimedia-icons-by-icons-land/ <br>
 * License: Free for non-commercial use.
 * 
 * @author Eudes Barbosa (eudes@imada.sdu.dk)
 */
final public class ParametersPanel extends AbstractParameterPanel {

	private static final long serialVersionUID = -959500439244225732L;

	// Variable declaration
	protected static JPanel transclust;
	protected static JPanel gecko;
	protected static JPanel randomforest;
	protected static JButton reset;
	protected static JButton start;
	protected JButton jButtonFolder;
	protected JFileChooser jFileChooser;
	protected JButton help;
	protected static JComboBox<String> jComboTempDir;
	protected static JTextField dirTextField;
	protected static PipelineSync run = null;
	protected String tmpDir;
	protected String sysTempDir;
	// Declaration end

	/**
	 * Configures panels associated with the differerent 
	 * modules of the application: Evolutionary Sequence Analysis 
	 * (Transitivity Clustering); Island Detection (Gecko); and 
	 * Statistical Learning Methods (Random Forest). It allows 
	 * user to specify all parameters necessary to run LiSSI.
	 */
	public ParametersPanel(){
		// Set user's temporary directory		
		String s = CommomStaticMethods.getLocalDirectory();
		if (s.substring(s.length() - 1).equals(File.separator)) {
			this.tmpDir = CommomStaticMethods.getLocalDirectory()
					.concat("tmp");
		} else {
			this.tmpDir = CommomStaticMethods.getLocalDirectory()
					.concat(File.separator).concat("tmp");
		}		
		// Set System default temporary directory
		sysTempDir = System.getProperty("java.io.tmpdir");

		// Create and configure panel components
		initComponents();
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		// Configure main panel
		setLayout(new MigLayout());	
		setPreferredSize(PanelDimension);
		// Add panels
		add(transclust,"width :700:, wrap");
		add(gecko, "width :700:, wrap");
		add(randomforest, "width :700:, wrap");
		//add(checkbox, "wrap, gapleft 20");
		add(jComboTempDir, "split 4, gapleft 10");
		add(dirTextField, "span, gapleft 5");
		add(jButtonFolder,"span");
		//add(help, "split 3, span, align left");
		add(reset, "span, gapleft 60, align right");
		//add(start, "span, align right");
	}

	@Override
	protected void initComponents() {

		// Configure Transitivity Clustering panel
		transclust = new TransClustPanel();
		transclust.setBorder(BorderFactory.createTitledBorder("Transitivity Clustering"));

		// Configure Gecko panel
		gecko = new GeckoPanel();
		gecko.setBorder(BorderFactory.createTitledBorder("Gecko"));

		// Configure Random Forest panel
		randomforest = new RandomForestPanel();
		randomforest.setBorder(BorderFactory.createTitledBorder("Random Forest"));

		// Configure Reset button
		reset = new CreateResetButton();
		reset.setEnabled(true);

		// Configure Start button
		start = new CreateStartButton();
		start.setEnabled(true);


		/* Configure Help button
		help = new CreateHelpButton();
		help.setEnabled(true);
		 */

		// Configure JTextField associated with temporary dir
		dirTextField = new JTextField(30);
		dirTextField.setEnabled(false);
		dirTextField.setText(tmpDir);

		// Create the combo box
		final String[] filterStrings = {"User Default", "System Default", "Other" };
		jComboTempDir = new JComboBox<String>(filterStrings);
		jComboTempDir.setSelectedIndex(0);
		// Add action
		jComboTempDir.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				if (jComboTempDir.getSelectedItem().equals("User Default")) {
					jButtonFolder.setEnabled(false);
					dirTextField.setEnabled(false);
					dirTextField.setText(tmpDir);
				} else if (jComboTempDir.getSelectedItem().equals("System Default")) {
					jButtonFolder.setEnabled(false);
					dirTextField.setEnabled(false);
					dirTextField.setText(sysTempDir);
				} else if (jComboTempDir.getSelectedItem().equals("Other")) {
					jButtonFolder.setEnabled(true);
					dirTextField.setEnabled(true);
				}
			}
		});

		//Configure a file chooser
		jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		// Configure Directory button
		jButtonFolder = new JButton();
		jButtonFolder.setEnabled(false);
		jButtonFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/generic_folder_grey.png")));
		jButtonFolder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int returnVal = jFileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = jFileChooser.getSelectedFile();
					// Set textfield
					dirTextField.setText(file.getAbsolutePath());
				}
			}
		});
	}

	@Override
	public void resetValues() {
		// Reset to default values
		((TransClustPanel) transclust).resetValues();
		((GeckoPanel) gecko).resetValues();
		((RandomForestPanel) randomforest).resetValues();
	}

	/** Presses Run button. */
	public void pressConfirmButton() {
		start.doClick();
	}

	/**
	 * Returns an error message and enable panel's buttons, 
	 * in case the pipeline crashes while being executed. 
	 * Allows user to try again with different parameters or 
	 * correcting any possible error.
	 */
	public static void pipelineError() {
		// Choose Parameters panel
		CreateMultiSplitPane.setPanel("Parameters");		
		// Enable buttons
		enableButtons(true);
		// Send error message
		String errorMessage = "An error occurred while running LiSSI.";
		JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Enables or disables the buttons associated with 
	 * running the analysis pipeline, i.e., Reset and Start. 
	 * @param b
	 */
	public static void enableButtons(boolean b) {
		reset.setEnabled(b);
		start.setEnabled(b);
		jComboTempDir.setEnabled(b);
		dirTextField.setEditable(b);		
		//checkbox.setEnabled(b);
	}

	/**
	 * Enables or disables all panels, i.e., 
	 * TransClust, Gecko and Random Forest.
	 * 
	 * @param b		True enables panels. False 
	 * 				disables.
	 */
	public static void enablePanels(boolean b) {	
		((TransClustPanel) transclust).enableComponents(b);
		((AbstractParameterPanel) gecko).enableComponents(b);
		((AbstractParameterPanel) randomforest).enableComponents(b);		
	}

	/** @return	Path to the temporary directory. */
	public static String getTempDir() {
		return dirTextField.getText();
	}

	/**
	 * Inner-class creates and configures a Reset Button. It 
	 * will restore all parameters to their default values.
	 */
	protected class CreateResetButton extends JButton {

		private static final long serialVersionUID = 2710167758558188804L;

		/**
		 * Creates and configures a Reset Button. It will restore 
		 * all parameters to their default values.
		 */
		public CreateResetButton(){
			super();
			// Set enable icon
			setIcon(new ImageIcon(getClass().getResource("/images/reset-icon.png")));
			// Configure appearance 
			setContentAreaFilled(false);
			setBorderPainted(true); 
			setFocusPainted(true); 
			setOpaque(false);
			// Set tip
			setToolTipText("Return parameters to default values.");

			// Add action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Use default values
					resetValues();					
				}
			});
		}
	}

	/**
	 * Inner-class creates and configures a Start Button. It 
	 * will start pipeline with user's parameters.
	 */
	protected class CreateStartButton extends JButton {

		private static final long serialVersionUID = -5287819202845870486L;

		/**
		 * Creates and configures a Start Button. It 
		 * will start pipeline with user's parameters.
		 */
		public CreateStartButton() {
			super();
			// Set enable icon
			//setIcon(new ImageIcon(getClass().getResource("/images/start-icon.png")));
			// Configure icon appearance 
			setContentAreaFilled(false);
			setBorderPainted(true); 
			setFocusPainted(true); 
			setOpaque(false);
			// Set tip
			setToolTipText("Run application.");
			// Add action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {					
					// Check if paths are valid
					File l1 = new File(dirTextField.getText());
					if (jComboTempDir.getSelectedItem().equals("User Default")) {
						l1.mkdirs();
					} else {						
						if (!l1.isDirectory()) {
							// Send error message
							String errorMessage = "Please select a temporary directory.";
							sendWarningMsg(errorMessage);	
						}					
					}
					// Check if Gecko should be included
					boolean check = ((GeckoPanel) gecko).useGecko();
					// Start pipeline...
					if (check == true) { //LiSSI pipeline						
						TransClust tc = ((TransClustPanel) transclust).getParameters();
						Gecko g = ((GeckoPanel) gecko).getParameters();
						RandomForest rf = ((RandomForestPanel) randomforest).getParameters();
						/* Check if every module is necessary
						// If user already provided some files */
						if (!g.getUserGeckoFile().equals("")) {
							tc.setUserBlastFile("Not provided.");
							if (tc.getUserTransClustFile().equals("")) {
								String msg = "Without a cluster file as a reference it " +
										"will not be possible to indicate if the organisms " +
										"posses the clusters.";
								sendWarningMsg(msg);
								//
								return;
							}	
						}
						if (!rf.getUserMatrix().equals("")) {
							tc.setUserBlastFile("Not provided.");
							if (tc.getUserTransClustFile().equals("")) {
								String msg = "Without a cluster file as a reference it " +
										"will not be possible to indicate if the organisms " +
										"posses the clusters.";
								sendWarningMsg(msg);
								//
								return;
							}	
						}
						//							
						int confirm = JOptionPane.showOptionDialog(null, "Any previous results will be deleted. Continue?",
								"ALERT", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
						if (confirm == 0) {
							enableButtons(false);
							enablePanels(false);
							run = new PipelineSync(tc, g, rf);
							run.exec();
						} else {
							return;
						}

					} else { //BFG pipeline
						TransClust tc = ((TransClustPanel) transclust).getParameters();
						RandomForest rf = ((RandomForestPanel) randomforest).getParameters();
						/* Check if every module is necessary
						// If user already provided some files */						
						if (!rf.getUserMatrix().equals("")) {
							if (tc.getUserTransClustFile().equals("")) {
								String msg = "Without a cluster file as a reference it " +
										"will not be possible to indicate if the organisms " +
										"posses the clusters.";
								sendWarningMsg(msg);
								//
								return;
							}							
						}
						//
						/*int dialogResult = JOptionPane.showConfirmDialog (null, "Previous results " +
								"will be deleted. Continue?");
						if (dialogResult == JOptionPane.YES_OPTION) { */
						int confirm = JOptionPane.showOptionDialog(null, "Any previous results will be deleted. Continue?",
								"ALERT", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
						if (confirm == 0) {
							enableButtons(false);
							enablePanels(false);
							run = new PipelineSync(tc, rf);
							run.exec();
						} else {
							return;
						} 
					}
				}
			});
		}
	}
	/**
	 * Inner-class creates and configures a Help Button. It 
	 * will open the browser with the extended documentation.
	 */
	protected class CreateHelpButton extends JButton {

		private static final long serialVersionUID = 7052252389818854847L;

		/**
		 * Creates and configures a Help Button. It 
		 * will open the browser with the extended documentation.
		 */
		public CreateHelpButton() {
			super();

			// Set enable icon
			setIcon(new ImageIcon(getClass().getResource("/images/questionmark_icon.png")));			

			// Configure appearance 
			setContentAreaFilled(false);
			setBorderPainted(true); 
			setFocusPainted(true); 
			setOpaque(false);

			// Set tip
			setToolTipText("Help");

			// Add action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {

					// TODO: essa porra toda que é a Baehêa

				}
			});
		}	
	}

}
