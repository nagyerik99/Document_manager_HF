package GUI_elements;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

//TODO: hibakezelés :D :D: :D:D:D:D:D:D:D

public class Database_frame extends JFrame{
	private DocumentTable docTable;
	private DB_Menu dbMenu;
	private SidePanel optionPanel;
	private FilterPanel filterpanel;
	private static final long serialVersionUID = 1L;
	private JFrame paneFrame;
	private JPanel tablePanel;
	
	public Database_frame(String name, ImageIcon icon) {
		super(name);
		this.setResizable(false);
		this.setBackground(new Color(0,119,128));
		this.setIconImage(icon.getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		tablePanel = new JPanel();
		tablePanel.setBorder(new LineBorder(this.getBackground(),5,false));
		docTable = new DocumentTable(this);
		docTable.setFillsViewportHeight(rootPaneCheckingEnabled);
		JScrollPane jpane = new JScrollPane(docTable);
		tablePanel.add(jpane);
		tablePanel.setBackground(this.getBackground());
		dbMenu = new DB_Menu(this,docTable);
		optionPanel = new SidePanel(this,docTable);
		docTable.setOptPanel(optionPanel);
		filterpanel = new FilterPanel(this,docTable);
		this.add(dbMenu,BorderLayout.NORTH);
		this.add(tablePanel,BorderLayout.CENTER);
		this.add(optionPanel,BorderLayout.EAST);
		this.add(filterpanel,BorderLayout.SOUTH);
		filterpanel.setVisible(false);
		paneFrame = new JFrame();
		pack();
		//this.setResizable(false);
	}
	
	protected File getDocumentDirectory() {
		String userDirectory = Paths.get("").toAbsolutePath().toString();
		return new File(userDirectory+File.separator+"Dokumentáció.pdf");
	}
	
	protected boolean isFilterVisible() {
		return filterpanel.isVisible();
	}
	
	protected void showFilterer(boolean opt) {
		filterpanel.setVisible(opt);
	}
	
	protected int showDialog(String messeage, String title, String messeageType) {
		if(messeageType.equals("error")) {
			JOptionPane.showMessageDialog(paneFrame,
				    messeage,
				    title,
				    JOptionPane.ERROR_MESSAGE);
		}else if(messeageType.equals("info")) {
			JOptionPane.showMessageDialog(paneFrame,
				    messeage,
				    title,
				    JOptionPane.INFORMATION_MESSAGE);
		}else if (messeageType.equals("dialog")) {
			int option = JOptionPane.showConfirmDialog(
				    paneFrame,
				    messeage,
				    title,
				    JOptionPane.YES_NO_OPTION);
			return option;
		}
		return -1;
		
	}

}
