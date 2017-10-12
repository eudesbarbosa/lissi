/**
 * 
 */
package dk.sdu.imada.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;


/**
 * Abstract class contains the main components 
 * required for a Progress JPanel.
 * 
 * @author Eudes Barbosa (eudes@imada.sdu.dk)
 */
public abstract class AbstractProgressPanel extends AbstractPanel {
	
	private static final long serialVersionUID = 338309150073137125L;
	
	/** A generic Cancel Button. */
	protected static JButton jButtonCancel;

	
	/**
	 * Enables or disable 'Kill Button'.
	 * 
	 * @param use	'True' if there is button should be set
	 * 				as enable, 'False' otherwise.
	 */
	public static void enableKillButton(boolean use){
		if(use == true) {
			jButtonCancel.setEnabled(true);			
		} else {
			jButtonCancel.setEnabled(false);			
		}
	}

	/**
	 * Provides an action to 'Kill Button' depending on what
	 * process is currently being executed.
	 * 
	 * @param action		Action to cancel that allows user to
	 * 						cancel current process.
	 */
	public static void setActionKillButton(ActionListener action){
		jButtonCancel.addActionListener(action);
	}

	/**
	 * Removes current action from 'Kill Button'.
	 */
	public static void cleanActionKillButton(){
		jButtonCancel.removeActionListener(jButtonCancel.getAction());
	}

	/**
	 * Click to cancel current process.
	 */
	public static void clickCancel() {
		jButtonCancel.doClick();		
	}	
}