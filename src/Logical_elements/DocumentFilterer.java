package Logical_elements;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.RowFilter;

/**
 * 
 * @author nagye
 *
 */
public class DocumentFilterer {
	/**
	 * DateMatcher attrubútum/változó a vlidálásra és formázásra
	 * @see DateMatcher
	 */
	private DateMatcher dateMatcher;
	/**
	 * a dateFilter létrehozásánál szükséges String tömb
	 */
	private static String[] when = new String[] {"after","before"};
	/**
	 * Az összes filtert tartalmazó filter lista
	 */
	private ArrayList<RowFilter<DocumentTableModel,Integer>> filterList;
	
	/**
	 * Default Konstruktor ami inicializálja a dateMAtcher-t és a filterList-et
	 */
	public DocumentFilterer(){
		dateMatcher = new DateMatcher();
		filterList = new ArrayList<RowFilter<DocumentTableModel,Integer>>();
	}
	
	/**
	 * kiüríti a szûrõfeltétel listát
	 */
	public void clearFilterer() {
		filterList.clear();
	}
	
	/**
	 * hozzáad egy új filtert a filterListhez a meghatározott feltételek alapján.
	 * Validál és ha minden dolog megfelelõ akkor létrehozza a szûrõt amit utána
	 * felvesz a filterListbe.
	 * @param text  szûrendõ szöveg/dátum a típustól meghatározva függõen
	 * @param column a szûrendõ oszlop azonosítója.
	 * @param field a filed típusa amit szûrünk
	 * @text a szûrendõ field egyszerû szöveg
	 * @date a szûrendõ field dátum
	 * @throws Exception ha a mezõ nem beazonosítható vagy ha nem megfelelõ a megadott dátum formátum
	 */
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
	
	/**
	 * Létrehoz egyetlen filtert and kapcsolattal a afilterListben meghatároztt elemkebõl
	 * @return filter a filterList elemeibõl
	 */
	public RowFilter<DocumentTableModel,Integer> getRowFilter(){
		return RowFilter.andFilter(filterList);
	}
	
	/**
	 * létrehoz egy szövegeket szûrõ filtert String filtert
	 * @param regex a szûrendõ/keresendõ szöveg/szövegrészlet
	 * @param where az oszlop/column neve ahova a filter kiértékelõdik
	 * @return String filterer a megadott szöveg/szövegrészletbõl
	 */
	private RowFilter<DocumentTableModel,Integer> createStringFilter(String regex, int where){
		return RowFilter.regexFilter(regex,where);
	}
	
	/**
	 * Létrehoz egy LocalDate típusú dátumokat szûrõ szûrõt/filterert a megadott paraméterek alapján
	 * @param date a szûrendõ dátum
	 * @param where melyik oszlopra értékelendõ ki a filter
	 * @param type a szûrési feltétel
	 * 	 	@before azon dátumok szûrése amelyek korábbiak mint a date-ben meghatározott dátum
	 * 		@after azon dátumok szûrése amelyek késõbbiek mint a dateB-ben meghatározott dátum

	 * @return LocalDate típusú elemekt szûrõ filter
	 */
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
	
	/**
	 * A kapott dátumokat tartalmazó String tömböt formázza
	 * létrehozza a dátum szûrõt a megfelelõ oszlopra
	 * majd hozzáadja a filterListhez
	 * @param dates a szûrendõ dátumok
	 * @param where melyik oszlopra/column-ra
	 * @throws ParseException ha a fordítás nem volt sikeres.
	 */
	private void addDateFilter(String[] dates, int where) throws ParseException {
		for(int i =0; i<dates.length;i++) {
			if(!dates[i].isEmpty()) {
				LocalDate tempDate = dateMatcher.format(dates[i]);
				filterList.add(createDateFilter(tempDate,where,when[i]));
			}
		}
	}
	
	
}
