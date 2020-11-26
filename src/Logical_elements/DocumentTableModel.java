package Logical_elements;
import java.awt.Desktop;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.table.DefaultTableModel;

/**	
 * Ez a DefaultTable modell reprezent�ci�ja
 * @author nagyerik99
 * bels� attribu�tuma egy HashTable ami a bet�lt�tt akt�v dokumentumokat t�rolja
 * �s a DocumentumTable modellje
 */
public class DocumentTableModel extends DefaultTableModel{
	private HashMap<String,Document> DocList;
	private static final long serialVersionUID = 1L;
	private static Object[] header = {"ID","T�pus","Kezdete","V�ge","F�jl"};
	
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
	 * @param newDoc
	 * @param viewRowID
	 * @param oldID
	 * @throws Exception
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
	 * @param docID
	 * @return
	 */
	public Document getRowData(String docID){
		return (DocList.get(docID));
	}
	
	/**
	 * Kit�rli a megfelel� sort a modellb�l �s a list�b�l is
	 * @param rowID
	 * @param row
	 * @throws NullPointerException
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
	 * @param docID
	 * @param desktop
	 * @throws Exception
	 * @throws NullPointerException
	 */
	public void openDoc(String docID,Desktop desktop) throws Exception,NullPointerException {
		if(docID==null) return;
		
		Document selectedDocument = DocList.get(docID);
		if(selectedDocument == null) {
			throw new NullPointerException("Hiba az �llom�ny nem tal�lhat�");
		}else {
			selectedDocument.openDoc(desktop);
		}
	}
	
	/**
	 * A Lista tartalm�t tudjuk kimenteni .ser t�pus� f�jlba
	 * @param savedFile
	 * @throws IOException
	 */
	public void saveFile(String savedFile) throws IOException {
		FileOutputStream save = new FileOutputStream(savedFile);
		ObjectOutputStream data = new ObjectOutputStream(save);
		data.writeObject(DocList);
		data.close();
		save.close();
	}
	
	/**
	 * .ser t�pus� f�jlokat sz�rializ�l be a List�nak amelyb�l azt�n l�trehozza a megjelen�t�sre haszn�lt modellt is.
	 * @param loadFile
	 * @throws IOException
	 * @throws ClassNotFoundException
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
		 * a loadFile hivja meg ez fogja majd updateelni a modellt amelyet a Table haszn�l
		 * @param map
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
	 * @param rowID
	 * @param doc
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
	 * @param wannabeID
	 * @return boolean
	 */
	public boolean idMatch(String wannabeID) {
		return DocList.containsKey(wannabeID);
	}
	
	
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
