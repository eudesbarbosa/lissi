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
package dk.sdu.imada.gui.transclust;

import dk.sdu.imada.gui.AbstractParameterPanel;

/** 
 * Class contains parameters' instructions for TransClust. Plus, 
 * access to  online manual and paper.
 * 
 * @author Eudes Barbosa
 */
public abstract class TransclustInstructions extends AbstractParameterPanel {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = 1L;

	/** E-value instruction. */
	final protected String EVALUE_INST = "<html>Expected Value. Describes the "
			+ "expected number of hits by chance for a database " +
			"of a particular size.</html>";
	
	/** Density parameter instruction. */
	final protected String DENS_INST = "<html>Density parameter. Represents the "
			+ "boundary between similar and dissimilar objects. " +
			"Higher values correspond to more stringent homology groups.</html>";
	
	/** TransClust online manual (cmc). */
	final protected String MANUAL_ONLINE = "http://transclust.mmci.uni-saarland.de/"
			+ "cmc/offline_documentation.php";
	
	/** Main TransClust reference. */
	final protected String PAPER_MAIN = "http://dx.doi.org/10.1038/nmeth0610-419";
	
	//------  Declaration end  ------//

}
