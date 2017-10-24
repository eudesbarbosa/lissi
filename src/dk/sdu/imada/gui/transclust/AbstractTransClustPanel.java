/**
 * 
 */
package dk.sdu.imada.gui.transclust;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.text.NumberFormatter;


/**
 * Class contains objects that will be used to 
 * create the 'TransClust' panel. Class use to 
 * organize the code.
 * 
 * @author Eudes Barbosa
 *
 */
public abstract class AbstractTransClustPanel extends TransclustInstructions {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = -3789457332892260460L;
	
	
	protected JFrame selectFilesFrame;
	protected JPanel jRight;
	protected JPanel jLeft;
	protected JLabel evalueLabel;
	protected JLabel singleThresholdLabel;
	protected JLabel multipleThresholdLabel;
	protected JLabel multipleLabelRange;
	protected JLabel multipleLabelSeparator;
	protected JLabel multipleLabelSteps;
	protected JSeparator separator;
	protected JLabel singleDensHelpButton;
	protected JLabel evalueHelpButton;
	protected JButton singleButton = new JButton();
	protected JButton multipleButton = new JButton();
	protected JButton loadFilesButton;
	protected JFormattedTextField evalueTextField;
	protected JFormattedTextField singleTextFieldValue;
	protected JFormattedTextField multipleTextFieldStartValue;
	protected JFormattedTextField multipleTextFieldEndValue;
	protected JFormattedTextField multipleTextFieldSteps;
	protected NumberFormatter numberFormatterAll;
	protected String userBlastFile = "";// "/home/eudes/LISSI/Results/Non-Pathogens_vs._Pathogens/Blast.out";	
	protected String userTransClustFile = "";// "/home/eudes/LISSI/Results/Non-Pathogens_vs._Pathogens/Cluster/Cluster_48.cls";


	//------  Declaration end  ------//

}
