package Logical_elements;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.*;

public class DateMatcher{
	private static Pattern DOTTED_DATE_PATTERN = Pattern.compile("\\d{4,}\\.\\d{2,}\\.\\d{2,}$");
	private static Pattern BACKSLASH_DATE_PATTERN = Pattern.compile("\\d{4,}\\/\\d{2,}\\/\\d{2,}$");
	private static Pattern DOTTED_INTERVAL = Pattern.compile("\\d{4,}\\.\\d{2,}\\.\\d{2,}\\-\\d{4,}\\.\\d{2,}\\.\\d{2,}$");
	private static Pattern BACKSLASH_INTERVAL = Pattern.compile("\\d{4,}\\/\\d{2,}\\/\\d{2,}\\-\\d{4,}\\/\\d{2,}\\/\\d{2,}$");
	private static Pattern ONLY_YEAR_PATTERN = Pattern.compile("\\d{4}\\-\\d{4}$");
	private static String dottedFormat = "yyyy.MM.dd";
	private static String backslashFormat = "yyyy/MM/dd";
	private static DateTimeFormatter dotFormatter = DateTimeFormatter.ofPattern(dottedFormat);
	private static DateTimeFormatter backslashFormatter = DateTimeFormatter.ofPattern(backslashFormat);
	private String dateType;
	
	public boolean matches(String date,String type) {
		if(type.equals("dotted")) {
			return DOTTED_DATE_PATTERN.matcher(date).matches();
		}else if(type.equals("backslash")) {
			return BACKSLASH_DATE_PATTERN.matcher(date).matches();
		}
		
		return false;
	}
	
	public boolean matchInterVal(String interval, String type) {
		if(type.equals("onlyyear")) {
			return ONLY_YEAR_PATTERN.matcher(interval).matches();
		}
		if(type.equals("dotted")) {
			return DOTTED_INTERVAL.matcher(interval).matches();
		}else if(type.equals("backslash")) {
			return BACKSLASH_INTERVAL.matcher(interval).matches();
		}
		
		return false;
	}
	
	public LocalDate format(String from) throws ParseException{
		LocalDate result = null;
		if(dateType.equals("dotted")) {
			result = LocalDate.parse(from,dotFormatter);
		}else if(dateType.equals("backslash")) {
			result = LocalDate.parse(from,backslashFormatter);
		}
		return result;
	}
	
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
