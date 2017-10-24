/**
 *
 */
package dk.sdu.imada.gui.statistics;


import java.io.File;

import dk.sdu.imada.decision.tree.DecisionTreeView;
import dk.sdu.imada.gui.CreateMultiSplitPane;
import dk.sdu.imada.gui.MainFrame;
import dk.sdu.imada.gui.ProgressPanel;
import dk.sdu.imada.gui.genome.GenomeSelection;
import dk.sdu.imada.methods.Cancelled;
import dk.sdu.imada.methods.Command;


/**
 * Class is used to visualize Random Forest results (ROC 
 * plots and decision trees) for both biases and the full 
 * data.
 * 
 * @author Eudes Barbosa
 */
public class ViewRFOutput implements Command, Cancelled {

	//------  Variable declaration  ------//

	/** Path to Gecko output file. */
	protected String geckoFile = "";

	/** Flag to indicated that Gecko was used or not. */
	protected boolean gecko = false;

	/** Flag to indicated that the process was cancelled. */
	protected static boolean cancel = false;
	
	//------  Declaration end  ------//


	/**
	 * Used for putative gene clusters (BFG). Displays 
	 * Random Forest results (ROC plots and decision 
	 * trees) for both biases and the full data.
	 */
	public ViewRFOutput() {
		this.gecko = false;
	}

	/**
	 * Used for islands (Gecko). Displays Random Forest 
	 * results (ROC plots and decision trees) for both 
	 * biases and the full data.
	 */
	public ViewRFOutput(String geckoOut) {
		this.gecko = true;		
		this.geckoFile = geckoOut;


		//------------------------------------//
		// TODO: remove all this
		System.out.println(geckoFile);
		String fullTree = "/media/eudesbarbosa/INTENSO/DK/Computational_Biology/LiSSI_2015/Actinobacteria/Results/" +
				"Non-Pathogens_vs._Pathogens/RandomForest/LISSI/plots/FullData/dt.xml";
		String giniTree = "/media/eudesbarbosa/INTENSO/DK/Computational_Biology/LiSSI_2015/Actinobacteria/Results/" +
				"Non-Pathogens_vs._Pathogens/RandomForest/LISSI/plots/FullData/MeanDecreaseGini_features.csv";	
		String title = "Full dataset";
		CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, fullTree, giniTree, geckoFile), "Decision Tree - Full data");
		return;
	}

	@Override
	public void cancelled() {
		cancel = true;
	}	

	public static boolean getStatus() {
		return cancel;
	}

	@Override
	public void exec() {
		// Update progress bar
		ProgressPanel.displayStatusMsg("Loading Random Forest plots");
		ProgressPanel.updateProgressStatus(true); 

		// Get lifestyles names and set directories
		String FULLDATA = "FullData";
		String lifeOne = GenomeSelection.getLifestyleOneName();
		String lifeTwo = GenomeSelection.getLifestyleTwoName();
		String dir = MainFrame.getGlobalParameters().getLocalDir();
		String dirRF = dir.concat("RandomForest").concat(File.separator)
				.concat("plots").concat(File.separator);

		//-----------------------------------------------------------------------//
		// Full data
		String fullROC = "";
		String fullDir = dirRF.concat(FULLDATA);
		File full = new File(fullDir);
		File[] list = full.listFiles();
		if (list != null) {
			for (File f : list) {
				if (f.isFile()) { 
					if(f.getName().toLowerCase().contains("_roc-plot_")) {
						fullROC = f.getAbsolutePath();
					}
				}
			}
		}
		if (cancel == false) 
			CreateMultiSplitPane.updateLayeredPane(new RandomForestROCPlot(fullROC, "Full Data"), "Full data");
		else
			return;
		// Plot Decision tree for full data
		String fullTree = dirRF.concat(FULLDATA).concat(File.separator).concat("FullData_decision_tree.xml");
		String giniTree = dirRF.concat(FULLDATA).concat(File.separator).concat("MeanDecreaseGini_features.csv");
		String title = "Full Data";
		if (cancel == false) {
			if (gecko == true) {
				CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, fullTree, giniTree, geckoFile), "Decision Tree - Full data");
			} else {
				CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, fullTree, giniTree), "Decision Tree - Full data");
			}
		} else {
			return;
		}
		//-----------------------------------------------------------------------//
		// Lifestyle One
		String oneROC = "";
		String oneDir = dirRF.concat(lifeOne);
		File one = new File(oneDir);
		list = one.listFiles();
		if (list != null) {
			for (File f : list) {
				if (f.isFile()) { 
					if(f.getName().toLowerCase().contains("_roc-plot_")) {
						oneROC = f.getAbsolutePath();
					}
				}
			}
		}
		// Plot ROC curve for lifestyle one
		String panelName = "Bias Class " + lifeOne;
		if (cancel == false)
			CreateMultiSplitPane.updateLayeredPane(new RandomForestROCPlot(oneROC, panelName), panelName);
		else
			return;
		// Plot Decision tree for full data
		String oneTree = dirRF.concat(lifeOne).concat(File.separator).concat(lifeOne).concat("_decision_tree.xml");
		giniTree = dirRF.concat(lifeOne).concat(File.separator).concat("MeanDecreaseGini_features.csv");
		panelName = "Decision Tree - Class " + lifeOne;
		title = "Bias lifestyle " + lifeOne;
		if (cancel == false) {
			if (gecko == true) {
				CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, oneTree, giniTree, geckoFile), panelName);
			} else {
				CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, oneTree, giniTree), panelName);
			}
		} else {
			return;
		}

		//-----------------------------------------------------------------------//
		// Lifestyle Two
		String twoROC = "";
		String twoDir = dirRF.concat(lifeTwo);
		File two = new File(twoDir);
		list = two.listFiles();
		if (list != null) {
			for (File f : list) {
				if (f.isFile()) { 
					if(f.getName().toLowerCase().contains("_roc-plot_")) {
						twoROC = f.getAbsolutePath();
					}
				}
			}
		}
		// Plot ROC curve for lifestyle two
		panelName = "Bias Class " + lifeTwo;
		if (cancel == false)
			CreateMultiSplitPane.updateLayeredPane(new RandomForestROCPlot(twoROC, panelName), panelName);
		else
			return;
		// Plot Decision tree for full data
		String twoTree = dirRF.concat(lifeTwo).concat(File.separator).concat(lifeTwo).concat("_decision_tree.xml");
		giniTree = dirRF.concat(lifeTwo).concat(File.separator).concat("MeanDecreaseGini_features.csv");
		panelName = "Decision Tree - Class " + lifeTwo;
		title = "Bias lifestyle " + lifeTwo;
		if (cancel == false) {
			if (gecko == true) 			
				CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, twoTree, giniTree, geckoFile), panelName);
			else 
				CreateMultiSplitPane.updateLayeredPane(new DecisionTreeView(title, twoTree, giniTree), panelName);
		} else {
			return;
		}
		// Update progress bar
		ProgressPanel.displayStatusMsg("");
		ProgressPanel.updateProgressStatus(false); 
	}
}