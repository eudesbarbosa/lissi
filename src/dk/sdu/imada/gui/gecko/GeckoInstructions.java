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
package dk.sdu.imada.gui.gecko;

import dk.sdu.imada.gui.AbstractParameterPanel;


/** 
 * Class contains parameters' instructions for Gecko. Plus, access to 
 * online manual and paper.
 * 
 * @author Eudes Barbosa
 */
public abstract class GeckoInstructions extends AbstractParameterPanel {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = 1L;

	/** Maximum distance explanation. */
	final protected String DISTANCE_INST = "<html>The maximum allowed distance "
			+ "between islands (indels).</html>" +
			"between the islands.";
	
	/** Minimum size explanation. */
	final protected String SIZE_INST = "<html>The minimum island size.</html>";
	
	/** Minimum coverage explanation. */
	final protected String COVERAGE_INST = "<html>The minimum number of covered genomes.</html>";
	
	/** Online manual (http). */
	final protected String ONLINE_MANUAL = "http://bio.informatik.uni-jena.de/software/gecko3/";
	
	/** Paper (doi)*/
	final protected String ONLINE_PAPER = "http://dx.doi.org/10.1186/1471-2105-14-S15-S14";
	
	//------  Declaration end  ------//
	
}
