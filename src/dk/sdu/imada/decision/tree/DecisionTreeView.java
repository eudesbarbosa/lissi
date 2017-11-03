package dk.sdu.imada.decision.tree;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections15.Transformer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import dk.sdu.imada.decision.tree.listeners.GraphMouseListenerOptions;
import dk.sdu.imada.decision.tree.listeners.GraphMouseListenerWordCloud;
import dk.sdu.imada.decision.tree.transformers.EdgeLabel;
import dk.sdu.imada.decision.tree.transformers.VertexLabel;
import dk.sdu.imada.decision.tree.transformers.VertexPaint;
import dk.sdu.imada.decision.tree.transformers.VertexShape;
import dk.sdu.imada.decision.tree.transformers.VertexTip;
import dk.sdu.imada.gui.MainFrame;
import dk.sdu.imada.gui.ProgressPanel;
import dk.sdu.imada.methods.gecko.IslandGecko;
import dk.sdu.imada.methods.genome.Gene;
import dk.sdu.imada.methods.genome.GeneInformation;
import dk.sdu.imada.methods.statistics.IndicatorMatrixTreeView;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


/**
 * Class creates a visualization for the decision tree (DT)  
 * generated in the analysis. It starts by parsing the 
 * DT file (xml format) and ends by plotting and adding 
 * functionalities to each node, for example: running 
 * NCBI Blast or Pfam. <br>
 * The class requires that it is established if the analysis 
 * was performed using only the gene clusters (BFG) or using 
 * the islands (LiSSI).
 *
 * @author Eudes Barbosa
 */
public class DecisionTreeView extends JPanel {
	/* Cool alternative implementation: 
	 * http://r-and-java.blogspot.dk/2013/04/strategies-for-integrating-r-with-java.html
	 */
	
	//------  Variable declaration  ------//

	private static final Logger logger = LogManager.getLogger(DecisionTreeView.class.getName());

	private static final long serialVersionUID = -8167156677682573825L;	


	/** Adds mouse behaviors and tooltips to the graph visualization base class. */
	protected VisualizationViewer<String, String> vs;	

	/** Objects holds parsed tree information. */
	protected ParseTree tree;

	/** 
	 * Map the nodes and their information. It has  
	 * an Integer as key, and a TreeNode object as 
	 * value. A child node connected by a "No" (absent) 
	 * are always odd numbers, while "Yes" (present) 
	 * are always even. This is important for to set 
	 * the edges labels in the Transformer associated 
	 * with the method 'setEdgeLabelTransformer()'.  
	 * The object is created at the same time as the DelegaTree, 
	 * method: mapNodeInformation().
	 */
	protected LinkedHashMap<String, TreeNode> mapTree =	
			new LinkedHashMap<String, TreeNode>();

	/** 
	 * Stores a list of Gene objects that are present in each 
	 * of the decision tree nodes. The key is a String with the 
	 * node identifier, and the value is the List of Genes. 
	 */
	protected LinkedHashMap<String, List<Gene>> genesInNodes =	
			new LinkedHashMap<String, List<Gene>>();			

	/** 
	 * Stores the IslandGecko objects that are present in each 
	 * of the decision tree nodes. The key is a String with the 
	 * node identifier, and the value is the List of Islands. 
	 */
	protected LinkedHashMap<String, IslandGecko> islandsInNodes =	
			new LinkedHashMap<String, IslandGecko>();	

	/** 
	 * Map each node (TreeNode) to a frame (JFrame). The frames 
	 * are created by calling either 'View Gene Information' or 
	 * 'View Island Information'.
	 */
	protected static LinkedHashMap<TreeNode, JFrame> nodeInfoFrame =	
			new LinkedHashMap<TreeNode, JFrame>();

	/** 
	 * Map each node (TreeNode) to a frame (JFrame). The frames 
	 * are created by calling either 'View Gene Profile' or 
	 * 'View Island Profile'.
	protected static LinkedHashMap<TreeNode, JFrame> nodeProfileFrame =	
			new LinkedHashMap<TreeNode, JFrame>(); 
	 */

	/** Array containing all Gecko islands. */
	protected static ArrayList<IslandGecko> geckoIslands = new ArrayList<>();

	/** Flag to indicate if Gecko was used in the analysis. */
	protected boolean gecko = false;

	
	//------  Declaration end  ------//


	/**
	 * Creates decision tree visualization for BFG 
	 * analysis (i.e., without islands [Gecko]).
	 *  
	 * @param title			Title of the plot. It will name 
	 * 						the panel.
	 * 
	 * @param treeFile		Path to decision tree file (xml format).
	 * @param giniTree		Path to decision tree gini index (csv 
	 * 						tabular format).	
	 */
	public DecisionTreeView(String title, String treeFile, String giniTree) {
		super();
		setBackground(Color.WHITE);
		// Parse tree
		parsesDecisionTree(treeFile);
		// Map node information
		DelegateTree<String,String> nodes = mapNodeInformation();
		// Get genes present in each node
		GeneInformation info = new GeneInformation(mapTree);
		genesInNodes = info.getMappedGenes();
		//
		visualize(nodes, giniTree);
		//
		configLayout(title);
	}

	/**
	 * Creates decision tree visualization for LiSSI 
	 * analysis (i.e., with islands [Gecko]).
	 * 
	 * @param title			Title of the plot. It will name 
	 * 						the panel.
	 * @param treeFile		Path to decision tree file (xml format).
	 * @param giniTree		Path to decision tree gini index (csv 
	 * 						tabular format).
	 * @param geckoFile		Path to Gecko output file.
	 */
	public DecisionTreeView(String title, String treeFile, String giniTree, String geckoFile) {
		super();
		setBackground(Color.WHITE);
		//
		this.gecko = true;
		// Parse tree
		parsesDecisionTree(treeFile);
		// Map node information
		DelegateTree<String,String> nodes = mapNodeInformation();
		// Get island information
		islandsInNodes = getIslandsInNodes(geckoFile);
		logger.info("I made it so far...");

		// Configure visualization
		visualize(nodes, giniTree);
		configLayout(title);
		logger.info("I should be done...(??)");
	}	

	/**
	 * Call methods to parse decision tree.
	 * @param treeFile		Path to decision tree file (xml format).
	 */
	protected void parsesDecisionTree(String treeFile) {
		// Parse tree xml file (structure)
		tree = new ParseTree(treeFile);
		// Calculate statistical functions
		tree.calculateValuesStatisticalFunctions();
	}

	/**
	 * Configures the layout of the components
	 * within the panel.
	 *  
	 * @param title		Title of the plot. It will name 
	 * 					the panel.
	 */
	protected void configLayout(String title) {
		setLayout(new MigLayout());
		setBorder(BorderFactory.createTitledBorder(title));
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.add(getLabelTreeStatistics(),"align left, gapleft 5, wrap");
		panel.add(vs, "wrap");
		add(new JScrollPane(panel));
	}

	/** @return	Returns a JLabel with the tree Statistical functions. */
	protected JLabel getLabelTreeStatistics() {
		JLabel label = new JLabel();
		label.setText( "<html><b>Specificity: </b>" + String.format("%.3g%n", tree.getSpecificity()) + "%"
				+ "<br><b>Sensitivity: </b>" + String.format("%.3g%n", tree.getSpecificity()) + "%"
				+ "<br><b>Accuracy: </b>" + String.format("%.3g%n", tree.getAccuracy()) + "%"
				+ "<br><i>(based on training set)</i><html>"
				);			
		//
		return label;
	}

	/**
	 * Creates a representation of the decision tree by 
	 * adding information to a DelegateTree object; while 
	 * mapping the nodes and their information. The Map 
	 * has an Integer as key, and a TreeNode object as 
	 * value. A child node connected by a "No" (absent) are 
	 * always odd numbers, while "Yes" (present) are always 
	 * even. 
	 * <br>
	 * This is important for to set the edges labels in the 
	 * Transformer associated with the method 
	 * 'setEdgeLabelTransformer()'. 
	 * 
	 * @return		Returns a DelegateTree that contains the 
	 * 				decision tree scheme.
	 */
	protected DelegateTree<String,String> mapNodeInformation() {
		DelegateTree<String, String> tree = new DelegateTree<String,String>();	
		int even = 2;
		int odd = 3;
		ArrayList<TreeNode> nodes = this.tree.getNodes();
		for (TreeNode n : nodes) {
			//System.out.println(n.getClassification());
			if (n.getImmediateParent().equals("root")) {
				tree.setRoot(n.getId());
				mapTree.put(n.getId(), n);
				continue;
			} else {
				if (n.getOperator().contains("ABS")) {
					tree.addChild(""+odd, n.getImmediateParent(), n.getId());
					mapTree.put(n.getId(),n);
				} else if (n.getOperator().contains("PRSNT")) { 
					tree.addChild(""+even, n.getImmediateParent(), n.getId());	
					mapTree.put(n.getId(),n);
				}			
			}
			//
			even += 2;
			odd += 2;
		}
		//
		return tree;
	}	

	/**
	 * @param geckoFile		Path to Gecko output file.
	 * @return				Returns a map associating the Gecko ID (String - Key) 
	 * 						with an IslandGecko object (contains a representation of 
	 * 						all islands for a given Gecko ID - Value). 
	 */
	protected LinkedHashMap<String, IslandGecko> getIslandsInNodes(String geckoFile) {
		// Initialize variable
		LinkedHashMap<String, IslandGecko> islandsInNodes =	
				new LinkedHashMap<String, IslandGecko>();

		// Parse Gecko output file and get all islands
		String local = MainFrame.getGlobalParameters().getLocalDir();
		int threads  = ProgressPanel.getProcessors();
		IndicatorMatrixTreeView parse = new IndicatorMatrixTreeView(geckoFile, 
				local, threads);
		geckoIslands = parse.getIslandList();
		ArrayList<TreeNode> nodes = this.tree.getNodes();
		for (TreeNode node : nodes) {
			if (node.hasChild()) {
				// Get cluster id and genes associated with it
				TreeNode child = mapTree.get(node.getDefaultChildID());
				String field = child.getNodeField();
				//System.out.println("@@@@@ " + field);
				String nodeID = parseNodeField(field);
				//System.out.println("@@@@@ " + nodeID);
				for (IslandGecko island : geckoIslands) {
					String islandID = island.getId() + "";
					//System.out.println("#### " + islandID);
					if (islandID.equals(nodeID)) {
						//System.out.println("Essa porra functiona? Sim, para o id " + islandID);
						islandsInNodes.put(field, island);
						continue;
					}
				}
			}
		}
		//
		return islandsInNodes;
	}

	/**
	 * @param node	Node field.
	 * @return		Returns the parsed node field, 
	 * 				meaning without 'ID.' and any 
	 * 				eventual pipe character ('|').
	 */
	protected String parseNodeField(String node) {
		// Parse field if necessary
		String clusterID = node;
		if (clusterID.contains("|")) {
			String[] data = clusterID.split("\\|");
			clusterID = data[0];
		}
		clusterID = clusterID.replaceAll("ID\\.", "");
		//
		return clusterID;
	}

	/**
	 * Creates the decision tree visualization and add all 
	 * Transformers and Listeners.
	 * @param tree			DelegateTree object.
	 * @param giniTree		Path to decision tree gini index (csv 
	 * 						tabular format).
	 */
	@SuppressWarnings("unchecked")
	protected void visualize(DelegateTree<String, String> tree, String giniTree) {
		// Configure Tree Layout
		vs = new VisualizationViewer<String, String>(
				(Layout<String, String>) new FRLayout<String,String>(tree), new Dimension(1000, 1000));
		TreeLayout<String, String> layout;
		layout = new TreeLayout<String, String>(tree, 200, 200); 
		vs.setGraphLayout(layout); 
		vs.setBackground(Color.WHITE);

		// Get render to include transformers
		RenderContext<String, String> renderContext = vs.getRenderContext();

		//Edits vertex size and shape
		Transformer<String,Shape> vertexShape = new VertexShape();
		renderContext.setVertexShapeTransformer(vertexShape);
		vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		// Edits the label associated with the edge
		Transformer<String, String> edgeLabel = new EdgeLabel();
		renderContext.setEdgeLabelTransformer(edgeLabel);

		// Modify vertex label
		Transformer<String,String> vertexLabel = new VertexLabel(mapTree);
		renderContext.setVertexLabelTransformer(vertexLabel);

		// Modify vertex color
		Transformer<String,Paint> vertexPaint = new VertexPaint(mapTree);  
		renderContext.setVertexFillPaintTransformer(vertexPaint);

		// Add tool tip to vertex
		LinkedHashMap<String, String> mappedGini = this.tree.getMeanDecreaseGini(giniTree);
		Transformer<String,String> vertexTip = new VertexTip(mapTree, mappedGini);
		vs.setVertexToolTipTransformer(vertexTip);

		// Add mouse listener (word cloud)
		JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
		vs.addGraphMouseListener(new GraphMouseListenerWordCloud(mapTree, genesInNodes, parent, gecko));

		// Add mouse listener (pop up menu)
		if (gecko == true)
			vs.addGraphMouseListener(new GraphMouseListenerOptions(mapTree, islandsInNodes, gecko));
		else
			vs.addGraphMouseListener(new GraphMouseListenerOptions(mapTree, genesInNodes));
	}

	/**
	 * @param node	TreeNode object.
	 * @return		Returns True if it a frame was 
	 * 				previously created for this node 
	 * 				and it is still alive; and False, 
	 * 				otherwise.
	 */
	public static boolean validateInformationFrame(TreeNode node) {
		try {
			// Just checking if exits, I might 
			// not use it...
			JFrame frame = nodeInfoFrame.get(node);
			frame.isActive(); //test condition
			return true;
		} catch (Exception e) {
			nodeInfoFrame.remove(node);
			return false;
		}
	}

	/** 
	 * Inserts new frame into map.
	 * @param node	TreeNode object.
	 * @param frame	JFrame object.
	 */
	public static void insertNewInformationFrame(TreeNode node, JFrame frame) {
		nodeInfoFrame.put(node, frame);
	}

	/**
	 * @param node	TreeNode object.
	 * @return		Returns JFrame associated with 
	 * 				TreeNode. 
	 */
	public static JFrame getInformationFrame(TreeNode node) {		
		return nodeInfoFrame.get(node);
	}	

	/** @return	Returns all Gecko islands. */
	public static ArrayList<IslandGecko> getAllGeckoIslands() {
		return geckoIslands;
	}
	
	/** 
	 * Inserts new frame into map.
	 * @param node	TreeNode object.
	 * @param frame	JFrame object.

	public static void insertNewProfileFrame(TreeNode node, JFrame frame) {
		nodeProfileFrame.put(node, frame);
	}
	 */

	/**
	 * @param node	TreeNode object.
	 * @return		Returns the frame associated with 
	 * 				a given node.

	public static JFrame getProfileFrame(TreeNode node) {
		return nodeProfileFrame.get(node);
	}
	 */

	/**
	 * @param node	TreeNode object.
	 * @return		Returns True if it a frame was 
	 * 				previously created for this node 
	 * 				and it is still alive. False, 
	 * 				otherwise.

	public static boolean validateProfileFrame(TreeNode node) {
		try {
			// Just checking if exits, I might 
			// not use it...
			JFrame frame = nodeProfileFrame.get(node);
			frame.isActive(); //test condition
			return true;
		} catch (Exception e) {
			nodeProfileFrame.remove(node);
			return false;
		}
	}
	 */
}