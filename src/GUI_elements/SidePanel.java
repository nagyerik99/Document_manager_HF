package GUI_elements;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class SidePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	DocumentTable table;
	private JButton add,remove,search,edit;
	Database_frame mainFrame;
	public SidePanel(Database_frame main, DocumentTable tab) {
		table = tab;
		mainFrame=main;
		this.setLayout(new GridLayout(10,1,5,5));
		this.setBorder(new EmptyBorder(9,0,0,10));
		
		add = new JButton("Hozzáadás");
		add.setActionCommand("add");
		this.setBackground(mainFrame.getBackground());
		remove = new JButton("Törlés");
		remove.setActionCommand("delete");
		remove.setEnabled(false);
		
		
		search = new JButton("Keresés");
		search.setActionCommand("search");
		
		edit = new JButton("Szerkesztés");
		edit.setActionCommand("edit");
		edit.setEnabled(false);
		
		ActionListener buttonAction = new PanelButton();
		add.addActionListener(buttonAction);
		remove.addActionListener(buttonAction);
		edit.addActionListener(buttonAction);
		search.addActionListener(buttonAction);
		
		add.setBackground(new Color(0,74,79));
		remove.setBackground(add.getBackground());
		edit.setBackground(add.getBackground());
		search.setBackground(add.getBackground());
		
		add.setForeground(new Color(210,241,250));
		remove.setForeground(new Color(210,241,250));
		edit.setForeground(new Color(210,241,250));
		search.setForeground(new Color(210,241,250));
		
		this.add(add);
		this.add(remove);
		this.add(search);
		this.add(edit);
	}
	
	protected void setDeletable(boolean opt) {
		remove.setEnabled(opt);
		edit.setEnabled(opt);
	}
	
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
