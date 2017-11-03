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
package dk.sdu.imada.methods.genome;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.decision.tree.TreeNode;
import dk.sdu.imada.methods.PipelineSync;


/**
 * Class creates a map associating each node (cluster 
 * ID - Key) and all genes that are present in that 
 * node (List of Gene objects - Value).
 * 
 * @author Eudes Barbosa	
 */
public class GeneInformation {
	
	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(GeneInformation.class.getName());

	/** 
	 * Stores a list of Gene objects that are present in each 
	 * of the decision tree nodes. The key is a String with the 
	 * node identifier, and the value is the List of Genes. 
	 */
	protected LinkedHashMap<String, List<Gene>> mapNodeGenes =	
			new LinkedHashMap<String, List<Gene>>();
	
	//------  Declaration end  ------//


	/**
	 * Creates a a map associating each node (cluster ID - Key) 
	 * and all genes that are present in that node (List of 
	 * Gene objects - Value).
	 * 
	 * @param mapTree	Map of the nodes and their information. It 
	 * 					has an Integer as key, and a TreeNode object 
	 * 					as value.
	 */
	public GeneInformation(LinkedHashMap<String,TreeNode> mapTree) {
		// Get node information
		getGenesInformation(mapTree);
	}
	

	/**
	 * @return Returns a LinkedHashMap with the nodes and their information. 
	 * It  has an Integer as key, and a TreeNode object as value.
	 */
	public LinkedHashMap<String, List<Gene>> getMappedGenes() {
		return mapNodeGenes;
	}
	
	
	/**
	 * Gets all genes present in the analysis and filter the ones that are 
	 * part of the clusters present in the decision tree.
	 * 
	 * @param nodes		Set of all nodes (TreeNode) present in the decision 
	 * 					tree.
	 */
	protected void getGenesInformation(LinkedHashMap<String,TreeNode> nodes) {
		//
		ArrayList<String> nodeClusters = new ArrayList<String>();
		// Iterate through all nodes
		Iterator<TreeNode> it = nodes.values().iterator();
		while (it.hasNext()){
			TreeNode n = it.next();
			if (n.hasChild()) {
				String cluster = nodes.get(n.getDefaultChildID()).getNodeField().replaceAll("ID\\.", "");
				nodeClusters.add(cluster);
			}
		}	
		// Get all genes		
		ArrayList<Gene> allGenes = getAllGenes();

		// Get information from genes that are in nodes
		List<Gene> listGenes = new ArrayList<>();
		for (String c : nodeClusters) {
			// If contains cluster, add to list
			int cluster = -1;
			try {
				cluster = Integer.parseInt(c);
			} catch (Exception e) {
				// If node has two clusters, parse it
				String[] data = c.split("\\|");
				String cl = data[0];	
				try {
					cluster = Integer.parseInt(cl);
				} catch (Exception e2) {
					logger.debug("Problem while reading the gene information to decision tree.");
					e2.printStackTrace();
				}
			}
			for (Gene g : allGenes) {
				// Ignores if pseudogene
				if(g.isPseudo())
					continue;
				if (cluster == g.getCluster()) {					
					listGenes.add(g);
				}
			}
			// Add to HashMap
			if (listGenes.size() > 0)
				mapNodeGenes.put(""+cluster, listGenes);
		}
	}
	

	/**
	 * @return Returns a list of all gene objects present 
	 * in all organisms used for the analysis.
	 */
	protected ArrayList<Gene> getAllGenes() {
		// Initialize variables
		ArrayList<Gene> genes = new ArrayList<Gene>();
		List<Genome> genomes = PipelineSync.getGenomes();
		// Iterate through all genomes
		for (Genome g : genomes)
			genes.addAll(g.getGenes());
		//
		return genes;
	}
	

	/** Print result (testing). */
	@SuppressWarnings("unused")
	private void printTest() {		
		logger.debug("----------------------TEST--------------------------");
		Iterator<List<Gene>> it = mapNodeGenes.values().iterator();
		while (it.hasNext()){
			//logger.debug("Imprimi essa porra! E ai?");
			for (Gene g : it.next()) {
				System.out.println(g.getName());
				System.out.println(g.getProduct());
			}			
		}
		logger.debug("--------------------TEST-END-------------------------");
	}
	
}
