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
package dk.sdu.imada.methods.statistics;

/**
 * Class parses Gecko output and sets gene order. 
 * It is mainly used for the visualization in the 
 * decision trees.
 * 
 * @author Eudes Barbosa
 */
public class IndicatorMatrixTreeView extends IndicatorMatrixGecko {

	/**
	 * Parses Gecko output and stores it in an ArrayList 
	 * of Islands. All islands will contain an ArrayList 
	 * of Gene objects, instead of a simple list of gene 
	 * identifiers (GI).
	 * <br>
	 * To retrieve the islands information use 
	 * the method 'getIslandList'.
	 * 
	 * @param geckoFile		Path to Gecko output file.
	 * @param localDir 		Path to local working directory.
	 * @param threads  		Number of threads.
	 */
	public IndicatorMatrixTreeView(String geckoFile, 
			String localDir, int threads) {
		super(geckoFile, localDir, threads);
		//logger.debug("I got the genomes!!!");
		parse();
		//logger.debug("It parsed...");
		setGeneOrder();
		//logger.debug("Order?");
		printLines(genomes);
	}
}
