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

import java.awt.Color;
import java.awt.Paint;
import java.util.LinkedHashMap;

import org.apache.commons.collections15.Transformer;

import dk.sdu.imada.decision.tree.AbstractTreeMapper;
import dk.sdu.imada.decision.tree.TreeNode;

/**
 * Class modifies vertex color.
 * 
 * @author Eudes Barbosa	
 */
public class VertexPaint extends AbstractTreeMapper implements
Transformer<String, Paint> {

	/** Color blue. */ 
	final protected Color BLUE = new Color(100, 100, 255);
	
	/** Color orange.*/
	final protected Color ORANGE = new Color(255, 196, 0);

	/**
	 * Instantiate class.
	 * @param map		Map the nodes and their information. It has  
	 * 					an Integer as key, and a TreeNode object as 
	 * 					value. A child node connected by a "No" (absent) 
	 * 					are always odd numbers, while "Yes" (present) 
	 * 					are always even.
	 */
	public VertexPaint(LinkedHashMap<String, TreeNode> map) {
		super(map);
	}

	@Override
	public Paint transform(String i) {	
		TreeNode node = map.get(i);
		double valueZero = (double) node.getScores().get(0).getCount();
		double valueOne = (double) node.getScores().get(1).getCount();
		if (valueZero >= valueOne) {
			return ORANGE;
		}
		return BLUE;
	}
}

