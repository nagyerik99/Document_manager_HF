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
import javax.swing.table.TableRowSorter;
import Logical_elements.Document;
import Logical_elements.DocumentTableModel;

/**
 * Jtable implementáció
 * Ez az osztály felelõs a tábla megjelenítéséért a kijelölt adatok visszaadásáért és az adatokkal való mûveletek elindtásáért
 */
public class DocumentTable extends JTable {
	private DocumentTableModel tablemodel;
	private SidePanel optpanel;
	private static final long serialVersionUID = 1L;
	private TableRowSorter<DocumentTableModel> sorter;
	private static Desktop desktop;
	private Database_frame mainFrame;
	private DefaultTableCellRenderer centerRenderer;
	public DocumentTable(Database_frame mainframe) {
		mainFrame=mainframe;
		desktop = Desktop.getDesktop();
		tablemodel = new DocumentTableModel();
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerRenderer.setVerticalAlignment(JLabel.CENTER);
		centerRenderer.setForeground(new Color(0,74,79));
		
		this.setBorder(new LineBorder(Color.white,1,false));
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.setSelectionBackground(new Color(27, 53, 56));
		this.setSelectionForeground(new Color(210,241,250));
		this.setDefaultRenderer(String.class, centerRenderer);
		this.setDefaultRenderer(LocalDate.class, centerRenderer);
		this.setModel(tablemodel);
		this.setBackground(new Color(55,175,184));
		this.getTableHeader().setBackground(mainFrame.getBackground());
		this.getTableHeader().setForeground((new Color(210,241,250)));
		this.getTableHeader().setFont(new Font(Font.SANS_SERIF,Font.BOLD,12));
		this.addMouseListener(new CellDblClick());
		this.getTableHeader().setReorderingAllowed(false);
		this.setGridColor(new Color(163, 246, 255));
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setAlignmentY(CENTER_ALIGNMENT);
		
		
		sorter = new TableRowSorter<DocumentTableModel>(tablemodel);
		this.setRowSorter(sorter);
		//this.setAutoCreateRowSorter(true);
	}
	
	@Override
    public boolean isCellEditable(int row, int column) {
    	return false;
    }
    
	/**
	 * az Hashtábla key mezõjének megdandó érték validitását vizsgálja
	 * @param docID megadni kívánt id
	 * @return boolean igaz ha valid a megadni kívánt id
	 */
	protected boolean checkIDValidity(String docID) {
		return !(tablemodel.idMatch(docID));
	}
	
	/**
	 * Sor Hozzáfûzése a modellhez DocumentTableModel
	 * @param doc az új felvenni kívánt dokumentum
	 */
    protected void addRow(String[] doc) {
    	Document addedDoc = new Document(doc);
    	tablemodel.addRow(addedDoc);
    }
    
    /**
     * A kijelölt sor szerkesztését hajtja végre 
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
     * 
     * @param sp SidePanel paraméterként átadása a nyomógombok kezeléséhez
     */
    protected void setOptPanel(SidePanel sp) {
    	optpanel = sp;
    }
    /**
     * kitörli a kijelölt sort a DocumentTableModel-bõl
     */
    protected void removeSelectedRow() {
    	int viewID = this.getSelectedRow();
    	int rowID=this.convertRowIndexToModel(viewID);
    	String docID = this.getValueAt(viewID, 0).toString();
    	tablemodel.removeSelectedRow(docID,rowID);
    }
    
    /**
     * A DocumentTable tartalmát menti ki egy .ser nevezetû fájlba
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
     * Betölti a DocumentTable-be a -ser típusú szerializalt fajlt
     * Csak akkor tölti be fájlt ha az a felhasználó is megerõsíti és a tárolt adatok elvesznek ekkor
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
     * Kitölri az összes tárolt sort/dokumentumot
     */
    protected void clearAll() {
    	tablemodel.clearAll();
    }
    
    /**
     * Hozzáadja a sorfiltert a táblához szûrés esetén hívódik
     * @param newRowFilter
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
		}catch(NullPointerException nullExcp) {
			mainFrame.showDialog(nullExcp.getMessage(), "Csatolt fájl megnyitása", "error");
		}catch(Exception excp) {
			mainFrame.showDialog(excp.getMessage(), "Csatolt Fájl megnyitása", "info");
		}
    }
    
    /**
     * Visszaadja a Modellbõl a kiválasztott sor adatait.
     * @return
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
     * A kétszeres kattintás esetére létrehozott belsõ osztály amely
     * az openFile függvényt hívja meg
     * @author nagyerik99
     *
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
