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
package dk.sdu.imada.gui.transclust;


import java.text.NumberFormat;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import dk.sdu.imada.gui.AbstractPanel;
import dk.sdu.imada.methods.transclust.TransClust;


/**
 * Class creates and displays Transitivity Clustering run information 
 * in a new panel. Information includes Blast and TransClust  
 * runtime, as well as basic statistics about the clusters.
 * 
 * @author Eudes Barbosa
 */
public class InformationPanelTC extends AbstractPanel {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = -5187449302850290709L;

	/** JTextArea where the clusters information will be displayed. */
	protected JTextArea textarea = new JTextArea();

	/** TransClust object with information about the parameters and runtime. */
	protected TransClust tc;

	/** Occurrences of all clusters throughout the genomes. */
	protected double[] occur;

	/** Cluster size 50th percentil. */
	protected int p50;

	/** Cluster size 75th percentil. */
	protected int p75;

	/** Cluster size 95th percentil. */
	protected int p95;

	/** Largest cluster. */
	protected int largest;

	/** Total number of clusters. */
	protected long totalClusters;

	/** Total number of genes under analysis (all genomes). */
	protected long totalGenes;

	//------  Declaration end  ------//


	/**
	 * Displays Transitivity Clustering run information into 
	 * a new panel. Information includes Blast and TransClust  
	 * runtime, as well as basic statistics about the clusters.
	 * 
	 * @param tc			TransClust object with information about 
	 * 						the parameters and runtime.
	 * 
	 * @param occurrences	Occurrences of all clusters throughout the 
	 * 						genomes. In this case, the specific information 
	 * 						about each is not relevant. It will just report 
	 * 						a summary.
	 */
	public InformationPanelTC(TransClust tc, double[] occurrences) {
		//
		this.tc = tc;
		this.occur = occurrences;  
		// 
		initComponents();
		configureLayout();		
	}

	@Override
	protected void configureLayout() {
		JScrollPane scroll = new JScrollPane(textarea);
		add(scroll);
	}

	@Override
	protected void initComponents() {
		// Collect info about clusters
		summaryClusters();
		// Set text
		String text = infoText();
		textarea.setText(text);
		textarea.setFont(LabelFontPlain);
	}

	/**
	 * @return		Returns text with summary of Transitivity Clustering 
	 * 				results and the runtime.
	 */
	private String infoText() {
		// Convert milliseconds to hours/minutes
		double realBlast = (2.78 * Math.pow(10, -7)) * tc.getRuntimeBlastReal();
		double estBlast = (2.78 * Math.pow(10, -7)) * tc.getRuntimeBlastEstimated();
		double trnsclust = (1.67 * Math.pow(10, -5)) * tc.getRuntimeTransClust();
		// Create text
		String text = "Homology detection - density parameter = " + tc.getStart() +
				"\n" +
				"\nTotal number of genes: " + NumberFormat.getIntegerInstance().format(totalGenes)+
				"\nTotal number of clusters: " + NumberFormat.getIntegerInstance().format(totalClusters)+
				"\nLargest cluster contains "+ largest + " gene products." +
				"\n50th percentil: "+ p50 +
				"\n75th percentil: "+ p75 +
				"\n95th percentil: "+ p95 +
				"\n\n----\n" +
				"\nTime required to run Blast: " + String.format("%.2f", realBlast) + " (" + String.format("%.2f", estBlast) + ") hour(s)." +
				"\nTime required to run TransClust: " + String.format("%.2f", trnsclust) + " minute(s)."		
				;
		return text;
	}

	/**
	 * Collects information about the clusters: 
	 * 50th, 75th and 95th percentils; plus, the total 
	 * number of clusters and the largest one.
	 */
	private void summaryClusters() {
		DescriptiveStatistics da = new DescriptiveStatistics(occur);
		// Get quartile information		
		p50 = (int) Math.abs(da.getPercentile(50));
		p75 = (int) Math.abs(da.getPercentile(75));
		p95 = (int) Math.abs(da.getPercentile(95));
		// Get other values
		largest = (int) Math.abs(da.getMax());
		totalClusters = da.getN();	
		totalGenes = Math.round(da.getSum());
	}
}
