/**
 *
 */
package dk.sdu.imada.gui.transclust;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.NumberFormatter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.gui.genome.GenomeSelection;
import dk.sdu.imada.methods.genome.DownloadGenomes;
import dk.sdu.imada.methods.genome.GenomeParser;
import dk.sdu.imada.methods.transclust.TransClust;

import net.miginfocom.swing.MigLayout;

/**
 * Class configures the objects associated with the Transitivity
 * Clustering Panel. It contains a JFormattedTextField for running 
 * TransClust with a single parameter, the density. 
 * 
 * @author Eudes Barbosa (eudes@imada.sdu.dk)
 */
public class TransClustPanel extends AbstractTransClustPanel {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = -5326632588552503356L;

	private static final Logger logger = LogManager.getLogger(TransClustPanel.class.getName());
	
	//------  Declaration end  ------//

	/** Configures the objects associated with the Transitivity Clustering Panel.*/
	public TransClustPanel() {
		// Create and configure panel components
		initComponents();		
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		// Configure main panel
		setLayout(new MigLayout());
		setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		// Create additional panels
		jLeft = new JPanel();
		jLeft.setLayout(new MigLayout());
		jLeft.setBackground(Color.WHITE);
		//
		jRight = new JPanel();	
		jRight.setLayout(new MigLayout());
		jRight.setBackground(Color.WHITE);

		//-------------------------------------------------------------------------------
		// Configure Single threshold part
		jLeft.add(singleThresholdLabel, "split 3, span, gaptop 2");
		jLeft.add(singleTextFieldValue, "span");
		jLeft.add(singleDensHelpButton,"span, wrap");
		jLeft.add(evalueLabel, "split 4, span, gaptop 2");
		jLeft.add(evalueTextField, "span");
		jLeft.add(evalueHelpButton, "span");
		jLeft.add(loadFilesButton,"span, align right");
		//jLeft.add(singleButton, "align right, gaptop 5");		
		// 
		add(jLeft,"top");

		//-------------------------------------------------------------------------------
		// Configure Range part
		jRight.add(multipleThresholdLabel, "gapleft 5, gaptop 15, align center, wrap");
		jRight.add(multipleLabelRange, "gaptop 1, gapleft 30, split 4, span");
		jRight.add(multipleTextFieldStartValue, "split 4, span");
		jRight.add(multipleLabelSeparator,"split 4, span");
		jRight.add(multipleTextFieldEndValue, "split 4, span, wrap");
		jRight.add(multipleLabelSteps, "gapleft , split 2, span");
		jRight.add(multipleTextFieldSteps, "split 2, span, wrap");
		//jRight.add(multipleButton, "align right");		
		// Add to panel
		//add(jRight, "b, gapbottom 5, align left");
	}

	@Override
	protected void initComponents() {
		// Configure separator (graphical line)
		separator = new JSeparator(SwingConstants.VERTICAL);
		separator.setBorder(new LineBorder(Color.DARK_GRAY));
		separator.setPreferredSize(new Dimension(1, 1));

		// Configure Blast components
		evalueLabel = new JLabel();
		evalueLabel.setFont(LabelFontPlain);
		evalueLabel.setText("Blast e-value: "); 
		evalueLabel.setToolTipText("Set e-value.");
		//
		evalueTextField = new JFormattedTextField(new Float(0.001));
		evalueTextField.setColumns(9);
		evalueTextField.setFormatterFactory(new AbstractFormatterFactory() {
			@Override
			public AbstractFormatter getFormatter(JFormattedTextField tf) {
				NumberFormat format = DecimalFormat.getInstance();
				format.setMinimumFractionDigits(2);
				//format.setMaximumFractionDigits(2);
				format.setRoundingMode(RoundingMode.HALF_UP);
				InternationalFormatter formatter = new InternationalFormatter(format);
				formatter.setAllowsInvalid(false);
				formatter.setMinimum(0.0000001);
				formatter.setMaximum(0.01);
				return formatter;
			}
		});

		// Configure Load files button
		loadFilesButton = new CreateLoadFilesButton();
		loadFilesButton.setToolTipText("Load Blast file");
		loadFilesButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent me) {
				// One click to open select file view
				// Two clicks to remove and selection
				if (me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 1) {
					if (selectFilesFrame == null) {
						selectFilesFrame = new SelectFilesView();
					} else if (!selectFilesFrame.isVisible()) {
						selectFilesFrame.setVisible(true);
					} else if (selectFilesFrame.isVisible()) {
						selectFilesFrame.toFront();
					}
				} else if (me.getButton() == MouseEvent.BUTTON3 && 
						me.getClickCount() == 1 &&
						(!userBlastFile.equals("") || !userTransClustFile.equals(""))) {
					// Change load button icon
					try {
						((SelectFilesView) selectFilesFrame).cleanSelectedFiles();
					} catch (Exception e) {
						logger.debug("It wasn't possible to change load file button icon.");
						e.printStackTrace();
					}
					// Ignores previously provided paths
					userBlastFile = "";
					userTransClustFile = "";
				}				
			}
			@Override
			public void mouseEntered(MouseEvent me) { }
			@Override
			public void mouseExited(MouseEvent me) { }
			@Override
			public void mousePressed(MouseEvent me) { }
			@Override
			public void mouseReleased(MouseEvent me) { }
		});

		// Configure Single Threshold components
		initSingleThreshold();
		// Configure Multiple threshold components
		initMultipleThresholds();
	}

	@Override
	public void resetValues() {
		// Set default values
		singleTextFieldValue.setValue(35);
		evalueTextField.setValue(0.001);
		userTransClustFile = "";
		userBlastFile = "";		
		// TODO: add values for range
		// ...
	}

	@Override
	public void enableComponents(Boolean b) {
		// Get all components from Right Panel (Range)
		Component[] componentsRight = jRight.getComponents();
		int size = componentsRight.length;
		// Iterate through components
		for(int i = 0; i < size; i++) {
			componentsRight[i].setEnabled(b);			
		}
		//------------------------------------
		// Get all components from Left Panel (Single)
		Component[] componentsLeft = jLeft.getComponents();
		size = componentsLeft.length;
		// Iterate through components
		for(int i = 0; i < size; i++) {
			componentsLeft[i].setEnabled(b);			
		}
	}

	/**
	 * @param file	If user selected a file, it 
	 * 				will set the location of 
	 * 				the TransClust file.
	 */
	protected void setTransclustFileLocation(String path) {
		userTransClustFile = path;
	}

	/**
	 * @param file	If user selected a file, it 
	 * 				will set the location of 
	 * 				the Blast file.
	 */
	protected void setBlastFileLocation(String path) {
		userBlastFile = path;
	}

	/**
	 * @return		Transitivity Clustering object with 
	 * 				all user's parameters.
	 */
	public TransClust getParameters() {
		TransClust tc = null;
		// Test if threshold was provided	
		int density;
		double evalue;
		try {
			density = Integer.parseInt(singleTextFieldValue.getValue().toString());
			evalue = Double.parseDouble(evalueTextField.getValue().toString());

			// Create TC object
			tc = new TransClust();
			tc.setStart(density);
			tc.setEvalue(evalue);
			tc.setRange(false);	
			tc.setUserBlastFile(userBlastFile);
			tc.setUserTransClustFile(userTransClustFile);

		} catch (Exception e) {
			String errorMessage = "Please assign all parameters for Transitivity Clustering.";
			JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
			// Log
			logger.warn("Attempting to run Transitivity Clustering without assinging all parameters. ");
		}
		//
		return tc;
	}	

	/**
	 * Configures all components associated with running 
	 * Transitivity Clustering with a single threshold.
	 */
	private void initSingleThreshold() {

		// Configure label
		singleThresholdLabel = new JLabel();
		singleThresholdLabel.setFont(LabelFontPlain);
		singleThresholdLabel.setText("Density parameter:  "); 
		singleThresholdLabel.setToolTipText("Add value between 1 and 324.");

		// Create FormattedTextField: holds threshold value
		singleTextFieldValue = new CreateFormattedTextField(1,324).getNewFormattedTextField();
		singleTextFieldValue.setColumns(5);
		singleTextFieldValue.setValue(35);

		// Configure button to run analysis
		singleButton = new CreateSingleThresholdRunButton();

		// Configure help buttons
		singleDensHelpButton = createHelpLabel(null, DENS_INST, MANUAL_ONLINE, PAPER_MAIN);
		evalueHelpButton = createHelpLabel(null, EVALUE_INST, MANUAL_ONLINE, PAPER_MAIN);

	}

	/**
	 * Configures all components associated with running 
	 * Transitivity Clustering with a multiple thresholds.
	 */
	private void initMultipleThresholds() {

		// Configure Labels
		multipleThresholdLabel = new JLabel();
		multipleThresholdLabel.setFont(LabelFontBold);
		multipleThresholdLabel.setText("Multiple Thresholds"); 
		//
		multipleLabelRange = new JLabel();
		multipleLabelRange.setFont(LabelFontPlain);
		multipleLabelRange.setText("Range:  ");
		//
		multipleLabelSeparator = new JLabel();
		multipleLabelSeparator.setFont(LabelFontPlain);
		multipleLabelSeparator.setText(" - "); 
		//
		multipleLabelSteps = new JLabel();
		multipleLabelSteps.setFont(LabelFontPlain);
		multipleLabelSteps.setText("Steps Size: "); 

		// Create FormattedTextField: holds values for the analysis		
		multipleTextFieldStartValue = new CreateFormattedTextField(1,324).getNewFormattedTextField();
		multipleTextFieldStartValue.setFont(LabelFontPlain);
		multipleTextFieldStartValue.setColumns(3);
		//
		multipleTextFieldEndValue = new CreateFormattedTextField(1,324).getNewFormattedTextField();
		multipleTextFieldEndValue.setFont(LabelFontPlain);
		multipleTextFieldEndValue.setColumns(3);
		//
		multipleTextFieldSteps = new CreateFormattedTextField(1,100).getNewFormattedTextField();
		multipleTextFieldSteps.setFont(LabelFontPlain);
		multipleTextFieldSteps.setColumns(9);

		// Configure button to run analysis
		multipleButton = new MultipleThresholdRunButton();
	}

	/**
	 * Inner-class creates a new frame that allows user 
	 * to select Blast and TransClust files (Optional).
	 */
	protected class SelectFilesView extends JFrame {

		private static final long serialVersionUID = 1090641479788142889L;

		// Variable declaration
		protected JFileChooser jFileChooser;
		protected JTextField blastTextField;
		protected JTextField transclustTextField;
		protected JButton blastFileSelectButton;
		protected JButton transclustFileSelectButton;
		protected JButton cancelButton;
		protected JButton doneButton;
		// Declaration end

		/**
		 * Creates and displays another frame so the user can 
		 * choose the appropriate Blast and TransClust files. 
		 * It is not mandatory to input both (or any).
		 */
		public SelectFilesView() {	
			super("Select file...");
			// Close if focus change
			/*
			addWindowFocusListener(new WindowFocusListener() {
				public void windowGainedFocus(WindowEvent e) {
					// Do nothing
				}
				public void windowLostFocus(WindowEvent e) { 
					setVisible(false);
				}
			});
			 */
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			// Initialize and configure components
			initComponents();
			configureLayout();		
			// Set visible			
			pack();
			setVisible(true);
		}

		/** Cleans the selected files. 
		 * @throws Exception */
		protected void cleanSelectedFiles() {
			setBlastFileLocation("");
			setTransclustFileLocation("");
			//
			transclustTextField.setText("TranClust file...");
			blastTextField.setText("Blast file...");
			//
			try {
				((CreateLoadFilesButton) loadFilesButton).changeLoadIcon("Load");
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Configures the layout of the components
		 * within the panel.
		 */
		protected void configureLayout() {
			JPanel panel = new JPanel();
			panel.setLayout(new MigLayout());
			//
			panel.add(blastTextField,"split 2, span");
			panel.add(blastFileSelectButton,"span, wrap");
			panel.add(transclustTextField,"split 2, span");
			panel.add(transclustFileSelectButton,"span, wrap");
			panel.add(cancelButton,"split 2, span, align center");
			panel.add(doneButton,"span, align center");
			//
			add(panel);
		}

		/**
		 * Initializes all objects associated with the panel. 
		 */
		protected void initComponents() {
			// Configure file chooser
			jFileChooser = new JFileChooser();
			jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			// Configure JTextField
			blastTextField = new JTextField(30);
			blastTextField.setEnabled(false);
			if(userBlastFile.equals(""))
				blastTextField.setText("Blast file...");
			else 
				blastTextField.setText(userBlastFile);			
			// 
			transclustTextField = new JTextField(30);
			transclustTextField.setEnabled(false);
			if(userTransClustFile.equals(""))
				transclustTextField.setText("TranClust file...");
			else 
				transclustTextField.setText(userTransClustFile);

			// Configure Canel button
			cancelButton = new JButton("Cancel");
			cancelButton.setFont(ButtonFont);
			cancelButton.setToolTipText("Cancel and empty file selection.");
			cancelButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					cleanSelectedFiles();
					//
					setVisible(true);
					dispose();
				}
			});

			// Configure Done button
			doneButton = new JButton("Done");
			doneButton.setFont(ButtonFont);
			doneButton.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(true);
					dispose();
				}
			});

			// Configure Folder buttons
			blastFileSelectButton = new JButton();
			blastFileSelectButton.setIcon(new ImageIcon(getClass().getResource("/images/generic_folder_grey.png")));
			blastFileSelectButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					int returnVal = jFileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = jFileChooser.getSelectedFile();
						// Set textfield
						String path = file.getAbsolutePath();
						blastTextField.setText(path);
						setBlastFileLocation(path);
						// Change load button icon
						try {
							((CreateLoadFilesButton) loadFilesButton).changeLoadIcon("Loaded");
							repaint();
						} catch (Exception e) {
							logger.debug("It wasn't possible to change load file button icon.");
							e.printStackTrace();
						}
					}
				}
			});
			//
			transclustFileSelectButton = new JButton();
			transclustFileSelectButton.setIcon(new ImageIcon(getClass().getResource("/images/generic_folder_grey.png")));
			transclustFileSelectButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					int returnVal = jFileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = jFileChooser.getSelectedFile();
						// Set textfield
						String path = file.getAbsolutePath();
						transclustTextField.setText(path);
						setTransclustFileLocation(path);
						// Change load button icon
						try {
							((CreateLoadFilesButton) loadFilesButton).changeLoadIcon("Loaded");
							repaint();
						} catch (Exception e) {
							logger.debug("It wasn't possible to change load file button icon.");
							e.printStackTrace();
						}
					}
				}
			});			
		}
	}

	/**
	 * InnerClass configures Cluster button. It will call Transitivity
	 * Clustering with the given density value.
	 */
	protected class CreateSingleThresholdRunButton extends JButton {

		private static final long serialVersionUID = -1066353881323061324L;

		/**
		 *  (not used)
		 */
		public CreateSingleThresholdRunButton() {
			// Set label
			setText("Cluster");
			setFont(ButtonFont);
			setToolTipText("Will cluster gene products using a single density value.");
			// Add action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Test if threshold was provided	
					int density;
					try {
						density = Integer.parseInt(singleTextFieldValue.getValue().toString());
						// Check if still necessary to download something
						boolean check = GenomeSelection.checkForDownload();
						if (check) { //if true start download
							DownloadGenomes download = new DownloadGenomes();
							download.exec();
						}
						// Parse genomes
						logger.info("Parsing GenBank files...");
						// Lifestyle One
						DefaultTableModel genomesL1 = GenomeSelection.getLifestyleOneTable();
						String nameL1 = GenomeSelection.getLifestyleOneName();
						// Lifestyle Two
						DefaultTableModel genomesL2 = GenomeSelection.getLifestyleTwoTable();
						String nameL2 = GenomeSelection.getLifestyleTwoName();
						GenomeParser parse = new GenomeParser(genomesL1, nameL1, genomesL2, nameL2);
						parse.exec();						

						// Run Transitivity Clustering
						logger.info("Calling Transitivity Clustering with density parameter = "+ density +". ");
						// Create TC object
						TransClust process = new TransClust();
						process.setStart(density);
						process.setRange(false);
						// Run TC object
						//RunTransClust tc = new RunTransClust(process);
						//tc.exec();												

					} catch (Exception e) {
						String errorMessage = "Please assign density parameter for Transitivity Clustering.";
						JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
						// Log
						logger.warn("Attempting to run Transitivity Clustering without assinging density parameter. ");
					}
				}
			});
		}
	}

	/**
	 * InnerClass configures Cluster button for a range of values. It 
	 * will call Transitivity Clustering with the given range of values, namely:
	 * range of density parameters and number of steps.  
	 */
	protected class MultipleThresholdRunButton extends JButton {

		private static final long serialVersionUID = 6505068025810254222L;		

		/**
		 *  (not used)
		 */
		public MultipleThresholdRunButton(){
			// Set label
			setText("Cluster");
			setFont(ButtonFont);
			// (Remove this part and set enable)
			setEnabled(false);
			setToolTipText("Not implemented yet.");
			//mButton.setToolTipText("Will cluster gene products using a range of thresholds.");

			// add action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {

					// Get values for range analysis
					int multipleRangeStart = (int) multipleTextFieldStartValue.getValue();
					int multipleRangeEnd = (int) multipleTextFieldEndValue.getValue();
					int multipleStepsNumber = (int) multipleTextFieldSteps.getValue();

					// Test if all parameters were provided
					// Start of message from Message Dialog
					String errorMessage = "Please assing the following parameter(s) for Transitivity Clustering:\n";
					// Dummy variable to test if some condition wasn't met
					String test = errorMessage;
					// Test if all parameters were provided
					if (multipleRangeStart == -1 || multipleRangeEnd == -1 || multipleStepsNumber == -1 ){
						errorMessage += "- Provide all parameters values.\n";					
					}
					// Test if it is a range
					if (multipleRangeStart == multipleRangeEnd){
						errorMessage += "- Range values must be different.\n";
					}
					// Test if range makes sense
					if (multipleRangeStart > multipleRangeEnd){
						errorMessage += "- The end of the range must be greater than the start.\n";
					}								
					if (errorMessage != test){
						JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// Parse genomes
						logger.info("Parsing GenBank files...");
						// Lifestyle One
						DefaultTableModel genomesL1 = GenomeSelection.getLifestyleOneTable();
						String nameL1 = GenomeSelection.getLifestyleOneName();
						// Lifestyle Two
						DefaultTableModel genomesL2 = GenomeSelection.getLifestyleTwoTable();
						String nameL2 = GenomeSelection.getLifestyleTwoName();
						GenomeParser parse = new GenomeParser(genomesL1, nameL1, genomesL2, nameL2);
						parse.exec();	

						// Run Transitivity Clustering
						logger.info("Calling Transitivity Clustering with density parameter ranging from "
								+ multipleRangeStart +" to "+ multipleRangeEnd 
								+"(" + multipleStepsNumber+ " steps). ");

						// Create TC object
						TransClust process = new TransClust();
						process.setStart(multipleRangeStart);
						process.setEnd(multipleRangeEnd);
						process.setSteps(multipleStepsNumber);
						process.setRange(true);

						//TODO: Run Transitivity Clustering (Range)
						// Essa porra toda que é a Bahea...me beije.
					} 
				}
			});    
		}
	}
}
