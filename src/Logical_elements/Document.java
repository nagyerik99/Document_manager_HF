package Logical_elements;
import java.awt.Desktop;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Ez az összetett osztly amely az egyes Dokumentumok tárolását vaéósítja meg
 * @author nagyerik99
 *
 */
public class Document implements Serializable{
	private static final long serialVersionUID = 1L;
	private String docName;
	private String docType;
	private LocalDate fromDate;
	private LocalDate toDate;
	private File attachedFile;
	
	/**
	 * A Document osztály default konstruktora amely a Dokumentum objektum létrehozásáért felel
	 * Elmenti az adatokat és csatolja a fájlt az objektumhoz
	 * @param input az eltárolandó adat string tömbbként
	 */
	public Document(String[] input){
		docName = input[0];
		docType = input[1];
		fromDate = LocalDate.parse(input[2]);
		toDate = LocalDate.parse(input[3]);
		
		if(input.length==5 && input[4]!=null)
			attachedFile = new File(input[4]);
		else 
			attachedFile = null;
		
	}
	
	/**
	 * Visszaadja a Dokumentum nevét/azonosítóját
	 * @return String a Dokumentum neve/azonosítója
	 */
	public String getDocID() {
		return docName;
	}
	
	/**
	 * Visszaadja a dokumentum megadott típusát
	 * @return String a dokuemntum típusa
	 */
	public String getType() {
		return docType;
	}
	
	/**
	 * Visszaadja a Kezdéshez felvett dátumot
	 * @return LocalDate a kezdõ dátum
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}
	/**
	 * Visszaadja a Vége Dátumot
	 * @return LocalDate a vége dátum
	 */
	public LocalDate getToDate() {
		return toDate;
	}
	
	/**
	 * Visszaadja a csatolt fájlat ha van
	 * @return null ha nincs csatolt fájl. File: ha van csatolt fájl
	 */
	public File getFile() {
		return attachedFile;
	}
	
	/**
	 * Megformálja a Dokuemntum osztály paramétereit úgy,hogy az a DocumentTableModel által megjeleníthetõ legyen
	 * @return Ojektum tömb ami a Dokuemntum adatait tartalmazza 
	 * kivéve a fájl mert annál a neve ha van csatolt fájl "Nics csatolmány" ha nincs
	 */
	protected Object[] toObjectArray() {
		String filename = null; 
		Object[] result;
		if(attachedFile != null) {
			filename = attachedFile.getName();
		}
		else {
			filename = "Nincs csatolmány";
		}
		
		result = new Object[] {docName,docType,fromDate,toDate,filename};
		
		return result;
	}
	
	/**
	 * Visszaadja, hogy tartalmaz e csatolmányt az objektum
	 * @return Igaz, ha van csatolmány és létezik.
	 */
	protected boolean containsFile() {
		if(attachedFile != null && attachedFile.exists()) {
			return true;
		}
			return false;
	}
	
	/**
	 * Megnyitja a csatolt fájlt
	 * @param desktop Dektop az asztalt reprezentáló objektum
	 * @throws Exception ha nem tudja megnyitni a dokuemntumot -> nincs csatolány
	 */
	protected void openDoc(Desktop desktop) throws Exception {
		if(!containsFile()) {
			throw new Exception("A Dokumentumhoz, nincs csatolt fájl!");
		}else {
			desktop.open(attachedFile);
		}
	}
	
	/**
	 * Kiiratás ellenõrzésre használtam
	 */
	public String toString() {
		return docName+" "+docType+" "+fromDate+" "+toDate;
	}
	
}
