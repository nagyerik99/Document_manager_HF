package Logical_elements;
import GUI_elements.DatabaseFrame;


/**
 * A main osztály ami meghívja a DatabaseFrame konstruktorát.
 * @author nagyerik99
 *
 */
public class DBmain {
	public static void main(String[] args) {
		DatabaseFrame dbFrame =  new DatabaseFrame("Fájl Nyilvántartó");
		dbFrame.setVisible(true);
		
	}
}
