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


import java.util.LinkedHashMap;

import org.apache.commons.collections15.Transformer;

import dk.sdu.imada.decision.tree.AbstractTreeMapper;
import dk.sdu.imada.decision.tree.TreeNode;


/**
 * Class modifies vertex label.
 * 
 * @author Eudes Barbosa
 */
public class VertexLabel extends AbstractTreeMapper implements
		Transformer<String, String> {

	
	/**
	 * Instantiate class.
	 * @param map		Map the nodes and their information. It has  
	 * 					an Integer as key, and a TreeNode object as 
	 * 					value. A child node connected by a "No" (absent) 
	 * 					are always odd numbers, while "Yes" (present) 
	 * 					are always even.
	 */
	public VertexLabel(LinkedHashMap<String, TreeNode> map) {
		super(map);
	}
	
	@Override
	public String transform(String i) {
		TreeNode node = (TreeNode) map.get(i);
		boolean check = node.hasChild();
		if (check == true) {
			TreeNode child = (TreeNode) map.get(node.getDefaultChildID());
			String field = child.getNodeField();
			/* If Field is compose, i.e., two or more clusters/islands 
			 * are equity suitable to perform the classification, I'm 
			 * arbitrarily selecting one. */
			if (field.contains("|")) {
				String[] data = field.split("\\|");
				field = data[0];
			}
			String label = "<html><b><center>" + node.getClassification() +
					"</center><br>["+ field+"]</b></html>";
			//
			return label;
		}
		//
		return  "<html><b>" + node.getClassification() +"</b></html>";
	}
}
