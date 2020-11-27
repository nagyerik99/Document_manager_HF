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
 * Ez az osztály felelõs a tábla megjelenítéséért a kijelölt adatok visszaadásáért és az adatokkal való mûveletek elindtásáért/kezeléséért.
 * JTable típusú osztály leszármazottja
 * @author nagyerik99
 * @see JTable 
 */
public class DocumentTable extends JTable {
	private static final long serialVersionUID = 1L;
	/**
	 * Desktop típusú változó a fájlok megnyitása végett
	 * @see Desktop
	 */
	private static Desktop desktop;
	/**
	 * A táblához tartozó adatok modellje.
	 * @see DocumentTableModel
	 */
	private DocumentTableModel tablemodel;
	/**
	 * Az akciógombokat tartalmazó panel referenciája. A velük való kommunikáció végett
	 * @see SidePanel
	 */
	private SidePanel optpanel;
	/**
	 * A modell szûrésést/rendezésést végzõ sorter
	 * @see TableRowSorter
	 */
	private TableRowSorter<DocumentTableModel> sorter;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 * A táblázat elemeinek megjelenítését segítõ attribútum
	 * formázás végett
	 * @see DefaultCellRenderer
	 */
	private DefaultTableCellRenderer centerRenderer;
	/**
	 * A táblázat fejléce
	 * @see JTableHeader
	 */
	private JTableHeader tableHeader;
	/**
	 * A beállítandó színek a táblázaton.
	 * @selBackGround a szelekció háttérszíne
	 * @selForeGround a szelekció betûszíne
	 * @mainBackGround a háttérszín
	 * @rendererColor a cellék betûszíne
	 * @gridColor a táblázat színe
	 * @see Color
	 */
	private static Color selbackGround = new Color(27, 53, 56),
						 selforeGround=new Color(210,241,250),
						 mainBackGround =new Color(55,175,184),
						 rendererColor = new Color(0,74,79),
						 gridColor = new Color(163, 246, 255);
	
	/**
	 * Default konstruktor ami a táblázat a hozzátartozó modell és sorter létrehozását valósítja meg.
	 * Formázza a táblázatot és beállítja a megfelelõ szempontokat
	 * @param mainframe a main Ablak referenciája
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
	 * Letitlja a cellék szerkesztését
	 */
	@Override
    public boolean isCellEditable(int row, int column) {
    	return false;
    }
    
	/**
	 * az Hashtábla key mezõjének megdandó érték validitását vizsgálja.
	 * 
	 * @param docID megadni kívánt id
	 * @return Igaz ha valid a megadni kívánt id, vagyis elfogadható
	 */
	protected boolean checkIDValidity(String docID) {
		return !(tablemodel.idMatch(docID));
	}
	
	/**
	 * Új Sor/Dokumentum hozzáfûzése a modellhez.
	 * Létrehozza a dokumnetumot majd hozzáadja a modellhez.
	 * @param doc az új felvenni kívánt dokumentum szöveges állomány formájában
	 */
    protected void addRow(String[] doc) {
    	Document addedDoc = new Document(doc);
    	tablemodel.addRow(addedDoc);
    }
    
    /**
     * A kijelölt sor szerkesztését hajtja végre.
     * Ha nem sikerült valamilyen oknál fogva azt jelzi a mainFrame-nek,
     * aki dialoggal közli a felhasználóval a bekövetkezett hibát
     * @param doc az új dokumentum
     * @param oldID a régi dokumentum azonosítója
     */
    protected void editRow(Document doc, String oldID) {
    	try {
    		tablemodel.editRow(doc,this.convertRowIndexToModel(this.getSelectedRow()),oldID);
    		mainFrame.showDialog("Sikeres szerkesztés!", "Fájl szerkesztés", "info");
    	}catch(Exception e) {
    		mainFrame.showDialog(e.getMessage(), "Fájl szerkesztés", "error");
    	}
    }
    
    /**
     * @param sp SidePanel paraméterként átadása a nyomógombok kezeléséhez
     */
    protected void setOptPanel(SidePanel sp) {
    	optpanel = sp;
    }
    
    /**
     * kitörli a kijelölt sort/elemet a modelljébõl
     */
    protected void removeSelectedRow() {
    	int viewID = this.getSelectedRow();
    	int rowID=this.convertRowIndexToModel(viewID);
    	String docID = this.getValueAt(viewID, 0).toString();
    	tablemodel.removeSelectedRow(docID,rowID);
    }
    
    /**
     * A Modell tartalmát menti ki a felhasználó által meghatározott fájlba
     * aminek a kiterjesztése *.ser kell legyen.
     * Sikertelen mentés esetén azt dialoggal jelzi a mainFrame-nek aki pedig a felhasználónak.
     * @param savedFile a kimenteni kívánt fájl elérési útvonala
     */
    protected void saveData(String savedFile) {
    	try {
    		tablemodel.saveFile(savedFile);
    		mainFrame.showDialog("Sikeres mentés!","Fájl mentés","info");
    	}catch(IOException e) {
    		mainFrame.showDialog(e.getMessage(),"Fájl Mentés","error");
    	}
    }
    
    /**
     * Betölti a DocumentTable-be a *.ser típusú szerializalt fajlt
     * Csak akkor tölti be fájlt ha az a felhasználó is megerõsíti és a tárolt adatok elvesznek ekkor.
     * Ha sikertelen volt a betöltés azt a felhazsnálónak jelzi.
     * 
     * @param loadFile a betölteni kívánt fájl elérési útvonala
     */
    protected void loadData(String loadFile) {
    	int option = mainFrame.showDialog("Biztosan be szeretné tölteni az adatokat?\n"
    			+ "A nyilvántartásban jelenleg levõ adatok, így elvesznek!",
    					"Fájl olvasás","dialog");
    	if(option==1) return;
    	
    	try {
    		tablemodel.loadFile(loadFile);
			mainFrame.showDialog("Sikeres Fájlolvasás!", "Fájl olvasás", "info");
    	}catch(IOException noread) {
			mainFrame.showDialog(noread.getMessage(), "Fájl olvasás", "error");
    	}catch(ClassNotFoundException noclass) {
    		mainFrame.showDialog(noclass.getMessage(), "Belsõ hiba(kasztolás)", "error");
    	}
    }
    
    /**
     * Kitörölteti az összes adatot a modellbõl
     */
    protected void clearAll() {
    	tablemodel.clearAll();
    }
    
    /**
     * Hozzáadja a sorfiltert a táblához szûrés esetén hívódik
     * @param newRowFilter az újonnan meghatározott szûrés
     */
    protected void addRowFilter(RowFilter<DocumentTableModel,Integer> newRowFilter) {
    	sorter.setRowFilter(newRowFilter);
    }
    
    /**
     * Megynitja a csatolt fájlt ha egyáltalanán van olyan
     * Ha nincs akkor pedig hibaüzenetet dob
     * Nem talált fájl esetén is NullPointerException-t dob
     * @param rowID a sorazonosító amely a kijelölt sort jelenti
     */
    protected void openFile(int rowID) {
    	try {
			String docID = this.getValueAt(rowID,0).toString();
			tablemodel.openDoc(docID,desktop);
		}catch(Exception excp) {
			mainFrame.showDialog(excp.getMessage(), "Csatolt Fájl megnyitása", "info");
		}
    }
    
    /**
     * Visszaadja a Modellbõl a kiválasztott sor adatait.
     * @return a kijelölt Document típusú változó
     * @see Document
     */
    protected Document getSelectedRowData() {
    	int rowID = this.getSelectedRow();
    	
    	String docID = this.getValueAt(rowID, 0).toString();
    	Document doc = tablemodel.getRowData(docID);
    	if(doc==null) {
    		mainFrame.showDialog("Az állomány szerkesztése nem sikerült", "Belsõ hiba", "error");
    	}
    	return doc;
    	
    }
    
    /**
     * kattintás esetén hívódik meg.
     * Kétszeres attints esetén megnyitja a csaolt fájlt ha van olyan
     * egyébként üzenettel jelzi, ha nincs csatolmány
     * Egyéb esetben pedig ha sor lett kijelölve akkor meghívja az optpanel (SidePanel)
     * setDeletable metódusát 
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
