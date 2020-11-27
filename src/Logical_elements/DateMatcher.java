package Logical_elements;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

/**
 * Osztály amely az egyes Stringek vizsgálatát valósítja meg dátumvizsgálat szempontjából patternekkel
 * és matcher-ekkel
 * @author nagyerik99
 */
public class DateMatcher{
	/**
	 * pontozott dátum formátum/pattern  yyyy.MM.dd
	 */
	private static Pattern DOTTED_DATE_PATTERN = Pattern.compile("\\d{4,}\\.\\d{2,}\\.\\d{2,}$");
	/**
	 * perjeles dátum formátum/pattern yyyy/MM/dd
	 */
	private static Pattern BACKSLASH_DATE_PATTERN = Pattern.compile("\\d{4,}\\/\\d{2,}\\/\\d{2,}$");
	/**
	 * pontozott dátum intervallum pattern yyyy.MM.dd-yyyy.MM.dd
	 */
	private static Pattern DOTTED_INTERVAL = Pattern.compile("\\d{4,}\\.\\d{2,}\\.\\d{2,}\\-\\d{4,}\\.\\d{2,}\\.\\d{2,}$");
	/**
	 * perjeles dátum intervallum pattern yyyy/MM/dd-yyyy/MM/dd
	 */
	private static Pattern BACKSLASH_INTERVAL = Pattern.compile("\\d{4,}\\/\\d{2,}\\/\\d{2,}\\-\\d{4,}\\/\\d{2,}\\/\\d{2,}$");
	/**
	 * pontozott formátum a fordításhot/parse-hez
	 */
	private static String dottedFormat = "yyyy.MM.dd";
	/**
	 * perjeles formátum a fordításhot/parse-hez
	 */
	private static String backslashFormat = "yyyy/MM/dd";
	/**
	 * pontozott dátum formátum formázó/fordító
	 */
	private static DateTimeFormatter dotFormatter = DateTimeFormatter.ofPattern(dottedFormat);
	/**
	 * perjeles dátum formátum formázó/fordító
	 */
	private static DateTimeFormatter backslashFormatter = DateTimeFormatter.ofPattern(backslashFormat);
	/**
	 * a matchelt dátum formátuma késõbbi formázásra/fordításra
	 */
	private String dateType;
	
	/**
	 * megvizsgálja, hogy a megadott string megeyezeik e ameghatározott formának
	 * @param date a vizsgálandó szöveg /String
	 * @param type a  meghatározott formátum
	 * @return Igaz, ha az adott formátumra egyezik a kapott szöveg patternje
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
	 * Megvizsgálja a kapott String-et, hogy megegyezik e a formátuma a kpott
	 * típusra meghatározott dárum intervallum pattern-el
	 * @param interval a vizsgálandó szövegminta
	 * @param type a meghatároztt típus
	 * @return Igaz, ha illeszkedik a string a meghatároztt pattern-re
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
	 * formázza a kapott stringet az elõzetesen validálásnál beállított dateType alapján.
	 * @param from a formázandó szöveg
	 * @return A formázott Dátum LocalDate formátumban
	 * @throws ParseException ha a formázás nem sikerülne
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
	 * Megvizsgálja a kapott stringet, hogy illeszkedik e vagy a dátum patternekre
	 * vagy pedig a dátum intervallum patternekre. Ha egyezik akkor beállítja a dateType változóját
	 * az egyezõ minta azonosítójára.
	 * @param dateText a vizsgálandó szöveg 
	 * @return Igaz, ha van egyezés valamelyik pattern-re
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
