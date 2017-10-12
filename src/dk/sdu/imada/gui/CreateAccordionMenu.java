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
package dk.sdu.imada.gui;


import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gg.slider.SideBar;
import com.gg.slider.SidebarSection;

import dk.sdu.imada.gui.genome.GenomeSelection;


/**
 * Class creates an Accordion Menu. The menu 
 * is divided into: Genomes Selection, Analysis and Results. The menu is 
 * associated with the MultiSplitPane. It is expected that for each item 
 * in the menu there is a corresponding panel in the MultiSplitPane.
 *<br><br>
 * - Gray arrow: <br>
 * Artist: Custom Icon Design <br>
 * License: Free for non-commercial use. <br>
 * http://www.iconarchive.com/show/mono-general-3-icons-by-custom-icon-design.html <br>
 * <br>
 * @author Eudes Barbosa	
 */
public class CreateAccordionMenu extends SideBar {
	
	//------  Variable declaration  ------//

	private static final long serialVersionUID = 667328899522559930L;

	private static final Logger logger = LogManager.getLogger(CreateAccordionMenu.class.getCanonicalName());

	/** 
	 * JList that stores the genome methods: load  
	 * and select genomes. 
	 */
	protected static JList<String> methodsList;

	/** 
	 * SideBar associated with genome methods: load  
	 * and select genomes.  
	 */
	protected static SidebarSection ssWizard;

	/** SideBar associated with analysis (parameters). */
	protected static SidebarSection ssAnalysis;

	/** 
	 * SideBar associated with results. As the analysis 
	 * is started and the new tabs will be added to this 
	 * object. 
	 */
	protected static SideBar innerSideBar;

	//------  Declaration end  ------//

	
	/**
	 * Creates and configures an accordion menu. The menu is divided into: 
	 * Genomes Selection, Analysis and Results. The menu is associated with 
	 * the MultiSplitPane. It is expected that for each item in the menu 
	 * there is a corresponding panel in the MultiSplitPane.
	 */
	public CreateAccordionMenu() {
		super(SideBarMode.TOP_LEVEL, true, 150, true);

		// Section: 'Genome Selection'
		String[] GenomesMethodsNames = {"Load Genomes", "Select Genomes","Parameters"};
		methodsList = new CreateJListNames(GenomesMethodsNames);
		methodsList.setEnabled(false);
		ssWizard = new SidebarSelectableAnalysis(this, "Analysis", methodsList, null);
		addSection(ssWizard);
		//ssWizard.expand();

		//--------------------------------------------------------------------
		/*
		// Section: 'Analysis'
		String[] Analysis = {"Parameters"};	
		JList<String> analysisMethods = new CreateJListNames(Analysis).getList();

		ssAnalysis = new SidebarSelectable(this, "Analysis", analysisMethods, null);
		addSection(ssAnalysis);		
		 */
		//--------------------------------------------------------------------
		// Section: 'Results'
		innerSideBar = new SideBar(SideBarMode.INNER_LEVEL, true, -1, true);
		SidebarSection ssResults = new SidebarSelectableResults(this, "Results",  innerSideBar, null);
		addSection(ssResults);
		
	}	

	/** Set panel as marked in the JList.
	 * @param i		Method index.
	 */
	public static void setSelectedMethod(int i) {
		methodsList.setSelectedIndex(i);
	}

	/** Set the Genome Selection as marked  
	 * in the JList.
	public static void setGenomeSelection(){
		methodsList.setSelectedIndex(1);
	}
	 */

	/** Expands Methods/Wizard tab. */
	public static void expandWizardTab() {
		ssWizard.expand();
	}

	/**
	 * Adds the inner side bars to 'Result'. The method 
	 * was design to be called after the pipeline started 
	 * to run.
	 */
	public static void addResultInnerBar() {
		// Remove previous inner bars
		innerSideBar.removeAll();
		// Get lifestyles
		String l1 = GenomeSelection.getLifestyleOneName();
		String l2 = GenomeSelection.getLifestyleTwoName();
		// 
		String[] ClusteringResultNames = {"Clustering Information", "Clustering Histogram"};
		String b1 = "Bias Class "+l1;
		String b2 = "Bias Class "+l2;
		String[] ClassificationResultNames = { "Joint Distribution", "Full data", b1, b2 }; 
		//
		String t1 = "Decision Tree - Class " + l1;
		String t2 = "Decision Tree - Class " + l2;
		String[] FeatureSelectionResultNames = { "Decision Tree - Full data", t1, t2 };

		// Add image to bars
		BufferedImage img;
		try {
			ClassLoader cl = CreateAccordionMenu.class.getClassLoader();
			img = ImageIO.read(cl.getClass().getResource("/images/arrow-redo-icon.png"));
			// Resize image
			//BufferedImage ret = new BufferedImage(25,25,BufferedImage.TYPE_INT_ARGB);
			//ret.getGraphics().drawImage(img,0,0,25,25,null);

			// Clustering Results
			JList<String> clustering = new CreateJListNames(ClusteringResultNames);
			innerSideBar.addSection(new SidebarSelectableResults(innerSideBar, "Clustering",
					clustering, new ImageIcon(img)));

			// Classification results
			JList<String> classification = new CreateJListNames(ClassificationResultNames);
			innerSideBar.addSection(new SidebarSelectableResults(innerSideBar, "Classification", 
					classification, new ImageIcon(img)));

			// Feature Selection Results
			JList<String> trees = new CreateJListNames(FeatureSelectionResultNames);
			innerSideBar.addSection(new SidebarSelectableResults(innerSideBar, "Feature Selection",
					trees, new ImageIcon(img)));

		} catch (IOException e) {
			logger.debug("It was not possible to load array icon (Result Section).");
			e.printStackTrace();
		}
	}
	
	/**
	 * Inner class extends SidebarSection functionalities by 
	 * adding another mouse listener to the side bar. Once it 
	 * is selected by the user, the first panel will be 
	 * automatically displayed.
	 */
	protected static class SidebarSelectableAnalysis extends SidebarSection {

		private static final long serialVersionUID = -7938604173007156134L;
		
		public SidebarSelectableAnalysis(final SideBar owner, String text,
				final JComponent component, Icon icon) {
			super(owner, text, component, icon);

			/* Add mouse listener. If the side bar is selected 
			 * the selected panel in the JList will be displayed.
			 */
			titlePanel.addMouseListener (new MouseAdapter() {
				@SuppressWarnings("unchecked")
				public void mouseReleased (MouseEvent e) {					
					if (SidebarSelectableAnalysis.this == sideBarOwner.getCurrentSection()) {
						try {
							CreateMultiSplitPane.setPanel("Wizard");
							int panel = ((JList<String>) component).getSelectedIndex();	
							WizardPanel.setPanel(panel + 1);
						} catch (Exception e1) {
							/* Do nothing.
							 * Not necessarily all of them have 
							 * a JList associated.
							 */							
						}
					}
				}
			});			
		}		
	}

	/**
	 * Inner class extends SidebarSection functionalities by 
	 * adding another mouse listener to the side bar. Once it 
	 * is selected by the user, the first panel will be 
	 * automatically displayed.
	 */
	protected static class SidebarSelectableResults extends SidebarSection {

		private static final long serialVersionUID = -4513686582774429717L;

		/**
		 * Creates a SideBar and add a mouse listener to it: 
		 * automatically display first panel in the JList if 
		 * it is selected.
		 */
		public SidebarSelectableResults(final SideBar owner, String text,
				final JComponent component, Icon icon) {
			super(owner, text, component, icon);

			/* Add mouse listener. If the side bar is selected, 
			 * it will first collapse all inner side bars.
			 */
			titlePanel.addMouseListener (new MouseAdapter() {
				public void mouseReleased (MouseEvent e) {
					if (SidebarSelectableResults.this != sideBarOwner.getCurrentSection()) {
						try {
							Component[] components = component.getComponents();	
							for (int i = 0; i < components.length; i++) {
								if(components[i] instanceof SidebarSelectableResults) 
									((SidebarSelectableResults) components[i]).collapse(true);
							}
						} catch (Exception e1) {
							// Do nothing.							
						}
					}
				}
			});

			/* Add mouse listener. If the side bar is selected 
			 * the first panel in the JList will be displayed.
			 */
			titlePanel.addMouseListener (new MouseAdapter() {
				@SuppressWarnings("unchecked")
				public void mouseReleased (MouseEvent e) {					
					if (SidebarSelectableResults.this == sideBarOwner.getCurrentSection()) {
						try {
							((JList<String>) component).setSelectedIndex(0);
							String panel = ((JList<String>) component).getModel().getElementAt(0);						
							CreateMultiSplitPane.setPanel(panel);
						} catch (Exception e1) {
							/* Do nothing.
							 * Not necessarily all of them have 
							 * a JList associated.
							 */							
						}
					}
				}
			});				
		}		
	}

	/**
	 * Inner-class to create a JList and add a selection listener to 
	 * it. Each time the user selects a different item from the JList 
	 * the correspondent panel will be displayed.
	 */
	public static class CreateJListNames extends JList<String> implements ListSelectionListener {

		private static final long serialVersionUID = 1L;

		/** Panels names. */
		protected String[] names;


		/**
		 * Creates a JList object with a 
		 * selection listener. The selection correspond to 
		 * the visualization of particular panel (MultiSliptPane).
		 * 
		 * @param names		Array containing the names of all panels 
		 * 					associated with a given SideBar object.
		 */
		public CreateJListNames(String[] names) {
			super(names);
			this.names = names;
			//methods.setSelectionModel(new DisabledItemSelectionModel());
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setSelectedIndex(0);
			addListSelectionListener(this);
			
			// Gives list selectable apparence, even thought it isn't
			setCellRenderer(new DefaultListCellRenderer() {
		        
				private static final long serialVersionUID = 1L;

				public Component getListCellRendererComponent(
		            @SuppressWarnings("rawtypes") JList list,
		            Object value,
		            int index,
		            boolean isSelected,
		            boolean cellHasFocus) {

		            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		            this.setEnabled(true);
		            return this;
		        }
		    });
		}

		@SuppressWarnings("unchecked")
		@Override
		public void valueChanged(ListSelectionEvent evt) {			
			JList<String> list = (JList<String>)evt.getSource();
			if (list.getSelectedIndex() > -1) {
				//System.out.println(list.getSelectedIndex() + "\t" + names[list.getSelectedIndex()]);
				CreateMultiSplitPane.setPanel(names[list.getSelectedIndex()]);
				//methods.clearSelection();
			}			 
		}

		/** [NOT USED] Inner-class disable item selection on JList. */
		protected class DisabledItemSelectionModel extends DefaultListSelectionModel {

			private static final long serialVersionUID = 1L;

			@Override
			public void setSelectionInterval(int index0, int index1) {
				super.setSelectionInterval(-1, -1);
			}
		}

	}	
}
