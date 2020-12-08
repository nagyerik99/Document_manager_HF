package Logical_elements;
import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.table.DefaultTableModel;

/**	
 * Ez az osztály valósítja meg a DefaultTableModel-t vagyi a DocumentTable által
 * használt modellt.
 * @see DocumentTable
 * @author nagyerik99
 */
public class DocumentTableModel extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	/**
	 * az a belsõ lista amely a hozzáadtott Dokumnetumokat tárolja és ez alapján rakja össze
	 * megjelenítendõ modellt sorait az osztály.
	 * Key : string ami az adott dokumnetum azonosítója neve. ez alapján gyorsan és könnyen meglehet találni a tartalmazott adatot.
	 */
	private HashMap<String,Document> DocList;
	/**
	 * a modell header-je 
	 */
	private static Object[] header = {"ID","Típus","Kezdete","Vége","Fájl"};
	
	/**
	 * Default konstruktor ami létrehozza a modellt és inicializálja a Doclist-et, mely a Documentumocat tartalmazza
	 */
	public DocumentTableModel() {
		super(header,0);
		DocList = new HashMap<String,Document>();
	}
	
	/**
	 * Documentum hozzáadása a listához és a modellhez
	 * @param doc
	 */
	public void addRow(Document doc){
			DocList.put(doc.getDocID(),doc);
			this.addRow(doc.toObjectArray());
	}
	/**
	 * A Dokumentum szerkesztésénél hívódik meg, az új dokumentumot hozzáadja a listához és a modellhez a régit pedig törli
	 * @param newDoc a felvevendõ dokumentum
	 * @param viewRowID a modell ViewModelljének a dokumentumhoz tartozó id-ja
	 * @param oldID a régi dokumentum id-ja/neve
	 * @throws Exception ha nem találta meg az oldID alapján az adott elemet, vagyis nincs a listában
	 */
	public void editRow(Document newDoc, int viewRowID, String oldID) throws Exception {
		String docID = newDoc.getDocID();
		Document oldDoc = DocList.get(oldID);
		if(oldDoc==null) throw new Exception("Hiányos fájl lista (régi ID)");
		
		DocList.remove(oldID);
		DocList.put(docID, newDoc);
		this.updateRow(viewRowID, newDoc);
	}
	
	/**
	 * Visszaadja az adott Documentumot ami a modellben kiválasztva szerepel
	 * @param docID  a kiválasztott dokumentum neve/id ja
	 * @return Document a kiválasztott dokumentum
	 * @see Document
	 */
	public Document getRowData(String docID){
		return (DocList.get(docID));
	}
	
	/**
	 * Kitörli a megfelelõ sort a modellbõl és a listából is
	 * @param rowID a dokumentum azoosítója
	 * @param row a modell sorAzonosítója
	 * @throws NullPointerException ha nem található a modellben a dokumentum
	 */
	public void removeSelectedRow(String rowID,int row) throws NullPointerException {
		DocList.remove(rowID);
		this.removeRow(row);
	}
	
	/**
	 * Kiüríti a listát és a modellt
	 */
	public void clearAll() {
		if(this.getRowCount()==0) return;
		
		this.setRowCount(0);
		DocList.clear();
	}
	/**
	 * A modellben kiválasztott elemre kétszer kattintva meghívódik a DocumentTable-ön keresztül
	 * megnyitja a csatolt fájlt / ha van olyan
	 * @param docID a dokumentum azonosítója
	 * @param desktop az asztalt jelképezõ objektum
	 * @throws Exception ha nincs csatolt állomány
	 */
	public void openDoc(String docID,Desktop desktop) throws Exception {
		if(docID==null) return;
		
		Document selectedDocument = DocList.get(docID);
		if(selectedDocument == null) {
			throw new Exception("Hiba az állomány nem található");
		}else {
			selectedDocument.openDoc(desktop);
		}
	}
	
	/**
	 * Az ids tömb paramétereivel megegyezõ mezõk/dokumentumok tartalmát tudjuk kimenteni a megadott fájlba
	 * @param ids a modellben éppen aktuálisan levõ fájlok id-jai
	 * @param savedFile a fájl absolutepath ja
	 * @throws IOException  ha valmilyen oknál fogva nem sikerült volna a fájlba írás.
	 */
	public void saveFile(String savedFile,ArrayList<String> ids) throws IOException {
		FileOutputStream save = new FileOutputStream(savedFile);
		ObjectOutputStream data = new ObjectOutputStream(save);
		HashMap<String,Document> saveAble = new HashMap<String,Document>();
		for(String id : ids) {
			if(DocList.containsKey(id)) {
				saveAble.put(id, DocList.get(id));
			}
		}
		data.writeObject(saveAble);
		data.close();
		save.close();
	}
	
	/**
	 * Megpróbálja betölteni szérializálni a megadott fájlt, amit betölt a listába és a modellbe is.
	 * @param loadFile a betöltendõ fájl path-je
	 * @throws IOException ha nem sikerült a betöltés
	 * @throws ClassNotFoundException ha a kasztolás sikertelen volt
	 */
		@SuppressWarnings("unchecked")
	public void loadFile(String loadFile) throws IOException,ClassNotFoundException{
		this.clearAll();
		FileInputStream loadableFile = new FileInputStream(loadFile);
		ObjectInputStream inputData = new ObjectInputStream(loadableFile);
		HashMap<String,Document> loadedMap = (HashMap<String,Document>) inputData.readObject();
		Map<String,Document> map = new TreeMap<String,Document>(loadedMap);
		this.updateModel(map);
		inputData.close();
		loadableFile.close();
	}
	
		/**
		 * frissíti/fellül írja a listát és a modellt is.
		 * @param map a betöltött állomány
		 */
	private void updateModel(Map<String,Document> map) {
		Iterator<Entry<String, Document>> iterator = map.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<String, Document> pair =iterator.next();
			DocList.put(pair.getKey(),pair.getValue());
			this.addRow(pair.getValue().toObjectArray());
		} 
	}
	
	/**
	 * A modell egy sorának megváltozott/szerkesztett sorának értékét frissíti
	 * @param rowID a változtatott sor id-je
	 * @param doc a megváltozott Dokumentum
	 */
	private void updateRow(int rowID, Document doc) {
		Object[] data = doc.toObjectArray();
		int i =0;
		while(this.getColumnCount()!=i) {
			this.setValueAt(data[i], rowID, i);
			i++;
		}
	}
	
	/**
	 * vissza adja,hogy van e a wannabeID hoz hasonló
	 * az adat szerkesztés és létrehozás viszgálatához kell.
	 * @param wannabeID a vizsgálandó id
	 * @return Igaz, ha van már hozzá hasonló id
	 */
	public boolean idMatch(String wannabeID) {
		return DocList.containsKey(wannabeID);
	}
	
	/**
	 * osztály vizsgálat ami a filter kiértékelésnél szükséges.
	 */
	
	@Override
	public Class<?> getColumnClass(int column){
		switch(column) {
		case 2:
		case 3:
			return LocalDate.class;
		default:
			return String.class;
		}
	}
	
}
