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


import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Abstract class implement common methods for JPanels 
 * associated with parameters: Evolutionary Sequence Analysis 
 * (Transitivity Clustering); Island Detection (Gecko); and 
 * Statistical Learning Methods (Random Forest).
 * <br><br>
 * - Load Icon <br>
 *  Free for non-commercial use. <br>
 *  https://www.iconfinder.com/icons/175319/add_file_icon <br>
 *  Author: (unknown) <br>
 * 
 * @author Eudes Barbosa
 */
public abstract class AbstractParameterPanel extends AbstractPanel {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = -2156387461438991410L;

	private static final Logger logger = LogManager.getLogger(AbstractParameterPanel.class);
	
	//------  Declaration end  ------//
	
	
	/** Reset all parameters to their default values */
	public abstract void resetValues();

	/**
	 * Enables or disables all components of the 
	 * panel.
	 *  
	 * @param b		True enable all components from the panel 
	 * 				(user can modify values); and False, otherwise.
	 */
	public void enableComponents(Boolean b) {
		// Get all components
		Component[] components = getComponents();
		int size = components.length;
		// Iterate through components
		for(int i = 0; i < size; i++) {
			components[i].setEnabled(b);			
		}
	}

	/**
	 * @param url		Path to HTML file that shall  
	 * 					be open. It should contain 
	 * 					instructions on	how to use  
	 * 					a certain parameter.
	 * 
	 * TODO: modify url to just reference (#id)
	 * 
	 * @param par 		The name of the parameter.
	 * 
	 * @return			Returns help button pointing 
	 * 					to designed HTML file.	
	
	protected JLabel createHelpLabel(String url, String par) {
		
		String text = "<html><FONT color=\"#000099\">(?)</FONT></html>";
		JLabel helpButton = new JLabel(text);
		helpButton.addMouseListener(new OpenUrlAction(url, par));
		return helpButton;
	}
	 */
	
	/**
	 * @param owner			Owner JFrame (can be null).
	 * @param instruction	Instructions on how to set the 
	 * 						parameter.
	 * @param manual		Link to online manual (can be null).
	 * @param paper			Link to online paper (can be null).
	 * @return				Returns a help label with instructions 
	 * 						about the parameters.
	 */
	protected JLabel createHelpLabel(JFrame owner, String instruction, String manual, String paper) {
		String text = "<html><FONT color=\"#000099\">(?)</FONT></html>";
		JLabel helpButton = new JLabel(text);
		helpButton.addMouseListener(new ViewHelp(owner, instruction, manual, paper));
		return helpButton;
	}

	/**
	 * Shows warning messages.
	 * @param msg	Message to be shown.
	 */
	protected void sendWarningMsg(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Warning", JOptionPane.INFORMATION_MESSAGE);
		return;	
	}

	/**
	 * Inner-class creates new 'Load File' button.
	 */
	protected class CreateLoadFilesButton extends JButton {

		private static final long serialVersionUID = 3942510043773539757L;

		// Variable declaration
		protected BufferedImage retLoad;
		protected BufferedImage retLoaded;

		/**
		 * Creates new 'Load File' button.
		 */
		public CreateLoadFilesButton() {
			// Configure button
			super();
			// Configure appearance 
			setContentAreaFilled(false);
			setBorderPainted(true); 
			setFocusPainted(true); 
			setOpaque(false);

			// Configure icon
			BufferedImage img;
			try {
				// Load image
				ClassLoader cl = CreateAccordionMenu.class.getClassLoader();
				img = ImageIO.read(cl.getClass().getResource("/images/load.png"));
				// Resize image
				retLoad = new BufferedImage(21,21,BufferedImage.TYPE_INT_ARGB);
				retLoad.getGraphics().drawImage(img,0,0,21,21, null);
				// Set as icon
				setIcon(new ImageIcon(retLoad));

				// Loaded image
				img = ImageIO.read(cl.getClass().getResource("/images/loaded.png"));
				// Resize image
				retLoaded = new BufferedImage(21,21,BufferedImage.TYPE_INT_ARGB);
				retLoaded.getGraphics().drawImage(img,0,0,21,21, null);							

			} catch (IOException e) {
				logger.debug("It was not possible to use 'load' icon.");
				e.printStackTrace();
			}			
		}


		/**
		 * Changes the icon type between two options: i) 'load', 
		 * when no file was selected (black); and ii) 'loaded', 
		 * when at least one file was selected (green).
		 *  
		 * @param icon		Kind of icon that should be displayed.
		 * @throws Exception
		 */
		public void changeLoadIcon(String icon) throws Exception {
			if (icon.toLowerCase().equals("load"))
				setIcon(new ImageIcon(retLoad));
			else if (icon.toLowerCase().equals("loaded"))
				setIcon(new ImageIcon(retLoaded));
			else
				throw new Exception("Unsupported icon option.");
		}

	}

	/**
	 * InnerClass creates a FormattedTextField where user will
	 * insert one of the parameters to be used in the Machine 
	 * Learning. Depending on the parameter different range of 
	 * values can be accepted. 
	 */
	protected class CreateFormattedTextField {

		// Variable declaration
		protected JFormattedTextField jText;
		protected NumberFormat numFormatAll;
		protected NumberFormatter numFormatter;
		protected int fieldValue;
		// End of declaration

		/**
		 * Creates a JFormattedTextField object using the provided range.
		 * 
		 * @param start			The start of the range of valid 
		 * 						numbers that can be used as parameters.
		 * 
		 * @param end			The end of the range of valid 
		 * 						numbers that can be used as parameters.
		 */
		public CreateFormattedTextField(int start, int end){
			try {
				if (start < end) {
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

		/** Configures a FormattedTextField with defined range. */
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

	/**
	 * Inner class add action to help button. It opens 
	 * and URL with specific instructions on how to use 
	 * a given parameter. 
	 */
	protected class OpenUrlAction extends MouseAdapter {

		// Variables declaration
		File htmlFile = null;
		String parameter = "";
		// Declaration end

		/** 
		 * Sole constructor. It specifies which URL shall 
		 * be open when button is pressed by the user.
		 *
		 * @param url		Path to HTML file that shall  
		 * 					be open. It should contain 
		 * 					instructions on	how to use  
		 * 					a certain parameter.
		 * @param par The name of the parameter.
		 */
		public OpenUrlAction(String url, String par) {
			URL urlFile = this.getClass().getClassLoader().getResource(url);
			try {
				JarURLConnection connection = (JarURLConnection) urlFile.openConnection();
				this.htmlFile = new File(connection.getJarFileURL().toURI());
			} catch (URISyntaxException | ClassCastException| IOException e) {
				try {
					this.htmlFile = new File(urlFile.toURI());
				} catch (URISyntaxException e1) {
					this.htmlFile = new File(urlFile.getPath());
				}
			}
			this.parameter = par;
		}

		@Override
		public void mouseClicked(MouseEvent me) {
			super.mouseClicked(me);
			open(htmlFile);			
		}

		/**
		 * Opens URL.
		 * @param uri		The URL that shall be open. It 
		 * 					should contain instructions on 
		 * 					how to use a certain parameter.
		 */
		private void open(File htmlFile) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(htmlFile.toURI());
				} catch (IOException e) { 
					logger.debug("Problem while loading HTML for " + parameter + ".");
					e.printStackTrace();
				}
			} else {
				logger.debug("Browser option is not available.");
				// Send error message
				String errorMessage = "Browser is not available...";
				JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
