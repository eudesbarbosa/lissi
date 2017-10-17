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
package dk.sdu.imada.gui.genome;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.methods.genome.DownloadGenomeList;
import dk.sdu.imada.methods.genome.ParseLocalGenomes;

import net.miginfocom.swing.MigLayout;

/**
 * Class configures the 'Load Genomes' panel. It allows 
 * user to either select the genomes from local folders 
 * or to download the list of all complete genomes available
 * at GenBank.
 * <br>
 * FOLDER ICON (INFO) <br>
 * Artist: Hamza Saleem <br>
 * License: Free for non-commercial use. <br>
 * Commercial usage: Not allowed <br>
 * http://www.iconarchive.com/show/stock-folder-style-2-icons-by-hamzasaleem/Folder-Plain-icon.html <br>
 * 
 * @author Eudes Barbosa
 */
final public class GenomeLoader extends AbstractGenomeLoader {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = -8548465379079910752L;
	
	private static final Logger logger = LogManager.getLogger(GenomeLoader.class);

	/** 
	 * Boolean flag indicates if user wants to download files 
	 * from NCBI. 
	 */
	protected static boolean genomeListDownloaded = false;

	//------  Declaration end  ------//

	/**
	 * Creates and configures the 'Load Genomes' panel. It allows 
	 * user to either select the genomes from local folders 
	 * or to download the list of all complete genomes available
	 * at GenBank.
	 */
	public GenomeLoader() {
		//
		super();
		// Initialize components
		initComponents();
		// Configure layout
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		setLayout(new MigLayout());
		setPreferredSize(PanelDimension);
		//
		add(loadLocal, "align left, gaptop 10, wrap");
		add(folderLife1, "split 3, gaptop2");
		add(jTextFieldPathLife1, "span");
		add(jButtonFolder1, "span, wrap");
		add(folderLife2, "split 3, gaptop 2");
		add(jTextFieldPathLife2, "span");
		add(jButtonFolder2,"span, wrap");
		add(downloadList, "gaptop 5, align left, wrap");
		//panel.add(jButtonCancel,"split 2, gaptop 10, align right");
		//add(jButtonConfirm, " gaptop 10, align right, wrap");		
	}

	@Override
	protected void initComponents() {
		// Configure main components
		
		// Create check button (download list from NCBI)
		downloadList = new JRadioButton("Download list from GenBank");
		downloadList.setMnemonic(KeyEvent.VK_C); 
		downloadList.setSelected(false);
		downloadList.setToolTipText("It will download a list of all available complete genomes from NCBI.");
		downloadList.setFont(LabelFontPlain);
		// Configure check button (use local files) 
		loadLocal = new JRadioButton("Select from local folder");
		loadLocal.setFont(LabelFontPlain);
		loadLocal.setMnemonic(KeyEvent.VK_C); 
		loadLocal.setSelected(true);
		loadLocal.setToolTipText("It will use only locally stored genomes. ");
		//
		group = new ButtonGroup();
		group.add(loadLocal);
		group.add(downloadList);

		//Configure a file chooser
		jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//
		jButtonConfirm = new CreateConfirmButton();

		//-----------------------------------------------------------------
		// Configure components associated with lifestyle one		
		folderLife1 = new JLabel("Lifestyle 1: ");
		folderLife1.setFont(LabelFontBold);
		//
		jTextFieldPathLife1 = new JTextField(15);
		jTextFieldPathLife1.setText("");
		jTextFieldPathLife1.setEditable(true);
		jTextFieldPathLife1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent evt) {
				directoryModification = true;				
			}
			@Override
			public void insertUpdate(DocumentEvent evt) {
				directoryModification = true;	
			}
			@Override
			public void removeUpdate(DocumentEvent evt) {
				directoryModification = true;	
			}

		});
		//
		jButtonFolder1 = new CreateSelectFolderButton(jTextFieldPathLife1);
		jButtonFolder1.setIcon(new ImageIcon(getClass().getResource("/images/generic_folder_grey.png")));
		jButtonFolder1.setToolTipText("Load lifesyle 1 genomes.");


		//-----------------------------------------------------------------
		// Configure components associated with lifestyle two		
		folderLife2 = new JLabel("Lifestyle 2: ");
		folderLife2.setFont(LabelFontBold);
		//
		jTextFieldPathLife2 = new JTextField(15);
		jTextFieldPathLife2.setText("");
		jTextFieldPathLife2.setEditable(true);
		jTextFieldPathLife2.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent evt) {
				directoryModification = true;				
			}
			@Override
			public void insertUpdate(DocumentEvent evt) {
				directoryModification = true;	
			}
			@Override
			public void removeUpdate(DocumentEvent evt) {
				directoryModification = true;	
			}			
		});
		//
		jButtonFolder2 = new CreateSelectFolderButton(jTextFieldPathLife2);
		jButtonFolder2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/generic_folder_grey.png")));
		jButtonFolder2.setToolTipText("Load lifesyle 2 genomes.");

	}

	/** 
	 * Presses Confirm button.
	 * @return	Returns 'True' if all requirement where satisfied; 
	 * 			'False' otherwise. 
	 */
	public boolean pressConfirmButton() {
		jButtonConfirm.doClick();
		return loadingDone;
	}

	/**
	 * Enables or disables modifications in the list of genomes used 
	 * in the analysis (i.e., lifestyles one and two). This 
	 * method is expect to be used in case the user decides  
	 * to cancel the pipeline and start again.
	 * 
	 * @param b
	 */
	public static void enableSelectionOptions(boolean b) {
		jButtonConfirm.setEnabled(b);		
	}	 

	/**
	 * InnerClass creates a button that allows user to select the folder 
	 * that contains the genomes of a given lifestyle.
	 */
	protected class CreateSelectFolderButton extends JButton {

		private static final long serialVersionUID = -3110237773242639568L;

		/**
		 * Creates a button that allows user to select the folder 
		 * that contains the genomes of a given lifestyle.
		 * 
		 * @param lifestyle		The JTextField associated with the lifestyle. The 
		 * 						path to the folder will be stored in the text field.
		 */
		public CreateSelectFolderButton(final JTextField lifestyle){
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					int returnVal = jFileChooser.showOpenDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = jFileChooser.getSelectedFile();
						// Set textfield
						lifestyle.setText(file.getAbsolutePath());
					}
				}
			}); //end-ActionListener
		}
	}

	/**
	 * InnerClass creates a button to confirm the use of the selected
	 * folder or to download the list of available genomes from GenBank. 
	 */
	protected class CreateConfirmButton extends JButton {

		private static final long serialVersionUID = -5039515156264552002L;

		/** Pathway to folder with genomes associated with lifestyle one. */
		protected String p1;

		/** Pathway to folder with genomes associated with lifestyle two. */
		protected String p2;

		/**
		 * Creates a button to confirm the use of the selected folder or 
		 * to download the list of available genomes from GenBank.
		 */
		public CreateConfirmButton(){
			// Set label
			setText("OK");
			setFont(ButtonFont);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {

					// Get paths from TextFields
					p1 = jTextFieldPathLife1.getText();
					p2 = jTextFieldPathLife2.getText();

					/* Downloading list from NCBI has priority over loading
					 * files from local diretory. Thus, if download option
					 * is checked, the program will ignore all given paths. 
					 */
					if (downloadList.isSelected()){
						// Check if any path was given
						if (p1.length() > 0 || p2.length() > 0){
							String errorMessage = "Path to Folders will not be used. Downloading genome list from GenBank...";
							JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
						}						
						// Log
						logger.info("Downloading list of available genomes from GenBank. ");			
						// Download list of available genomes from GenBank 
						if (genomeListDownloaded == false) {
							DownloadGenomeList getList = new DownloadGenomeList();
							getList.exec();
							loadingDone = true;
							genomeListDownloaded = true;
						}

						//-------------------------------------------------------------------------------------

						/* In case the loadLocal box is checked, the program
						 * will proceed to load and parse files from local folders	
						 */
					} else if (loadLocal.isSelected()) {
						// Check if paths to folders were given
						if (p1.equals(null) || p2.equals(null)) {
							// Send error message
							String errorMessage = "Please select directories.";
							JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
							loadingDone = false;
							return;
						}
						// Check if paths are valid
						File l1 = new File(p1);
						File l2 = new File(p2);
						if (!l1.isDirectory() || !l2.isDirectory()) {
							// Send error message
							String errorMessage = "Please select directories.";
							JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
							loadingDone = false;
							return;		
							// Checks if paths are equal
						} if (l1.equals(l2)) {
							// Send error message
							String errorMessage = "Please select distinct directories.";
							JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
							loadingDone = false;
							return;
						} else {
							// Check if it is necessary to parse folder again
							if (directoryModification == true) {
								// Log
								logger.info("Genomes selected from local folders. ");
								// Select genomes from local folders
								ParseLocalGenomes browse = new ParseLocalGenomes(p1, p2);
								browse.exec();
								loadingDone = true;
								directoryModification = false;
							}
						}
					}
				}
			}); //end-ActionListener
		}
	}
}
