package GUI_elements;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import Logical_elements.Document;
import Logical_elements.DocumentTableModel;

/**
 * Ez az oszt�ly felel�s a t�bla megjelen�t�s��rt a kijel�lt adatok visszaad�s��rt �s az adatokkal val� m�veletek elindt�s��rt/kezel�s��rt.
 * JTable t�pus� oszt�ly lesz�rmazottja
 * @author nagyerik99
 * @see JTable 
 */
public class DocumentTable extends JTable {
	private static final long serialVersionUID = 1L;
	/**
	 * Desktop t�pus� v�ltoz� a f�jlok megnyit�sa v�gett
	 * @see Desktop
	 */
	private static Desktop desktop;
	/**
	 * A t�bl�hoz tartoz� adatok modellje.
	 * @see DocumentTableModel
	 */
	private DocumentTableModel tablemodel;
	/**
	 * Az akci�gombokat tartalmaz� panel referenci�ja. A vel�k val� kommunik�ci� v�gett
	 * @see SidePanel
	 */
	private SidePanel optpanel;
	/**
	 * A modell sz�r�s�st/rendez�s�st v�gz� sorter
	 * @see TableRowSorter
	 */
	private TableRowSorter<DocumentTableModel> sorter;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 * A t�bl�zat elemeinek megjelen�t�s�t seg�t� attrib�tum
	 * form�z�s v�gett
	 * @see DefaultCellRenderer
	 */
	private DefaultTableCellRenderer centerRenderer;
	/**
	 * A t�bl�zat fejl�ce
	 * @see JTableHeader
	 */
	private JTableHeader tableHeader;
	/**
	 * A be�ll�tand� sz�nek a t�bl�zaton.
	 * @selBackGround a szelekci� h�tt�rsz�ne
	 * @selForeGround a szelekci� bet�sz�ne
	 * @mainBackGround a h�tt�rsz�n
	 * @rendererColor a cell�k bet�sz�ne
	 * @gridColor a t�bl�zat sz�ne
	 * @see Color
	 */
	private static Color selbackGround = new Color(27, 53, 56),
						 selforeGround=new Color(210,241,250),
						 mainBackGround =new Color(55,175,184),
						 rendererColor = new Color(0,74,79),
						 gridColor = new Color(163, 246, 255);
	
	/**
	 * Default konstruktor ami a t�bl�zat a hozz�tartoz� modell �s sorter l�trehoz�s�t val�s�tja meg.
	 * Form�zza a t�bl�zatot �s be�ll�tja a megfelel� szempontokat
	 * @param mainframe a main Ablak referenci�ja
	 */
	public DocumentTable(DatabaseFrame mainframe) {
		mainFrame=mainframe;
		desktop = Desktop.getDesktop();
		tablemodel = new DocumentTableModel();
		tableHeader = this.getTableHeader();
		
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerRenderer.setVerticalAlignment(JLabel.CENTER);
		centerRenderer.setForeground(rendererColor);
		
		this.setModel(tablemodel);
		this.setBackground(mainBackGround);
		this.addMouseListener(new CellDblClick());
		
		this.setBorder(new LineBorder(Color.white,1,false));
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setSelectionBackground(selbackGround);
		this.setSelectionForeground(selforeGround);
		this.setDefaultRenderer(String.class, centerRenderer);
		this.setDefaultRenderer(LocalDate.class, centerRenderer);
		
		tableHeader.setBackground(mainBackGround);
		tableHeader.setForeground(rendererColor);
		tableHeader.setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		tableHeader.setReorderingAllowed(false);
		
		this.setGridColor(gridColor);
		sorter = new TableRowSorter<DocumentTableModel>(tablemodel);
		this.setRowSorter(sorter);
	}
	
	/**
	 * Letitlja a cell�k szerkeszt�s�t
	 */
	@Override
    public boolean isCellEditable(int row, int column) {
    	return false;
    }
    
	/**
	 * az Hasht�bla key mez�j�nek megdand� �rt�k validit�s�t vizsg�lja.
	 * 
	 * @param docID megadni k�v�nt id
	 * @return Igaz ha valid a megadni k�v�nt id, vagyis elfogadhat�
	 */
	protected boolean checkIDValidity(String docID) {
		return !(tablemodel.idMatch(docID));
	}
	
	/**
	 * �j Sor/Dokumentum hozz�f�z�se a modellhez.
	 * L�trehozza a dokumnetumot majd hozz�adja a modellhez.
	 * @param doc az �j felvenni k�v�nt dokumentum sz�veges �llom�ny form�j�ban
	 */
    protected void addRow(String[] doc) {
    	Document addedDoc = new Document(doc);
    	tablemodel.addRow(addedDoc);
    }
    
    /**
     * A kijel�lt sor szerkeszt�s�t hajtja v�gre.
     * Ha nem siker�lt valamilyen okn�l fogva azt jelzi a mainFrame-nek,
     * aki dialoggal k�zli a felhaszn�l�val a bek�vetkezett hib�t
     * @param doc az �j dokumentum
     * @param oldID a r�gi dokumentum azonos�t�ja
     */
    protected void editRow(Document doc, String oldID) {
    	try {
    		tablemodel.editRow(doc,this.convertRowIndexToModel(this.getSelectedRow()),oldID);
    		mainFrame.showDialog("Sikeres szerkeszt�s!", "F�jl szerkeszt�s", "info");
    	}catch(Exception e) {
    		mainFrame.showDialog(e.getMessage(), "F�jl szerkeszt�s", "error");
    	}
    }
    
    /**
     * @param sp SidePanel param�terk�nt �tad�sa a nyom�gombok kezel�s�hez
     */
    protected void setOptPanel(SidePanel sp) {
    	optpanel = sp;
    }
    
    /**
     * kit�rli a kijel�lt sort/elemet a modellj�b�l
     */
    protected void removeSelectedRow() {
    	int viewID = this.getSelectedRow();
    	int rowID=this.convertRowIndexToModel(viewID);
    	String docID = this.getValueAt(viewID, 0).toString();
    	tablemodel.removeSelectedRow(docID,rowID);
    }
    
    /**
     * A Modell tartalm�t menti ki a felhaszn�l� �ltal meghat�rozott f�jlba
     * aminek a kiterjeszt�se *.ser kell legyen.
     * Sikertelen ment�s eset�n azt dialoggal jelzi a mainFrame-nek aki pedig a felhaszn�l�nak.
     * @param savedFile a kimenteni k�v�nt f�jl el�r�si �tvonala
     */
    protected void saveData(String savedFile) {
    	try {
    		tablemodel.saveFile(savedFile);
    		mainFrame.showDialog("Sikeres ment�s!","F�jl ment�s","info");
    	}catch(IOException e) {
    		mainFrame.showDialog(e.getMessage(),"F�jl Ment�s","error");
    	}
    }
    
    /**
     * Bet�lti a DocumentTable-be a *.ser t�pus� szerializalt fajlt
     * Csak akkor t�lti be f�jlt ha az a felhaszn�l� is meger�s�ti �s a t�rolt adatok elvesznek ekkor.
     * Ha sikertelen volt a bet�lt�s azt a felhazsn�l�nak jelzi.
     * 
     * @param loadFile a bet�lteni k�v�nt f�jl el�r�si �tvonala
     */
    protected void loadData(String loadFile) {
    	int option = mainFrame.showDialog("Biztosan be szeretn� t�lteni az adatokat?\n"
    			+ "A nyilv�ntart�sban jelenleg lev� adatok, �gy elvesznek!",
    					"F�jl olvas�s","dialog");
    	if(option==1) return;
    	
    	try {
    		tablemodel.loadFile(loadFile);
			mainFrame.showDialog("Sikeres F�jlolvas�s!", "F�jl olvas�s", "info");
    	}catch(IOException noread) {
			mainFrame.showDialog(noread.getMessage(), "F�jl olvas�s", "error");
    	}catch(ClassNotFoundException noclass) {
    		mainFrame.showDialog(noclass.getMessage(), "Bels� hiba(kasztol�s)", "error");
    	}
    }
    
    /**
     * Kit�r�lteti az �sszes adatot a modellb�l
     */
    protected void clearAll() {
    	tablemodel.clearAll();
    }
    
    /**
     * Hozz�adja a sorfiltert a t�bl�hoz sz�r�s eset�n h�v�dik
     * @param newRowFilter az �jonnan meghat�rozott sz�r�s
     */
    protected void addRowFilter(RowFilter<DocumentTableModel,Integer> newRowFilter) {
    	sorter.setRowFilter(newRowFilter);
    }
    
    /**
     * Megynitja a csatolt f�jlt ha egy�ltalan�n van olyan
     * Ha nincs akkor pedig hiba�zenetet dob
     * Nem tal�lt f�jl eset�n is NullPointerException-t dob
     * @param rowID a sorazonos�t� amely a kijel�lt sort jelenti
     */
    protected void openFile(int rowID) {
    	try {
			String docID = this.getValueAt(rowID,0).toString();
			tablemodel.openDoc(docID,desktop);
		}catch(Exception excp) {
			mainFrame.showDialog(excp.getMessage(), "Csatolt F�jl megnyit�sa", "info");
		}
    }
    
    /**
     * Visszaadja a Modellb�l a kiv�lasztott sor adatait.
     * @return a kijel�lt Document t�pus� v�ltoz�
     * @see Document
     */
    protected Document getSelectedRowData() {
    	int rowID = this.getSelectedRow();
    	
    	String docID = this.getValueAt(rowID, 0).toString();
    	Document doc = tablemodel.getRowData(docID);
    	if(doc==null) {
    		mainFrame.showDialog("Az �llom�ny szerkeszt�se nem siker�lt", "Bels� hiba", "error");
    	}
    	return doc;
    	
    }
    
    /**
     * kattint�s eset�n h�v�dik meg.
     * K�tszeres attints eset�n megnyitja a csaolt f�jlt ha van olyan
     * egy�bk�nt �zenettel jelzi, ha nincs csatolm�ny
     * Egy�b esetben pedig ha sor lett kijel�lve akkor megh�vja az optpanel (SidePanel)
     * setDeletable met�dus�t 
     * @author nagyerik99
     * @see MouseAdatpter
     */
    private class CellDblClick extends MouseAdapter{

		@Override
		public void mouseClicked(MouseEvent event) {
	    	int rowID=DocumentTable.this.rowAtPoint(event.getPoint());
			if(event.getClickCount()==2) {
				DocumentTable.this.openFile(rowID);
			}
			
			if(rowID!=-1) {
					optpanel.setDeletable(true);
			}
		}   	
    }
    
    
}
