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
package dk.sdu.imada.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Abstract class implement basic methods associated 
 * with JPanels. Plus, it contains the font that shall be 
 * used through out the application. 
 * 
 * @author Eudes Barbosa
 */
public abstract class AbstractPanel extends JPanel {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = -2156387461438991410L;
	
	private static final Logger logger = LogManager.getLogger(AbstractPanel.class.getName());

	/** Label font style - Plain. */
	protected final Font LabelFontPlain = new Font("Tahoma", Font.PLAIN, 14);
	
	/** Label font style - Bold. */
	protected final Font LabelFontBold = new Font("Tahoma", Font.BOLD, 14);
	
	/** Label font style - Italic. */
	protected final Font LabelFontItalic = new Font("Tahoma", Font.ITALIC, 14);

	/** Button font style - Plain. */
	protected final Font ButtonFont = new Font("Tahoma", Font.PLAIN, 12);
	
	/** Panel dimensions. */
	protected final Dimension PanelDimension = new Dimension(400, 400);
	
	//------  Declaration end  ------//
	
	/** Creates a white panel with an empty title border layout. */
	public AbstractPanel() {
		super();
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createTitledBorder(""));
	}	

	/**
	 * Configures the layout of the components
	 * within the panel.
	 */
	protected abstract void configureLayout();
	
	
	/** Initializes all objects associated with the panel. */
	protected abstract void initComponents();
	
		
	/**
	 * InnerClass creates a FormattedTextField where user will
	 * insert one of the parameters to be used in the Machine 
	 * Learning. Depending on the parameter different range of 
	 * values can be accepted. 
	 */
	protected class CreateFormattedTextField {

		//------  Variable declaration  ------//

		/** Formatted text field. */
		protected JFormattedTextField jText;
		
		/** Number Format. */
		protected NumberFormat numFormatAll;
		
		/** Number Formatter. */
		protected NumberFormatter numFormatter;
		
		/** Stores the value of the field. */
		protected int fieldValue;
		
		//------  Declaration end  ------//

		/**
		 * Creates a JFormattedTextField object using the provided range.
		 * 
		 * @param start			The start of the range of valid 
		 * 						numbers that can be used as parameters.
		 * 
		 * @param end			The end of the range of valid 
		 * 						numbers that can be used as parameters.
		 */
		public CreateFormattedTextField(int start, int end) {
			try {
				if (start < end){
					setFormatterRange(start, end);
				} else {
					throw new Exception();
				}
			} catch (Exception e) {
				logger.debug("Fail to create JFormattedTextField with provided range of values. " +
						"End of the range is smaller than the start. ");
				e.printStackTrace();
			}
			// Add listener
			addFormatterListener();

		}

		/**
		 * Returns the FormattedTextField that was just created.
		 * 
		 * @return		The just created FormattedTextField. It ranges
		 * 				was set up in the constructor.  
		 */
		public JFormattedTextField getNewFormattedTextField() {
			return jText;
		}


		/** Configures a FormattedTextField with defined range */
		private void setFormatterRange(int start, int end) {
			// Create number format for small values (K-fold and runs)
			numFormatAll = NumberFormat.getInstance();
			NumberFormatter numFormatter = new NumberFormatter(numFormatAll);
			numFormatter.setValueClass(Integer.class);
			numFormatter.setMinimum(start);
			numFormatter.setMaximum(end);
			numFormatter.setCommitsOnValidEdit(true); //commit on each keystroke instead of focus lost
			// Create JFormatterTextField
			jText = new JFormattedTextField(numFormatter);

		}


		/** Adds listener to current FormattedTextField. */
		private void addFormatterListener() {
			jText.getDocument().addDocumentListener(new DocumentListener(){    	
				@Override
				public void insertUpdate(DocumentEvent e) {
					setValue();
				}
				@Override
				public void removeUpdate(DocumentEvent e) {
					setValue();
				}
				@Override
				public void changedUpdate(DocumentEvent e) {
					setValue();
				}

				/** Sets number to value */
				private void setValue(){
					Runnable doAssist = new Runnable() {
						@Override
						public void run() {
							// Set value to user's choice, if any...  
							if (jText.getText().trim().length() == 0) {
								jText.setValue(null);
								fieldValue = -1;    		
							} else {
								try {  
									fieldValue = Integer.parseInt(jText.getText().replaceAll(",", ""));
								} catch(NumberFormatException nfe) {
									fieldValue = -1;
									jText.setValue(null);  
								}  
							}
						}
					};
					SwingUtilities.invokeLater(doAssist);
				}
			});	
		}
	}

}
