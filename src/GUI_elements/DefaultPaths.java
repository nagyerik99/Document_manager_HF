package GUI_elements;
import java.awt.Image;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.ImageIcon;

/**
 * Enum "oszt�ly" ami az egyes iconok/k�pek t�rol�s�t megjelen�t�s�t teszi lehet�v�.
 * �gy minden GUI element hozz� tud f�rni az iconokhoz �s nem kell stattikusan t�rolni az oszt�lyokn�l, vagy �ppen param�terk�nt �tadni.
 * @author nagye
 *
 */
public enum DefaultPaths {
	/**
	 * mainAblak iconj�nak r�sz path-ja
	 */
	MainIcon("view\\mainIcon.png"),
	/**
	 * AddFrame dialog ablak iconj�nak r�sz pathja
	 */
	AddIcon("view\\addFile.png"),
	/**
	 * Docuent�ci� neve/el�r�si �tja
	 */
	Document("Dokument�ci�a.pdf"),
	/**
	 * saveFile icon pathja
	 */
	saveFile("view\\saveFile.png"),
	/**
	 * loadFile/men� icon pathja
	 */
	openFile("view\\openFile.png"),
	/**
	 * Dokument�ci� megnyit�sa icon
	 */
	openDoc("view\\openDoc.png"),
	/**
	 * az �ppen aktu�lis munkak�nyvt�r
	 */
	WorkDir("");
	
	/**
	 * az �tvonal mint sz�veges param�ter
	 */
	private String path;
	
	/**
	 * Konstruktor ami be�ll�tja az adott enum pathj�t.
	 * @param value
	 */
	DefaultPaths(String value) {
		this.path=value;
	}
	
	/**
	 * Visszaadja a kiv�lasztott enum absolutePath-j�t
	 * @return az elem el�r�si �tvonala
	 */
	public String getPath() {
		return this.getDirectory()+File.separator+path;
	}
	
	/**
	 * Visszaadja az adott EnumPath-hez tartoz� elemet mint Image
	 * @return a meghat�rozott k�p
	 */
	public Image getImage() {
		ImageIcon icon = new ImageIcon(this.getPath());
		return icon.getImage();
	}
	
	/**
	 * Visszaadja az adott path-hez tartoz� elemet mint ImageIcon
	 * @see Imageicon
	 * @return az adott Icon elem
	 */
	public ImageIcon getIcon() {
		return new ImageIcon(getPath());
	}
	
	/**
	 * Visszadja az �ppen aktu�lis WorkingDirectory-t
	 * @return a working directory path-ja
	 */
	public String getDirectory() {
		String userDirectory = Paths.get("").toAbsolutePath().toString();
		return userDirectory;
	}
}
