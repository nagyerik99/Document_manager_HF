package Logical_elements;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Teszteset a filterek valid param�terrel t�rt�n� l�trehoz�s�ra
 * enn�l a tesztesetn�l minden param�terrel le kell tudni futnia Exception hiba n�lk�l a k�dnak
 * @author nagyerik99
 *
 */
@RunWith(value=Parameterized.class)
public class DocumentFiltererTestValidData {
	private DocumentFilterer filterer;
	private String date;
	
	public DocumentFiltererTestValidData(String str) {
		date=str;
		filterer = new DocumentFilterer();
	}
	
	@Parameters
	public static Collection<String> data(){
		return Arrays.asList("2020.11.10",
							 "2000/06/02",
							 "2020.10.01-2020.11.01",
							 "2010/06/02-2020/01/01");
	}
	
	
	@Test
	public void testAdd()throws Exception {
		filterer.add(date, 2, "date");
	}
}
		
