/**
 * 
 */
package dk.sdu.imada.methods;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.gui.CreateAccordionMenu;
import dk.sdu.imada.gui.CreateMultiSplitPane;
import dk.sdu.imada.gui.MainFrame;
import dk.sdu.imada.gui.ParametersPanel;
import dk.sdu.imada.gui.ProgressPanel;
import dk.sdu.imada.gui.WizardPanel;
import dk.sdu.imada.gui.genome.GenomeLoader;
import dk.sdu.imada.gui.genome.GenomeSelection;
import dk.sdu.imada.gui.transclust.ViewResults;
import dk.sdu.imada.methods.gecko.Gecko;
import dk.sdu.imada.methods.gecko.RunGecko;
import dk.sdu.imada.methods.genome.Genome;
import dk.sdu.imada.methods.genome.GenomeParser;
import dk.sdu.imada.methods.genome.GenomeWithTransClustParser;
import dk.sdu.imada.methods.statistics.RandomForest;
import dk.sdu.imada.methods.statistics.RunRandomForest;
import dk.sdu.imada.methods.transclust.ParseTransClustResults;
import dk.sdu.imada.methods.transclust.RunTransClust;
import dk.sdu.imada.methods.transclust.TransClust;


/**
 * Class synchronizes the analysis pipeline. It can be 
 * based either on putative groups of homology proteins 
 * (clusters) or on putative genomic islands. 
 * <br>
 * > Clusters (as described in the Briefings and 
 * Functional Genomics paper): it simply cluster the genes 
 * into potential homologous groups and attempts to find 
 * the most discriminant clusters for each lifestyle.
 * <br>
 * > Islands:<br>
 * i) cluster the genes into potential homologous 
 * groups; ii) finds regions with conserved gene clusters 
 * order (islands); and iii), attempts to find the most 
 * discriminant islands for each lifestyle.
 * 
 * @author Eudes Barbosa
 */
public class PipelineSync implements Command, Cancelled {

	//------  Variable declaration  ------//
	
	private static final Logger logger = LogManager.getLogger(PipelineSync.class.getName());

	/** Queue executor for external services. */
	public static ExecutorQueueService queueExecutorExternalServices =
			new ExecutorQueueService();
	
	protected static boolean downloadComplete = false;
	protected static boolean processInterrupted;
	protected boolean runGecko = false;
	protected static SwingWorker<Void, Void> worker = null;
	
	/** Genome parser process. */
	protected GenomeParser parseG = null;
	
	/** TransClust (homology detection) process. */
	protected RunTransClust clust = null;	
	
	/** Gecko (island detection) process. */
	protected RunGecko gecko = null;
	
	/** Random Forest process. */
	protected RunRandomForest learning = null;
	
	/** 
	 * Object containing all the required Transitivity 
	 * Clustering parameters (Homology detection). 
	 */				
	protected TransClust t = null;
	
	/** 
	 * Object containing all the required Gecko 
	 * parameters (island detection).
	 */
	protected Gecko g = null;
	
	/** 
	 * Object containing all the required Random 
	 * Forest parameters. 
	 */
	protected RandomForest rf = null;
	
	
	protected static List<Genome> genomes = new ArrayList<Genome>();
	protected static ArrayList<String> clusters = new ArrayList<String>();
	
	//protected ParseTransClustResults parseTC = null;


	//------  Declaration end  ------//


	/**
	 * Creates a pipeline that simply cluster the genes into 
	 * potential homologous groups and attempts to find the most 
	 * discriminant clusters for each lifestyle. In other words, 
	 * it to analyze the genomes as described in the Briefings and 
	 * Functional Genomics paper. 
	 *  
	 * @param t		An object containing all the required Transitivity Clustering 
	 * 				parameters.
	 * @param rf	An object containing all the required Random Forest parameters.
	 */
	public PipelineSync(TransClust t, RandomForest rf) {
		this.t	= t;
		this.rf = rf;			
		// NOT using Gecko
		this.runGecko = false;
	}

	/**
	 * Creates a pipeline to: i) cluster the genes into potential homologous 
	 * groups; ii) finds regions with conserved gene clusters order (islands); 
	 * and iii), attempts to find the most discriminant islands for each lifestyle.
	 * 
	 * @param t		An object containing all the required Transitivity Clustering 
	 * 				parameters.
	 * 
	 * @param g		An object containing all the required Gecko parameters.
	 * 
	 * @param rf	An object containing all the required Random Forest parameters.
	 */
	public PipelineSync(TransClust t, Gecko g, RandomForest rf) {
		this.t	= t;
		this.g = g;
		this.rf = rf;		
		// Using Gecko
		this.runGecko = true;
	}

	@Override
	public void exec() {
		// Initiate variable
		processInterrupted = false;

		// Disable option/selection buttons
		enableSelectionOptions(false);	
		WizardPanel.enableRunButton(false);

		// Add functionality to cancel button
		addCancelButtonFunction();

		// Create results inner bar
		CreateAccordionMenu.addResultInnerBar();

		// Clean previous results (if any)
		CommomMethods.cleanDirectories();
		
		// Global pipeline
		// Create inner class to run tasks in the background
		worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				//GenomeParser parse = new GenomeParser(genomesL1, nameL1, genomesL2, nameL2);
				//parse.exec();	
				//-------------------------------------//
				// 		Initialize layered panels	   //
				//-------------------------------------//
				initializePanels();

				//-------------------------------------//
				// Parse genomes and insert info in DB //
				//-------------------------------------//

				// Verify if download was completed
				if (downloadComplete == false) {
					String info = "Analysis will only start when genomes were downloaded.";
					JOptionPane.showMessageDialog(null, info, null, JOptionPane.INFORMATION_MESSAGE);
				}
				checkDownloadCompletion();		
				
				// Parse
				if (!worker.isCancelled()) {
					logger.info("Parsing GenBank files.");	
					// Lifestyle One
					DefaultTableModel genomesL1 = GenomeSelection.getLifestyleOneTable();
					String nameL1 = GenomeSelection.getLifestyleOneName();
					// Lifestyle Two
					DefaultTableModel genomesL2 = GenomeSelection.getLifestyleTwoTable();
					String nameL2 = GenomeSelection.getLifestyleTwoName();
					//
					/*if (t.getUserTransClustFile().equals("")) {
						parseG = new GenomeParser(genomesL1, nameL1, genomesL2, nameL2);
					} else {
						parseG = new GenomeWithTransClustParser(genomesL1, nameL1, 
								genomesL2, nameL2, t.getUserTransClustFile()); */
					String transClustTEMP = "/media/eudesbarbosa/INTENSO/DK/Computational_Biology/" +
							"LiSSI_2015/Actinobacteria/Results/Non-Pathogens_vs._Pathogens/Gecko/Cluster_USEARCH_.75.cls";
					parseG = new GenomeWithTransClustParser(genomesL1, nameL1, 
							genomesL2, nameL2, transClustTEMP);
						clusters =  ((GenomeWithTransClustParser) parseG).getClusters();
					//}
					parseG.exec();					
					genomes = parseG.getGenomes();					
				}
				
				
				
				
				
				// TODO : remove it
				//-----------------------------------------------------------------//
				String geckoTEMP = "/media/eudesbarbosa/INTENSO/DK/" +
						"Computational_Biology/LiSSI_2015/Actinobacteria/Results/" +
						"Non-Pathogens_vs._Pathogens/Gecko/Gecko_q10.out";
				learning = new RunRandomForest(rf, geckoTEMP);
				learning.exec();
				//-----------------------------------------------------------------//
				
				
				
				

				//-----------------------------//
				// Run Transitivity Clustering //
				//-----------------------------//				
				if (!worker.isCancelled()) { 
					if (t.getUserTransClustFile().equals("")) {
						//---------------------------------------//
						// Run TransClust
						clust = new RunTransClust(t);
						clust.exec();
						//---------------------------------------//
						// Update genome information with cluster id's					
						clusters = clust.getClusters();
						// Lifestyle One
						DefaultTableModel genomesL1 = GenomeSelection.getLifestyleOneTable();
						String nameL1 = GenomeSelection.getLifestyleOneName();
						// Lifestyle Two
						DefaultTableModel genomesL2 = GenomeSelection.getLifestyleTwoTable();
						String nameL2 = GenomeSelection.getLifestyleTwoName();
						// Parse
						parseG = new GenomeWithTransClustParser(genomesL1, nameL1, 
								genomesL2, nameL2, clusters);
						parseG.exec();					
						genomes = parseG.getGenomes();
						/*
						long genesAnalyzed = clusters.size();
						if (genesAnalyzed < 300000) { 
							UpdateGenomesHashMap updated = new UpdateGenomesHashMap();
							genomes = updated.updateGenomes(genomes, clusters);
						} else {
							UpdateGenomesHashMap updated = new UpdateGenomesHashMapLarge();
							genomes = updated.updateGenomes(genomes, clusters);
						}
						 */
					} else { //cluster file provided by the user
						// View results - send TransClust object
						ViewResults view = new ViewResults(t, clusters);
						view.exec();
					}
				}

				//-----------//
				// Run Gecko //
				//-----------//				
				/* Check if it is necessary to include Gecko  
				 * in the pipeline. 
				 */
				if (!worker.isCancelled()) {
					if (runGecko == true) {
						// Run Gecko
						gecko = new RunGecko(g);
						gecko.exec();
					}
				}

				//-------------------//
				// Run Random Forest
				//-------------------//
				if (!worker.isCancelled()) {
					logger.info("Running Statistical Methods.");
					// Run random forest
					if (runGecko == false)
						learning = new RunRandomForest(rf);
					else
						learning = new RunRandomForest(rf, gecko.getGeckoOut());
					learning.exec();
				}
				// END
				return null;
			} 

			/** Reports process completed as expected. */
			protected void done() {
				// Update status and enable buttons
				ProgressPanel.displayStatusMsg("LiSSI analysis done. Please check results.");
				ProgressPanel.updateProgressStatus(false);
				ProgressPanel.enableProcessors(true);
				ProgressPanel.cleanActionKillButton();
				ProgressPanel.enableKillButton(false);
				enableSelectionOptions(true);	
				WizardPanel.enableRunButton(true);
			}
		};
		//
		worker.execute();
	}

	/**
	 * Initialize layered panel with empty ones while 
	 * the process is running.
	 */
	protected void initializePanels() {
		// Get lifestyle names
		String lifeOne = GenomeSelection.getLifestyleOneName();
		String lifeTwo = GenomeSelection.getLifestyleTwoName();
		// Clustering panels
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), "Clustering Information");
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), "Clustering Histogram");
		// Machine Learning panels
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), "Joint Distribution");
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), "Full data");
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), "Decision tree - Full data");
		//
		String panelName = "Bias Class " + lifeOne;
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), panelName);
		panelName = "Decision Tree - Class " + lifeOne;
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), panelName);
		//		
		panelName = "Bias Class " + lifeTwo;
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), panelName);
		panelName = "Decision Tree - Class " + lifeTwo;
		CreateMultiSplitPane.updateLayeredPane(new JPanel(), panelName);
	}

	/**
	 * Creates a small thread that constantly checks 
	 * if all genomes were already download. When the 
	 * download is complete the pipeline can start. 
	 */
	protected static void checkDownloadCompletion(){
		try {
			Thread.sleep(1000);
			boolean complete = getDownloadStatus();
			if (complete == false) {
				checkDownloadCompletion();
			} else {
				return;
			}
		} catch (InterruptedException e) {
			if (worker.isCancelled())
				Thread.currentThread().interrupt();
			else
				e.printStackTrace();		   
		}
	}

	/**
	 * Gets the status of the download process.
	 * @return		True if complete, False 
	 * 				otherwise.
	 */
	public static boolean getDownloadStatus() {
		return downloadComplete;
	}

	/**
	 * Sets the status of the download process.
	 * @param status
	 */
	public static void setDownloadComplete(boolean status) {
		downloadComplete = status;
	}

	/**
	 * @return		Returns a list of all genomes 
	 * 				used in the analysis.
	 */
	public static List<Genome> getGenomes() {
		return genomes;
	}

	/**
	 * @return		Returns a list of all clusters 
	 * 				(TransClust output).
	 */
	public static ArrayList<String> getClusters() {
		return clusters;
	}

	//-------------------------------------------------------------
	// CANCELLATION ROUTINES
	//
	@Override
	public void cancelled() {
		if (processInterrupted == false) {
			// Cancel all processes and threads
			if (!worker.isDone()) {
				worker.cancel(true);
				cancellAllWorkers();
			}
			// Enable buttons (ParametersPanel)
			ParametersPanel.enableButtons(true);
			// Enable buttons (Load and Select panels)
			enableSelectionOptions(true);
			// Update status
			ProgressPanel.updateProgressBarValue(0);
			ProgressPanel.enableProcessors(true);
			ProgressPanel.cleanActionKillButton();
			ProgressPanel.enableKillButton(false);
			//
			logger.info("Process interrupted. ");
			// Avoid sending multiple messages
			processInterrupted = true;
		}
	}

	/**
	 * Add functionality to Cancel button (Progress 
	 * Panel). If action is performed it will cancel 
	 * all other processes on cascade.
	 */
	protected void addCancelButtonFunction() {
		// Add functionality to Cancel button
		ProgressPanel.setActionKillButton(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				ProgressPanel.displayStatusMsg("Process cancelled...");
				cancelled();
			}
		});
		ProgressPanel.enableKillButton(true);
	}

	/** Cancels all SwingWorkers that are not done. */
	protected void cancellAllWorkers() {
		// Cancel Random Forest
		if (learning != null)
			learning.cancelled();
		// Cancel Gecko
		if (gecko != null)
			gecko.cancelled();
		// Cancel clustering
		if (clust != null)
			clust.cancelled();
		// Cancel parsing
		if(parseG != null)
			parseG.cancelled();			
	}

	/**
	 * Enables/disables options from 'Selection' and 
	 * 'Load' genomes panels. While the pipeline is 
	 * running the user should not be allowed to add or 
	 * remove genomes from the list.
	 * 
	 * @param b		True to enable, and False otherwise.
	 */
	protected void enableSelectionOptions(boolean b){
		ParametersPanel.enableButtons(b);
		ParametersPanel.enablePanels(b);
		GenomeSelection.enableSelectionOptions(b);
		GenomeLoader.enableSelectionOptions(b);
	}
	
	/**
	 * @return	Returns status of the process. False 
	 * 			if it was cancelled; and True, otherwise.
	 */
	public static boolean getProcessStatus() {
		return processInterrupted;
	}

	/**
	 * Clean up the database before closing 
	 * the application.

	public static void cleanLissiDB() {
		if (lissiDB != null)
			lissiDB.cleanup();
	}	
	 */

	/**
	 * @return		Returns the count associated 
	 * 				with database progress.

	public static long getDatabaseStatus() {
		return databaseCountDown.getCount();
	}
	 */
}
