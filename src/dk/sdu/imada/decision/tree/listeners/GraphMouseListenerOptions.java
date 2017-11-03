package dk.sdu.imada.decision.tree.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

import dk.sdu.imada.decision.tree.CommomTreeMethods;
import dk.sdu.imada.decision.tree.DecisionTreeView;
import dk.sdu.imada.decision.tree.TreeNode;
import dk.sdu.imada.decision.tree.listeners.clusters.GraphListenerViewGeneInfo;
import dk.sdu.imada.decision.tree.listeners.clusters.GraphListenerViewGeneProfile;
import dk.sdu.imada.decision.tree.listeners.islands.GraphListenerViewIsland;
import dk.sdu.imada.decision.tree.listeners.islands.GraphListenerViewIslandProfile;
import dk.sdu.imada.methods.gecko.IslandGecko;
import dk.sdu.imada.methods.genome.Gene;

import edu.uci.ics.jung.visualization.control.GraphMouseListener;

/**
 * Class creates a mouse listener associated with a pop up menu. It will be open 
 * when the user click with the right button in any given node from the decision 
 * tree.
 * 
 * @author Eudes Barbosa (eudes@imada.sdu.dk)	
 */
@SuppressWarnings("rawtypes")
public class GraphMouseListenerOptions extends CommomTreeMethods implements GraphMouseListener {

	static Logger logger = Logger.getLogger(GraphMouseListenerOptions.class);

	//----------------------//
	// Variable declaration //
	//----------------------//

	/** Flag to indicate if Gecko was used in the analysis. */
	protected boolean gecko = false;

	/** */
	protected JPopupMenu popup;

	/** */
	protected TreeNode node;

	/** 
	 * Map the nodes and their information. It has  
	 * an Integer as key, and a TreeNode object as 
	 * value. 
	 * Note: A child node connected by a "No" (absent) 
	 * are always odd numbers, while "Yes" (present) 
	 * are always even. 
	 */
	protected LinkedHashMap<String, TreeNode> mapTree =	
			new LinkedHashMap<String, TreeNode>();

	/** 
	 * Stores a list of Gene objects that are present in each 
	 * of the decision tree nodes. The key is a String with the 
	 * node identifier, and the value is the List of Genes. 
	 */
	protected LinkedHashMap<String, List<Gene>> genesInNodes;

	/** 
	 * Stores the IslandGecko objects that are present in each 
	 * of the decision tree nodes. The key is a String with the 
	 * node identifier, and the value is the List of Islands. 
	 */
	protected LinkedHashMap<String, IslandGecko> islandsInNodes;

	//------------------//
	// Declaration end  //
	//------------------//

	/**
	 * Creates a mouse listener for gene cluster analysis (without Gecko). 
	 * A pop up menu will be open when the user click with the right button 
	 * in any given node from the decision tree.
	 * 
	 * @param mapTree		Map with nodes and their information. It has  
	 * 						an Integer as key, and a TreeNode object as 
	 * 						value. A child node connected by a "No" (absent)  
	 *						are always odd numbers, while "Yes" (present) 
	 * 						are always even.
	 * 
	 * @param genesInNodes	List of Gene objects that are present in each 
	 * 						of the decision tree nodes. The key is a String 
	 * 						with the node identifier, and the value is the 
	 * 						List of Genes. 
	 */
	public GraphMouseListenerOptions(LinkedHashMap<String, TreeNode> mapTree, 
			LinkedHashMap<String, List<Gene>> genesInNodes) {
		//
		this.mapTree = mapTree;
		this.genesInNodes = genesInNodes;
		this.gecko = false;		
	}

	/**
	 * Creates a mouse listener for gene cluster analysis (with Gecko). 
	 * A pop up menu will be open when the user click with the right button 
	 * in any given node from the decision tree.
	 * 
	 * @param mapTree			Map with nodes and their information. It has  
	 * 							an Integer as key, and a TreeNode object as 
	 * 							value. A child node connected by a "No" (absent)  
	 *							are always odd numbers, while "Yes" (present) 
	 * 							are always even.
	 * 
	 * @param islandsInNodes	List of islands objects that are present in each 
	 * 							of the decision tree nodes. The key is a String 
	 * 							with the node identifier, and the value is the 
	 * 							List of Genes. 
	 * 
	 * @param gecko				Boolean is not used. It simply helps to 
	 * 							differentiate the Constructors.	
	 */
	public GraphMouseListenerOptions(LinkedHashMap<String, TreeNode> mapTree, 
			LinkedHashMap<String, IslandGecko> islandsInNodes, boolean gecko) {
		//
		this.mapTree = mapTree;
		this.islandsInNodes = islandsInNodes;
		this.gecko = true;
	}

	/**
	 * Creates a pop up menu for each node
	 * in the tree. The menu will be activated  
	 * when user click with right button. 
	 * <br>
	 * Note: Analysis using gene clusters and 
	 * islands will have different menus.
	 * 
	 * @param v		Tree node.
	 * @param me	Mouse event.
	 */
	public void graphClicked(Object v, MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON3) {
			//
			node = mapTree.get(v);		
			if(node.hasChild()) {							
				if (gecko == false) {					
					popup = new ClusterPopupMenu();
				} else if (gecko == true) {
					popup = new IslandPopupMenu();
				}
				//System.out.println("Options for node " + v);
				popup.show(me.getComponent(), me.getX(), me.getY());
			}
		}
		me.consume();				
	}

	@Override
	public void graphPressed(Object v, MouseEvent me) {	}

	@Override
	public void graphReleased(Object v, MouseEvent me) { }

	/** @return Returns user's threshold similiarity. */
	protected int getSimilarityThreshold() {
		String[] choices = { "100", "95", "90", "85", "80", "75" };
		String sim = (String) JOptionPane.showInputDialog(null, "Available options:",
				"Similarity Threshold (%)", JOptionPane.QUESTION_MESSAGE, null, 
				choices, // Array of choices
				choices[0] // Initial choice
				); 
		//
		if (sim == null) {
			return -1;
		}
		return Integer.parseInt(sim);
	}

	/**
	 * Inner-class creates a pop up menu with options for 
	 * islands analysis.
	 */
	protected class IslandPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = -1080798110973648777L;

		/** Instanciate class. */
		public IslandPopupMenu() {
			//
			final JMenuItem itemViewInfo;		
			final JMenuItem itemViewProfile;

			//-------------------------------------------------------//
			// View island information
			add(itemViewInfo = new JMenuItem("Island Information", null));
			itemViewInfo.setHorizontalTextPosition(JMenuItem.RIGHT);
			/* Add listener - If frame doesn't exist, create one. 
			 * Otherwise, find it in reference and set as visible. */
			itemViewInfo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean islandFrameAlreadyExists = DecisionTreeView.validateInformationFrame(node);
					if (islandFrameAlreadyExists == true) { //find in reference
						DecisionTreeView.getInformationFrame(node).setVisible(true);	
					} else if (islandFrameAlreadyExists == false) { //create frame
						// Get island id and genes associated with it
						TreeNode child = mapTree.get(node.getDefaultChildID());
						String field = child.getNodeField();
						IslandGecko is = islandsInNodes.get(field);
						String fieldParsed = parseNodeField(field);
						// Create frame
						JFrame frame = new GraphListenerViewIsland(is, fieldParsed);
						DecisionTreeView.insertNewInformationFrame(node, frame);
						frame.setVisible(true);
					}
				}							
			});		

			//-------------------------------------------------------//
			// View profile information
			add(itemViewProfile = new JMenuItem("Similar Profiles", null));
			itemViewProfile.setHorizontalTextPosition(JMenuItem.RIGHT);
			/* Add listener - If frame doesn't exist, create one. 
			 * Otherwise, find it in reference and set as visible. */
			itemViewProfile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*
					 boolean profileFrameAlreadyExists = DTView.validateProfileFrame(node);
					if (profileFrameAlreadyExists == true) { //find in reference
						DTView.getProfileFrame(node).setVisible(true);	
					} else 	if (profileFrameAlreadyExists == false) { //create frame
					 */
					// Get island id and genes associated with it
					TreeNode child = mapTree.get(node.getDefaultChildID());
					String field = child.getNodeField();
					String fieldParsed = parseNodeField(field);
					// Create frame
					int sim = getSimilarityThreshold();
					if (sim > -1) {
						JFrame frame = new GraphListenerViewIslandProfile(fieldParsed, sim);
						// DTView.insertNewProfileFrame(node, frame);
						frame.setVisible(true);
						/*} else { 
							return;
						}
						 */
					}
				}
			});	
		}
	}

	/**
	 * Inner-class creates a pop up menu with options for clusters or 
	 * individual gene analysis.
	 */
	protected class ClusterPopupMenu extends JPopupMenu {

		private static final long serialVersionUID = 4604952372411258160L;

		/** Instanciate class. */
		public ClusterPopupMenu() {
			//
			final JMenuItem itemViewInfo;
			final JMenuItem itemViewProfile;

			// Get cluster id and genes associated with it
			TreeNode child = mapTree.get(node.getDefaultChildID());
			String field = child.getNodeField();
			List<Gene> genes = getGenesInNodes(field, genesInNodes);

			//-------------------------------------------------------//
			// View gene information
			add(itemViewInfo = new JMenuItem("Gene Information", null));
			itemViewInfo.setHorizontalTextPosition(JMenuItem.RIGHT);
			/* Add listener - If frame doesn't exist, create one. 
			 * Otherwise, find it in reference and set as visible. */
			itemViewInfo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					boolean frameAlreadyExists = DecisionTreeView.validateInformationFrame(node);
					if (frameAlreadyExists == true) { //find in reference
						DecisionTreeView.getInformationFrame(node).setVisible(true);	
					} else if (frameAlreadyExists == false) { //create frame
						// Get cluster id and genes associated with it
						TreeNode child = mapTree.get(node.getDefaultChildID());
						String field = child.getNodeField();
						List<Gene> genes = getGenesInNodes(field, genesInNodes);
						// Create frame
						JFrame frame = new GraphListenerViewGeneInfo(genes, parseNodeField(field));
						DecisionTreeView.insertNewInformationFrame(node, frame);
						frame.setVisible(true);
					}
				}
			});	

			//-------------------------------------------------------//
			// View profile information
			add(itemViewProfile = new JMenuItem("Similar Profiles", null));
			itemViewProfile.setHorizontalTextPosition(JMenuItem.RIGHT);
			/* Add listener - If frame doesn't exist, create one. 
			 * Otherwise, find it in reference and set as visible. */
			itemViewProfile.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					/*
					boolean profileFrameAlreadyExists = DTView.validateProfileFrame(node);
					if (profileFrameAlreadyExists == true) { //find in reference
						DTView.getProfileFrame(node).setVisible(true);	
					} else 	if (profileFrameAlreadyExists == false) { //create frame
					 */
					// Get cluster id
					TreeNode child = mapTree.get(node.getDefaultChildID());
					String field = child.getNodeField();
					String fieldParsed = parseNodeField(field);
					// Create frame
					int sim = getSimilarityThreshold();
					if (sim > -1) {
						JFrame frame = new GraphListenerViewGeneProfile(fieldParsed, sim);
						//DTView.insertNewProfileFrame(node, frame);
						frame.setVisible(true);
						/*	
						} else {
							return;
						}
						 */
					}
				}
			});	

			//-------------------------------------------------------//					
			// Export as fasta
			addSeparator();
			JMenuItem itemExportFasta;	
			add(itemExportFasta = new JMenuItem("Export as Fasta", null));
			itemExportFasta.setHorizontalTextPosition(JMenuItem.RIGHT);
			itemExportFasta.addActionListener(new GraphListenerExportFasta(genes));			
		}		
	}
}
