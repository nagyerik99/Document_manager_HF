package GUI_elements;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import Logical_elements.DocumentFilterer;

/**
 * A szûrés panel amely a modellben lévõ adatok szûrését valósítja meg.
 * Összerakja a grafikus formáját a panelnek, és actionListener-rel figyeli
 * az egyes szûrési feltételek változását
 * @author nagyerik99
 * @see JPanel
 *
 */
public class FilterPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	/**
	 * a keresõ mezõk
	 */
	private JTextField name,type,fromDate,toDate;
	/**
	 * A keresõ mezõkhöz tartozó szöveges megnevezés a vizuális felismerhetõség végett.
	 */
	private JLabel nameLabel,typeLabel,fromDateLabel,toDateLabel;
	/**
	 * a keresõ mezõket és a hozzájuk tartozó labeleket tároló panelek
	 * a vizuális megjelenítésre kellenek
	 */
	private JPanel namePanel,typePanel,fromDatePanel,toDatePanel;
	/**
	 * Az adatokat/dokumnetumokat tároló tábla
	 * @see DocumnetTable
	 */
	private DocumentTable docTable;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 * A szûrõt osszeállító objektum
	 * @see DocumentFilterer
	 */
	private DocumentFilterer filterer;
	/**
	 * Tooltip a dátum mezõkhöz tartozó labeleknek, hogy a felhasználó útmutatást kapjon
	 * a várt fromátumokról.
	 */
	private static String dateToolTipText = "Dátum: 'yyyy/MM/dd' vagy 'yyyy.MM.dd' \n"
			+ "Intervallum: Két azonos formátumú dátum kötõjellel elválasztva";
	/**
	 * formai igazítása a labeleknek
	 */
	private int	labelAlignment = JLabel.LEFT;
	/**
	 * betûszín
	 */
	private static Color foreGroundColor = new Color(210,241,250);
	/**
	 * háttérszín
	 */
	private static Color backGroundColor;
	
	/**
	 * Default konstruktor.
	 * Létrehozza és inicializálja a szükséges elemket, a filtert, es formázza a panelt
	 * @param main
	 * @param table
	 */
	public FilterPanel(DatabaseFrame main,DocumentTable table) {
		docTable = table;
		mainFrame = main;
		filterer = new DocumentFilterer();
		backGroundColor = mainFrame.getBackground();
		
		namePanel=new JPanel();
		typePanel=new JPanel();
		fromDatePanel=new JPanel();
		toDatePanel=new JPanel();
		
		this.setBackAndForeGround(this);
		this.setBackAndForeGround(namePanel);
		this.setBackAndForeGround(typePanel);
		this.setBackAndForeGround(fromDatePanel);
		this.setBackAndForeGround(toDatePanel);
		
		this.setLayoutToPanel(namePanel,5,0,0,0,BoxLayout.LINE_AXIS);
		this.setLayoutToPanel(typePanel,5,0,0,0,BoxLayout.LINE_AXIS);
		this.setLayoutToPanel(fromDatePanel,5,0,0,0,BoxLayout.LINE_AXIS);
		this.setLayoutToPanel(toDatePanel,5,0,5,0,BoxLayout.LINE_AXIS);
		this.setLayoutToPanel(this,5,10,5,120,BoxLayout.PAGE_AXIS);
		
		nameLabel = new JLabel("Azonosító: ",labelAlignment);
		this.defineLabel(nameLabel, null);
		
		typeLabel = new JLabel("Típusa: ",labelAlignment);
		this.defineLabel(typeLabel, null);

		fromDateLabel = new JLabel("Kezdete:",labelAlignment);
		this.defineLabel(fromDateLabel, dateToolTipText);
		
		toDateLabel = new JLabel("Vége: ",labelAlignment);
		this.defineLabel(toDateLabel,dateToolTipText);
		
		ActionListener filterer = new FilterAction();
		
		name= new JTextField(20);
		this.defineTextField(name, filterer);
		
		type = new JTextField(20);
		this.defineTextField(type, filterer);
		
		fromDate = new JTextField(20);
		this.defineTextField(fromDate, filterer);
		
		toDate = new JTextField(20);
		this.defineTextField(toDate, filterer);
		
		namePanel.add(nameLabel);
		namePanel.add(Box.createHorizontalStrut(9));
		namePanel.add(name);
		
		typePanel.add(typeLabel);
		typePanel.add(Box.createHorizontalStrut(26));
		typePanel.add(type);
		
		fromDatePanel.add(fromDateLabel);
		fromDatePanel.add(Box.createHorizontalStrut(20));
		fromDatePanel.add(fromDate);
		
		toDatePanel.add(toDateLabel);
		toDatePanel.add(Box.createHorizontalStrut(35));
		toDatePanel.add(toDate);
		
		this.add(namePanel);
		this.add(typePanel);
		this.add(fromDatePanel);
		this.add(toDatePanel);
	}
	
	/**
	 * Beállítja aszíneket a kapott komponensnek
	 * @see JComponent
	 * @param comp a kapott komponens
	 */
	private void setBackAndForeGround(JComponent comp) {
		comp.setBackground(backGroundColor);
		comp.setForeground(foreGroundColor);
	}
	
	/**
	 * Beállítja a layout-ot és a formázást a paneleknek
	 * @param panel a formázandó panel
	 * @param top igazítás fent
	 * @param left igazítás balra
	 * @param bottom igazítás lent
	 * @param right igazítás jobbra
	 * @param AXIS a layout iránya/eloszlása
	 */
	private void setLayoutToPanel(JPanel panel,int top,int left,int bottom, int right,int AXIS) {
		panel.setLayout(new BoxLayout(panel,AXIS));
		panel.setBorder(new EmptyBorder(top,left,bottom,right));
	}
	
	/**
	 * Inicializálja a paraméterként kapott labelt
	 * @param label a formázandó label
	 * @param ToolTipText "good to know" szöveg / ha van
	 */
	private void defineLabel(JLabel label,String ToolTipText) {
		this.setBackAndForeGround(label);
		if(ToolTipText!=null) label.setToolTipText(ToolTipText);
	}
	
	/**
	 * beállítja a paraméterként kapott textfield-et
	 * @param textField formázandó field
	 * @param listener Action listener amit az adott fieldhez kell adni
	 * @see ActionListener
	 */
	private void defineTextField(JTextField textField, ActionListener listener) {
		textField.setHorizontalAlignment(labelAlignment);
		this.setBackAndForeGround(textField);
		textField.addActionListener(listener);
	}
	
	/**
	 * Belsõ osztály ami implementálja az ActionListenr interface-t 
	 * A mezõ változásakor, ami enter leütést jelöl , hívódik meg és illeszti össze a filterer segítségével
	 * a szûrendõ adatokat majd átadja a táblának szûrésre.
	 * @author nagyerik99
	 *
	 */
	private class FilterAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

			filterer.clearFilterer();
			String nameText = name.getText();
			String typeText = type.getText();
			String fromDateText = fromDate.getText().strip();
			String toDateText = toDate.getText().strip();
			
			try {
				//ha nevet keresunk
				if(!nameText.isEmpty()) {
					filterer.add(nameText,0,"text");
					}
				//ha tipust keresunk
				if(!typeText.isEmpty()) {
					filterer.add(typeText,1,"text");
					}
				//ha dátumot és/vagy intervallumot keresünk
				if(!fromDateText.isEmpty() || !toDateText.isEmpty()) {
					
						filterer.add(fromDateText, 2, "date");
						filterer.add(toDateText, 3, "date");
					}
			}catch(Exception error) {
				mainFrame.showDialog(error.getMessage(), "Keresés Hiba!", "error");
			}
			
			docTable.addRowFilter(filterer.getRowFilter());
		}
	}
}
