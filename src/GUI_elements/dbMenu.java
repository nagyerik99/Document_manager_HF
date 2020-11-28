package GUI_elements;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Desktop;
/**
 * Szabványos JMenuBar ami az egyes menüpontok megjelenítését és kezelését valósítja meg.
 * @author nagyerik99
 * @see JMenuBar 
 */
public class dbMenu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * az adatokat kezelõ tábla
	 * @see DocumentTable
	 */
	private DocumentTable docTable;
	/**
	 * Menüpontok
	 * @file a fájlokkal kapcsolatos menüpont
	 * @help a segítséggel kapcsolatos menüpont
	 */
	private JMenu file,help;
	/**
	 * az egyes almenüpontok/választható opciók
	 * @saveFile a táblában található adatok mentése
	 * @loadFile a mentett adatok betöltése a táblába
	 * @deleteAll törli az összes táblában található elemet
	 * @openDoc megnyitja a dokumentációt mint segítséget
	 */
	private JMenuItem saveFile,loadFile,deleteAll,openDoc;
	/**
	 * fájl választás a mentéshez/betöltéshez
	 */
	private JFileChooser fileManager;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 *  az egyes dialogok a mentéstõl/betöltéstõl függõen a 
	 *  filechooser ablak neve
	 */
	private static String saveDialog="Adatok mentése",
							loadDialog="Adatok betöltése";
	/**
	 * Default konstruktor ami létrehozza a menüpanelt
	 * @param main a main ablak
	 * @param dataTable az adatokat tároló tábla
	 */
	public	dbMenu(DatabaseFrame main,DocumentTable dataTable) {
		this.docTable = dataTable;
		this.mainFrame = main;
		
		fileManager = new JFileChooser();
		fileManager.addChoosableFileFilter(new FileNameExtensionFilter(" Serializable file *.ser","ser"));
		fileManager.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileManager.setAcceptAllFileFilterUsed(false);
		
		file = new JMenu("Fájl");
		help = new JMenu("Súgó");
		
		openDoc= new JMenuItem("Dokumentáció");
		saveFile = new JMenuItem("Adatok mentése (*.ser)");
		loadFile = new JMenuItem("Adatok betöltése (*.ser)");
		deleteAll = new JMenuItem("Összes törlése");
		
		this.setMenuItemDefaults(openDoc, "helpDoc", this, DefaultPaths.openDoc.getIcon());
		this.setMenuItemDefaults(saveFile, "saveFile", this,DefaultPaths.saveFile.getIcon());
		this.setMenuItemDefaults(loadFile, "loadFile", this,DefaultPaths.openFile.getIcon());
		this.setMenuItemDefaults(deleteAll, "deleteAll", this, null);
		
		help.add(openDoc);
		
		file.add(saveFile);
		file.add(loadFile);
		file.add(deleteAll);
		
		this.add(file);
		this.add(help);
	}
	
	/**
	 * beállítja az alap dolgokat az egyes menuItem-eknek
	 * @param item a megjelenítendõ icon ha van
	 * @param actionCommand az actionCommand amit hozzárendelünk a menüponthoz
	 * @param listener az ActionListener osztályunk ami a kiválasztásukat kezeli le.
	 */
	private void setMenuItemDefaults(JMenuItem item, String actionCommand, ActionListener listener, ImageIcon icon) {
		if(icon!=null) item.setIcon(icon);
		
		item.addActionListener(listener);
		item.setActionCommand(actionCommand);
	}
	
	/**
	 * Az egyes menüpontok kiválasztásának esetét kezeli le, és valssza ki a megfelelõ opcioót
	 * az actionCommand alapján.
	 * @saveFile kimenti a megadott névvel a fájlt (magát a típusát is utána kell írni a fájlnévnek(-.ser))
	 * @loadFile megpróbálja betölteni a kiválasztott .ser adatot
	 * @deleteAll kitölri a táblában lévõ összes adatot
	 * @helpDoc megnyitja a dokumentációt 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		int result;
		
		if(e.getActionCommand().equals("saveFile")) {
			fileManager.setDialogTitle(saveDialog);
			result = fileManager.showSaveDialog(dbMenu.this.getParent());
				
			if(result == JFileChooser.APPROVE_OPTION) {
				String filename = fileManager.getSelectedFile().getAbsolutePath();
				docTable.saveData(filename);
			}
			
		}else if(e.getActionCommand().equals("deleteAll")) {
			docTable.clearAll();
			
		}else if(e.getActionCommand().equals("loadFile")) {
			fileManager.setDialogTitle(loadDialog);
			result = fileManager.showOpenDialog(dbMenu.this.getParent());
			
			if(result==JFileChooser.APPROVE_OPTION) {
				File selFile = fileManager.getSelectedFile();
				if(selFile.getName().matches("(.+)\\.ser$")) {
					docTable.loadData(selFile.getAbsolutePath());
				}else {
					mainFrame.showDialog("Nem megfelelõ a fájl formátuma!", "Fájl betöltése", "error");
				}
				
			}
			
		}else if(e.getActionCommand().equals("helpDoc")) {
			Desktop d = Desktop.getDesktop();
			
			try {
				File doc = new File(DefaultPaths.Document.getPath());
				d.open(doc);
			}catch(Exception exception) {
				mainFrame.showDialog(exception.getMessage(), "Fájl megnyitása", "error");
			}
		}
	}
}
