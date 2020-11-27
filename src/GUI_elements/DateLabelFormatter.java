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
	 * a form�z�s patternje
	 */
	private String pattern ="yyyy-MM-dd";
	/**
	 * a form�z� amely a form�z�st v�gzi a pattern mint�j�ra
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
	 * Date t�pus�ra form�zza a kapott String-et
	 * @throws ParseException ha a form�z�s sikertelen
	 * @param from a form�zand� d�tum String form�tumban
	 * @return Date t�pus� form�zott �rt�k
	 * @see ParseException
	 */
	public Date formatDate(String from) throws ParseException {
		Date result=null;
		result = formatter.parse(from);
		return result;
	}
	
	/**
	 * Checkolja, hogy a kapott k�t d�tum valid intervallumot hat�roz-e meg.
	 * @param from a kezd�s d�tuma String-k�nt meghat�rozva.
	 * @param to a befejez�s d�tuma String-k�nt meghat�rozva.
	 * @return Igaz, ha valid az intervallum, egy�bk�nt hamis
	 * @throws ParseExcepton ha a form�z�s nem siker�lt.
	 */
	public boolean isValidDateInterval(String from, String to) throws ParseException{
		if(from.isEmpty() || to.isEmpty()) return false;
		
			Date fromDate = this.formatDate(from);
			Date toDate = this.formatDate(to);
			
			
			return !(toDate.before(fromDate)) || (toDate==fromDate);
	}

}
