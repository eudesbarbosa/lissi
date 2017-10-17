/**
 * 
 */
package dk.sdu.imada.methods;

import javax.swing.JOptionPane;

import dk.sdu.imada.gui.ProgressPanel;


/**
 * Class contains generic exception procedure for 
 * when a crutial part of the pipeline fails.
 * 
 * @author Eudes Barbosa (eudes@imada.sdu.dk) 
 */
public class BrokePipelineException extends Exception {

	private static final long serialVersionUID = -7647408722972325653L;

	/** 
	 * Creates generic exception procedure for 
	 * when a crutial part of the pipeline fails.
	 * 
	 * @param message	Message to be display on 
	 *  				progress panel.
	 * @param e			The original exception.
	 */
	public BrokePipelineException(String message, Exception e) {
		// Cancel pipeline
		ProgressPanel.clickCancel();
		// Change message
		String error = "Process failed : " + message + ". Execution halted...\n";
		ProgressPanel.displayStatusMsg("Error...");
		ProgressPanel.updateProgressStatus(false);
		// Display error panel
		JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
		// Print exception
		e.printStackTrace();
	}

}