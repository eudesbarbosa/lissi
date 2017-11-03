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
import java.util.List;

import dk.sdu.imada.methods.genome.Gene;


/**
 * Abstract class contains methods shared by listeners 
 * methods.
 * 
 * @author Eudes Barbosa
 */
public abstract class CommomTreeMethods {	
	
	/**
	 * @param clusterID		String for the cluster ID.
	 * @return				Returns a list of all genes 
	 * 						present in the node, i.e., associated 
	 * 						with the given cluster ID.
	 */
	protected List<Gene> getGenesInNodes(String clusterID, 
			LinkedHashMap<String, List<Gene>> genesInNodes) {
		List<Gene> genes = null;
		String cluster = parseNodeField(clusterID);
		// Get all genes in node
		genes = genesInNodes.get(cluster);
		//
		return genes;
	}

	/**
	 * @param node	Node field.
	 * @return		Returns the parsed node field.
	 */
	protected String parseNodeField(String node) {
		// Parse field if necessary
		String clusterID = node;
		if (clusterID.contains("|")) {
			String[] data = clusterID.split("\\|");
			clusterID = data[0];
		}
		clusterID = clusterID.replaceAll("ID\\.", "");
		//System.out.println("@@@@@@@@@@@@@@@@@ "+clusterID);
		//
		return clusterID;
	}

}
