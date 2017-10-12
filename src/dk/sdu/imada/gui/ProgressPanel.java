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


import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;

import net.miginfocom.swing.MigLayout;

/**
 * Class configures the objects associated with the Progress Panel.
 * It contains a ProgressBar, a 'kill' button, and a slider. The 
 * ProgressBar presents the progress of each individual activity. 
 * The 'Kill' button allows the stop of each activity. The slider
 * reports the number of available cores and the number of cores
 * to be used in the analysis.
 * 
 * @author Eudes Barbosa
 */
public class ProgressPanel extends AbstractProgressPanel {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = 5167986453576862317L;

	/** Progress bar. */
	private static JProgressBar jProgressBar;
	
	/** Progress label. It reports the current activity. */
	private static JLabel jLabelProcess;
	
	/** 
	 * Core slider. It let user define the number of cores 
	 * to be used.
	 */
	private static JSlider jSliderCores;
	
	/** Reports the number of available cores. */
	protected JLabel jAvailableCores;
	
	//------  Declaration end  ------//

	/** Configures the objects associated with the Progress Panel. */
	public ProgressPanel() {
		super();
		// Initialize components
		initComponents();
		// Configure layout
		configureLayout();
	}

	@Override
	protected void configureLayout() {
		// Configure layout
		setLayout(new MigLayout());
		//
		add(jButtonCancel, "split 5, span, align left");
		add(jProgressBar, " span, w ::5000");
		add(jLabelProcess, "span, gaptop 5");	
		//
		add(jAvailableCores, "span");
		add(jSliderCores, "span, gapleft 5, align right");
	}

	@Override
	protected void initComponents() {
		// Configure progress bar
		jProgressBar = new JProgressBar();
		jProgressBar.setVisible(true);
		jProgressBar.setIndeterminate(false);

		// Configure label associated with running process
		jLabelProcess = new JLabel();
		jLabelProcess.setText("               "); //dummy initialization
		jLabelProcess.setFont(LabelFontPlain);
		jLabelProcess.setMinimumSize(new java.awt.Dimension(700, 
				jLabelProcess.getPreferredSize().height));
		jLabelProcess.setPreferredSize(new java.awt.Dimension(600, 
				jLabelProcess.getPreferredSize().height));
		jLabelProcess.setMaximumSize(new java.awt.Dimension(jLabelProcess.getPreferredSize()
				.width, jLabelProcess.getPreferredSize().height));

		// Configure Kill Button
		jButtonCancel = new JButtonCancel();

		// Configure core-slider
		int cores = Runtime.getRuntime().availableProcessors(); //get available cores
		jSliderCores = new JSlider(JSlider.HORIZONTAL, 1, cores, 1);
		if(cores<8){
			jSliderCores.setMinorTickSpacing(1);
			jSliderCores.setMajorTickSpacing(1);
		} else{
			jSliderCores.setMinorTickSpacing(1);
			jSliderCores.setMajorTickSpacing(5);
		}
		jSliderCores.setPaintTicks(true);
		jSliderCores.setPaintLabels(true);
		jSliderCores.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

		// Configure label associated with core-slider 
		jAvailableCores = new JLabel();
		jAvailableCores.setFont(LabelFontPlain);
		jAvailableCores.setText("Available cores:");
	}

	/**
	 * Returns the selected number of processors.
	 * 
	 * @return		Integer that represents that 
	 * 				number of processors that user
	 * 				selected to be used.
	 */
	public static int getProcessors(){
		return jSliderCores.getValue();
	}

	/**
	 * Enables or disables the JSlider associated 
	 * with the number of available cores.
	 * 
	 * @param b		True to enable, and False to 
	 * 				disable.
	 */
	public static void enableProcessors(boolean b) {
		jSliderCores.setEnabled(b);
	}

	/** Updates progress bar value with the provided integer.
	 * 
	 *  @param value	Integer that represents the percentage of
	 *  				the work that was already performed. The 
	 *  				integer will be used to update the progress
	 *  				bar. 
	 */
	public static void updateProgressBarValue(int value) {
		jProgressBar.setValue(value);		
	}

	/** Updates progress bar value 
	 * 
	 * @param status		'True' if progress bar should be
	 * 						set as indeterminate, 'False' otherwise.		
	 */
	public static void updateProgressStatus(boolean status) {
		jProgressBar.setIndeterminate(status);		
	}


	/** Changes the status depending on what process is running.
	 *  
	 *  @param status		An informative text about the current process
	 *  					that is being executed.
	 */
	public static void displayStatusMsg(String status){
		jLabelProcess.setText(status);
	}
}
