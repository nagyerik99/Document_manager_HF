package GUI_elements;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Desktop;

public class DB_Menu extends JMenuBar implements ActionListener {
	private static final long serialVersionUID = 1L;
	private DocumentTable docTable;
	private JMenu file,help;
	private JMenuItem saveFile,loadFile,deleteAll,openDoc;
	private JFileChooser fileManager;
	private Database_frame mainFrame;
	
	public	DB_Menu(Database_frame main,DocumentTable dataTable) {
		docTable = dataTable;
		mainFrame = main;
		
		fileManager = new JFileChooser();
		fileManager.addChoosableFileFilter(new FileNameExtensionFilter(" Serializable file *.ser","ser"));
		fileManager.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileManager.setAcceptAllFileFilterUsed(false);
		
		
		file = new JMenu("F�jl");
		help = new JMenu("S�g�");
		
		openDoc= new JMenuItem("Dokument�ci�");
		openDoc.setActionCommand("helpDoc");
		openDoc.addActionListener(this);
		
		saveFile = new JMenuItem("Adatok ment�se (*.ser)");
		saveFile.addActionListener(this);
		saveFile.setActionCommand("saveFile");
		
		loadFile = new JMenuItem("Adatok bet�lt�se (*.ser)");
		loadFile.addActionListener(this);
		loadFile.setActionCommand("loadFile");
		
		deleteAll = new JMenuItem("�sszes t�rl�se");
		deleteAll.setActionCommand("deleteAll");
		deleteAll.addActionListener(this);
		
		help.add(openDoc);
		file.add(saveFile);
		file.add(loadFile);
		file.add(deleteAll);
		
		this.add(file);
		this.add(help);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("saveFile")) {
			fileManager.setDialogTitle("Adatok ment�se");
			int result = fileManager.showSaveDialog(DB_Menu.this.getParent());
			if(result == JFileChooser.APPROVE_OPTION) {
				String filename = fileManager.getSelectedFile().getAbsolutePath();
				docTable.saveData(filename);
			}
			
		}else if(e.getActionCommand().equals("deleteAll")) {
			docTable.clearAll();
		}else if(e.getActionCommand().equals("loadFile")) {
			fileManager.setDialogTitle("Adatok bet�lt�se");
			int result = fileManager.showOpenDialog(DB_Menu.this.getParent());
			if(result==JFileChooser.APPROVE_OPTION) {
				String filename = fileManager.getSelectedFile().getAbsolutePath();
				docTable.loadData(filename);
			}
		}else if(e.getActionCommand().equals("helpDoc")) {
			Desktop d = Desktop.getDesktop();
			try {
				d.open(mainFrame.getDocumentDirectory());
			}catch(IOException ioError) {
				ioError.printStackTrace();
			}
		}
	}
}
