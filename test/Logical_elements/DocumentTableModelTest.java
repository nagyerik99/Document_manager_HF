package Logical_elements;

import org.junit.Before;
import org.junit.Test;

import java.awt.Desktop;
import java.util.ArrayList;

import org.junit.Assert;

/**
 * A Table modell hez tartoz� funkci�k ellen�rz�se �s vizsg�lata
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
	 * addRow �s removeSelectedRow vizsg�lata
	 */
	@Test
	public void testAddRemoveRow() {
		Assert.assertEquals(0,modell.getRowCount(),0);
		modell.addRow(doc1);
		Assert.assertEquals(1,modell.getRowCount(), 0);
		modell.addRow(doc2);
		modell.addRow(doc3);
		modell.addRow(doc4);
		//�rt�keggyez�s vizsg�lata
		Assert.assertArrayEquals(doc1.toObjectArray(),modell.getRowData(doc1.getDocID()).toObjectArray());
		//t�rl�s
		modell.removeSelectedRow(doc1.getDocID(), 1);
		modell.removeSelectedRow(doc4.getDocID(), 2);
		//t�rl�s ut�ni elemsz�m vizsg�lat
		Assert.assertEquals(2,modell.getRowCount(),0);
		//�sszes elem t�rl�se �s modell vizsg�lata
		modell.clearAll();
		Assert.assertEquals(0,modell.getRowCount(),0);
	}
	
	/**
	 * EditRow kiv�telkezel�s�nek vizsg�lata
	 * Exception-t dob hiszen nem l�tez� indexn�l akarunk elemet cser�lni.
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
	 * Sikeres szerkeszt�s vizsg�lat
	 * Ha l�tez� indexel h�vjuk meg az editRow f�ggv�nyt
	 * �s az �rt�kv�ltoz�s vizsg�lata
	 * @throws Exception nem l�tez� index
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
	 * Neml�tez� Dokumentumindex-hez tartoz� f�jl megnyit�sa
	 * Exception dob�s
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testOpenDoc() throws Exception {
		modell.openDoc("nullDocID",d);
	}
	/**
	 * F�jl megnyit�s vizsg�lata
	 * Nincs csatolt f�jl �gy Excception hib�t kell dobnia
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testOpenDoc2()throws Exception{
		modell.addRow(doc1);
		modell.openDoc(doc1.getDocID(),d);
	}
	
	/**
	 * A f�jl ment�s�nek �s bet�lt�s�nek vizsg�lata
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
