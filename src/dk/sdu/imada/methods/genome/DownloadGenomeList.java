/**
 * 
 */
package dk.sdu.imada.methods.genome;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

import dk.sdu.imada.gui.ProgressPanel;
import dk.sdu.imada.gui.genome.GenomeSelection;
import dk.sdu.imada.methods.BrokePipelineException;
import dk.sdu.imada.methods.Command;
import dk.sdu.imada.methods.CommomMethods;

/**
 * Class downloads a list with all complete genomes availables 
 * at NCBI. It parses the list of genomes and pipe it into the 
 * 'Available Genomes' table at Genome Selection panel.
 * 
 * @author Eudes Barbosa
 */
public class DownloadGenomeList implements Command {

	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(DownloadGenomeList.class.getName());

	/** FTP client used to download list from NCBI. */
	protected FileTransferClient ftp;

	/** SwingerWorker to run tasks in background. */
	protected SwingWorker<?,?> workerGenomeList;

	/** 
	 * Summary file of old reference sequences 
	 * (no longer updated). 
	 */
	private static final String summaryOldFileNCBI = 
			"ftp://ftp.ncbi.nlm.nih.gov/genomes/archive/old_refseq/Bacteria/summary.txt";

	/** Summary file of new genomes. */
	private static final String summaryNewFileNCBI = 
			"ftp://ftp.ensemblgenomes.org/pub/current/bacteria/new_genomes.txt";
	
	//------  Declaration end  ------//


	@SuppressWarnings("rawtypes")
	@Override
	public void exec() {
		// Connect to server
		connect();
		// Create abstract class to run tasks in the background
		workerGenomeList = new SwingWorker() {
			@Override
			protected Object doInBackground() throws Exception {

				// Change progress bar status
				ProgressPanel.displayStatusMsg("Downloading list of organisms from NCBI...");
				ProgressPanel.updateProgressStatus(true);

				// Get list
				ArrayList<String> result = new ArrayList<>();
				try {
					result = FillTable();
				} catch (IOException e) {
					logger.debug("Unable to load local list of available genomes. ");
					e.printStackTrace();
				}

				// Remove all rows from model
				GenomeSelection.emptyAvailableGenomes();
				// Insert rows from Summary
				for (String s: result) {
					String[] data = s.split("\t");
					GenomeSelection.addRowAvailableGenomes(new Object[]{data[0], data[1], data[2], false, data[3], null});
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
				ProgressPanel.displayStatusMsg("              ");
				ProgressPanel.cleanActionKillButton();
				ProgressPanel.enableKillButton(false);
				// Log
				logger.info("List - download complete. ");		
			}
		};  

		// Add functionality to Kill button
		ProgressPanel.setActionKillButton(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// Cancel tasks
				workerGenomeList.cancel(true);
				// Remove all rows from model
				GenomeSelection.emptyAvailableGenomes();
			}
		});
		ProgressPanel.enableKillButton(true);

		// Start worker
		workerGenomeList.execute();	
	}
	

	/**
	 * Retrieves summary file from NCBI. The file contains all 
	 * currently available bacterial genomes.
	 * 
	 * @throws FTPException
	 * @throws IOException
	 * @throws BrokePipelineException 
	 */
	protected ArrayList<String> FillTable() throws FTPException, IOException, BrokePipelineException {
		ArrayList<String> result = new ArrayList<String>();

		//Downloading file from server to memory
		try {
			// Extract information from old ref_seq
			byte[] summaryFile = ftp.downloadByteArray(summaryOldFileNCBI);
			result.addAll(parseOldRefSeq(summaryFile));



			// TODO: Extract information from new genomes
			// ...


		} catch (Exception e) {
			String message = "Failed to download and process summary file from GenBank.";
			e.printStackTrace();
			throw new BrokePipelineException(message, e);
		}
		// Disconnect client
		ftp.disconnect();
		//
		return result;
	}


	/** 
	 * 	@param summaryFile 	Byte array with summary file of old
	 * 						reference sequences (no longer updated).
	 * 
	 *  @return	Returns information from summary file from NCBI. 
	 *  The file contains all available bacterial genomes from old 
	 *  reference sequences (no longer updated).
	 */
	protected ArrayList<String> parseOldRefSeq(byte[] summaryFile) {
		// Initialize variable
		ArrayList<String> result = new ArrayList<String>();
		// Pattern applied to ignore 'plasmids'
		Pattern chrome = Pattern.compile("chromosome", Pattern.CASE_INSENSITIVE); 

		// Process file
		String s = new String(summaryFile);
		String[] allGenomesTemp = s.split("\n");
		// Read all lines of the file 
		for (int m = 1; m < allGenomesTemp.length; m++) {	
			String [] data = allGenomesTemp[m].split("\t");
			Matcher matcherChrome = chrome.matcher(data[6]);
			// Returns if Replicon column ~ 'chromosome'
			if (matcherChrome.find()) {	
				result.add(data[0].concat("\t").concat(data[3])
						.concat("\t").concat(data[5]).concat("\t").concat(data[4]));
			}	
		}	 			
		//
		return result;
	}

	
	/**
	 * Configures FTP client. In case there is some 
	 * error, it displays a message dialog and try again.
	 */
	protected void connect(){
		// Create FTP client
		try {
			ftp = CommomMethods.CreateFTPClient();
		} catch (FTPException | IOException e) {
			JOptionPane.showMessageDialog(null, "Unable to connect to ftp server.");
			logger.error("Unable to connect to ftp server. Check your internet connection. ");
			e.printStackTrace();
			connect();		
		}
	}
	
	
	/** 
	 * Specific for testing: avoids downloading the file from NCBI 
	 * every time we start the tool.
	 * @throws IOException
	 */
	protected ArrayList<String> FillTableDEBUGGING() throws IOException {
		// Initialize variables
		ArrayList<String> result = new ArrayList<String>();
		Pattern chrome = Pattern.compile("chromosome", Pattern.CASE_INSENSITIVE); //applied to ignore 'plasmids'
		String stringLine;
		// Set file path

		File file = new File(getClass().getResource("res/summary.txt").getPath());
		FileReader fr;
		try {
			// Buffer file
			fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			// Read all lines of the file 
			while ((stringLine = br.readLine()) != null) {
				String [] data = stringLine.split("\t");
				Matcher matcherChrome = chrome.matcher(data[6]);
				// Returns if Replicon column ~ 'chromosome'
				if (matcherChrome.find()) {	
					result.add(data[0].concat("\t").concat(data[3]).concat("\t").concat(data[5]).concat("\t").concat(data[4]));
				}		
			}
			// Close file
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//
		return result;
	}
	
}
