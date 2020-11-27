package GUI_elements;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * 
 * @author nagye
 *
 */
public class DateLabelFormatter extends AbstractFormatter {
	private static final long serialVersionUID = 1L;
	/**
	 * a formázás patternje
	 */
	private String pattern ="yyyy-MM-dd";
	/**
	 * a formázó amely a formázást végzi a pattern mintájára
	 */
	private SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	
	
	@Override
	public Object stringToValue(String text) throws ParseException {
		return formatter.parseObject(text);
	}

	@Override
	public String valueToString(Object value) throws ParseException {
		if(value!=null) {
			Calendar cal = (Calendar) value;
			return formatter.format(cal.getTime());
		}
		return "";
	}
	
	/**
	 * Date típusúra formázza a kapott String-et
	 * @throws ParseException ha a formázás sikertelen
	 * @param from a formázandó dátum String formátumban
	 * @return Date típusú formázott érték
	 * @see ParseException
	 */
	public Date formatDate(String from) throws ParseException {
		Date result=null;
		result = formatter.parse(from);
		return result;
	}
	
	/**
	 * Checkolja, hogy a kapott két dátum valid intervallumot határoz-e meg.
	 * @param from a kezdés dátuma String-ként meghatározva.
	 * @param to a befejezés dátuma String-ként meghatározva.
	 * @return Igaz, ha valid az intervallum, egyébként hamis
	 * @throws ParseExcepton ha a formázás nem sikerült.
	 */
	public boolean isValidDateInterval(String from, String to) throws ParseException{
		if(from.isEmpty() || to.isEmpty()) return false;
		
			Date fromDate = this.formatDate(from);
			Date toDate = this.formatDate(to);
			
			
			return !(toDate.before(fromDate)) || (toDate==fromDate);
	}

}
