package Logical_elements;
import GUI_elements.DatabaseFrame;


/**
 * A main oszt�ly ami megh�vja a DatabaseFrame konstruktor�t.
 * @author nagyerik99
 *
 */
public class DBmain {
	/**
	 * A DatabaseFrame objektum, melyet a program futtat�skor elind�t
	 */
	private static DatabaseFrame dbFrame;
	public static void main(String[] args) {
		dbFrame = new DatabaseFrame("F�jl Nyilv�ntart�");
		dbFrame.setVisible(true);
		
	}
}
