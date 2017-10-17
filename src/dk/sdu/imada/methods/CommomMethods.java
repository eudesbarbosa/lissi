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
package dk.sdu.imada.methods;


import java.io.IOException;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

import dk.sdu.imada.gui.MainFrame;



/**
 * Class contains common methods that will be used 
 * for different classes during the analysis.
 * 
 * @author Eudes Barbosa
 */
public class CommomMethods {

	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(CommomMethods.class.getName());

	/** NCBI ftp default user, 'anonymous'. */
	protected static final String user = "anonymous";

	/** NBI ftp host address. */
	protected static final String host = "ftp.ncbi.nlm.nih.gov";

	//------  Declaration end  ------//


	/** 
	 * Removes previously created files. 
	 * @param localDir	Path to local working directory.
	 */
	public static void cleanDirectories(String localDir) {
		//String localDir = getLocalDirectory();
		File directory = null;
		try {
			// Clean TransClust folder
			String transClustDir = localDir.concat(File.separator).concat("TransClust");		
			directory = new File(transClustDir);
			if (directory.exists()) {
				FileUtils.cleanDirectory(directory);
			}			
			// Clean Gecko folder
			String geckoDir =  localDir.concat(File.separator).concat("Gecko");
			directory = new File(geckoDir);
			if (directory.exists()) {
				FileUtils.cleanDirectory(directory);
			}
			// Clean Random Forest
			String randomForestDir = localDir.concat(File.separator).concat("RandomForest");
			directory = new File(randomForestDir);
			if (directory.exists())
				FileUtils.cleanDirectory(directory);

		} catch (IOException e) {
			logger.error("Fail to clean local directories : " + directory.getAbsolutePath());
			e.printStackTrace();
		} 

	}


	/**
	 * @return	Returns an FTPClient object based on the settings 
	 * found in the configuration file. FTPClient specific for NCBI 
	 * and anonymous use.
	 * 
	 * @throws FTPException
	 * @throws IOException
	 */
	public static FileTransferClient CreateFTPClient() 
			throws FTPException, IOException {
		// @param pwd	Password to NCBI connection (valid email address).
		// NCBI FTP password : user email
		String pwd = MainFrame.getGlobalParameters().getEmail();
		// Validate input
		if (pwd.equals("")) {
			logger.error("FTP file transfer failed. Please provided "
					+ "adequate values Password.");
			return null;
		}
		// Create and set remove host
		FileTransferClient ftpClient = new FileTransferClient();
		//
		ftpClient.setRemoteHost(host);
		ftpClient.setUserName(user);
		ftpClient.setPassword(pwd);
		ftpClient.connect();
		//		
		return ftpClient;
	}


	/**
	 * Sends a message to progress panel to indicate that the configuration 
	 * file was correctly loaded. It makes a thread sleep until it is 
	 * possible to send the message. This is necessary due to the fact that 
	 * loading the graphical part is usually faster than parsing the 
	 * configuration file. 

	public static void notifyProgressBar(){
		try {		    
			Thread.sleep(100);
			try {
				ProgressPanel.displayStatusMsg("Configurations successfully loaded.");
			} catch (Exception e) {
				notifyProgressBar();
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	} 
	 */
}