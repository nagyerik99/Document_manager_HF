package GUI_elements;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DateLabelFormatter extends AbstractFormatter {
	private static final long serialVersionUID = 1L;
	
	private String pattern ="yyyy-MM-dd";
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
	
	public Date formatDate(String from) {
		Date result=null;
		try {
			result = formatter.parse(from);
		}catch(ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean isValidDateInterval(String from, String to){
		if(from.isEmpty() || to.isEmpty()) return false;
		
			Date fromDate = this.formatDate(from);
			Date toDate = this.formatDate(to);
			
			
			return !(toDate.before(fromDate)) || (toDate==fromDate);
	}

}
