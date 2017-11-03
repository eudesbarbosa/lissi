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

import java.util.ArrayList;

/**
 * Class defines a tree node object.
 * 
 * @author Eudes Barbosa
 */
public class TreeNode {

	//------  Variable declaration  ------//

	/** Node identifier. */
	protected String id = "";
	
	/** Number of elements in node. Default = 0. */
	protected long totalElementsInNode = 0;
	
	/** Node field. */
	protected String nodeField = "Root";
	
	/** Node operator (e. g., 'greater than'). */
	protected String operator = "";
	
	/** Node classification. */
	protected String classification = "";
	
	/** Node immediate parent. Default = 'root'. */
	protected String immediateParent = "root";
	
	/** 
	 * Flag to indicate if node has child. 
	 * Default = False.
	 */
	protected boolean hasChild = false;
	
	/** Default child identifier value. */
	protected String defaultChildID = "none";
	
	/** ArrayList with node scores. */
	protected ArrayList<ScoreDistribution> scores = new ArrayList<>();
	
	//------  Declaration end  ------//

	/** @return	Returns the default child identifier. */
	public String getDefaultChildID() {
		return defaultChildID;
	}

	/**
	 * Sets default child identifier.
	 * 
	 * @param defaultChildID The default child identifier.
	 */
	public void setDefaultChildID(String defaultChildID) {
		this.defaultChildID = defaultChildID;
	}
	
	/**
	 *  @return Returns True if node has child;
	 * 	False, otherwise.
	 */
	public boolean hasChild() {
		return hasChild;
	}

	/**
	 * Sets if node has child.
	 * 
	 * @param hasChild	'True' if node has child
	 * 					and 'False' otherwise.
	 */
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	
	/**
	 * @return Returns the node classifications, i.e., 
	 * the class that is mainly represented.
	 */
	public String getClassification() {
		return classification;
	}

	/**
	 * Sets class of node.
	 * 
	 * @param classification The node classifications, i.e., 
	 * 						 the class that is mainly represented.
	 */
	public void setClassification(String classification) {
		this.classification = classification;
	}
	
	/** @return Returns the node immediate parent, if exists. */
	public String getImmediateParent() {
		return immediateParent;
	}

	/**
	 * Sets node immediate parent.
	 * 
	 * @param immediateParent	The node immediate parent, if exists.
	 */
	public void setImmediateParent(String immediateParent) {
		this.immediateParent = immediateParent;
	}

	/** @return Returns the node identifier. */
	public String getId() {
		return id;
	}

	/** @return Returns node scores. */
	public ArrayList<ScoreDistribution> getScores() {
		return scores;
	}

	/**
	 * Sets node score distribution.
	 * 
	 * @param score Node scores.
	 */
	public void addScores(ScoreDistribution score) {
		this.scores.add(score);
	}

	/**
	 * Sets node identifier.
	 * 
	 * @param id 	Node identifier.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return Returns the total number of elements in node.
	 */
	public long getTotal() {
		return totalElementsInNode;
	}

	/** 
	 * Sets total number of elements in node.
	 * 
	 * @param total	Total number of elements in node.
	 */
	public void setTotal(long total) {
		this.totalElementsInNode = total;
	}

	/** @return Returns node field/feature used for split. */
	public String getNodeField() {
		return nodeField;
	}

	/**
	 * Sets node field.
	 * 
	 * @param nodeField	The node field/feature used for split.
	 */
	public void setNodeField(String nodeField) {
		this.nodeField = nodeField;
	}

	/**
	 * @return Returns node operator. If present, greater than. 
	 * If absent, smaller or equal than.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets node operator.
	 * @param operator	The node operator. If present, greater than. 
	 * 					If absent, smaller or equal than.
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Inner-class defines the node score distribution.
	 */
	public static class ScoreDistribution {

		//------  Variable declaration  ------//
		
		/** Lifestyle assoiated with node. */
		protected String lifestyle = "";
		
		/** Count of elements in node. */
		protected long count = -1;
		
		/** Confidence. */
		protected String confidence = "";
		
		//------  Declaration end  ------//

		/** @return Returns the lifestyle. */
		public String getLifestyle() {
			return lifestyle;
		}

		/**
		 * Sets lifestyle.
		 * 
		 * @param lifestyle Lifestyle.
		 */
		public void setLifestyle(String lifestyle) {
			this.lifestyle = lifestyle;
		}

		/** @return Returns the count. */
		public long getCount() {
			return count;
		}

		/**
		 * Sets count.
		 * 
		 * @param count Count.
		 */
		public void setCount(long count) {
			this.count = count;
		}

		/** @return Returns the confidence. */
		public String getConfidence() {
			return confidence;
		}

		/**
		 * Sets confidence.
		 * 
		 * @param confidence	Confidence.
		 */
		public void setConfidence(String confidence) {
			this.confidence = confidence;
		}		

	}

}
