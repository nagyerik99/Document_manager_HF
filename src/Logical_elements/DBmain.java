package Logical_elements;
import GUI_elements.DatabaseFrame;


/**
 * A main osztály ami meghívja a DatabaseFrame konstruktorát.
 * @author nagyerik99
 *
 */
public class DBmain {
	/**
	 * A DatabaseFrame objektum, melyet a program futtatáskor elindít
	 */
	private static DatabaseFrame dbFrame;
	public static void main(String[] args) {
		dbFrame = new DatabaseFrame("Fájl Nyilvántartó");
		dbFrame.setVisible(true);
		
	}
}
