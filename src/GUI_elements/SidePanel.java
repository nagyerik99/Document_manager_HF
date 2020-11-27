package GUI_elements;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * 
 * @author nagye
 *
 */
public class SidePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	DocumentTable table;
	private JButton add,remove,search,edit;
	DatabaseFrame mainFrame;
	private static Color buttonForeGround = new Color(210,241,250),
						 buttonBackGround =new Color(0,74,79);
	/**
	 * 
	 * @param main
	 * @param tab
	 */
	public SidePanel(DatabaseFrame main, DocumentTable tab) {
		table = tab;
		mainFrame=main;
		
		this.setLayout(new GridLayout(10,1,5,5));
		this.setBorder(new EmptyBorder(9,0,0,10));
		this.setBackground(mainFrame.getBackground());
		
		ActionListener buttonAction = new PanelButton();
		
		add = new JButton("Hozz�ad�s");
		remove = new JButton("T�rl�s");
		search = new JButton("Keres�s");
		edit = new JButton("Szerkeszt�s");
		this.setButtonDefault(add, buttonBackGround, buttonForeGround, buttonAction, "add", true);
		this.setButtonDefault(remove, buttonBackGround, buttonForeGround, buttonAction, "delete", false);
		this.setButtonDefault(search, buttonBackGround, buttonForeGround, buttonAction, "search", true);
		this.setButtonDefault(edit, buttonBackGround, buttonForeGround, buttonAction, "edit", false);
		
		this.add(add);
		this.add(remove);
		this.add(search);
		this.add(edit);
	}
	
	/**
	 * 
	 * @param button
	 * @param backGround
	 * @param foreGround
	 * @param listener
	 * @param ac
	 * @param enabled
	 */
	private void setButtonDefault(JButton button, Color backGround, Color foreGround, ActionListener listener, String ac, boolean enabled) {
		button.setBackground(backGround);
		button.setForeground(foreGround);
		button.setActionCommand(ac);
		button.addActionListener(listener);
		button.setEnabled(enabled);
	}
	
	/**
	 * Haszn�lhat�v� teszi vagy letilja a szerkeszt�s �s t�rl�s gombokat
	 * @param opt a be�ll�tand� opci�: igaz vagy hamis
	 */
	protected void setDeletable(boolean opt) {
		remove.setEnabled(opt);
		edit.setEnabled(opt);
	}
	
	/**
	 * Az akci� gombok megnyom�sa eset�n h�v�dik meg.
	 * @add eset�n l�trehozza az AddFrame-et hozz�ad�sra
	 * @delete eset�n megh�vja a DocumentTable removeSelectedRow met�dus�t
	 * @edit eset�n l�trehozza az AddFrame-et szerkeszt�sre
	 * @search eset�n megh�vja megh�vja a DatabaseFrame
	 * isFilterVisible �s a showFilterer met�dust
	 * @author nagyerik99
	 * @see DatabaseFrame
	 * @see AddFrame
	 */
	public class PanelButton implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("add")) {
				AddFrame frame = new AddFrame(mainFrame,table,false);
				frame.setVisible(true);
			}else if(e.getActionCommand().equals("delete")) {
				table.removeSelectedRow();
				setDeletable(false);
			}else if(e.getActionCommand().equals("edit")) {
				AddFrame editFrame = new AddFrame(mainFrame,table,true);
				editFrame.setVisible(true);
				setDeletable(false);
			}else if(e.getActionCommand().equals("search")) {
				boolean set = mainFrame.isFilterVisible();
				mainFrame.showFilterer(!set);
			}
		}
	}
}
