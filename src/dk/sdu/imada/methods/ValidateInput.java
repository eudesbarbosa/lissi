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
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class contains methods to validate and 
 * parse the input provided by the user.
 * 
 * @author Eudes Barbosa
 */
public class ValidateInput extends Configuration {

	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(ValidateInput.class.getName());


	/** Help message. */
	protected final String help = "SUMMARY\n"+
			"LiSSI stands for LifeStyle-Specific-Islands. It was developed " + 
			"to identify islands mainly associated with a given life-style. " + 
			"LiSSI is divided into three sequential modules: \n" + 
			"- Evolutionary Sequence Analysis; \n" + 
			"- Island Detection; \n" + 
			"- Statistical Learning Methods. \n" + 
			"Optionally, the tool can be used without the islands detection. " + 
			"In that case, it will report putative homologous genes that are " + 
			"mainly associated with a given lifestyle.\n"+
			"\nABOUT\n"+
			"\tLiSSI command line version 1.0\n"+
			"\tCopyright 2017 by Eudes Barbosa\n"+
			"\nCITATION\n" +
			"Barbosa, Eudes, et al. \"LifeStyle-Specific-Islands (LiSSI): "
			+ "Integrated Bioinformatics Platform for Genomic Island Analysis.\" "
			+ "Journal of Integrative Bioinformatics (2017).\n" +
			"\nUSAGE\n"+
			"\tjava -jar [java virtual machine options] LiSSI.jar " +
			"-c <configuration_file>";

	//------  Declaration end  ------//


	/**
	 * Validates the command line arguments. If 
	 * all provided information is corrected it generates 
	 * an object with all the configuration settings.
	 * 
	 * @param args		The command line arguments.
	 */
	public ValidateInput(String[] args) {
		// Check input arguments 
		String configPath = checkInputArgs(args);

		// Validate configuration settings
		validateConfigFile(configPath);
	}


	/**
	 * Creates a new configuration object 
	 * based on new loaded configuration file.
	 */
	public ValidateInput() {
		// Validate new input
		selectNewFile();
	}


	/**
	 * Allows the user to select a different configuration file either 
	 * in case the first attempted has failed or if s/he simply wants 
	 * to load another file.
	 */
	protected void selectNewFile() {
		//Configure a file chooser
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// Display dialog 
		int returnVal = jFileChooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = jFileChooser.getSelectedFile();
			// Validate configuration file again
			validateConfigFile(file.getAbsolutePath());
		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			// Exit
			logger.info("Exiting...");
			System.exit(0);
		}
	}


	/**
	 * Validates the configuration file provided by the user. It checks: 
	 * if it has a valid email address (necessary as a password for NCBI 
	 * FTP access); if the provided local directory exists and can be used 
	 * (writable); and, if the BLAST binary files are stored in place.
	 *  
	 * @param path		Path to the user's configuration file. 
	 */
	protected void validateConfigFile(String path) {		
		// Intilialize error msg
		String errorMsg = "Please attend for the following errors in your configuration file: \n";
		String test = errorMsg;

		// Get nodes information
		NodeList nList = getNodeList(path);

		// Iterate through nodes (get info)
		for (int i = 0; i < nList.getLength(); i++) {
			// Genome folder settings
			Node node = nList.item(i);
			Element eElement = (Element) node;

			// Server settings
			if (eElement.getAttribute("id").equals("Server")) {
				// Validate provided email address
				String email = eElement.getElementsByTagName("password").item(0).getTextContent();
				boolean check = this.setEmail(email);
				if (!check) {
					// If email not valid, add to error message
					//errorMsg += "- Provided password is not a valid e-mail address \n";
					logger.warn("Provided password is not a valid e-mail address. "
							+ "LiSSI won't be able to download files from NCBI.");
				}
			}

			// Local folder settings
			if (eElement.getAttribute("id").equals("LocalFolder")) {
				String localDir = eElement.getElementsByTagName("folder").item(0).getTextContent();
				// Validate provided local folder
				boolean check = this.setLocalDir(localDir);
				if (!check) {
					// If problem with local folder, add to error message
					errorMsg += "- Problem with provided local folder \n";
				}				
			}

			// Blast bin folder settings
			if (eElement.getAttribute("id").equals("Blast")) {
				// Validate path to BLAST bin folder
				String blastBin = eElement.getElementsByTagName("folder").item(0).getTextContent();
				boolean check = this.setBlastDir(blastBin);
				if (!check) {
					// If problem with blast folder, add to error message
					errorMsg += "- Problem with BLAST bin folder \n";
				}	
			}

			// R bin folder settings
			if (eElement.getAttribute("id").equals("R")) {
				// Validate path to R bin folder
				String rDir = eElement.getElementsByTagName("folder").item(0).getTextContent();
				boolean check = this.setRDir(rDir);
				if (!check){
					// If problem with blast folder, add to error message
					errorMsg += "- Problem with R bin folder \n";
				}				
			}
			
		}
		//
		if (!test.equals(errorMsg)) {
			logger.error("Error while reading configuration file.\n" + errorMsg);
			this.setAllValid(false);
		}
	}

	/**
	 * @param path	Path to configuration file. 
	 * @return	Reads the configuration file and gets the node information,
	 * i.e., the user's settings. Returns a list of nodes found in the 
	 * configuration file.
	 */
	protected NodeList getNodeList(String path) {
		//
		NodeList nList = null;
		// Read XML file
		try {			
			File xmlFile = new File(path);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			// Remove spaces and line breaks from fields
			doc.getDocumentElement().normalize();
			// Get nodes information
			nList = doc.getElementsByTagName("set");
		} catch (Exception e) {
			logger.error("Failed to read configuration file (xml). ");
			System.out.println("Failed to read configuration file (xml). ");
			System.out.println("Try the option '-h' for more information.");
		}
		//
		return nList;
	}

	/**
	 * Validates the command line arguments.
	 * 
	 * @param args		The command line arguments.
	 * @return 
	 */
	public String checkInputArgs(String[] args) {
		// Configure options
		CommandLine commandLine;
		// Help
		Option option_Help = Option.builder("h")
				.longOpt( "help" )
				.desc( "print this message."  )
				//.hasArg()
				.argName( "HELP" )
				.build();
		// Configuration file
		Option option_Config = Option.builder("c")
				.longOpt( "config" )
				.desc( "configuration file."  )
				.hasArg()
				.argName( "CONFIG" )
				.build();
		//
		Options options = new Options();
		options.addOption(option_Help);
		options.addOption(option_Config);
		//
		CommandLineParser parser = new DefaultParser();
		try {
			// Parse command line arguments
			commandLine = parser.parse(options, args);
			// Print help
			if(commandLine.hasOption("h")){
				System.out.println(help);				
				for (Iterator<?> it=options.getOptions().iterator(); it.hasNext(); ) {
					String line = it.next().toString().replace(" option: ", "-");
					line = line.replaceAll("\\[", "").replaceAll("\\]", "").replace("ARG", "");
					line = line.replace(":", "").replaceAll("\\s+", " ").trim();
					System.out.println("   "+line);
				}
				System.exit(0);
			}
			// If no configuration file
			if(!commandLine.hasOption("c")) {
				System.out.println("Missing configuration file.");
				System.out.println("Try the option '-h' for more information.");				
				System.exit(0);
			}
			try {
				// Verify if provided string is a path to file
				String path = commandLine.getOptionValue("c").toString();
				FilenameUtils.getPath(path);
				//
				return(path);

			} catch (Exception e) {
				System.out.println(commandLine.getOptionValue("c").toString());
				System.out.println("Error while reading configuration file. \nExiting...");
				//e.printStackTrace();
				System.exit(0);			
			}
		} catch (ParseException exception) {
			logger.error("Parse error: ");
			logger.error(exception.getMessage());
		}
		//
		return "";
	}

}
