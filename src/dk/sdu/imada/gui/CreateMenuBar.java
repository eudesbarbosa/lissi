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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Class creates add functionality to the objects associated 
 * with the menu bar. The menu holds two options, namely: 
 * File, Edit and Help.
 * 
 * @author Eudes Barbosa
 *
 */
public class CreateMenuBar extends JMenu {

	//------  Variable declaration  ------//

	private static final long serialVersionUID = 880908944695322307L;

	private static final Logger logger = LogManager.getLogger(CreateMenuBar.class.getName());

	/** Url to LiSSI website at compbio.sdu.dk */
	final private String HELP = "http://lissi.compbio.sdu.dk";

	final private String ABOUT = "<html><b>LiSSI v1.0</b><br><br>"+
			"LiSSI was the result of a partnership between the " +
			"Federal University of Minas Gerais and the University of " +
			"Southern Denmark.<br><br>" +
			"<b>Main developer</b><br>" +
			"Eudes Barbosa<br><br>" +
			// -  <FONT color=\"#000099\">eudes@imada.sdu.dk</FONT><br><br>" +
			"<b>Group leaders</b><br>" +
			"Jan Baumbach - <FONT color=\"#000099\">jan.baumbach@imada.sdu.dk</FONT><br>" +
			"Vasco Azevedo - <FONT color=\"#000099\">vasco@icb.ufmg.br</FONT></html>";

	//------  Declaration end  ------//
	

	/**
	 * Creates and adds functionality to the objects associated 
	 * with the MenuBar. The menu holds three options, namely: 
	 * File, Edit and Help.
	 * 
	 * @param type		String containing the type of 
	 * 					menu that shall be created. There 
	 * 					are three valid types: 'File', 'Edit'
	 * 					and 'Help'. 
	 */
	public CreateMenuBar(String type) {
		try {
			if (type.toLowerCase().equals("file")){
				createFileMenu();
			} else if(type.toLowerCase().equals("edit")){
				createEditMenu();
			} else if(type.toLowerCase().equals("help")){	
				createHelpMenu();
			} else {
				throw new Exception();
			}
		} catch (Exception e){
			logger.error("Unknown menu type. Valid options: 'file', "
					+ "'edit', and 'help'. ");
			e.printStackTrace();
		}
	}


	/**
	 * Creates and functionality to the 'File' menu.
	 */
	private void createFileMenu() {
		//
		setText("File");
		//
		JMenuItem jMenuItemClose = new JMenuItem();
		/*
		JMenuItem jMenuItemNewProject = new JMenuItem();
		JMenuItem jMenuItemLoadProject = new JMenuItem();
		JMenuItem jMenuItemSaveProject = new JMenuItem();


		//-------------------------------------------------------------------------
		jMenuItemNewProject.setText("New Project");
		jMenuItemNewProject.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newProject(evt);
			}

			private void newProject(ActionEvent evt) {
				// Auto-generated method stub

			}
		});
		add(jMenuItemNewProject);


		//-------------------------------------------------------------------------
		jMenuItemLoadProject.setText("Load Project");
		jMenuItemLoadProject.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadProject(evt);
			}

			// Loads previous project
			private void loadProject(ActionEvent evt) {
				// Auto-generated method stub

			}
		});
		add(jMenuItemLoadProject);


		//-------------------------------------------------------------------------
		jMenuItemSaveProject.setText("Save Project");
		jMenuItemSaveProject.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveProject(evt);
			}

			// Saves current project
			private void saveProject(ActionEvent evt) {
				// Auto-generated method stub

			}
		});
		add(jMenuItemSaveProject);
		 */


		//-------------------------------------------------------------------------
		jMenuItemClose.setText("Close");
		jMenuItemClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeApplication(evt);
			}
			// Closes the application
			private void closeApplication(ActionEvent evt) {				
				MainFrame.close();
			}
		});
		add(jMenuItemClose);

	}

	
	/**
	 * Creates and functionality to the 'Edit' menu.
	 */
	private void createEditMenu() {
		//
		setText("Edit");
		// TODO: Create 'Edit' menu.
	}

	
	/**
	 * Creates and functionality to the 'Help' menu.
	 */
	private void createHelpMenu() {
		//
		setText("Help");
		//
		JMenuItem jMenuItemHelp = new JMenuItem();
		JMenuItem jMenuItemAbout = new JMenuItem();


		//-------------------------------------------------------------------------
		jMenuItemHelp.setText("Help");
		jMenuItemHelp.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				open(HELP);
			}

		});
		add(jMenuItemHelp);

		//-------------------------------------------------------------------------
		jMenuItemAbout.setText("About");
		jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				JTextPane text = new JTextPane();
				text.setEditable(false);
				//text.setBackground(Color.GRAY);
				HTMLEditorKit kit = new HTMLEditorKit();
				HTMLDocument doc = new HTMLDocument();
				text.setEditorKit(kit);
				text.setDocument(doc);
				try {
					kit.insertHTML(doc, doc.getLength(), ABOUT, 0, 0, HTML.Tag.B);
					JOptionPane.showMessageDialog(null, text, "About", JOptionPane.INFORMATION_MESSAGE);
				} catch (BadLocationException | IOException e) {
					logger.error("Fail to load 'About' information.");
					e.printStackTrace();
				}

			}
		});
		add(jMenuItemAbout);

	}

	/**
	 * Opens URL.
	 * @param uri	The URL that shall be open.
	 */
	private void open(String url) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(URI.create(url));
			} catch (IOException e) { 
				logger.debug("Problem while loading HTML.");
				e.printStackTrace();
			}
		} else {
			logger.debug("Browser option is not available.");
			// Send error message
			String errorMessage = "Browser is not available...";
			JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
