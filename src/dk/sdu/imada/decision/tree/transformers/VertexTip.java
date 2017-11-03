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
 * Class adds tip on the vertex based on mean 
 * decrease in Gini Index. Only nodes with 
 * child will store a value, the others will 
 * contain a "NA".
 * 
 * @author Eudes Barbosa
 */
public class VertexTip extends AbstractTreeMapper implements Transformer<String, String> {

	
	//------  Variable declaration  ------//

	/** 
	 * Map the nodes and their mean decrease Gini index, 
	 * as described in varSelRF output (R package). Only 
	 * nodes with child will store a value, the others will 
	 * containg a "NA". Variable is used in the vertexTip 
	 * Transformer. The Key is the node identifier, and the 
	 * Value is the mean decrease Gini index.   
	 */
	protected LinkedHashMap<String, String> mappedGini = 
			new LinkedHashMap<String, String>();

	//------  Declaration end  ------//


	/**
	 * Adds tip on the vertex based on mean 
	 * decrease in Gini Index. Only nodes with 
	 * child will store a value, the others will 
	 * containg a "NA".
	 * 
	 * @param mapTree	Tree map.
	 * @param mapGini	Tree map of Gini Index.
	 */
	public VertexTip(LinkedHashMap<String, TreeNode> mapTree, LinkedHashMap<String, String> mapGini) {
		super(mapTree);
		this.mappedGini = mapGini;
	}

	@Override
	public String transform(String s) {
		try {
			String text = "Mean Decrease Gini Index: ";
			//
			return text.concat(mappedGini.get(s));
		} catch (NullPointerException e) {
			String l1 = getConfidence(s, 0);
			String l2 = getConfidence(s, 1);
			//
			return "<html>" + l1 + "<br>" + l2 + "</html>";
		}
	}

	/** 
	 * @return		Returns a string with the 
	 * 				lifestyle name and the 
	 * 				confidence level in which 
	 * 				it was assigned to the node. 
	 */
	private String getConfidence(String s, int i) {	
		// Get confidence
		double conf = Double.parseDouble(((TreeNode) map.get(s)).getScores()
				.get(i).getConfidence());
		String confidence = ((TreeNode) map.get(s)).getScores().get(i)
				.getLifestyle() + " confidence: " 
				+ String.format("%.3g%n", conf);
		//
		return confidence;				
	}
}
