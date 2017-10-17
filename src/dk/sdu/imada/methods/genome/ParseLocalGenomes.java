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
package dk.sdu.imada.methods.genome;


import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.gui.ProgressPanel;
import dk.sdu.imada.gui.genome.GenomeLoader;
import dk.sdu.imada.gui.genome.GenomeSelection;
import dk.sdu.imada.methods.Command;
import dk.sdu.imada.methods.Parser;


/**
 * Class scans folders searching for GenBank files. It will 
 * report a list of genomes for each lifestyle. The information 
 * in the list contains the basic information parsed from each 
 * GenBank file.
 * 
 * @author Eudes Barbosa
 */
public class ParseLocalGenomes extends Parser implements Command {
	
	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(GenomeLoader.class.getName());

	/** Path to folder with lifestyle one genomes. */
	protected String pathLifestyleOne;

	/** Path to folder with lifestyle two genomes. */
	protected String pathLifestyleTwo;

	/** SwingWorker to run tasks in the background. */
	protected SwingWorker<?,?> worker;

	//------  Declaration end  ------//

	
	/**
	 * Scans provided paths and returns the GenBank files 
	 * within each folder.
	 * 
	 * @param p1	Path to folder with lifestyle one genomes.
	 * @param p2	Path to folder with lifestyle two genomes.
	 */
	public ParseLocalGenomes(String p1,String p2){
		this.pathLifestyleOne = p1;
		this.pathLifestyleTwo = p2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void exec() {
		// Create abstract class to run tasks in the background
		worker = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {
				// Change progress bar status
				ProgressPanel.displayStatusMsg("Parsing local genomes...");
				ProgressPanel.updateProgressStatus(true);

				// Empty previous tables
				GenomeSelection.emptyLifestyleOne();
				GenomeSelection.emptyLifestyleTwo();

				// Add organisms to LifestyleOne table
				ArrayList<String> list = browseLocalFiles(pathLifestyleOne);
				for(String s : list){
					GenomeSelection.addRowLifestyleOne(parseLocalGBK(s));
				}

				// Add organisms to LifestyleTwo table
				list = browseLocalFiles(pathLifestyleTwo);
				for(String s : list){
					GenomeSelection.addRowLifestyleTwo(parseLocalGBK(s));
				}
				//
				return null;
			}
			@Override
			protected void done() {
				// Change pane
				//CreateMultiSplitPane.setPanel("Select Genomes");
				//CreateAccordionMenu.setGenomeSelection();
				// Change progress bar status
				ProgressPanel.updateProgressStatus(false);
				ProgressPanel.displayStatusMsg("");
				ProgressPanel.enableKillButton(false);
				// Log
				logger.info("Local genomes parsed (header only). ");				
			}
		};    		

		// Add functionality to Kill button
		ProgressPanel.setActionKillButton(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// Cancel tasks
				worker.cancel(true);
				// Remove all rows from models
				GenomeSelection.emptyLifestyleOne();
				GenomeSelection.emptyLifestyleTwo();
			}
		});
		ProgressPanel.enableKillButton(true);

		// Start worker
		worker.execute();	
	}

	/**
	 * Scans given local folder and call method to extract header from GenBank 
	 * files. Specially designed for files stores in the local disk.
	 */
	protected ArrayList<String> browseLocalFiles(String path){
		// Initialize
		ArrayList<String> files = new ArrayList<String>();
		//List all files in directory
		File f = new File(path);
		File[] listOfFiles = f.listFiles();
		for (int j = 0; j < listOfFiles.length; j++) {
			if (listOfFiles[j].isFile()) {
				String fileName = listOfFiles[j].getName();
				// Accept only GenBank files
				if (fileName.toLowerCase().endsWith(".gbk") || 
						fileName.toLowerCase().endsWith(".gb")){ 
					// Add to array
					files.add(listOfFiles[j].getAbsolutePath().toString());	
				}
			}
		}
		//
		return files;
	}

	/** 
	 * Extracts information from GenBank files. Specially 
	 * designed for files stores in the local disk.
	 */
	protected Object[] parseLocalGBK(String fileName) {
		try {
			// Buffer file
			BufferedReader br = getBufferedReader(fileName);
			// Extract organism name
			String stringLine;
			String organismName;
			String accession = null;
			while ((stringLine = br.readLine()) != null) {
				if (stringLine.contains("ACCESSION   ")) {
					accession = stringLine.replace("ACCESSION   ", "");
				}
				if(stringLine.contains("  ORGANISM  ")){
					organismName = stringLine.replace("  ORGANISM  ", "");
					if (accession == null) {
						throw new Exception();
					} else {
						return new Object[] {organismName, false, fileName, null, accession};
					}
				}					
			}
			br.close();
		} catch (Exception e) {
			logger.error("Error while parsing local GenBank files. \n" +
					"File not found: " + fileName);
			e.printStackTrace();
		}
		//
		return null;
	}
}
