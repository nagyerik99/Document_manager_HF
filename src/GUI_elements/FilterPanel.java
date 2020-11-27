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
 * A sz�r�s panel amely a modellben l�v� adatok sz�r�s�t val�s�tja meg.
 * �sszerakja a grafikus form�j�t a panelnek, �s actionListener-rel figyeli
 * az egyes sz�r�si felt�telek v�ltoz�s�t
 * @author nagyerik99
 * @see JPanel
 *
 */
public class FilterPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	/**
	 * a keres� mez�k
	 */
	private JTextField name,type,fromDate,toDate;
	/**
	 * A keres� mez�kh�z tartoz� sz�veges megnevez�s a vizu�lis felismerhet�s�g v�gett.
	 */
	private JLabel nameLabel,typeLabel,fromDateLabel,toDateLabel;
	/**
	 * a keres� mez�ket �s a hozz�juk tartoz� labeleket t�rol� panelek
	 * a vizu�lis megjelen�t�sre kellenek
	 */
	private JPanel namePanel,typePanel,fromDatePanel,toDatePanel;
	/**
	 * Az adatokat/dokumnetumokat t�rol� t�bla
	 * @see DocumnetTable
	 */
	private DocumentTable docTable;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 * A sz�r�t ossze�ll�t� objektum
	 * @see DocumentFilterer
	 */
	private DocumentFilterer filterer;
	/**
	 * Tooltip a d�tum mez�kh�z tartoz� labeleknek, hogy a felhaszn�l� �tmutat�st kapjon
	 * a v�rt from�tumokr�l.
	 */
	private static String dateToolTipText = "D�tum: 'yyyy/MM/dd' vagy 'yyyy.MM.dd' \n"
			+ "Intervallum: K�t azonos form�tum� d�tum k�t�jellel elv�lasztva";
	/**
	 * formai igaz�t�sa a labeleknek
	 */
	private int	labelAlignment = JLabel.LEFT;
	/**
	 * bet�sz�n
	 */
	private static Color foreGroundColor = new Color(210,241,250);
	/**
	 * h�tt�rsz�n
	 */
	private static Color backGroundColor;
	
	/**
	 * Default konstruktor.
	 * L�trehozza �s inicializ�lja a sz�ks�ges elemket, a filtert, es form�zza a panelt
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
		
		nameLabel = new JLabel("Azonos�t�: ",labelAlignment);
		this.defineLabel(nameLabel, null);
		
		typeLabel = new JLabel("T�pusa: ",labelAlignment);
		this.defineLabel(typeLabel, null);

		fromDateLabel = new JLabel("Kezdete:",labelAlignment);
		this.defineLabel(fromDateLabel, dateToolTipText);
		
		toDateLabel = new JLabel("V�ge: ",labelAlignment);
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
	 * Be�ll�tja asz�neket a kapott komponensnek
	 * @see JComponent
	 * @param comp a kapott komponens
	 */
	private void setBackAndForeGround(JComponent comp) {
		comp.setBackground(backGroundColor);
		comp.setForeground(foreGroundColor);
	}
	
	/**
	 * Be�ll�tja a layout-ot �s a form�z�st a paneleknek
	 * @param panel a form�zand� panel
	 * @param top igaz�t�s fent
	 * @param left igaz�t�s balra
	 * @param bottom igaz�t�s lent
	 * @param right igaz�t�s jobbra
	 * @param AXIS a layout ir�nya/eloszl�sa
	 */
	private void setLayoutToPanel(JPanel panel,int top,int left,int bottom, int right,int AXIS) {
		panel.setLayout(new BoxLayout(panel,AXIS));
		panel.setBorder(new EmptyBorder(top,left,bottom,right));
	}
	
	/**
	 * Inicializ�lja a param�terk�nt kapott labelt
	 * @param label a form�zand� label
	 * @param ToolTipText "good to know" sz�veg / ha van
	 */
	private void defineLabel(JLabel label,String ToolTipText) {
		this.setBackAndForeGround(label);
		if(ToolTipText!=null) label.setToolTipText(ToolTipText);
	}
	
	/**
	 * be�ll�tja a param�terk�nt kapott textfield-et
	 * @param textField form�zand� field
	 * @param listener Action listener amit az adott fieldhez kell adni
	 * @see ActionListener
	 */
	private void defineTextField(JTextField textField, ActionListener listener) {
		textField.setHorizontalAlignment(labelAlignment);
		this.setBackAndForeGround(textField);
		textField.addActionListener(listener);
	}
	
	/**
	 * Bels� oszt�ly ami implement�lja az ActionListenr interface-t 
	 * A mez� v�ltoz�sakor, ami enter le�t�st jel�l , h�v�dik meg �s illeszti �ssze a filterer seg�ts�g�vel
	 * a sz�rend� adatokat majd �tadja a t�bl�nak sz�r�sre.
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
				//ha d�tumot �s/vagy intervallumot keres�nk
				if(!fromDateText.isEmpty() || !toDateText.isEmpty()) {
					
						filterer.add(fromDateText, 2, "date");
						filterer.add(toDateText, 3, "date");
					}
			}catch(Exception error) {
				mainFrame.showDialog(error.getMessage(), "Keres�s Hiba!", "error");
			}
			
			docTable.addRowFilter(filterer.getRowFilter());
		}
	}
}
