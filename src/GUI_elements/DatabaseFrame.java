package GUI_elements;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * A main Ablak ami az elemek inicializ�l�st �s a kezeel�s�ket val�s�tja meg.
 * Illetve Esetleges Exception-ok eset�n dialoggal jelzi azt a felhaszn�l�nak.
 * @author nagyerik99
 */
public class DatabaseFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	/**
	 * az adatok megjelen�t�s��rt felel�s t�bla
	 * @see DocumentTable
	 */
	private DocumentTable docTable;
	/**
	 * szabv�nyos men�
	 * @see dbMenu
	 */
	private dbMenu dbMenu;
	/**
	 * a nyom�gombokat �s m�veleteiket kezel� panel
	 * @see SidePanel
	 */
	private SidePanel optionPanel;
	/**
	 *  a T�bla sz�r�s�t megval�s�t� panel
	 *  @see FilterPanel
	 */
	private FilterPanel filterpanel;
	/**
	 * a t�bla(@DocumentTable) panelja.
	 */
	private JPanel tablePanel;
	/**
	 * a T�bla g�rgethet�s�g�t val�s�tja meg
	 */
	private JScrollPane tablePane;
	/**
	 * statikus v�ltoz� a sz�nek be�ll�t�s�ra
	 * @backGround a h�tt�rsz�n
	 */
	private static Color backGround = new Color(0,119,128);
	
	/**
	 * Az oszt�ly default konstruktora,
	 * inicializ�lja az egyes elemeket �s �ssze�ll�tja az ablakot haszn�latra.
	 * @param name @String az ablak megjeln� neve
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
	 * Visszaadja, hogy l�that� e a sz�r�panel.
	 * @return Igaz, ha a sz�r�panel l�that�.
	 * egy�bk�nt hamis
	 */
	protected boolean isFilterVisible() {
		return filterpanel.isVisible();
	}
	
	/**
	 * Be�ll�tja a sz�r�panel l�that�s�g�t
	 * @param opt boolean t�pus� v�ltoz�
	 * Ha az �rt�ke igaz akkor l�that�v� teszi a panelt.
	 * Ha Hamis akkor elrejti.
	 */
	protected void showFilterer(boolean opt) {
		filterpanel.setVisible(opt);
	}
	
	/**
	 * 
	 * @param messeage a megjelen�tend� �zenet
	 * @param title a Dialog ablak neve
	 * @param messeageType az �zenet t�pusa
	 * @return ha p�rbesz�d ablak volt akkor a v�alsztott opci� �rt�ke, egy�bk�nt -1
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
