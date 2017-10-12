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


import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dk.sdu.imada.gui.genome.GenomeLoader;
import dk.sdu.imada.gui.genome.GenomeSelection;

import net.miginfocom.swing.MigLayout;

/**
 * Class creates a Wizard-like panel to guide the user 
 * through LiSSI pipeline.
 * 
 * @author Eudes Barbosa	
 */
public class WizardPanel extends AbstractPanel {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = 443546394673666195L;

	/** Initial panel with a (cool) logo. */
	protected final int LOGO = 0;

	/** Genome loader panel. */
	protected final int GENOME_LOADER = 1;

	/** Genome selection panel. */
	protected final int GENOME_SELECTION = 2;

	/** Analysis panel. */
	protected final int ANALYSIS = 3;

	/** Current panel index number. */
	protected int currentPanel = 1;

	/** Layered panel to create a wizard-like flow. */
	protected static JPanel layeredPanel;

	/** Genome loader panel. */
	protected GenomeLoader genomeLoader;

	/** Genome selection panel. */
	protected GenomeSelection genomeSelection;

	/** Parameters panel. */
	protected ParametersPanel parametersPanel;

	/** Back button. */
	protected JButton backButton;

	/** Next button. */
	protected JButton nextButton;

	/** Run button. */
	protected static JButton runButton;
	
	//------  Declaration end  ------//



	/** Instantiate Wizard panel. */
	public WizardPanel() {
		super();
		initComponents();
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		setLayout(new MigLayout());
		setPreferredSize(PanelDimension);
		add(backButton, "split 3, span, align center");
		add(nextButton, "span");
		add(runButton, "span, wrap");
		add(layeredPanel,"align center, push, grow, wrap");
	}

	@Override
	protected void initComponents() {
		// Configure layered panel
		layeredPanel = configLayeredPanel();
		// Configure buttons
		backButton = new BackButton();
		nextButton = new NextButton();
		runButton = new RunButton();
	}

	/** @return Returns the configured layered panel. */
	protected JPanel configLayeredPanel() {
		// Configure
		JPanel panel = new JPanel(new CardLayout());
		// Add panels
		//panel.add(new IntroPanel(), Integer.toString(LOGO));
		panel.add(genomeLoader = new GenomeLoader(), Integer.toString(GENOME_LOADER));
		panel.add(genomeSelection = new GenomeSelection(), Integer.toString(GENOME_SELECTION));
		panel.add(parametersPanel = new ParametersPanel(), Integer.toString(ANALYSIS));
		//		
		return panel;		
	}

	/**
	 * Sets which of the panels inside the Layered Panel should be visible.
	 * @param panel			The index of the panel that should visible. 
	 */
	public static void setPanel(int panel){
		CardLayout cardLayout = (CardLayout) layeredPanel.getLayout();
		cardLayout.show(layeredPanel, Integer.toString(panel));
	}

	/**
	 * Enables/Disables back button.
	 * @param b		True to enable; False otherwise. 
	 */
	protected void enableBackButton(boolean b) {
		backButton.setEnabled(b);
	}

	/**
	 * Enables/Disables next button.
	 * @param b		True to enable; False otherwise. 
	 */
	protected void enableNextButton(boolean b) {
		nextButton.setEnabled(b);
	}

	/**
	 * Enables/Disables run button.
	 * @param b		True to enable; False otherwise. 
	 */
	public static void enableRunButton(boolean b) {
		runButton.setEnabled(b);
	}

	/** Inner-class configures back button. */
	protected class BackButton extends JButton {

		private static final long serialVersionUID = 1L;

		/** Instantiate class.*/
		public BackButton() {
			// Set label
			setText("< Back");
			setFont(ButtonFont);
			setEnabled(false);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					// Set new panel
					int panel = currentPanel - 1;
					if (panel == 0) {  //after logo
						setPanel(panel);
						enableBackButton(false);
					} else if (panel == 2) { //after genome loader
						setPanel(panel);
						enableRunButton(false);
					} else {
						setPanel(panel);
					}	
					currentPanel = panel;
					enableNextButton(true);
					CreateAccordionMenu.setSelectedMethod(panel-1);
				}					
			});
		}
	}

	/** Inner-class configures next button. */
	protected class NextButton extends JButton {

		//------  Variable declaration  ------//

		private static final long serialVersionUID = 5379456881201816652L;

		/** Boolean indicates if side bar was already expanded. */
		protected boolean alreadyExpanded = false;
		
		//------  Declaration end  ------//

		/** Instantiate class.*/
		public NextButton() {
			// Set label
			setText("Next >");
			setFont(ButtonFont);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					int panel = currentPanel + 1;
					if (panel == 3) { // after genome selection
						boolean genomeSelected = checkGenomeSelection();
						if (genomeSelected) {
							setPanel(panel);
							enableNextButton(false);
							enableRunButton(true);
							update(panel); //update							
						}
					} else if (panel == 2) { //after genome loader
						boolean genomeLoader = checkGenomeLoader();
						if (genomeLoader) {
							if (alreadyExpanded == false) {
								CreateAccordionMenu.expandWizardTab();
								alreadyExpanded = true;
							}
							setPanel(panel);
							update(panel); //update	
						}
					}
					/* Not used if Logo panel is ignored. 
					} else if (panel == 1) { //after logo
						setPanel(panel);
						if (alreadyExpanded == false) {
							CreateAccordionMenu.expandWizardTab();
							alreadyExpanded = true;
						}
						update(panel); //update	
					}*/
					
				}
				
				/** 
				 * Updates Wizard panel. 
				 * @param panel	Panel number.
				 */
				private void update(int panel) {
					// Update
					currentPanel = panel;
					enableBackButton(true);	
					CreateAccordionMenu.setSelectedMethod(panel-1);
				}
			});
		}

		/**
		 * @return 	True if genomes were properly loaded; 
		 * 			False, otherwise. 
		 */
		protected boolean checkGenomeLoader() {				
			return genomeLoader.pressConfirmButton();
		}

		/**
		 * @return 	Returns True if genomes were already selected; 
		 * 			and False, otherwise. 
		 */
		protected boolean checkGenomeSelection() {
			return genomeSelection.pressConfirmButton();
		}
	}

	/** Inner-class configures run button. */
	protected class RunButton extends JButton {

		//------  Variable declaration  ------//

		private static final long serialVersionUID = 2709234682778692930L;
		
		//------  Declaration end  ------//


		/** Instanciate class.*/
		public RunButton() {
			// Set label
			setText("Run");
			setFont(ButtonFont);
			setEnabled(false);
			// Set Action
			addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					parametersPanel.pressConfirmButton();
				}
			});
		}
	}

	/** 
	 * Inner-class configures introduction panel. <br>
	 * Not used in this version.
	  */
	protected class IntroPanel extends AbstractPanel {
		
		//------  Variable declaration  ------//

		private static final long serialVersionUID = 1L; 

		/** LiSSI presentation text. */
		final protected String TEXT = "<html><b>LiSSI</b> was developed to identify islands mainly associated " +
				"with a given lifestyle. Optionally, the tool can be used without the islands " +
				"detection. In that case, it will report putative homologous genes that are mainly " +
				"associated with a given life-style. </html>";
				//"<br><br>Version 1.0<br><br>For help: " +
				//"<FONT color=\"#000099\">eudes@imada.sdu.dk</FONT></html>";
		
		//------  Declaration end  ------//


		/** Instanciate class. */
		public IntroPanel() {
			configureLayout();
		}

		@Override
		protected void configureLayout() {
			// Configure panel
			setBackground(Color.WHITE);
			setLayout(new MigLayout());
			// Create label with logo
			ClassLoader cl = WizardPanel.class.getClassLoader();
			ImageIcon img = new ImageIcon(cl.getClass().getResource("/images/lissi_logo.png"));
			JLabel logo = new JLabel();
			logo.setIcon(img);
			// Create text area
			JLabel textarea = new JLabel();
			StringBuilder sb = new StringBuilder(64);
			sb.append(TEXT);
			textarea.setFont(LabelFontPlain);
			textarea.setText(sb.toString());
			// Add to panel
			add(logo, "align center, top, push, grow 100 100, gapleft 15, wrap");
			add(textarea,"gapleft 15, growy, align center");	
		}

		@Override
		protected void initComponents() { }		
	}
}
