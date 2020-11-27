package GUI_elements;
import java.awt.Image;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

/**
 * Enum "osztály" ami az egyes iconok/képek tárolását megjelenítését teszi lehetõvé.
 * így minden GUI element hozzá tud férni az iconokhoz és nem kell stattikusan tárolni az osztályoknál, vagy éppen paraméterként átadni.
 * @author nagye
 *
 */
public enum DefaultPaths {
	/**
	 * mainAblak iconjának rész path-ja
	 */
	MainIcon("view\\mainIcon.png"),
	/**
	 * AddFrame dialog ablak iconjának rész pathja
	 */
	AddIcon("view\\addFile.png"),
	/**
	 * Docuentáció neve/elérési útja
	 */
	Document("Dokumentációa.pdf"),
	/**
	 * saveFile icon pathja
	 */
	saveFile("view\\saveFile.png"),
	/**
	 * loadFile/menü icon pathja
	 */
	openFile("view\\openFile.png"),
	/**
	 * Dokumentáció megnyitása icon
	 */
	openDoc("view\\openDoc.png"),
	/**
	 * az éppen aktuális munkakönyvtár
	 */
	WorkDir("");
	
	/**
	 * az útvonal mint szöveges paraméter
	 */
	private String path;
	
	/**
	 * Konstruktor ami beállítja az adott enum pathját.
	 * @param value
	 */
	DefaultPaths(String value) {
		this.path=value;
	}
	
	/**
	 * Visszaadja a kiválasztott enum absolutePath-ját
	 * @return az elem elérési útvonala
	 */
	public String getPath() {
		return this.getDirectory()+File.separator+path;
	}
	
	/**
	 * Visszaadja az adott EnumPath-hez tartozó elemet mint Image
	 * @return a meghatározott kép
	 */
	public Image getImage() {
		ImageIcon icon = new ImageIcon(this.getPath());
		return icon.getImage();
	}
	
	/**
	 * Visszaadja az adott path-hez tartozó elemet mint ImageIcon
	 * @see Imageicon
	 * @return az adott Icon elem
	 */
	public ImageIcon getIcon() {
		return new ImageIcon(getPath());
	}
	
	/**
	 * Visszadja az éppen aktuális WorkingDirectory-t
	 * @return a working directory path-ja
	 */
	public String getDirectory() {
		String userDirectory = Paths.get("").toAbsolutePath().toString();
		return userDirectory;
	}
}
