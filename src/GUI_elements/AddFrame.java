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

public class AddFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	private static ImageIcon icon = new ImageIcon("D:\\Files and Stuffs\\Eclipse_workspace\\Document_manager_HF\\view\\addFile.png");
	private JButton save,cancel,addFile;
	private JPanel centerPanel,labelPanel,buttonPanel,helpFilePanel;
	private JTextField name, type;
	private JDatePickerImpl fromDatePicker, toDatePicker;
	private JDatePanelImpl  fromDatePanel, toDatePanel;
	private JFileChooser fileChooser;
	private String selectedFile;
	private UtilDateModel fromDateModel,toDateModel;
	private JLabel fileLabel,nameLabel,typeLabel,fromDateLabel,toDateLabel, selFileLabel;
	
	private Database_frame mainFrame;
	private DocumentTable docTab;
	private String oldID,req="Kötelezõen Kitöltendõ mezõ!";
	private static Color 
			backGroundColor,
			foreGroundColor = new Color(210,241,250),
			reqFieldColor = new Color(247, 134, 134),
			buttonColor=new Color(0,74,79);
	
	/**
	 * A JDialog típusú AddFrame osztály konstruktora
	 * @param main a Fõ ablak(Database_frame)
	 * @param table  DocumentTable típusú változó az Adatokat tároló Táblázat
	 * @param toEdit boolean típusú változó amely meghatározza, hogy szerkesztésre vagy új elem létrehozására lesz az ablak inicializálva
	 */
	public AddFrame(Database_frame main, DocumentTable table, boolean toEdit){
		super(main,"Új elem hozzáadása",true);
		docTab=table;
		mainFrame=main;
		backGroundColor=mainFrame.getBackground();
		
		//Dialog alapparaméterek beállítása
		this.setIconImage(icon.getImage());
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
	 * @param comp JComponent típus
	 * @param backGround Color típus a háttérnek választott szín
	 * @param foreGround Color típus a betûszínnek váalsztott szín
	 */
	private void setBackAndForeGround(JComponent comp,Color backGround,Color foreGround) {
		comp.setBackground(backGround);
		comp.setForeground(foreGround);
	}
	
	/**
	 * Ha szerkesztésre lett létrehozva az ablak, akkor betölti a kiválasztott Dokumentum adatait
	 * a megfelelõ mezõkbe
	 */
	protected void loadValue() {
		Document data = docTab.getSelectedRowData();
		//Ha esetleg nem lenne adat
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
	 * @return booelan Igaz a visszatérési értéke, ha sikeres volt a Dokumentum felvétele
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
	 * @return boolean Igaz a visszatérési értéke, ha sikeres volt a Dokumentum szerkesztése
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
	 * @return String[]
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
	 *
	 */
	private class NameFieldVerifier extends InputVerifier{

		@Override
		public boolean verify(JComponent input) {
			JTextField id = (JTextField)input;
			boolean result=false;
			if(!id.getText().isEmpty() && !id.getText().equals(oldID)) {
				result = docTab.checkIDValidity(id.getText());
				
			}else if(id.getText().equals(oldID)) {
				result = true;
			}
			
			AddFrame.this.save.setEnabled(result);
			return result;
		}
		
	}
	
	/**
	 * AddFrameButtons belsõ osztály amely implementálja az ActionListener Interface-t
	 * A Mentés és a Mégse gombok megnyomása esetén hívódik az actionPerformed override-olt függvénye
	 * Belsõ paraméterektõl függõen vagy Elment/Szerkeszt vagy Bezárja az Ablakot
	 * @author nagyerik99
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
