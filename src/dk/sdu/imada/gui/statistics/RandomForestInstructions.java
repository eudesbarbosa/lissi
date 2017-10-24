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
package dk.sdu.imada.gui.statistics;

import dk.sdu.imada.gui.AbstractParameterPanel;

/** 
 * Class contains parameters' instructions for Random Forest. Plus, 
 * access to  online manual and paper.
 * 
 * @author Eudes Barbosa
 */
public abstract class RandomForestInstructions extends AbstractParameterPanel {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = 1L;

	/** K-fold instruction. */
	final protected String KFOLD_INST = "<html>K-fold cross validation. The data set will be "
			+ "divided in k subsets of " +
			"approximately the same size.</html>";
	
	/** Number of trees instruction. */
	final protected String NTREES_INST = "<html>Number of trees to grow per run. Avoid small "
			+ "number to ensure that every row is " +
			"predicted at least a couple of times.</html>"; 
	
	/** Number of runs instruction.*/
	final protected String NRUNS_INST = "<html>Number of runs. Number of times the process "
			+ "will be repeated with different k " +
			"subsets and trees.</html>";
	
	/** RandomForest package manual. */
	final protected String PACK_MANUAL = "https://cran.r-project.org/web/packages/"
			+ "randomForest/randomForest.pdf";
	
	/** Random Forest paper. */
	final protected String PAPER_ONLINE = "http://dx.doi.org/10.1023/A:1010933404324";
	
	//------  Declaration end  ------//


}
