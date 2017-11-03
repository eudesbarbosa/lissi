package dk.sdu.imada.decision.tree.listeners;

import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import dk.sdu.imada.decision.tree.AbstractTreeMapper;
import dk.sdu.imada.decision.tree.ClickAwayDialog;
import dk.sdu.imada.decision.tree.TreeNode;
import dk.sdu.imada.methods.genome.Gene;
import edu.uci.ics.jung.visualization.control.GraphMouseListener;


/**
 * Class used only for gene cluster analysis (BFG). It implements a 
 * listener that will display a word cloud with the information from all 
 * genes in the cluster: gene name and product. 
 * 
 * @author Eudes Barbosa (eudes@imada.sdu.dk)	
 */
@SuppressWarnings("rawtypes")
public class GraphMouseListenerWordCloud extends AbstractTreeMapper implements GraphMouseListener {
	
	/** 
	 * Stores a list of Gene objects that are present in each 
	 * of the decision tree nodes. The key is a String with the 
	 * node identifier, and the value is the List of Genes. 
	 */
	protected LinkedHashMap<String, List<Gene>> genesInNodes =	
			new LinkedHashMap<String, List<Gene>>();

	/** Component's parent JFrame. */
	protected JFrame parent;
	
	/** Flag to indicate if Gecko was used in the analysis. */
	protected boolean gecko = false;

	/**
	 * Instantiate class.
	 * @param mapTree			Map the nodes and their information. It has  
	 * 							an Integer as key, and a TreeNode object as 
	 * 							value.
	 * @param genesInNodes		Maps a list of Gene objects that are present in each 
	 * 							of the decision tree nodes.		
	 * @param parent			Parent JFrame.
	 * @param gecko				Boolean to indicate if Gecko was used or not.
	 */
	public GraphMouseListenerWordCloud(LinkedHashMap<String, TreeNode> mapTree,
			LinkedHashMap<String, List<Gene>> genesInNodes,  JFrame parent, boolean gecko) {
		super(mapTree);
		//
		this.parent = parent;
		this.gecko = gecko;
		this.genesInNodes = genesInNodes;
	}

	@Override
	public void graphClicked(Object v, MouseEvent me) {
		//
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout());
		TreeNode node = map.get(v);
		//
		if (me.getButton() == MouseEvent.BUTTON1) {
			//
			ClickAwayDialog dialog = new ClickAwayDialog(parent, v);
			if (gecko == false && node.hasChild()) {
				// Get cluster id and genes associated with it
				TreeNode child = map.get(node.getDefaultChildID());
				String field = child.getNodeField();
				List<Gene> genes = getGenesInNodes(field);
				// Create word cloud
				panel.add(new ViewWordCloud(genes), "align center, wrap");
				// Change dimension
				dialog.setPreferredSize(new Dimension(525, 600));
				dialog.setMinimumSize(new Dimension(525, 600));					
			}
			//
			panel.add(new BarplotPanel(node), "align center");
			dialog.add(panel);
			int x = me.getXOnScreen();
			int y = me.getYOnScreen();
			dialog.setLocation(x,y);
			dialog.setVisible(true);
		}
		me.consume();				
	}

	@Override
	public void graphPressed(Object v, MouseEvent me) { }

	@Override
	public void graphReleased(Object v, MouseEvent me) { }
	
	/**
	 * @param clusterID		String for the cluster ID.
	 * @return				Returns a list of all genes 
	 * 						present in the node, i.e., associated 
	 * 						with the given cluster ID.
	 */
	protected List<Gene> getGenesInNodes(String clusterID) {
		List<Gene> genes = null;
		String cluster = parseNodeField(clusterID);
		// Get all genes in node
		genes = genesInNodes.get(cluster);
		//
		return genes;
	}
}	

