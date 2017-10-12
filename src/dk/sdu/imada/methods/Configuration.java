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

import java.io.File;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Class stores all objects configuration 
 * settings.
 * 
 * @author Eudes Barbosa 
 */
public class Configuration {

	//------  Variable declaration  ------//


	/** Path to R binary files. */
	protected String rDir = "/usr/bin/";

	/** Path to Blast binary files. */
	protected String blastDir = "/usr/bin/";

	/** Path to local working directory. */
	protected String localDir;

	/** 
	 * User email. Used as password at 
	 * NCBI website.
	 */
	protected String email = "";
	
	/** 
	 * Variable to define if all 
	 * configurations settings are 
	 * correct. Default: True.
	 */
	protected boolean allValid = true;
	


	//------  Declaration end  ------//



	/** @return Returns True if all 
	 * parameters settings are valid; 
	 * otherwise, False.
	 */
	protected boolean isAllValid() {
		return allValid;
	}


	/**
	 * Specifies if the provided 
	 * parameters are valid. Should be 
	 * set as False if any value is 
	 * incorrect.
	 * 
	 * @param allValid	True if all 
	 * parameters settings are valid; 
	 * otherwise, False. Default value: 
	 * True.
	 */
	protected void setAllValid(boolean allValid) {
		this.allValid = allValid;
	}
	
	
	/** @return Returns path to local working directory. */
	public String getLocalDir() {
		return localDir;
	}


	/**
	 * Sets path to local working directory.
	 * 
	 * @param localDir Path to local working directory.
	 * @return 
	 */
	public boolean setLocalDir(String localDir) {
		File f = new File(localDir);
		this.localDir = localDir;
		return f.exists() && f.isDirectory() && f.canWrite();
	}	


	/** @return Returns path to R binary files. */
	public String getRDir() {
		return rDir;
	}


	/**
	 * Sets the path to R binary files.
	 * 
	 * @param rDir Path to R binary files.
	 * @return 
	 */
	public boolean setRDir(String rDir) {
		this.rDir = rDir;
		return new File(rDir, "Rscript").exists();
	}


	/** @return Returns path to Blast binary files. */
	public String getBlastDir() {
		return blastDir;
	}


	/**
	 * Sets the path to Blast binary files.
	 * 
	 * @param blastDir Path to Blast binary files.
	 * 
	 * @return Returns True if directory contains 
	 * Blast binary files; otherwise, False.
	 */
	public boolean setBlastDir(String blastDir) {
		this.blastDir = blastDir;
		return new File(blastDir, "blastp").exists();
	}


	/**
	 * @return Returns user's email. Used 
	 * as password at NCBI website.
	 */
	public String getEmail() {
		return email;
	}


	/**
	 * Verifies if provided e-mail address has a valid format. 
	 * Afterwards, sets user's email. Email used as password 
	 * at NCBI website.
	 * 
	 * @param email	User's email.
	 * 
	 * @return				True if email is in a valid format; 
	 * 						otherwise, False.
	 */
	public boolean setEmail(String email) {
		// Verifies if null or empty
		if (email == null || email.equals("")) {
			return false;
		}	
		// Verifies valid format
		try {			
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			return false;
		}
		//
		this.email = email;
		return true;
	}

}
