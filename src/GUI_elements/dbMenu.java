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
 * Szabv�nyos JMenuBar ami az egyes men�pontok megjelen�t�s�t �s kezel�s�t val�s�tja meg.
 * @author nagyerik99
 * @see JMenuBar 
 */
public class dbMenu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	/**
	 * az adatokat kezel� t�bla
	 * @see DocumentTable
	 */
	private DocumentTable docTable;
	/**
	 * Men�pontok
	 * @file a f�jlokkal kapcsolatos men�pont
	 * @help a seg�ts�ggel kapcsolatos men�pont
	 */
	private JMenu file,help;
	/**
	 * az egyes almen�pontok/v�laszthat� opci�k
	 * @saveFile a t�bl�ban tal�lhat� adatok ment�se
	 * @loadFile a mentett adatok bet�lt�se a t�bl�ba
	 * @deleteAll t�rli az �sszes t�bl�ban tal�lhat� elemet
	 * @openDoc megnyitja a dokument�ci�t mint seg�ts�get
	 */
	private JMenuItem saveFile,loadFile,deleteAll,openDoc;
	/**
	 * f�jl v�laszt�s a ment�shez/bet�lt�shez
	 */
	private JFileChooser fileManager;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 *  az egyes dialogok a ment�st�l/bet�lt�st�l f�gg�en a 
	 *  filechooser ablak neve
	 */
	private static String saveDialog="Adatok ment�se",
							loadDialog="Adatok bet�lt�se";
	/**
	 * Default konstruktor ami l�trehozza a men�panelt
	 * @param main a main ablak
	 * @param dataTable az adatokat t�rol� t�bla
	 */
	public	dbMenu(DatabaseFrame main,DocumentTable dataTable) {
		this.docTable = dataTable;
		this.mainFrame = main;
		
		fileManager = new JFileChooser();
		fileManager.addChoosableFileFilter(new FileNameExtensionFilter(" Serializable file *.ser","ser"));
		fileManager.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileManager.setAcceptAllFileFilterUsed(false);
		
		file = new JMenu("F�jl");
		help = new JMenu("S�g�");
		
		openDoc= new JMenuItem("Dokument�ci�");
		saveFile = new JMenuItem("Adatok ment�se (*.ser)");
		loadFile = new JMenuItem("Adatok bet�lt�se (*.ser)");
		deleteAll = new JMenuItem("�sszes t�rl�se");
		
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
	 * be�ll�tja az alap dolgokat az egyes menuItem-eknek
	 * @param item a megjelen�tend� icon ha van
	 * @param actionCommand az actionCommand amit hozz�rendel�nk a men�ponthoz
	 * @param listener az ActionListener oszt�lyunk ami a kiv�laszt�sukat kezeli le.
	 */
	private void setMenuItemDefaults(JMenuItem item, String actionCommand, ActionListener listener, ImageIcon icon) {
		if(icon!=null) item.setIcon(icon);
		
		item.addActionListener(listener);
		item.setActionCommand(actionCommand);
	}
	
	/**
	 * Az egyes men�pontok kiv�laszt�s�nak eset�t kezeli le, �s valssza ki a megfelel� opcio�t
	 * az actionCommand alapj�n.
	 * @saveFile kimenti a megadott n�vvel a f�jlt (mag�t a t�pus�t is ut�na kell �rni a f�jln�vnek(-.ser))
	 * @loadFile megpr�b�lja bet�lteni a kiv�lasztott .ser adatot
	 * @deleteAll kit�lri a t�bl�ban l�v� �sszes adatot
	 * @helpDoc megnyitja a dokument�ci�t 
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
					mainFrame.showDialog("Nem megfelel� a f�jl form�tuma!", "F�jl bet�lt�se", "error");
				}
				
			}
			
		}else if(e.getActionCommand().equals("helpDoc")) {
			Desktop d = Desktop.getDesktop();
			
			try {
				File doc = new File(DefaultPaths.Document.getPath());
				d.open(doc);
			}catch(Exception exception) {
				mainFrame.showDialog(exception.getMessage(), "F�jl megnyit�sa", "error");
			}
		}
	}
}
