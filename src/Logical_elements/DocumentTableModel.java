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
 * Ez az oszt�ly val�s�tja meg a DefaultTableModel-t vagyi a DocumentTable �ltal
 * haszn�lt modellt.
 * @see DocumentTable
 * @author nagyerik99
 */
public class DocumentTableModel extends DefaultTableModel{
	private static final long serialVersionUID = 1L;
	/**
	 * az a bels� lista amely a hozz�adtott Dokumnetumokat t�rolja �s ez alapj�n rakja �ssze
	 * megjelen�tend� modellt sorait az oszt�ly.
	 * Key : string ami az adott dokumnetum azonos�t�ja neve. ez alapj�n gyorsan �s k�nnyen meglehet tal�lni a tartalmazott adatot.
	 */
	private HashMap<String,Document> DocList;
	/**
	 * a modell header-je 
	 */
	private static Object[] header = {"ID","T�pus","Kezdete","V�ge","F�jl"};
	
	/**
	 * Default konstruktor ami l�trehozza a modellt �s inicializ�lja a Doclist-et, mely a Documentumocat tartalmazza
	 */
	public DocumentTableModel() {
		super(header,0);
		DocList = new HashMap<String,Document>();
	}
	
	/**
	 * Documentum hozz�ad�sa a list�hoz �s a modellhez
	 * @param doc
	 */
	public void addRow(Document doc){
			DocList.put(doc.getDocID(),doc);
			this.addRow(doc.toObjectArray());
	}
	/**
	 * A Dokumentum szerkeszt�s�n�l h�v�dik meg, az �j dokumentumot hozz�adja a list�hoz �s a modellhez a r�git pedig t�rli
	 * @param newDoc a felvevend� dokumentum
	 * @param viewRowID a modell ViewModellj�nek a dokumentumhoz tartoz� id-ja
	 * @param oldID a r�gi dokumentum id-ja/neve
	 * @throws Exception ha nem tal�lta meg az oldID alapj�n az adott elemet, vagyis nincs a list�ban
	 */
	public void editRow(Document newDoc, int viewRowID, String oldID) throws Exception {
		String docID = newDoc.getDocID();
		Document oldDoc = DocList.get(oldID);
		if(oldDoc==null) throw new Exception("Hi�nyos f�jl lista (r�gi ID)");
		
		DocList.remove(oldID);
		DocList.put(docID, newDoc);
		this.updateRow(viewRowID, newDoc);
	}
	
	/**
	 * Visszaadja az adott Documentumot ami a modellben kiv�lasztva szerepel
	 * @param docID  a kiv�lasztott dokumentum neve/id ja
	 * @return Document a kiv�lasztott dokumentum
	 * @see Document
	 */
	public Document getRowData(String docID){
		return (DocList.get(docID));
	}
	
	/**
	 * Kit�rli a megfelel� sort a modellb�l �s a list�b�l is
	 * @param rowID a dokumentum azoos�t�ja
	 * @param row a modell sorAzonos�t�ja
	 * @throws NullPointerException ha nem tal�lhat� a modellben a dokumentum
	 */
	public void removeSelectedRow(String rowID,int row) throws NullPointerException {
		DocList.remove(rowID);
		this.removeRow(row);
	}
	
	/**
	 * Ki�r�ti a list�t �s a modellt
	 */
	public void clearAll() {
		if(this.getRowCount()==0) return;
		
		this.setRowCount(0);
		DocList.clear();
	}
	/**
	 * A modellben kiv�lasztott elemre k�tszer kattintva megh�v�dik a DocumentTable-�n kereszt�l
	 * megnyitja a csatolt f�jlt / ha van olyan
	 * @param docID a dokumentum azonos�t�ja
	 * @param desktop az asztalt jelk�pez� objektum
	 * @throws Exception ha nincs csatolt �llom�ny
	 */
	public void openDoc(String docID,Desktop desktop) throws Exception {
		if(docID==null) return;
		
		Document selectedDocument = DocList.get(docID);
		if(selectedDocument == null) {
			throw new Exception("Hiba az �llom�ny nem tal�lhat�");
		}else {
			selectedDocument.openDoc(desktop);
		}
	}
	
	/**
	 * Az ids t�mb param�tereivel megegyez� mez�k/dokumentumok tartalm�t tudjuk kimenteni a megadott f�jlba
	 * @param ids a modellben �ppen aktu�lisan lev� f�jlok id-jai
	 * @param savedFile a f�jl absolutepath ja
	 * @throws IOException  ha valmilyen okn�l fogva nem siker�lt volna a f�jlba �r�s.
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
	 * Megpr�b�lja bet�lteni sz�rializ�lni a megadott f�jlt, amit bet�lt a list�ba �s a modellbe is.
	 * @param loadFile a bet�ltend� f�jl path-je
	 * @throws IOException ha nem siker�lt a bet�lt�s
	 * @throws ClassNotFoundException ha a kasztol�s sikertelen volt
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
		 * friss�ti/fell�l �rja a list�t �s a modellt is.
		 * @param map a bet�lt�tt �llom�ny
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
	 * A modell egy sor�nak megv�ltozott/szerkesztett sor�nak �rt�k�t friss�ti
	 * @param rowID a v�ltoztatott sor id-je
	 * @param doc a megv�ltozott Dokumentum
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
	 * vissza adja,hogy van e a wannabeID hoz hasonl�
	 * az adat szerkeszt�s �s l�trehoz�s viszg�lat�hoz kell.
	 * @param wannabeID a vizsg�land� id
	 * @return Igaz, ha van m�r hozz� hasonl� id
	 */
	public boolean idMatch(String wannabeID) {
		return DocList.containsKey(wannabeID);
	}
	
	/**
	 * oszt�ly vizsg�lat ami a filter ki�rt�kel�sn�l sz�ks�ges.
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
