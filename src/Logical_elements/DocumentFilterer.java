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
	 * DateMatcher attrub�tum/v�ltoz� a vlid�l�sra �s form�z�sra
	 * @see DateMatcher
	 */
	private DateMatcher dateMatcher;
	/**
	 * a dateFilter l�trehoz�s�n�l sz�ks�ges String t�mb
	 */
	private static String[] when = new String[] {"after","before"};
	/**
	 * Az �sszes filtert tartalmaz� filter lista
	 */
	private ArrayList<RowFilter<DocumentTableModel,Integer>> filterList;
	
	/**
	 * Default Konstruktor ami inicializ�lja a dateMAtcher-t �s a filterList-et
	 */
	public DocumentFilterer(){
		dateMatcher = new DateMatcher();
		filterList = new ArrayList<RowFilter<DocumentTableModel,Integer>>();
	}
	
	/**
	 * ki�r�ti a sz�r�felt�tel list�t
	 */
	public void clearFilterer() {
		filterList.clear();
	}
	
	/**
	 * hozz�ad egy �j filtert a filterListhez a meghat�rozott felt�telek alapj�n.
	 * Valid�l �s ha minden dolog megfelel� akkor l�trehozza a sz�r�t amit ut�na
	 * felvesz a filterListbe.
	 * @param text  sz�rend� sz�veg/d�tum a t�pust�l meghat�rozva f�gg�en
	 * @param column a sz�rend� oszlop azonos�t�ja.
	 * @param field a filed t�pusa amit sz�r�nk
	 * @text a sz�rend� field egyszer� sz�veg
	 * @date a sz�rend� field d�tum
	 * @throws Exception ha a mez� nem beazonos�that� vagy ha nem megfelel� a megadott d�tum form�tum
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
				throw new Exception("Nem megfelel� D�tumform�tum!");
			}
		}else {
			throw new Exception("Nem azonos�that� mez�!");
		}
	}
	
	/**
	 * L�trehoz egyetlen filtert and kapcsolattal a afilterListben meghat�roztt elemkeb�l
	 * @return filter a filterList elemeib�l
	 */
	public RowFilter<DocumentTableModel,Integer> getRowFilter(){
		return RowFilter.andFilter(filterList);
	}
	
	/**
	 * l�trehoz egy sz�vegeket sz�r� filtert String filtert
	 * @param regex a sz�rend�/keresend� sz�veg/sz�vegr�szlet
	 * @param where az oszlop/column neve ahova a filter ki�rt�kel�dik
	 * @return String filterer a megadott sz�veg/sz�vegr�szletb�l
	 */
	private RowFilter<DocumentTableModel,Integer> createStringFilter(String regex, int where){
		return RowFilter.regexFilter(regex,where);
	}
	
	/**
	 * L�trehoz egy LocalDate t�pus� d�tumokat sz�r� sz�r�t/filterert a megadott param�terek alapj�n
	 * @param date a sz�rend� d�tum
	 * @param where melyik oszlopra �rt�kelend� ki a filter
	 * @param type a sz�r�si felt�tel
	 * 	 	@before azon d�tumok sz�r�se amelyek kor�bbiak mint a date-ben meghat�rozott d�tum
	 * 		@after azon d�tumok sz�r�se amelyek k�s�bbiek mint a dateB-ben meghat�rozott d�tum

	 * @return LocalDate t�pus� elemekt sz�r� filter
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
	 * A kapott d�tumokat tartalmaz� String t�mb�t form�zza
	 * l�trehozza a d�tum sz�r�t a megfelel� oszlopra
	 * majd hozz�adja a filterListhez
	 * @param dates a sz�rend� d�tumok
	 * @param where melyik oszlopra/column-ra
	 * @throws ParseException ha a ford�t�s nem volt sikeres.
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
