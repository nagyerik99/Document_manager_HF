package Logical_elements;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.RowFilter;

public class DocumentFilterer {
	private DateMatcher dateMatcher;
	
	private ArrayList<RowFilter<DocumentTableModel,Integer>> filterList;
	
	public DocumentFilterer(){
		dateMatcher = new DateMatcher();
		filterList = new ArrayList<RowFilter<DocumentTableModel,Integer>>();
	}
	
	public void clearFilterer() {
		filterList.clear();
	}
	
	public void add(String text,int column,String field) throws Exception{
		if(field.equals("text")) {
			filterList.add(
					createStringFilter(text,column));
		}else if(field.equals("date")) {
			if(dateMatcher.validateDate(text)) {
				String[] stringDates = text.split("-");
				addDateFilter(stringDates,column);
			}else {
				throw new Exception("Nem megfelelõ Dátumformátum!");
			}
		}else {
			throw new Exception("Nem azonosítható mezõ!");
		}
	}
	
	public RowFilter<DocumentTableModel,Integer> getRowFilter(){
		return RowFilter.andFilter(filterList);
	}
	
	private RowFilter<DocumentTableModel,Integer> createStringFilter(String regex, int where){
		return RowFilter.regexFilter(regex,where);
	}
	
	private RowFilter<DocumentTableModel,Integer> createDateFilter(LocalDate date,int where,String type){
		RowFilter<DocumentTableModel,Integer> dateFilter = new RowFilter<DocumentTableModel,Integer>(){
			@Override
			public boolean include(Entry<? extends DocumentTableModel, ? extends Integer> entry) {
				DocumentTableModel model = entry.getModel();
				LocalDate compare = (LocalDate)model.getValueAt(entry.getIdentifier(),where);
				if(type.equals("after")) {
					return (compare.isAfter(date) || compare.isEqual(date));
				}else if(type.equals("before")) {
					return (compare.isBefore(date) || compare.isEqual(date));
				}
				return false;
			}
		};
		
		return dateFilter;
	}
	
	private void addDateFilter(String[] dates, int where) throws ParseException {
		String[] when = new String[] {"after","before"};
		for(int i =0; i<dates.length;i++) {
			if(!dates[i].isEmpty()) {
				LocalDate tempDate = dateMatcher.format(dates[i]);
				filterList.add(createDateFilter(tempDate,where,when[i]));
			}
		}
	}
	
	
}
