package GUI_elements;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A main Ablak ami az elemek inicializálást és a kezeelésüket valósítja meg.
 * Illetve Esetleges Exception-ok esetén dialoggal jelzi azt a felhasználónak.
 * @author nagyerik99
 */
public class DatabaseFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	/**
	 * az adatok megjelenítéséért felelõs tábla
	 * @see DocumentTable
	 */
	private DocumentTable docTable;
	/**
	 * szabványos menü
	 * @see dbMenu
	 */
	private dbMenu dbMenu;
	/**
	 * a nyomógombokat és mûveleteiket kezelõ panel
	 * @see SidePanel
	 */
	private SidePanel optionPanel;
	/**
	 *  a Tábla szûrését megvalósító panel
	 *  @see FilterPanel
	 */
	private FilterPanel filterpanel;
	/**
	 * a tábla(@DocumentTable) panelja.
	 */
	private JPanel tablePanel;
	/**
	 * a Tábla görgethetõségét valósítja meg
	 */
	private JScrollPane tablePane;
	/**
	 * statikus változó a színek beállítására
	 * @backGround a háttérszín
	 */
	private static Color backGround = new Color(0,119,128);
	
	/**
	 * Az osztály default konstruktora,
	 * inicializálja az egyes elemeket és összeállítja az ablakot használatra.
	 * @param name @String az ablak megjelnõ neve
	 */
	public DatabaseFrame(String name) {
		super(name);
		this.setResizable(false);
		this.setBackground(backGround);
		this.setIconImage(DefaultPaths.MainIcon.getImage());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		docTable = new DocumentTable(this);
		docTable.setFillsViewportHeight(rootPaneCheckingEnabled);
		
		optionPanel = new SidePanel(this,docTable);
		docTable.setOptPanel(optionPanel);
		
		tablePanel = new JPanel();
		tablePane = new JScrollPane(docTable);
		tablePanel.add(tablePane);
		tablePanel.setBackground(backGround);
		
		dbMenu = new dbMenu(this,docTable);
		
		filterpanel = new FilterPanel(this,docTable);
		filterpanel.setVisible(false);
		
		this.add(dbMenu,BorderLayout.NORTH);
		this.add(tablePanel,BorderLayout.CENTER);
		this.add(optionPanel,BorderLayout.EAST);
		this.add(filterpanel,BorderLayout.SOUTH);
		
		pack();
	}
	
	/**
	 * Visszaadja, hogy látható e a szûrõpanel.
	 * @return Igaz, ha a szûrõpanel látható.
	 * egyébként hamis
	 */
	protected boolean isFilterVisible() {
		return filterpanel.isVisible();
	}
	
	/**
	 * Beállítja a szûrõpanel láthatóságát
	 * @param opt boolean típusú változó
	 * Ha az értéke igaz akkor láthatóvá teszi a panelt.
	 * Ha Hamis akkor elrejti.
	 */
	protected void showFilterer(boolean opt) {
		filterpanel.setVisible(opt);
	}
	
	/**
	 * 
	 * @param messeage a megjelenítendõ üzenet
	 * @param title a Dialog ablak neve
	 * @param messeageType az üzenet típusa
	 * @return ha párbeszéd ablak volt akkor a váalsztott opció értéke, egyébként -1
	 */
	protected int showDialog(String messeage, String title, String messeageType) {
		if(messeageType.equals("error")) {
			JOptionPane.showMessageDialog(
					this,
				    messeage,
				    title,
				    JOptionPane.ERROR_MESSAGE);
		}else if(messeageType.equals("info")) {
			JOptionPane.showMessageDialog(
					this,
				    messeage,
				    title,
				    JOptionPane.INFORMATION_MESSAGE);
		}else if (messeageType.equals("dialog")) {
			int option = JOptionPane.showConfirmDialog(
				    this,
				    messeage,
				    title,
				    JOptionPane.YES_NO_OPTION);
			return option;
		}
		return -1;
		
	}

}
