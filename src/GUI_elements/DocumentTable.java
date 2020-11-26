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
 * Jtable implement�ci�
 * Ez az oszt�ly felel�s a t�bla megjelen�t�s��rt a kijel�lt adatok visszaad�s��rt �s az adatokkal val� m�veletek elindt�s��rt
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
	 * az Hasht�bla key mez�j�nek megdand� �rt�k validit�s�t vizsg�lja
	 * @param docID megadni k�v�nt id
	 * @return boolean igaz ha valid a megadni k�v�nt id
	 */
	protected boolean checkIDValidity(String docID) {
		return !(tablemodel.idMatch(docID));
	}
	
	/**
	 * Sor Hozz�f�z�se a modellhez DocumentTableModel
	 * @param doc az �j felvenni k�v�nt dokumentum
	 */
    protected void addRow(String[] doc) {
    	Document addedDoc = new Document(doc);
    	tablemodel.addRow(addedDoc);
    }
    
    /**
     * A kijel�lt sor szerkeszt�s�t hajtja v�gre 
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
     * 
     * @param sp SidePanel param�terk�nt �tad�sa a nyom�gombok kezel�s�hez
     */
    protected void setOptPanel(SidePanel sp) {
    	optpanel = sp;
    }
    /**
     * kit�rli a kijel�lt sort a DocumentTableModel-b�l
     */
    protected void removeSelectedRow() {
    	int viewID = this.getSelectedRow();
    	int rowID=this.convertRowIndexToModel(viewID);
    	String docID = this.getValueAt(viewID, 0).toString();
    	tablemodel.removeSelectedRow(docID,rowID);
    }
    
    /**
     * A DocumentTable tartalm�t menti ki egy .ser nevezet� f�jlba
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
     * Bet�lti a DocumentTable-be a -ser t�pus� szerializalt fajlt
     * Csak akkor t�lti be f�jlt ha az a felhaszn�l� is meger�s�ti �s a t�rolt adatok elvesznek ekkor
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
     * Kit�lri az �sszes t�rolt sort/dokumentumot
     */
    protected void clearAll() {
    	tablemodel.clearAll();
    }
    
    /**
     * Hozz�adja a sorfiltert a t�bl�hoz sz�r�s eset�n h�v�dik
     * @param newRowFilter
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
		}catch(NullPointerException nullExcp) {
			mainFrame.showDialog(nullExcp.getMessage(), "Csatolt f�jl megnyit�sa", "error");
		}catch(Exception excp) {
			mainFrame.showDialog(excp.getMessage(), "Csatolt F�jl megnyit�sa", "info");
		}
    }
    
    /**
     * Visszaadja a Modellb�l a kiv�lasztott sor adatait.
     * @return
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
     * A k�tszeres kattint�s eset�re l�trehozott bels� oszt�ly amely
     * az openFile f�ggv�nyt h�vja meg
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
