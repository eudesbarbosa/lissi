/**
 *
 */
package dk.sdu.imada.gui.genome;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.gui.AbstractPanel;
import dk.sdu.imada.gui.ProgressPanel;
import dk.sdu.imada.methods.PipelineSync;
import dk.sdu.imada.methods.genome.DownloadGenomes;

import net.miginfocom.swing.MigLayout;

/**
 * Class allows user to select and download genomes straight from NCBI or to select  
 * a local directory with desired genomes. The selected organisms must be assigned 
 * in one of two lifestyles. Afterwards, they will be (download and) parsed into 
 * the analysis pipeline. 
 * 
 * @author Eudes Barbosa
 */
final public class GenomeSelection extends AbstractGenomeSelection {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = 5999718495513827268L;

	private static final Logger logger = LogManager.getLogger(GenomeSelection.class.getName());

	//------  Declaration end  ------//
	

	/**
	 * Initialize and creates the components from 'Genome Selection' 
	 * panel. Through this panel user will be able to select the genomes
	 * that shall be use in the analysis. 
	 * <br>
	 * Allows user to select and download genomes straight from NCBI or to select  
	 * a local directory with desired genomes. The selected organisms must be assigned 
	 * in one of two lifestyles. Afterwards, they will be (download and) parsed into 
	 * the analysis pipeline. 
	 */
	public GenomeSelection() {
		super();
		// Initialize components
		initComponents();	
		// Configure layout
		configureLayout();
	}	

	@Override
	protected void initComponents() {
		// Create and configure items associated with 'Available Genomes'
		configureAvailableGenomesComponents();
		// Create and configure 'Lifestyle One' table
		genomeLifestyleOne = new CreateGenomeTable("lifestyle");
		// Set scrollable view 
		jScrollLifestyleOne = new JScrollPane();
		jScrollLifestyleOne.setViewportView(genomeLifestyleOne);
		if (genomeLifestyleOne.getColumnModel().getColumnCount() > 0) {
			genomeLifestyleOne.getColumnModel().getColumn(0).setPreferredWidth(235);
			genomeLifestyleOne.getColumnModel().getColumn(1).setMinWidth(35);
			genomeLifestyleOne.getColumnModel().getColumn(1).setMaxWidth(60);
			// Hid column 'accession'
			genomeLifestyleOne.getColumnModel().getColumn(2).setWidth(0);
			genomeLifestyleOne.getColumnModel().getColumn(2).setMinWidth(0);
			genomeLifestyleOne.getColumnModel().getColumn(2).setMaxWidth(0);
			// Hid column 'project.id'
			genomeLifestyleOne.getColumnModel().getColumn(3).setWidth(0);
			genomeLifestyleOne.getColumnModel().getColumn(3).setMinWidth(0);
			genomeLifestyleOne.getColumnModel().getColumn(3).setMaxWidth(0);
			// Hid column 'accession.local'
			genomeLifestyleOne.getColumnModel().getColumn(4).setWidth(0);
			genomeLifestyleOne.getColumnModel().getColumn(4).setMinWidth(0);
			genomeLifestyleOne.getColumnModel().getColumn(4).setMaxWidth(0);
		}

		// Create and configure 'Lifestyle Two' table
		genomeLifestyleTwo = new CreateGenomeTable("lifestyle");
		jScrollLifestyleTwo = new JScrollPane();
		jScrollLifestyleTwo.setViewportView(genomeLifestyleTwo);
		if (genomeLifestyleTwo.getColumnModel().getColumnCount() > 0) {
			genomeLifestyleTwo.getColumnModel().getColumn(0).setPreferredWidth(235);
			genomeLifestyleTwo.getColumnModel().getColumn(1).setMinWidth(35);
			genomeLifestyleTwo.getColumnModel().getColumn(1).setMaxWidth(60);
			// Hid column 'accession'
			genomeLifestyleTwo.getColumnModel().getColumn(2).setWidth(0);
			genomeLifestyleTwo.getColumnModel().getColumn(2).setMinWidth(0);
			genomeLifestyleTwo.getColumnModel().getColumn(2).setMaxWidth(0);
			// Hid column 'project.id'
			genomeLifestyleTwo.getColumnModel().getColumn(3).setWidth(0);
			genomeLifestyleTwo.getColumnModel().getColumn(3).setMinWidth(0);
			genomeLifestyleTwo.getColumnModel().getColumn(3).setMaxWidth(0);
			// Hid column 'accession.local'
			genomeLifestyleTwo.getColumnModel().getColumn(4).setWidth(0);
			genomeLifestyleTwo.getColumnModel().getColumn(4).setMinWidth(0);
			genomeLifestyleTwo.getColumnModel().getColumn(4).setMaxWidth(0);
		}

		// Create and configure Lifestyle One buttons
		jButtonSelectL1 = new CreateSelectButton(availableGenomes, genomeLifestyleOne, genomeLifestyleTwo);
		jButtonSelectL1.setIcon(new ImageIcon(getClass().getResource("/images/right_arrow.png")));
		jButtonSelectL1.setToolTipText("Adding duplicated organisms is not allowed. Organisms can only be added to one lifestyle.");
		//
		jButtonDeselectL1 = new CreateDeselectButton(genomeLifestyleOne);
		jButtonDeselectL1.setIcon(new ImageIcon(getClass().getResource("/images/left_arrow.png")));
		//
		//jButtonUncheckL1 = new CreateUncheckButton("lifestyle");
		jButtonUncheckL1 = new CreateUncheckButton(genomeLifestyleOne);


		// Create and configure Lifestyle Two buttons
		jButtonSelectL2 = new CreateSelectButton(availableGenomes, genomeLifestyleTwo, genomeLifestyleOne);
		jButtonSelectL2.setIcon(new ImageIcon(getClass().getResource("/images/right_arrow.png")));
		jButtonSelectL2.setToolTipText("Adding duplicated organisms is not allowed. Organisms can only be added to one lifestyle.");
		//
		jButtonDeselectL2 = new CreateDeselectButton(genomeLifestyleTwo);
		jButtonDeselectL2.setIcon(new ImageIcon(getClass().getResource("/images/left_arrow.png")));
		//
		jButtonUncheckL2 = new CreateUncheckButton(genomeLifestyleTwo);

		// Create 'Use Genomes' button
		jButtonUseGenomes = new CreateUseGenomesButton();
		// Create Load Available Genome button
		//jButtonLoadAvailableGenomes = new CreateLoadGenomeButton();
		// Create Uncheck all for Available Genomes table
		jButtonUncheck = new CreateUncheckButton();


		/*	Creating and organizing all Labels , namely:
		 *  jLabel: associated with Available Genomes
		 *  jLabel1 and jTextField1: associated with Lifestyle 1
		 *  jLabel2 and jTextField2: associated with Lifestyle 2
		 *  jLabelAdd*: associated with the 'Add' button
		 *  jLabelRemove*: associated with the 'Remove' button 
		 */
		jLabelAvailableGenomes.setFont(LabelFontBold); 
		jLabelAvailableGenomes.setText("Available Genomes (NCBI)");
		//
		jLabelL1.setFont(LabelFontBold);
		jLabelL1.setText("Lifestyle 1:");
		jLabelL1.setToolTipText("Use alphanumeric characters only.");
		//
		jLabelL2.setFont(LabelFontBold); 
		jLabelL2.setText("Lifestyle 2:");
		jLabelL2.setToolTipText("Use alphanumeric characters only.");
		//
		jLabelAdd.setFont(LabelFontBold); 
		jLabelAdd.setText("Add");
		//
		jLabelRemove.setFont(LabelFontBold); 
		jLabelRemove.setText("Remove");
		//
		jLabelAdd2.setFont(LabelFontBold);
		jLabelAdd2.setText("Add");
		//
		jLabelRemove2.setFont(LabelFontBold);
		jLabelRemove2.setText("Remove");
	}

	@Override
	protected void configureLayout() {
		setLayout(new MigLayout("wrap 3", "", ""));
		setPreferredSize(PanelDimension);

		// Initialize additional panels
		JPanel jLeft= new JPanel();
		JPanel jCenter = new JPanel();
		JPanel jRight = new JPanel(); 

		// Configure left part
		jLeft.setLayout(new MigLayout());
		jLeft.setBackground(Color.WHITE);
		jLeft.add(jLabelAvailableGenomes, "top, align left, gaptop 40, wrap");
		jLeft.add(jScrollAvailableGenomes, "gaptop 5, align left, hmax 500, growx, wrap");
		jLeft.add(availableGenomeFilter, "b, split 3, span, grow 0");
		jLeft.add(JTextFilterAvailableGenomes, "b, span, growx");
		jLeft.add(jButtonUncheck, "b, span, grow 0");
		// Add left panel
		add(jLeft, "top");

		// Configure center part
		jCenter.setLayout(new MigLayout());
		jCenter.setBackground(Color.WHITE);
		//jCenter.add(jButtonLoadAvailableGenomes, "top, align center, wrap");
		//
		jCenter.add(jButtonSelectL1, "gaptop 135, align right, grow 0, span, wrap");
		jCenter.add(jLabelAdd, "gapright 15, align right, grow 0, span, wrap");
		jCenter.add(jButtonDeselectL1, "align right, grow 0, span, wrap");
		jCenter.add(jLabelRemove, "align right, grow 0, span, wrap");
		//
		jCenter.add(jButtonSelectL2 , "gaptop 100, align right, grow 0, span, wrap");
		jCenter.add(jLabelAdd2, "gapright 15, align right, grow 0, span, wrap");
		jCenter.add(jButtonDeselectL2, "align right, grow 0, span, wrap");
		jCenter.add(jLabelRemove2, "align right, grow 0, span, wrap"); 
		// Add center panel
		add(jCenter, "top");

		// Configure right part
		jRight.setLayout(new MigLayout());
		jRight.setBackground(Color.WHITE);
		jRight.add(jLabelL1, "gapleft 80, gaptop 75");
		jRight.add(jTextFieldL1, "gaptop 10, wrap");
		jRight.add(jScrollLifestyleOne,"top, hmax 150, growx, wrap, span");
		jRight.add(jButtonUncheckL1, "align right, grow 0, span, wrap");
		jRight.add(jLabelL2, "gapleft 80, gaptop 5");
		jRight.add(jTextFieldL2, "gaptop 15, wrap");
		jRight.add(jScrollLifestyleTwo, "b, growx, hmax 150, span, wrap");
		jRight.add(jButtonUncheckL2, "align right, grow 0, span, wrap");
		//jRight.add(jButtonUseGenomes, "south, align right, gaptop 25, w ::150");
		// Add right panel
		add(jRight, "top, wrap");
	}

	/**
	 * Enables or disables modifications in the list of genomes used 
	 * in the analysis (i.e., lifestyles one and two). This 
	 * method is expect to be used in case the user decides  
	 * to cancel the pipeline and start again.
	 * 
	 * @param b			True if user is allowed to access components. 
	 * 					False, otherwise.
	 */
	public static void enableSelectionOptions(boolean b) {
		// Change status
		lockTables = b;
		// Lock text field
		jTextFieldL1.setEditable(b);
		jTextFieldL2.setEditable(b);	
		// Lock tables
		availableGenomes.setEnabled(b);
		genomeLifestyleOne.setEnabled(b);
		genomeLifestyleTwo.setEnabled(b);
	}

	/**
	 * Adds row to Available Genomes table (NCBI).
	 * @param row		New row.
	 */
	public static void addRowAvailableGenomes(Object[] row) {
		((DefaultTableModel) availableGenomes.getModel()).addRow(row);
	}

	/**
	 * Empty available Genomes table (NCBI).
	 */
	public static void emptyAvailableGenomes(){
		((DefaultTableModel) availableGenomes.getModel()).setRowCount(0);
	}

	/**
	 * Adds a new row to lifestyle One table. It first verify if the value
	 * wasn't already added to one of the two lifestyles tables.
	 * 
	 * @param row		Object array that contains information about the
	 * 					genome, namely: organism name, checked (boolean),
	 * 					accession number and project identifier.
	 */
	public static void addRowLifestyleOne(Object[] row){

		if (lockTables == false) {
			// Create object with all values in lifestyle One
			int rows = genomeLifestyleOne.getModel().getRowCount();
			Object[] contentL1 = new Object[rows];
			for (int i = 0; i < rows; i++) {
				contentL1[i] = genomeLifestyleOne.getModel().getValueAt(i, 4);
			}

			// Create object with all values in lifestyle Two
			rows = genomeLifestyleTwo.getModel().getRowCount();
			Object[] contentL2 = new Object[rows];
			for (int i = 0; i < rows; i++) {
				contentL2[i] = genomeLifestyleTwo.getModel().getValueAt(i, 4);
			}

			// Verifies if the value is either duplicated or already present in the other lifestyle
			boolean existL1 = Arrays.asList(contentL1).contains(row[4]); //check if not duplicated
			boolean existL2 = Arrays.asList(contentL2).contains(row[4]); //check if already in lifestyle Two
			if (!existL1 & !existL2) {        			
				((DefaultTableModel) genomeLifestyleOne.getModel()).addRow(row);
			} else {
				logger.error("User attemped to include organisms more than one time: " 
						+ row[0].toString()
						+ " | " + row[4].toString());
			}
		} else {
			return; //do nothing
		}
	}

	/**
	 * @return 			Returns lifestyle One table.	
	 */
	public static DefaultTableModel getLifestyleOneTable(){
		DefaultTableModel table = (DefaultTableModel) genomeLifestyleOne.getModel();
		return table;
	}

	/**
	 * Removes all rows from lifestyle One table.
	 */
	public static void emptyLifestyleOne() {
		if (lockTables == false)
			((DefaultTableModel) genomeLifestyleOne.getModel()).setRowCount(0);
	}

	/**
	 * Updates table content of lifestyle one. Used after the genome is 
	 * downloaded from GenBank. The accession number is substitute per the 
	 * path to the local file. Plus, the project ID is set to 'null', since 
	 * it is normally used to differentiate a local file from another that 
	 * needs to be downloaded.
	 * 
	 * @param accession				Accession number of the genome.
	 * @param localFileName			Full path to recently downloaded genome.
	 */
	public static void updateLifestyleOne(String accession, String localFileName) {
		// Create object with all values in lifestyle One
		int rows = genomeLifestyleOne.getModel().getRowCount();
		// Iterate throw rows
		for (int i = 0; i < rows; i++) {
			String rowValue = (String) genomeLifestyleOne.getModel().getValueAt(i, 2);
			rowValue = rowValue.replaceAll("\\.[0-9]$", "");
			if(rowValue.equals(accession)){
				// Substitute accession number with local file path
				genomeLifestyleOne.getModel().setValueAt(localFileName, i, 2); 
				// Remove project id
				genomeLifestyleOne.getModel().setValueAt(null, i, 3);
				//
				return;
			}
		}
	}

	/**
	 * Gets the name assign for lifestyle one.
	 * 
	 * @return		String containing name of lifestyle one.
	 */
	public static String getLifestyleOneName() {
		return jTextFieldL1.getText();
	}

	/**
	 * Adds a new row to lifestyle Two table. It first verify if the value
	 * wasn't already added to one of the two lifestyles tables.
	 * 
	 * @param row		Object array that contains information about the
	 * 					genome, namely: organism name, checked (boolean),
	 * 					accession number and project identifier.
	 */
	public static void addRowLifestyleTwo(Object[] row) {
		if (lockTables == false) {
			// Create object with all values in lifestyle One
			int rows = genomeLifestyleOne.getModel().getRowCount();
			Object[] contentL1 = new Object[rows];
			for (int i = 0; i < rows; i++) {
				contentL1[i] = genomeLifestyleOne.getModel().getValueAt(i, 0);
			}

			// Create object with all values in lifestyle Two
			rows = genomeLifestyleTwo.getModel().getRowCount();
			Object[] contentL2 = new Object[rows];
			for (int i = 0; i < rows; i++) {
				contentL2[i] = genomeLifestyleTwo.getModel().getValueAt(i, 4);
			}

			// Verifies if the value is either duplicated or already present in the other lifestyle
			boolean existL1 = Arrays.asList(contentL1).contains(row[4]); //check if already in lifestyle One 
			boolean existL2 = Arrays.asList(contentL2).contains(row[4]); //check if not duplicated
			if (!existL1 & !existL2) {        			
				((DefaultTableModel) genomeLifestyleTwo.getModel()).addRow(row);
			} else {
				logger.error("User attemped to include organisms more than one time: " 
						+ row[0].toString()
						+ " | " + row[4].toString());
			}
		} else {
			return; //do nothing
		}
	}

	/**
	 * @return 			Returns lifestyle Two table.	
	 */
	public static DefaultTableModel getLifestyleTwoTable(){
		DefaultTableModel table = (DefaultTableModel) genomeLifestyleTwo.getModel();
		return table;
	}

	/**
	 * Remove all rows from lifestyle Two table. 
	 */
	public static void emptyLifestyleTwo() {
		if (lockTables == false)
			((DefaultTableModel) genomeLifestyleTwo.getModel()).setRowCount(0);
	}

	/**
	 * Updates table content of lifestyle two. Used after the genome is 
	 * downloaded from GenBank. The accession number is substitute per the 
	 * path to the local file. Plus, the project ID is set to 'null', since 
	 * it is normally used to differentiate a local file from another that 
	 * needs to be downloaded.
	 * 
	 * @param accession				Accession number of the genome.
	 * @param localFileName			Full path to recently downloaded genome.
	 */
	public static void updateLifestyleTwo(String accession, String localFileName) {
		// Create object with all values in lifestyle One
		int rows = genomeLifestyleTwo.getModel().getRowCount();
		// Iterate throw rows
		for (int i = 0; i < rows; i++) {
			String rowValue = (String) genomeLifestyleTwo.getModel().getValueAt(i, 2);
			rowValue = rowValue.replaceAll("\\.[0-9]$", "");
			if(rowValue.equals(accession)){
				// Substitute accession number with local file path
				genomeLifestyleTwo.getModel().setValueAt(localFileName, i, 2); 
				// Remove project id
				genomeLifestyleTwo.getModel().setValueAt(null, i, 3);
				//
				return;
			}
		}
	}

	/**
	 * Gets the name assign for lifestyle two.
	 * 
	 * @return		String containing name of lifestyle two.
	 */
	public static String getLifestyleTwoName() {
		return jTextFieldL2.getText();
	}

	/**
	 * Verifies if any of the genomes assigned to the two lifestyles 
	 * list has to be download from GenBank. 
	 * 
	 * @return			True if any of the genomes in the lifestyle 
	 * 					lists has to be downloaded, and False otherwise. 
	 */
	public static boolean checkForDownload() {
		// Check all rows from lifestyle One
		int rows = genomeLifestyleOne.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			// If using local genomes, accessing the 5th column
			// suppose to return a NullPointerException.
			try {				
				if (!genomeLifestyleOne.getModel().getValueAt(i, 4).equals(null))
					continue;					
			} catch (NullPointerException e) {
				return true;				
			}
		}		
		// Check all rows from lifestyle Two
		rows = genomeLifestyleTwo.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			// If using local genomes, accessing the 5th column
			// suppose to return a NullPointerException.
			try {
				if (genomeLifestyleTwo.getModel().getValueAt(i, 4).equals(null)) {
					continue;
				}
			} catch (NullPointerException e) {
				return true;
			}
		}
		// Otherwise return false (no need to download)
		return false;
	}


	/**
	 * Validates number of organisms in each lifestyle 
	 * (at least five in each).
	 * @return	Returns True if both lifestyles present 
	 * 			at least five organisms; False otherwise.
	 */
	protected boolean validateNumberOfOrganisms() {
		Set<String> list = new HashSet<>();
		// Lifestyle One
		int rows = genomeLifestyleOne.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			list.add(genomeLifestyleOne.getModel().getValueAt(i, 0).toString());
		}
		if (list.size() < 5) {
			return false;
		}
		//------------------------//
		// Lifestyle Two
		list = new HashSet<>();
		rows = genomeLifestyleTwo.getModel().getRowCount();
		for (int i = 0; i < rows; i++) {
			list.add(genomeLifestyleTwo.getModel().getValueAt(i, 0).toString());
		}
		if (list.size() < 5) {
			return false;
		}
		//
		return true;
	}

	/** 
	 * Presses Confirm button.
	 * @return	Returns 'True' if all requirement where satisfied; 
	 * 			'False' otherwise. 
	 */
	public boolean pressConfirmButton() {
		jButtonUseGenomes.doClick();
		return selectionDone;
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void configureAvailableGenomesComponents() {		
		// Create and configure Available Genomes table
		availableGenomes = new CreateGenomeTable("NCBI");
		// Set scrollable view
		jScrollAvailableGenomes = new JScrollPane();
		jScrollAvailableGenomes.setViewportView(availableGenomes);
		if (availableGenomes.getColumnModel().getColumnCount() > 0) {
			availableGenomes.getColumnModel().getColumn(0).setPreferredWidth(50);
			availableGenomes.getColumnModel().getColumn(1).setPreferredWidth(30);
			availableGenomes.getColumnModel().getColumn(2).setPreferredWidth(220);
			availableGenomes.getColumnModel().getColumn(3).setPreferredWidth(10);
			// Hid column 'project id'
			availableGenomes.getColumnModel().getColumn(4).setWidth(0);
			availableGenomes.getColumnModel().getColumn(4).setMinWidth(0);
			availableGenomes.getColumnModel().getColumn(4).setMaxWidth(0);
		}      

		/*  Creating and configuring Filter combo box.
		 *  Main functions:
		 *  - Filter Taxonomic IDs;
		 *  - Filter organism names;
		 *  - Filter accesion number.  
		 */
		// Create the combo box
		final String[] filterStrings = { "Organism name", "Taxonomic ID", "Accession" };
		availableGenomeFilter = new JComboBox(filterStrings);
		// Add action
		availableGenomeFilter.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				if (availableGenomeFilter.getSelectedItem().equals("Organism name")){
					columnID = 0;
				}else if (availableGenomeFilter.getSelectedItem().equals("Taxonomic ID")) {
					columnID = 1;        	
				}else if (availableGenomeFilter.getSelectedItem().equals("Accession")) {
					columnID = 2;
				}
			}
		});

		// Create row filter
		rowSorter = new TableRowSorter<TableModel>(availableGenomes.getModel());
		availableGenomes.setRowSorter(rowSorter);
		// Create and configure Text field
		JTextFilterAvailableGenomes = new JTextField();			
		JTextFilterAvailableGenomes.setFont(LabelFontPlain); // Set font	   
		// Create row filter and add listener to JText
		JTextFilterAvailableGenomes.getDocument().addDocumentListener(new DocumentListener(){    	
			@Override
			public void insertUpdate(DocumentEvent e) {
				newRowFilter();			
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				newRowFilter();			
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				newRowFilter();			
			}

			/** Method to filter rows from 'Available Genome' table.
			 *  It is based on the status of ComboBox.
			 */
			private void newRowFilter() {
				// Extract typed text
				String text = JTextFilterAvailableGenomes.getText().toLowerCase();	        	
				// Works only if user types something
				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					// 0 == search for organism name
					if (columnID == 0){
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 2));
						// 1 == search for taxonomic id	
					} else if (columnID == 1){	        		
						rowSorter.setRowFilter(RowFilter.regexFilter("^" + text, 1));
						// 2 == search for accession number	
					} else if (columnID == 2){	        		
						rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0));
					}
				}
			}

		});
	}

	/**
	 * Inner class to create an Use Genomes button. It
	 * will verify if the genomes are ready to be used in 
	 * the analysis and report to the class that synchronizes  
	 * the pipeline.
	 */
	protected class CreateUseGenomesButton extends JButton {

		private static final long serialVersionUID = -8258659542387893199L;

		/**
		 * Creates and configures an Use Genomes button. It
		 * will verify if the genomes are ready to be used in 
		 * the analysis and report to the class that synchronizes  
		 * the pipeline.
		 */
		public CreateUseGenomesButton() {
			// Configure 'Next button' 
			setText("Use Genomes");
			setFont(ButtonFont);

			// Add action: checkpoint validation
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Start of message from Message Dialog
					String errorMessage = "Please apply the following modification(s):\n";
					// Dummy variable to test if some condition wasn't met
					String test = errorMessage;
					// Test if at least 4 genomes are being analyzed
					boolean checkOrganismNumbers = validateNumberOfOrganisms();
					if (checkOrganismNumbers == false) {
						errorMessage = errorMessage + "- Add at least five genomes to each lifestyles. \n";
					}
					// Test if user set lifestyles names
					if (lifestyle1 == null || lifestyle2 == null){
						errorMessage = errorMessage + "- Assign names to lifestyles. \n";
						// Test if lifestyles have different names	
					} else if (lifestyle1.equalsIgnoreCase(lifestyle2)) {        	
						errorMessage = errorMessage + "- Assign different names to lifestyles. \n";
					}
					// Test if Lifestyle 1 Table is empty
					try {
						if (genomeLifestyleOne.getValueAt(0, 0) == null) {
							errorMessage = errorMessage + "- Select organisms for lifestyle one. \n";
						}
					} catch (IndexOutOfBoundsException e) {
						errorMessage = errorMessage + "- Select organisms for lifestyle one. \n";
					}
					// Test if Lifestyle 2 Table is empty
					try {
						if (genomeLifestyleTwo.getValueAt(0, 0) == null) {
							errorMessage = errorMessage + "- Select organisms for lifestyle two. \n";        		
						}  
					} catch (IndexOutOfBoundsException e) {
						errorMessage = errorMessage + "- Select organisms for lifestyle two. \n";   
					}
					if (errorMessage != test) {
						selectionDone = false;
						JOptionPane.showMessageDialog(null, errorMessage, "Warning", JOptionPane.INFORMATION_MESSAGE);
					} else {
						// Change View to analysis
						//CreateAccordionMenu.setAnalysisView();
						//CreateMultiSplitPane.setPanel("Parameters");

						// Check if it is necessary to download anything
						boolean check = checkForDownload();
						if (check) { // Download
							PipelineSync.setDownloadComplete(false);
							DownloadGenomes download = new DownloadGenomes();
							download.exec();
							selectionDone = true;
						} else { // Move on to TransClust
							// Set download as incomplete
							PipelineSync.setDownloadComplete(true);
							// Update status and change panel
							ProgressPanel.displayStatusMsg("Ready for Analysis");
							selectionDone = true;
						}
					}
				}
			});   
		}
	}

	/**
	 * Inner class to create an Uncheck Boxes button. It
	 * will remove all selection from either the Available
	 * Genome table or one of the Lifestyles table. 
	 */
	protected class CreateUncheckButton extends JButton {

		private static final long serialVersionUID = 8443380879357851032L;

		/**
		 * Creates and configures an Uncheck Boxes button. It
		 * will remove all selection from the Available Genome 
		 * table (NCBI).
		 */
		public CreateUncheckButton() {
			// Configure label
			setText("Uncheck all");
			setFont(ButtonFont);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Set all row as unchecked (Column 3 == false)
					for(int row = 0;row <  availableGenomes.getRowCount();row++) {
						availableGenomes.setValueAt(false, row, 3);        		              
					}
				}
			});
		}

		/**
		 * Creates and configures an Uncheck Boxes button. It
		 * will remove all selection from one of the Lifestyles 
		 * table. 
		 * 
		 * @param lifestyleTable		JTable associated with a 
		 * 								given lifestyle.
		 */
		public CreateUncheckButton(final JTable lifestyleTable) {
			// Configure 'Uncheck All' Button (Lifestyle 1):
			setText("Uncheck all");
			setFont(ButtonFont);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Set all row as unchecked (Column 1 == false)
					for(int row = 0;row < ((DefaultTableModel) lifestyleTable.getModel()).getRowCount();row++) {
						lifestyleTable.setValueAt(false, row, 1);        		              
					}
				}
			});
		}
	}

	/**
	 * Inner class to create a button for removing genomes
	 * from one of the lifestyles (analysis pipeline). 
	 */
	protected class CreateDeselectButton extends JButton {

		private static final long serialVersionUID = 1880526988929865482L;

		/** Table model associated with a given lifestyle.*/
		protected DefaultTableModel model;

		/**
		 * Create and configures a button for removing genomes
		 * from one of the lifestyles (analysis pipeline). 
		 * 
		 * @param lifestyleTable 	JTable associated with a 
		 * 							given lifestyle.
		 */
		public CreateDeselectButton(final JTable lifestyleTable) {
			// Configure 'Deselect button - Lifestyle2': 
			model = (DefaultTableModel) lifestyleTable.getModel();
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Verify if any row is selected
					if (!lifestyleTable.getSelectionModel().isSelectionEmpty()) {
						// Create object that will store rows that shall not be removed
						Object[][] content = new Object[lifestyleTable.getRowCount()][3];
						// Interact with all rows in the model. Select rows that:
						// - Are *not* null;
						// - Are *not* checked.
						for(int row = 0;row < lifestyleTable.getRowCount();row++) {        		
							if (lifestyleTable.getValueAt(row, 0) != null){
								// Selected == false
								boolean testChecked = (boolean) lifestyleTable.getValueAt(row, 1); 
								if (!testChecked) { 
									content[row][0] = model.getValueAt(row, 0); //organism
									content[row][1] = model.getValueAt(row, 2); //accession
									content[row][2] = model.getValueAt(row, 3); //project id
								}
							}
						}
						// Remove the previous model
						model.setRowCount(0);
						// Add new model
						for(int i = 0;i < content.length;i++) {
							if (content[i][0] != null){
								model.addRow(new Object[] {content[i][0], false, content[i][1], content[i][2]});
							}
						}
						// Setting minimum row count
						if(model.getRowCount() == 0){
							//int addRow = 8 - modelL2.getRowCount();
							model.setRowCount(8);
						}        	
					}
				}
			});			
		}			
	}

	/**
	 * Inner class to  create a button for adding selected genomes 
	 * into one of the lifestyles. It does not allow genomes to 
	 * be added in both lifestyles.
	 */
	protected class CreateSelectButton extends JButton {

		private static final long serialVersionUID = 8746554465470171711L;

		/** Table model associated with a given lifestyle A.*/
		protected DefaultTableModel modelA;

		/** Table model associated with a given lifestyle B.*/
		protected DefaultTableModel modelB;

		/** Table model associated with Available Genome (NCBI).*/
		protected DefaultTableModel modelGT;

		/**
		 * Create and configures button for adding selected genomes 
		 * into one of the lifestyles. It does not allow genomes to 
		 * be added in both lifestyles.
		 * 
		 * @param genomesTable			JTable for Available Genome (NCBI). 
		 * @param lifestyleTableA		JTable for lifestyle A.
		 * @param lifestyleTableB 		JTable for lifestyle B.
		 */
		public CreateSelectButton(final JTable genomesTable, JTable lifestyleTableA, JTable lifestyleTableB) {

			modelA = (DefaultTableModel) lifestyleTableA.getModel();
			modelB = (DefaultTableModel) lifestyleTableB.getModel();
			modelGT = (DefaultTableModel) genomesTable.getModel();

			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Verify if any row is selected 
					if (!genomesTable.getSelectionModel().isSelectionEmpty()) {
						// Verify if selected row has null value
						int k = genomesTable.getSelectionModel().getMinSelectionIndex();
						if (genomesTable.getValueAt(k, 0) != null){
							/* Yes, there are easier ways of writing this condition.
							 * For some reason the logical approach did not worked */
							if(!(modelA.getValueAt(0, 0) != null)){ 
								modelA.setRowCount(0);
							}
							// Create object with all selected values 
							Object[] contentL1 = new Object[modelA.getRowCount()];
							for (int i = 0; i < modelA.getRowCount(); i++) {
								contentL1[i] = modelA.getValueAt(i, 0);
							}
							// Create object with all values in L2
							Object[] contentL2 = new Object[modelB.getRowCount()];
							for (int i = 0; i < modelB.getRowCount(); i++) {
								contentL2[i] = modelB.getValueAt(i, 0);
							}
							// Adding selected rows
							for(int row = 0;row < genomesTable.getRowCount();row++) {
								boolean test = (boolean) genomesTable.getValueAt(row, 3); // Selected == true
								boolean existL1 = Arrays.asList(contentL1).contains(genomesTable.getValueAt(row, 2)); // Verify if not duplicated
								boolean existL2 = Arrays.asList(contentL2).contains(genomesTable.getValueAt(row, 2)); // Verify if already in L2
								if (test && !existL1 && !existL2) {        			
									modelA.addRow(new Object[] {genomesTable.getValueAt(row, 2), false, genomesTable.getValueAt(row, 0), genomesTable.getValueAt(row, 4), null}); 
								}
							}
						}
						// Setting minimum row count
						if(modelA.getRowCount() == 0) {
							//int addRow = 8 - modelL1.getRowCount();
							modelA.setRowCount(8);
						}
					}
				}
			});
		}
	}

	/** 
	 * Inner class to configures the 'Load Genomes' button. It calls a class to 
	 * create a new frame. It gives the user the possibility to either download a 
	 * list of all available genomes from NCBI or to select a given local folder.
	 */
	protected class CreateLoadGenomeButton extends JButton {

		private static final long serialVersionUID = -9003256374571819820L;

		/**
		 * Creates and configures the 'Load Genomes' button. It calls a class to 
		 * create a new frame. It gives the user the possibility to either download a 
		 * list of all available genomes from NCBI or to select a given local folder.
		 */
		public CreateLoadGenomeButton() {
			// Configure
			setText("Load Genomes...");
			setFont(ButtonFont);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					new GenomeLoader();					
				}
			});
		}
	}
}
