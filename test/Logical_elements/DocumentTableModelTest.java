package Logical_elements;

import org.junit.Before;
import org.junit.Test;

import java.awt.Desktop;
import java.util.ArrayList;

import org.junit.Assert;

/**
 * A Table modell hez tartozó funkciók ellenõrzése és vizsgálata
 * @author nagyerik99
 * 
 */
public class DocumentTableModelTest {
	Desktop d;
	DocumentTableModel modell;
	Document doc1,doc2,doc3,doc4;
	String[] val1,val2,val3,val4;
	
	@Before
	public void setUp() {
		d=Desktop.getDesktop();
		modell = new DocumentTableModel();
		val1=new String[] {"testItem1","testType1","2020-11-27","2028-01-01"};
		val2=new String[] {"testItem2","testType2","2010-01-27","2018-01-01"};
		val3=new String[] {"testItem3","testType3","2005-05-27","2020-06-22"};
		val4=new String[] {"testItem4","testType4","1999-03-25","2007-11-19"};
		doc1= new Document(val1);
		doc2= new Document(val2);
		doc3= new Document(val3);
		doc4= new Document(val4);
	}
	
	/**
	 * addRow és removeSelectedRow vizsgálata
	 */
	@Test
	public void testAddRemoveRow() {
		Assert.assertEquals(0,modell.getRowCount(),0);
		modell.addRow(doc1);
		Assert.assertEquals(1,modell.getRowCount(), 0);
		modell.addRow(doc2);
		modell.addRow(doc3);
		modell.addRow(doc4);
		//értékeggyezés vizsgálata
		Assert.assertArrayEquals(doc1.toObjectArray(),modell.getRowData(doc1.getDocID()).toObjectArray());
		//törlés
		modell.removeSelectedRow(doc1.getDocID(), 1);
		modell.removeSelectedRow(doc4.getDocID(), 2);
		//törlés utáni elemszám vizsgálat
		Assert.assertEquals(2,modell.getRowCount(),0);
		//összes elem törlése és modell vizsgálata
		modell.clearAll();
		Assert.assertEquals(0,modell.getRowCount(),0);
	}
	
	/**
	 * EditRow kivételkezelésének vizsgálata
	 * Exception-t dob hiszen nem létezõ indexnél akarunk elemet cserélni.
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testEditRow() throws Exception {
		modell.addRow(doc1);
		modell.addRow(doc2);
		modell.addRow(doc3);
		modell.addRow(doc4);
		Document newdoc = new Document(new String[] {"testedit","testtype","1111-11-11" ,"2222-01-01"});
		modell.editRow(newdoc, 2, "testNull");
	}
	
	/**
	 * Sikeres szerkesztés vizsgálat
	 * Ha létezõ indexel hívjuk meg az editRow függvényt
	 * és az értékváltozás vizsgálata
	 * @throws Exception nem létezõ index
	 */
	@Test
	public void testEditRow2() throws Exception{
		modell.addRow(doc2);
		modell.addRow(doc3);
		String id = doc2.getDocID();
		Document newdoc = new Document(new String[] {id,"testtype","1111-11-11" ,"2222-01-01"});
		modell.editRow(newdoc, 0, id);
		Assert.assertTrue(modell.idMatch(id));
		Assert.assertEquals(2, modell.getRowCount());
		Document doc = new Document(new String[] {"newid","newtype","2010-06-02","2020-05-05"});
		modell.editRow(doc, 1, doc3.getDocID());
		Assert.assertEquals(2,modell.getRowCount());
		Assert.assertArrayEquals(doc.toObjectArray(),modell.getRowData(doc.getDocID()).toObjectArray());
	}
	
	/**
	 * Nemlétezõ Dokumentumindex-hez tartozó fájl megnyitása
	 * Exception dobás
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testOpenDoc() throws Exception {
		modell.openDoc("nullDocID",d);
	}
	/**
	 * Fájl megnyitás vizsgálata
	 * Nincs csatolt fájl így Excception hibát kell dobnia
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testOpenDoc2()throws Exception{
		modell.addRow(doc1);
		modell.openDoc(doc1.getDocID(),d);
	}
	
	/**
	 * A fájl mentésének és betöltésének vizsgálata
	 * @throws Exception
	 */
	@Test
	public void SaveAndLoadTest() throws Exception {
		modell.addRow(doc1);
		modell.addRow(doc2);
		modell.addRow(doc3);
		modell.addRow(doc4);
		ArrayList<String> ids = new ArrayList<String>();
		ids.add(doc1.getDocID());
		ids.add(doc4.getDocID());
		modell.saveFile("test.ser",ids);
		modell.loadFile("test.ser");
		Assert.assertArrayEquals(doc1.toObjectArray(),modell.getRowData(doc1.getDocID()).toObjectArray());
		Assert.assertArrayEquals(doc4.toObjectArray(),modell.getRowData(doc4.getDocID()).toObjectArray());
		Assert.assertEquals(2, modell.getRowCount());
	}
	
	
}
