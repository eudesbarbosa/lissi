/**
 * 
 */
package dk.sdu.imada.gui.genome;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 * @author eudesbarbosa
 *
 */
public class CreateGenomeTable extends JTable {

	private static final long serialVersionUID = 2540408396134973505L;

	// Variable declaration
	protected DefaultTableModel model;
	protected boolean editable = true;
	
	/**
	 * @param type
	 */
	public CreateGenomeTable(String type) {
		super();
		setBackground(Color.WHITE);
		//
		try {
			if (type.toLowerCase().equals("ncbi")){
				createAllGenomesModel();
			} else if (type.toLowerCase().equals("lifestyle")){
				createLifestyleModel();
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("Please check the documentation for a valid JTable type.");
			e.printStackTrace();
		}

	}


	/**
	 * Configure 'Lifestyle1 table': 
	 * Download selected organisms for lifestyle1
	 */
	private void createLifestyleModel() {
		// 
		model = new DefaultTableModel(
				new Object [][] {
						{null, null, null, null, null}
				},
				new String [] {
						"Organism", "Check", "accession", "project.id", "local.accession"
				}
				) {
			private static final long serialVersionUID = 1L;
			// Set types for cells from each column
			@SuppressWarnings("rawtypes")
			Class[] types = new Class [] {
				java.lang.Object.class, java.lang.Boolean.class, 
				java.lang.Object.class, java.lang.Object.class,
				java.lang.Object.class
			};
			// Set which cells can be edited
			boolean[] canEdit = new boolean [] {
					false, true, false, false, false
			};
			// Class returns if user is allowed to edit cell
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}    
			// Class returns cell type
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}  
			
		};
		// Set initial row number
		model.setRowCount(8);
		// Add model to table and create row sorter
		setModel(model);
		setAutoCreateRowSorter(true);


		// Add Selection Listener to the table
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = getSelectionModel();
				// Starts if-statement when some row is selected	
				if (!lsm.isSelectionEmpty()) {
					// Find out which indexes are selected
					int minIndex = lsm.getMinSelectionIndex();
					int maxIndex = lsm.getMaxSelectionIndex();
					// For all indexes in range set checked as true
					// If row is selected.
					for (int i = minIndex; i <= maxIndex; i++) {
						if (lsm.isSelectedIndex(i)) {
							// Convert the 'visualization' index to the model index
							int j = convertRowIndexToModel(i);
							// Set 'checked' to true
							model.setValueAt(true, j, 1);
						}
					}
				} 
			}
		});
	}

	/**
	 * Create and initialize table model for genomes list.
	 * Display all complete genomes available at NCBI or at
	 * specified folder. 
	 */
	private void createAllGenomesModel() {
		//
		model = new DefaultTableModel(
				// Initialize cell values	
				new Object [][] {
						{null, null, null, null, null}
				},
				// Set column names
				new String [] {
						"Accession", "Tax. Id", "Organism", "Check", "project.id",
				}
				) {
			private static final long serialVersionUID = 1L;
			// Set types for cells from each column
			@SuppressWarnings("rawtypes")
			Class[] types = new Class [] {
				java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
			};
			// Set which cells can be edited
			boolean[] canEdit = new boolean [] {
					false, false, false, true, false
			};            
			// Class returns cell type
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return types [columnIndex];
			}
			// Class returns if user is allowed to edit cell
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}                
		}; 
		// Set initial row number        
		model.setRowCount(30);
		// Add model to table and create row sorter
		setModel(model);
		setAutoCreateRowSorter(true);

		// Table header cannot be changed:
		getTableHeader().setReorderingAllowed(false);

		// Add Selection Listener to the table
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				ListSelectionModel lsm = getSelectionModel();
				// Starts if-statement when some row is selected	
				if (!lsm.isSelectionEmpty()) {
					// Find out which indexes are selected
					int minIndex = lsm.getMinSelectionIndex();
					int maxIndex = lsm.getMaxSelectionIndex();
					// For all indexes in range set checked as true
					// If row is selected.
					for (int i = minIndex; i <= maxIndex; i++) {
						if (lsm.isSelectedIndex(i)) {
							// Convert the 'visualization' index to the model index
							int j = convertRowIndexToModel(i);
							// Set 'checked' to true
							model.setValueAt(true, j, 3);
						}
					}
				} 
			}
		});
	}




}
