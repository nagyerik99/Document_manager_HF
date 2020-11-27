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
 * @JDialog t�pus� oszt�ly, amely az adatok felv�tel�nek/szerkeszt�s�nek vizu�lis megjelen�t�s��rt felel.
 */
public class AddFrame extends JDialog{
	private static final long serialVersionUID = 1L;
	/**
	 * t�pus� nyom�gombok a ment�s,kil�p�s, �s f�jlv�laszt� megnyit�s�ra
	 */
	private JButton save,cancel,addFile;
	/**
	 * t�pus panelek a megjelen�t�s form�z�s�ra.
	 */
	private JPanel centerPanel,labelPanel,buttonPanel,helpFilePanel;
	/**
	 * Beviteli mez�
	 */
	private JTextField name, type;
	/*
	 * jdatepicker jar f�jl bels� oszt�lya.
	 * A d�tum kiv�laszt�sa grafikusan.
	 */
	private JDatePickerImpl fromDatePicker, toDatePicker;
	/**
	 * jdatepicker jar f�jl bels� oszt�lya.
	 * A kiv�lasztott d�tum megjelen�t�s�t v�gzi.
	 */
	private JDatePanelImpl  fromDatePanel, toDatePanel;
	/**
	 * a csatoland� f�jl kiv�laszt�s�ra f�jlv�laszt�
	 */
	private JFileChooser fileChooser;
	/**
	 *  kiv�lasztott f�jl pathja, ha van csatova egy�bk�nt null
	 */
	private String selectedFile;
	/**
	 * 	jdatepicker jar f�jl bels� oszt�lya.
	 *  A d�tumv�laszt� mez�k �rt�k�hez modell.
	 */
	private UtilDateModel fromDateModel,toDateModel;
	/**
	 * label a sz�veges t�j�koztat�s megjelen�t�s�re
	 */
	private JLabel fileLabel,nameLabel,typeLabel,fromDateLabel,toDateLabel, selFileLabel;
	/**
	 * A main Ablak
	 * @see DatabaseFrame
	 */
	private DatabaseFrame mainFrame;
	/**
	 * A Dokumentumok megjelen�t�s�t v�gz� t�bla
	 * @see DocumentTable
	 */
	private DocumentTable docTab;
	/**
	 * statikus sz�veg tag:
	 * @oldID a Szerkeszt�s eset�n bet�lt�tt dokumnetum azonos�t�ja
	 * @req a megjelen�tend� sz�veg k�telez� kit�lt�s eset�n.
	 */
	private String oldID,req="K�telez�en Kit�ltend� mez�!";
	private static Color 
			backGroundColor,
			foreGroundColor = new Color(210,241,250),
			reqFieldColor = new Color(247, 134, 134),
			buttonColor=new Color(0,74,79);
	
	/**
	 * A JDialog t�pus� AddFrame oszt�ly konstruktora
	 * l�trehozza a dialog ablakot �s a hozz�tartoz� elemeket a meghat�rozott forma szerint.
	 * @param main @Database_frame t�p�s� f�ablak referenci�ja
	 * @param table @DocumentTable t�pus� v�ltoz� az Adatokat t�rol� T�bl�zat
	 * @param toEdit @boolean t�pus� v�ltoz� amely meghat�rozza, hogy szerkeszt�sre vagy �j elem l�trehoz�s�ra lesz az ablak inicializ�lva
	 */
	public AddFrame(DatabaseFrame main, DocumentTable table, boolean toEdit){
		super(main,"�j elem hozz�ad�sa",true);
		docTab=table;
		mainFrame=main;
		backGroundColor=mainFrame.getBackground();
		
		//Dialog alapparam�terek be�ll�t�sa
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
		
		//Panel sz�nek be�ll�t�sa
		this.setBackAndForeGround(centerPanel, backGroundColor,foreGroundColor);
		this.setBackAndForeGround(buttonPanel, backGroundColor,foreGroundColor);
		this.setBackAndForeGround(labelPanel, backGroundColor,foreGroundColor);
		this.setBackAndForeGround(helpFilePanel, backGroundColor,foreGroundColor);

		//Panel Layout-ok be�ll�t�sa
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
		dateproperties.put("text.month", "H�nap");
		dateproperties.put("text.year", "�v");
		
		fromDatePanel = new JDatePanelImpl(fromDateModel, dateproperties);
		fromDatePicker = new JDatePickerImpl(fromDatePanel,new DateLabelFormatter());
		
		toDatePanel = new JDatePanelImpl(toDateModel, dateproperties);
		toDatePicker = new JDatePickerImpl(toDatePanel,new DateLabelFormatter());
		
		//F�jlV�laszt� init
		fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PDF Documents *.pdf","pdf"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		//labelek initjei 
		fileLabel=new JLabel("Csatolm�ny:");
		this.setBackAndForeGround(fileLabel, backGroundColor,foreGroundColor);
		
		nameLabel = new JLabel("Azonos�t�:*");
		this.setBackAndForeGround(nameLabel, backGroundColor,reqFieldColor);
		nameLabel.setToolTipText(req);
		
		typeLabel = new JLabel("T�pus:");
		this.setBackAndForeGround(typeLabel,backGroundColor,foreGroundColor);
		
		fromDateLabel = new JLabel("Kezdete:*");
		this.setBackAndForeGround(fromDateLabel, backGroundColor,reqFieldColor);
		fromDateLabel.setToolTipText(req);
		
		toDateLabel = new JLabel("V�ge:*");
		this.setBackAndForeGround(toDateLabel, backGroundColor,reqFieldColor);
		toDateLabel.setToolTipText(req);
		
		selFileLabel = new JLabel("F�jl: ");
		this.setBackAndForeGround(selFileLabel, backGroundColor,foreGroundColor);
		
		//Button init
		save = new JButton("Ment�s");
		save.addActionListener(actionFrame);
		save.setActionCommand("add");
		this.setBackAndForeGround(save, buttonColor,foreGroundColor);
		
		cancel = new JButton("M�gse");
		cancel.setActionCommand("cancel");
		cancel.addActionListener(actionFrame);
		this.setBackAndForeGround(cancel, buttonColor,foreGroundColor);

		addFile = new JButton("F�jl hozz�ad�sa");
		addFile.setActionCommand("OpenFile");
		addFile.addActionListener(actionFrame);
		this.setBackAndForeGround(addFile, buttonColor,foreGroundColor);

		//Elemek Panelhez csatol�sa
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
		
		//Ha szerkeszt�sre haszn�ljuk a panelt
		if(toEdit) {
			this.loadValue();
		}
		
		this.pack();
	}
	
	/**
	 * Be�ll�tja a H�tt�r �s bet�szint a komponensnek
	 * @param comp @JComponent t�pus
	 * @param backGround @Color t�pus a h�tt�rnek v�lasztott sz�n
	 * @param foreGround @Color t�pus a bet�sz�nnek v�alsztott sz�n
	 * @see JComponent
	 * @see Color
	 */
	private void setBackAndForeGround(JComponent comp,Color backGround,Color foreGround) {
		comp.setBackground(backGround);
		comp.setForeground(foreGround);
	}
	
	/**
	 * Ha szerkeszt�sre lett l�trehozva az ablak, akkor bet�lti a kiv�lasztott Dokumentum adatait
	 * a megfelel� mez�kbe
	 * Ha null �rt�k� a kiv�lasztott elem(vagyis nem lett kiv�lasztva elem),
	 * akkor befejezi a m�veeletv�gz�st
	 */
	protected void loadValue() {
		Document data = docTab.getSelectedRowData();
		if(data == null) {
			this.dispose();
		}
		//Document adatainak bet�lt�se
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
	 *  Valid�ltatja a megadott adatokat �s ha Valid akkor �ssze�ll�tja a Felv�telere k�ldend� Dokumentumot
	 * @return @boolean t�pus� Igaz, ha a visszat�r�si �rt�ke, ha sikeres volt a Dokumentum felv�tele,
	 * egy�bk�nt Hamis
	 */
	private boolean addDocumentToTable() {
		try {
			validateData();//ha nem dob exception akkor minden adat megfelel�
			String[] result = createString();
			docTab.addRow(result);
			return true;
		}catch(Exception e) {
			mainFrame.showDialog(e.getMessage(), "Adatok kit�lt�se","error");
			return false;
		}
	}
	
	/**
	 * Valid�ltatja a megadott adatokat �s ha Valid akkor �ssze�ll�tja a Szerkeszt�sre k�ldend� Dokumentumot
	 * @return boolean t�pus� Igaz, ha a visszat�r�si �rt�ke, ha sikeres volt a Dokumentum szerkeszt�se
	 * egy�bk�nt Hamis
	 */
	private boolean updateDocumentAtTable() {
		try {
			validateData();
			String[] result = createString();
			Document edited = new Document(result);
			docTab.editRow(edited,oldID);
			return true;
		}catch(Exception e) {
			mainFrame.showDialog(e.getMessage(), "Adatok kit�lt�se","error");
			return false;
		}
	}
	
	/**
	 * Visszaadja a Dialog mez�inek adataib�l �ssze�ll�tott String-t�mb�t.
	 * @return @String[] t�pus� t�mb az adatokb�l.
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
	 * Valid�lja az ablak mez�inek �rt�k�t.
	 * @throws @Exception t�pus� kiv�telt dob, ha valamelyik felt�tel enm volt megfelel� a valid�l�shoz.
	 * @see Exception
	 */
	private void validateData() throws Exception {
		DateLabelFormatter formatter = new DateLabelFormatter();
		String[] validate = createString();
		boolean validInterval = formatter.isValidDateInterval(validate[2], validate[3]);
		
		if(!validInterval) throw new Exception("K�rem adjon meg megfelel� d�tum intervallumot!");
		
		if(validate[0].isEmpty()) throw new Exception("K�rem minden k�telez�en kit�ltend� mez�t t�lsts�n ki!");
		
	}
	
	/**
	 * 
	 * @author nagyerik99
	 * @InputVerifier extension
	 * A megnevez�s/azonos�t� mez� valid�l�s�ra egy extension.
	 * Levizsg�lja, hogy a megadott id/n�v szerepel e m�r a modellben/t�bl�ban �s
	 * ha igen akkor g�tolja a f�jl/dokumentum ment�s�st
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
	 * AddFrameButtons bels� oszt�ly amely implement�lja az ActionListener Interface-t
	 * @ActionListener  interf�szt implement�l.
	 * A Ment�s �s a M�gse gombok megnyom�sa eset�n h�v�dik az actionPerformed override-olt f�ggv�nye
	 * Bels� param�terekt�l f�gg�en vagy Elment/Szerkeszt vagy Bez�rja az Ablakot
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
					selFileLabel.setText("F�jl: "+fileChooser.getSelectedFile().getName());
					selectedFile = fileChooser.getSelectedFile().getAbsolutePath();
				}else if(returnval ==JFileChooser.CANCEL_OPTION) {
					selectedFile = null;
					selFileLabel.setText("F�jl: ");
				}
			}
		}
	}	
}
