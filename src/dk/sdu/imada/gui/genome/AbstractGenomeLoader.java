/**
 * 
 */
package dk.sdu.imada.gui.genome;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dk.sdu.imada.gui.AbstractPanel;

/**
 * Class contains objects that will be used to 
 * create the 'Load Genomes' panel. Class use to 
 * organize the code.
 * 
 * @author Eudes Barbosa
 *
 */
public abstract class AbstractGenomeLoader extends AbstractPanel {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = 3547236768174701114L;

	/** TODO : Find out this does */
	protected ButtonGroup group;
	
	/** */
	protected JRadioButton downloadList;
	
	/** */
	protected JRadioButton loadLocal;
	
	/** */
	protected JLabel folderLife1;
	
	/** */
	protected JLabel folderLife2;
	
	/** */
	protected JTextField jTextFieldPathLife1;
	
	/** */
	protected JTextField jTextFieldPathLife2;
	
	/** */
	protected JFileChooser jFileChooser;
	
	/** */
	protected JButton jButtonFolder1;
	
	/** */
	protected JButton jButtonFolder2;
	
	/** */
	protected JButton jButtonCancel;
	
	/** */
	protected static JButton jButtonConfirm;
	
	/** */
	protected boolean loadingDone = false;

	/** */
	protected boolean directoryModification = false;

	//------  Declaration end  ------//


}
