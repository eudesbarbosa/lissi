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
package dk.sdu.imada.decision.tree.transformers;

import org.apache.commons.collections15.Transformer;

/**
 * Class edits the label associated with each edge.
 * @author Eudes Barbosa
 */
public class EdgeLabel implements Transformer<String, String> {
	
	@Override
	public String transform(String c) {
		int value = Integer.parseInt(c);
		if (value%2 == 0) {
			//System.out.println(value + "\tNO\t" + value%2);
			return "<html><b>Yes</b></html>";
		} else if (value%2 > 0) {
			//System.out.println(value + "\tYES\t" + value%2);
			return "<html><b>No</b></html>";
		}
		//
		return "";
	}

}
