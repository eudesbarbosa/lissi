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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import dk.sdu.imada.gui.AbstractPanel;


/**
 * Class contains global variables required to create 
 * a 'Genome Selection' panel. Class used to organize code.
 * 
 * @author Eudes Barbosa
 */
public abstract class AbstractGenomeSelection extends AbstractPanel {


	//------  Variable declaration  ------//


	private static final long serialVersionUID = -3155939610492501862L;

	/** 
	 * Table stores information about genomes 
	 * available at NCBI.
	 */
	protected static JTable availableGenomes;
	
	/** Table stores information of genomes in Lifestyle One.*/
	protected static JTable genomeLifestyleOne;
	
	/** Table stores information of genomes in Lifestyle Two.*/
	protected static JTable genomeLifestyleTwo;
	
	/** ScrollPane for NCBI genomes Table. */
	protected JScrollPane jScrollAvailableGenomes;
	
	/** ScrollPane for Lifestyle One Table. */
	protected JScrollPane jScrollLifestyleOne;
	
	/** ScrollPane for Lifestyle Two Table. */
	protected JScrollPane jScrollLifestyleTwo;
	
	/** Text filter for NCBI genomes Table. */
	protected JTextField JTextFilterAvailableGenomes;
	
	/** Row sorter for NCBI genomes Table. */
	protected TableRowSorter<TableModel> rowSorter;
	
	/** Button to select genomes for Lifestyle One. */
	protected JButton jButtonSelectL1;
	
	/** Button to deselect genomes for Lifestyle One. */
	protected JButton jButtonDeselectL1;
	
	/** Button to select genomes for Lifestyle Two. */
	protected JButton jButtonSelectL2;
	
	/** Button to deselect genomes for Lifestyle Two. */
	protected JButton jButtonDeselectL2;
	
	/** 
	 * Use Genomes Button. It will verify if the genomes are 
	 * ready to be used in the analysis. <br> Not used in 
	 * this version.
	 */
	protected JButton jButtonUseGenomes;
	
	/** Uncheck selection for NCBI genomes Table. */
	protected JButton jButtonUncheck;
	
	/** Uncheck selection for Lifestyle One Table. */
	protected JButton jButtonUncheckL1;
	
	/** Uncheck selection for Lifestyle Two Table. */
	protected JButton jButtonUncheckL2;
	
	/** Label of  NCBI genomes Table. */
	protected JLabel jLabelAvailableGenomes = new JLabel();

	/** Label of  Lifestyle One table. */
	protected JLabel jLabelL1 = new JLabel();
	
	/** Label of  Lifestyle Two table. */
	protected JLabel jLabelL2 = new JLabel();
	
	/** Label associated with select genomes for Lifestyle One. */
	protected JLabel jLabelAdd = new JLabel();
	
	/** Label associated with deselect genomes for Lifestyle One. */
	protected JLabel jLabelRemove = new JLabel();
	
	/** Label associated with select genomes for Lifestyle Two. */
	protected JLabel jLabelAdd2 = new JLabel();
	
	/** Label associated with deselect genomes for Lifestyle Two. */
	protected JLabel jLabelRemove2 = new JLabel();
	
	/**
	 * Combo box for NCBI genomes Table. It filters by: <br>
	 * 	- Taxonomic IDs; <br>
	 *  - Organism names; <br>
	 *  - Accession numbers.  
	 */
	protected JComboBox<String> availableGenomeFilter;
	
	/** 
	 * Selected column on Combo Box associated 
	 * with NCBI genomes Table.
	 */
	protected int columnID = 0;
	
	/** Text Field with Lifestyle One name. Max 15 characters. */
	protected static JTextField jTextFieldL1 = new JTextField("One", 15);
	
	/** Text Field with Lifestyle Two name. Max 15 characters. */
	protected static JTextField jTextFieldL2 = new JTextField("Two",15);
	
	/** Lifestyle One name. */
	protected static String lifestyle1 = "One";
	
	/** Lifestyle Two name. */
	protected static String lifestyle2 = "Two";
	
	/** 
	 * Boolean variable to indicate if tables can be modified. 
	 * Tables should be locked while analysis is running.
	 */
	protected static boolean lockTables = false;
	
	/** 
	 * Boolean variable to indicate that necessary amount of
	 * genomes was selected for both lifestyles.
	 */
	protected boolean selectionDone = false;
	
	
	//------  Declaration end  ------//

}
