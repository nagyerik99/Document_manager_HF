package Logical_elements;

import org.junit.Before;
import org.junit.Test;

/**
 * DocumentumFilterer �s a Date Matcher vizsg�lata invalid �rt�kekkel
 * @author nagyerik99
 *
 */
public class DocumentFiltererTestInvalidData {
	String wrongType;
	DocumentFilterer filterer;
	String invalid1,invalid2,invalid3,invalid4,invalid5;
	
	@Before
	public void SetUp() {
		/**
		 * invalid d�tum form�tumok amelyekre a rendszer Exceptionnal v�laszol
		 */
		filterer = new DocumentFilterer();
		invalid1="2020-11-10";
		invalid2="asdasdasda";
		invalid3="2020.1.1";
		invalid4="20.11.11";
		invalid5="10/10/10-20/08/05";
	}
	
	/**
	 * Rossz d�tum t�pusok, az�rt teszteltem �ke k�l�n mert param�teresen az els� ut�n exception miatt kil�p.
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void TestInvalid1() throws Exception{
		filterer.add(invalid1, 3, "date");
	}
	
	@Test(expected=Exception.class)
	public void TestInvalid2() throws Exception{
		filterer.add(invalid2, 3, "date");
	}
	
	@Test(expected=Exception.class)
	public void TestInvalid3() throws Exception{
		filterer.add(invalid3, 3, "date");
	}
	
	@Test(expected=Exception.class)
	public void TestInvalid4() throws Exception{
		filterer.add(invalid4, 3, "date");
	}
	
	@Test(expected=Exception.class)
	public void TestInvalid5() throws Exception{
		filterer.add(invalid5, 3, "date");
	}
	
	/**
	 * rossz t�pus megad�s
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void TestAddWrongType() throws Exception {
		filterer.add(wrongType,3,"wrong");
	}
}
