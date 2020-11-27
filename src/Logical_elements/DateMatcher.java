package Logical_elements;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

/**
 * Oszt�ly amely az egyes Stringek vizsg�lat�t val�s�tja meg d�tumvizsg�lat szempontj�b�l patternekkel
 * �s matcher-ekkel
 * @author nagyerik99
 */
public class DateMatcher{
	/**
	 * pontozott d�tum form�tum/pattern  yyyy.MM.dd
	 */
	private static Pattern DOTTED_DATE_PATTERN = Pattern.compile("\\d{4,}\\.\\d{2,}\\.\\d{2,}$");
	/**
	 * perjeles d�tum form�tum/pattern yyyy/MM/dd
	 */
	private static Pattern BACKSLASH_DATE_PATTERN = Pattern.compile("\\d{4,}\\/\\d{2,}\\/\\d{2,}$");
	/**
	 * pontozott d�tum intervallum pattern yyyy.MM.dd-yyyy.MM.dd
	 */
	private static Pattern DOTTED_INTERVAL = Pattern.compile("\\d{4,}\\.\\d{2,}\\.\\d{2,}\\-\\d{4,}\\.\\d{2,}\\.\\d{2,}$");
	/**
	 * perjeles d�tum intervallum pattern yyyy/MM/dd-yyyy/MM/dd
	 */
	private static Pattern BACKSLASH_INTERVAL = Pattern.compile("\\d{4,}\\/\\d{2,}\\/\\d{2,}\\-\\d{4,}\\/\\d{2,}\\/\\d{2,}$");
	/**
	 * pontozott form�tum a ford�t�shot/parse-hez
	 */
	private static String dottedFormat = "yyyy.MM.dd";
	/**
	 * perjeles form�tum a ford�t�shot/parse-hez
	 */
	private static String backslashFormat = "yyyy/MM/dd";
	/**
	 * pontozott d�tum form�tum form�z�/ford�t�
	 */
	private static DateTimeFormatter dotFormatter = DateTimeFormatter.ofPattern(dottedFormat);
	/**
	 * perjeles d�tum form�tum form�z�/ford�t�
	 */
	private static DateTimeFormatter backslashFormatter = DateTimeFormatter.ofPattern(backslashFormat);
	/**
	 * a matchelt d�tum form�tuma k�s�bbi form�z�sra/ford�t�sra
	 */
	private String dateType;
	
	/**
	 * megvizsg�lja, hogy a megadott string megeyezeik e ameghat�rozott form�nak
	 * @param date a vizsg�land� sz�veg /String
	 * @param type a  meghat�rozott form�tum
	 * @return Igaz, ha az adott form�tumra egyezik a kapott sz�veg patternje
	 */
	public boolean matches(String date,String type) {
		if(type.equals("dotted")) {
			return DOTTED_DATE_PATTERN.matcher(date).matches();
		}else if(type.equals("backslash")) {
			return BACKSLASH_DATE_PATTERN.matcher(date).matches();
		}
		
		return false;
	}
	
	/**
	 * Megvizsg�lja a kapott String-et, hogy megegyezik e a form�tuma a kpott
	 * t�pusra meghat�rozott d�rum intervallum pattern-el
	 * @param interval a vizsg�land� sz�vegminta
	 * @param type a meghat�roztt t�pus
	 * @return Igaz, ha illeszkedik a string a meghat�roztt pattern-re
	 */
	public boolean matchInterVal(String interval, String type) {
		if(type.equals("dotted")) {
			return DOTTED_INTERVAL.matcher(interval).matches();
		}else if(type.equals("backslash")) {
			return BACKSLASH_INTERVAL.matcher(interval).matches();
		}
		
		return false;
	}
	
	/**
	 * form�zza a kapott stringet az el�zetesen valid�l�sn�l be�ll�tott dateType alapj�n.
	 * @param from a form�zand� sz�veg
	 * @return A form�zott D�tum LocalDate form�tumban
	 * @throws ParseException ha a form�z�s nem siker�lne
	 */
	public LocalDate format(String from) throws ParseException{
		LocalDate result = null;
		if(dateType.equals("dotted")) {
			result = LocalDate.parse(from,dotFormatter);
		}else if(dateType.equals("backslash")) {
			result = LocalDate.parse(from,backslashFormatter);
		}
		return result;
	}
	
	/**
	 * Megvizsg�lja a kapott stringet, hogy illeszkedik e vagy a d�tum patternekre
	 * vagy pedig a d�tum intervallum patternekre. Ha egyezik akkor be�ll�tja a dateType v�ltoz�j�t
	 * az egyez� minta azonos�t�j�ra.
	 * @param dateText a vizsg�land� sz�veg 
	 * @return Igaz, ha van egyez�s valamelyik pattern-re
	 */
	public boolean validateDate(String dateText) {
		if(dateText==null) return false;
		
		if(dateText.isEmpty()) return true;

		if(this.matches(dateText,"dotted")) {
			dateType="dotted";
			return true;	
		}else if(this.matches(dateText,"backslash")){
			dateType="backslash";
			return true;
		}else if(this.matchInterVal(dateText,"dotted")){
			dateType = "dotted";
			return true;
		}else if(this.matchInterVal(dateText,"backslash")){
			dateType = "backslash";
			return true;
		}	
		return false;
	}
}
