package Logical_elements;
import javax.swing.ImageIcon;
import GUI_elements.Database_frame;

public class DB_main {
	private static  ImageIcon icon = new ImageIcon("D:\\Files and Stuffs\\Eclipse_workspace\\Document_manager_HF\\view\\mainIcon.png");
	
	public static void main(String[] args) {
		Database_frame db_frame =  new Database_frame("Fájl Nyilvántartó",icon);
		db_frame.setVisible(true);
		
	}
}
