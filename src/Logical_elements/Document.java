package Logical_elements;
import java.awt.Desktop;
import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Ez az �sszetett osztly amely az egyes Dokumentumok t�rol�s�t va��s�tja meg
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
	 * A Document oszt�ly default konstruktora amely a Dokumentum objektum l�trehoz�s��rt felel
	 * Elmenti az adatokat �s csatolja a f�jlt az objektumhoz
	 * @param input az elt�roland� adat string t�mbbk�nt
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
	 * Visszaadja a Dokumentum nev�t/azonos�t�j�t
	 * @return String a Dokumentum neve/azonos�t�ja
	 */
	public String getDocID() {
		return docName;
	}
	
	/**
	 * Visszaadja a dokumentum megadott t�pus�t
	 * @return String a dokuemntum t�pusa
	 */
	public String getType() {
		return docType;
	}
	
	/**
	 * Visszaadja a Kezd�shez felvett d�tumot
	 * @return LocalDate a kezd� d�tum
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}
	/**
	 * Visszaadja a V�ge D�tumot
	 * @return LocalDate a v�ge d�tum
	 */
	public LocalDate getToDate() {
		return toDate;
	}
	
	/**
	 * Visszaadja a csatolt f�jlat ha van
	 * @return null ha nincs csatolt f�jl. File: ha van csatolt f�jl
	 */
	public File getFile() {
		return attachedFile;
	}
	
	/**
	 * Megform�lja a Dokuemntum oszt�ly param�tereit �gy,hogy az a DocumentTableModel �ltal megjelen�thet� legyen
	 * @return Ojektum t�mb ami a Dokuemntum adatait tartalmazza 
	 * kiv�ve a f�jl mert ann�l a neve ha van csatolt f�jl "Nics csatolm�ny" ha nincs
	 */
	protected Object[] toObjectArray() {
		String filename = null; 
		Object[] result;
		if(attachedFile != null) {
			filename = attachedFile.getName();
		}
		else {
			filename = "Nincs csatolm�ny";
		}
		
		result = new Object[] {docName,docType,fromDate,toDate,filename};
		
		return result;
	}
	
	/**
	 * Visszaadja, hogy tartalmaz e csatolm�nyt az objektum
	 * @return Igaz, ha van csatolm�ny �s l�tezik.
	 */
	protected boolean containsFile() {
		if(attachedFile != null && attachedFile.exists()) {
			return true;
		}
			return false;
	}
	
	/**
	 * Megnyitja a csatolt f�jlt
	 * @param desktop Dektop az asztalt reprezent�l� objektum
	 * @throws Exception ha nem tudja megnyitni a dokuemntumot -> nincs csatol�ny
	 */
	protected void openDoc(Desktop desktop) throws Exception {
		if(!containsFile()) {
			throw new Exception("A Dokumentumhoz, nincs csatolt f�jl!");
		}else {
			desktop.open(attachedFile);
		}
	}
	
	/**
	 * Kiirat�s ellen�rz�sre haszn�ltam
	 */
	public String toString() {
		return docName+" "+docType+" "+fromDate+" "+toDate;
	}
	
}
