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
package dk.sdu.imada.decision.tree;

import java.util.LinkedHashMap;

import dk.sdu.imada.decision.tree.CommomTreeMethods;
import dk.sdu.imada.decision.tree.TreeNode;


/**
 * Abstract class creates a Transformer 
 * using a map that contains: either the nodes and 
 * their general information; or the nodes and their 
 * mean decreased Gini Index.
 * 
 * @author Eudes Barbosa	
 */
public class AbstractTreeMapper extends CommomTreeMethods {

	//------  Variable declaration  ------//

	/** 
	 * Map the nodes and their information. It has  
	 * an Integer as key, and a TreeNode object as 
	 * value. A child node connected by a "No" (absent) 
	 * are always odd numbers, while "Yes" (present) 
	 * are always even. This is important for to set 
	 * the edges labels in the Transformer associated 
	 * with the method 'setEdgeLabelTransformer()'.
	 */
	protected LinkedHashMap<String, TreeNode> map =	
			new LinkedHashMap<>();

	//------  Declaration end  ------//


	/**
	 * Instantiate class.
	 * @param map		Map with eitheir general node 
	 * 					information.
	 */
	public AbstractTreeMapper(LinkedHashMap<String,TreeNode> map) {
		this.map = map;
	}

}