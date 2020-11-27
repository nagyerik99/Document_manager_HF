package GUI_elements;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdatepicker.impl.*;

import Logical_elements.Document;

/**
 * @author nagyerik99
 * @JDialog típusú osztály, amely az adatok felvételének/szerkesztésének vizuális megjelenítéséért felel.
 */
public class AddFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	/**
	 * típusú nyomógombok a mentés,kilépés, és fájlválasztó megnyitására
	 */
	private JButton save,cancel,addFile;
	/**
	 * típus panelek a megjelenítés formázására.
	 */
	private JPanel centerPanel,labelPanel,buttonPanel,helpFilePanel;
	/**
	 * Beviteli mezõ
	 */
	private JTextField name, type;
	/*
	 * jdatepicker jar fájl belsõ osztálya.
	 * A dátum kiválasztása grafikusan.
	 */
	private JDatePickerImpl fromDatePicker, toDatePicker;
	/**
	 * jdatepicker jar fájl belsõ osztálya.
	 * A kiválasztott dátum megjelenítését végzi.
	 */
	private JDatePanelImpl  fromDatePanel, toDatePanel;
	/**
	 * a csatolandó fájl kiválasztására fájlválasztó
	 */
	private JFileChooser fileChooser;
	/**
	 *  kiválasztott fájl pathja, ha van csatova egyébként null
	 */
	private String selectedFile;
	/**
	 * 	jdatepicker jar fájl belsõ osztálya.
	 *  A dátumválasztó mezõk értékéhez modell.
	 */
	private UtilDateModel fromDateModel,toDateModel;
	/**
	 * label a szöveges tájékoztatás megjelenítésére
	 */
	private JLabel fileLabel,nameLabel,typeLabel,fromDateLabel,toDateLabel, selFileLabel;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 * A Dokumentumok megjelenítését végzõ tábla
	 * @see DocumentTable
	 */
	private DocumentTable docTab;
	/**
	 * statikus szöveg tag:
	 * @oldID a Szerkesztés esetén betöltött dokumnetum azonosítója
	 * @req a megjelenítendõ szöveg kötelezõ kitöltés esetén.
	 */
	private String oldID,req="Kötelezõen Kitöltendõ mezõ!";
	private static Color 
			backGroundColor,
			foreGroundColor = new Color(210,241,250),
			reqFieldColor = new Color(247, 134, 134),
			buttonColor=new Color(0,74,79);
	
	/**
	 * A JDialog típusú AddFrame osztály konstruktora
	 * létrehozza a dialog ablakot és a hozzátartozó elemeket a meghatározott forma szerint.
	 * @param main @Database_frame típúsú fõablak referenciája
	 * @param table @DocumentTable típusú változó az Adatokat tároló Táblázat
	 * @param toEdit @boolean típusú változó amely meghatározza, hogy szerkesztésre vagy új elem létrehozására lesz az ablak inicializálva
	 */
	public AddFrame(DatabaseFrame main, DocumentTable table, boolean toEdit){
		super(main,"Új elem hozzáadása",true);
		docTab=table;
		mainFrame=main;
		backGroundColor=mainFrame.getBackground();
		
		//Dialog alapparaméterek beállítása
		this.setIconImage(DefaultPaths.AddIcon.getImage());
		this.setPreferredSize(new Dimension(450,250));
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setForeground(foreGroundColor);
		this.setBackground(backGroundColor);
		
		//init Panel
		centerPanel = new JPanel();
		buttonPanel = new JPanel();
		labelPanel = new JPanel();
		helpFilePanel = new JPanel();
		
		//Panel színek beállítása
		this.setBackAndForeGround(centerPanel, backGroundColor,foreGroundColor);
		this.setBackAndForeGround(buttonPanel, backGroundColor,foreGroundColor);
		this.setBackAndForeGround(labelPanel, backGroundColor,foreGroundColor);
		this.setBackAndForeGround(helpFilePanel, backGroundColor,foreGroundColor);

		//Panel Layout-ok beállítása
		centerPanel.setLayout(new GridLayout(5,1,10,10));
		centerPanel.setBorder(new EmptyBorder(10,10,10,10));
		
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		labelPanel.setLayout(new GridLayout(5,1,10,5));
		labelPanel.setBorder(new EmptyBorder(10,10,10,5));
		
		ActionListener actionFrame = new AddFrameButtons();
		
		//TextField-ek initje
		name = new JTextField("",20);
		name.setInputVerifier(new NameFieldVerifier());
		this.setBackAndForeGround(name, backGroundColor, foreGroundColor);
		
		type = new JTextField("",20);
		this.setBackAndForeGround(type, backGroundColor, foreGroundColor);
		
		//DatePickers init
		fromDateModel = new UtilDateModel();
		toDateModel = new UtilDateModel();
		
		Properties dateproperties = new Properties();
		dateproperties.put("text.today", "Ma");
		dateproperties.put("text.month", "Hónap");
		dateproperties.put("text.year", "Év");
		
		fromDatePanel = new JDatePanelImpl(fromDateModel, dateproperties);
		fromDatePicker = new JDatePickerImpl(fromDatePanel,new DateLabelFormatter());
		
		toDatePanel = new JDatePanelImpl(toDateModel, dateproperties);
		toDatePicker = new JDatePickerImpl(toDatePanel,new DateLabelFormatter());
		
		//FájlVálasztó init
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents *.pdf","pdf"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		//labelek initjei 
		fileLabel=new JLabel("Csatolmány:");
		this.setBackAndForeGround(fileLabel, backGroundColor,foreGroundColor);
		
		nameLabel = new JLabel("Azonosító:*");
		this.setBackAndForeGround(nameLabel, backGroundColor,reqFieldColor);
		nameLabel.setToolTipText(req);
		
		typeLabel = new JLabel("Típus:");
		this.setBackAndForeGround(typeLabel,backGroundColor,foreGroundColor);
		
		fromDateLabel = new JLabel("Kezdete:*");
		this.setBackAndForeGround(fromDateLabel, backGroundColor,reqFieldColor);
		fromDateLabel.setToolTipText(req);
		
		toDateLabel = new JLabel("Vége:*");
		this.setBackAndForeGround(toDateLabel, backGroundColor,reqFieldColor);
		toDateLabel.setToolTipText(req);
		
		selFileLabel = new JLabel("Fájl: ");
		this.setBackAndForeGround(selFileLabel, backGroundColor,foreGroundColor);
		
		//Button init
		save = new JButton("Mentés");
		save.addActionListener(actionFrame);
		save.setActionCommand("add");
		this.setBackAndForeGround(save, buttonColor,foreGroundColor);
		
		cancel = new JButton("Mégse");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(actionFrame);
		this.setBackAndForeGround(cancel, buttonColor,foreGroundColor);

		addFile = new JButton("Fájl hozzáadása");
		addFile.setActionCommand("OpenFile");
		addFile.addActionListener(actionFrame);
		this.setBackAndForeGround(addFile, buttonColor,foreGroundColor);

		//Elemek Panelhez csatolása
		labelPanel.add(nameLabel);
		labelPanel.add(typeLabel);
		labelPanel.add(fromDateLabel);
		labelPanel.add(toDateLabel);
		labelPanel.add(fileLabel);

		centerPanel.add(name);
		centerPanel.add(type);
		centerPanel.add(fromDatePicker);
		centerPanel.add(toDatePicker);
		helpFilePanel.setLayout(new GridLayout(1,2,20,5));
		centerPanel.add(helpFilePanel);
		
		helpFilePanel.add(addFile);
		helpFilePanel.add(selFileLabel);
		
		buttonPanel.add(save);
		buttonPanel.add(cancel);
		
		this.add(labelPanel,BorderLayout.WEST);
		this.add(centerPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		
		//Ha szerkesztésre használjuk a panelt
		if(toEdit) {
			this.loadValue();
		}
		
		this.pack();
	}
	
	/**
	 * Beállítja a Háttér és betûszint a komponensnek
	 * @param comp @JComponent típus
	 * @param backGround @Color típus a háttérnek választott szín
	 * @param foreGround @Color típus a betûszínnek váalsztott szín
	 * @see JComponent
	 * @see Color
	 */
	private void setBackAndForeGround(JComponent comp,Color backGround,Color foreGround) {
		comp.setBackground(backGround);
		comp.setForeground(foreGround);
	}
	
	/**
	 * Ha szerkesztésre lett létrehozva az ablak, akkor betölti a kiválasztott Dokumentum adatait
	 * a megfelelõ mezõkbe
	 * Ha null értékû a kiválasztott elem(vagyis nem lett kiválasztva elem),
	 * akkor befejezi a mûveeletvégzést
	 */
	protected void loadValue() {
		Document data = docTab.getSelectedRowData();
		if(data == null) {
			this.dispose();
		}
		//Document adatainak betöltése
		name.setText(data.getDocID());
		type.setText(data.getType());
		oldID = data.getDocID();
		Date fromDate = Date.from(data.getFromDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date toDate = Date.from(data.getToDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
		fromDateModel.setValue(fromDate);
		toDateModel.setValue(toDate);
		if(data.getFile()!=null) {
			selFileLabel.setText(data.getFile().getName());
			selectedFile = data.getFile().getAbsolutePath();
			fileChooser.setSelectedFile(new File(selectedFile));
		}
	}
	
	/**
	 *  Validáltatja a megadott adatokat és ha Valid akkor összeállítja a Felvételere küldendõ Dokumentumot
	 * @return @boolean típusú Igaz, ha a visszatérési értéke, ha sikeres volt a Dokumentum felvétele,
	 * egyébként Hamis
	 */
	private boolean addDocumentToTable() {
		try {
			validateData();//ha nem dob exception akkor minden adat megfelelõ
			String[] result = createString();
			docTab.addRow(result);
			return true;
		}catch(Exception e) {
			mainFrame.showDialog(e.getMessage(), "Adatok kitöltése","error");
			return false;
		}
	}
	
	/**
	 * Validáltatja a megadott adatokat és ha Valid akkor összeállítja a Szerkesztésre küldendõ Dokumentumot
	 * @return boolean típusú Igaz, ha a visszatérési értéke, ha sikeres volt a Dokumentum szerkesztése
	 * egyébként Hamis
	 */
	private boolean updateDocumentAtTable() {
		try {
			validateData();
			String[] result = createString();
			Document edited = new Document(result);
			docTab.editRow(edited,oldID);
			return true;
		}catch(Exception e) {
			mainFrame.showDialog(e.getMessage(), "Adatok kitöltése","error");
			return false;
		}
	}
	
	/**
	 * Visszaadja a Dialog mezõinek adataiból összeállított String-tömböt.
	 * @return @String[] típusú tömb az adatokból.
	 * @see @String
	 */
	private String[] createString() { 
		String docname = name.getText();
		String doctype = type.getText();
		String fromDate = fromDatePicker.getJFormattedTextField().getText();
		String toDate = toDatePicker.getJFormattedTextField().getText();
		String[] result;
		result = new String[] {
				docname,doctype,fromDate,
				toDate,selectedFile};
		return result;
	}
	
	/**
	 * Validálja az ablak mezõinek értékét.
	 * @throws @Exception típusú kivételt dob, ha valamelyik feltétel enm volt megfelelõ a validáláshoz.
	 * @see Exception
	 */
	private void validateData() throws Exception {
		DateLabelFormatter formatter = new DateLabelFormatter();
		String[] validate = createString();
		boolean validInterval = formatter.isValidDateInterval(validate[2], validate[3]);
		
		if(!validInterval) throw new Exception("Kérem adjon meg megfelelõ dátum intervallumot!");
		
		if(validate[0].isEmpty()) throw new Exception("Kérem minden kötelezõen kitöltendõ mezõt tölstsön ki!");
		
	}
	
	/**
	 * 
	 * @author nagyerik99
	 * @InputVerifier extension
	 * A megnevezés/azonosító mezõ validálására egy extension.
	 * Levizsgálja, hogy a megadott id/név szerepel e már a modellben/táblában és
	 * ha igen akkor gátolja a fájl/dokumentum mentésést
	 * @see InputVerifier
	 */
	private class NameFieldVerifier extends InputVerifier{
		private boolean result=false;

		@Override
		public boolean verify(JComponent input) {
			JTextField id = (JTextField)input;
			
			if(!id.getText().isEmpty() && !id.getText().equals(oldID)) {
				result = docTab.checkIDValidity(id.getText());
				
			}else if(id.getText().equals(oldID)) {
				result = true;
			}
			
			save.setEnabled(result);
			return result;
		}
		
	}
	
	/**
	 * AddFrameButtons belsõ osztály amely implementálja az ActionListener Interface-t
	 * @ActionListener  interfészt implementál.
	 * A Mentés és a Mégse gombok megnyomása esetén hívódik az actionPerformed override-olt függvénye
	 * Belsõ paraméterektõl függõen vagy Elment/Szerkeszt vagy Bezárja az Ablakot
	 * @author nagyerik99
	 * @see ActionListener
	 *
	 */
	private class AddFrameButtons implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("cancel")) {
				AddFrame.this.dispose();
			}else if(e.getActionCommand().equals("add")) {
				boolean result =false;
				if(oldID!=null) {
					result= updateDocumentAtTable();
				}else {
					result =addDocumentToTable();	
				}
				
				if(result) AddFrame.this.dispose();
				
			}else if(e.getActionCommand().equals("OpenFile")){
				int returnval = fileChooser.showOpenDialog(AddFrame.this);
				if(returnval==JFileChooser.APPROVE_OPTION) {
					selFileLabel.setText("Fájl: "+fileChooser.getSelectedFile().getName());
					selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
				}else if(returnval ==JFileChooser.CANCEL_OPTION) {
					selectedFile = null;
					selFileLabel.setText("Fájl: ");
				}
			}
		}
	}	
}
