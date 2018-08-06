package pl.ankiety.pomocniczeKlasy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.ankiety.UsersView;
import pl.ankiety.pomocniczeKlasy.PollsInMonth.Result;



public class UserStatiscics extends User{
	private String sqlExceptions = "";
	protected TimePeriod currentTimePeriod;
	protected Vector<PollsInYear> pollsInYear;
	private class CurrentTimePeriodIndexes{
		public int y;
		public int m;
	}
	CurrentTimePeriodIndexes indexes = new CurrentTimePeriodIndexes();

	public UserStatiscics(String login) {
		super(login);
		currentTimePeriod = new TimePeriod();
		
		pollsInYear = new Vector();
		try {
			setResults();
		} catch (ClassNotFoundException ex) {
			sqlExceptions += "<BR><BR>poleciał wyjątek ClassNotFoundException<BR><BR>" + ex.getMessage();
			Logger.getLogger(UserStatiscics.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			sqlExceptions += "<BR><BR>poleciał wyjątek SQLException<BR><BR>" + ex.getMessage();
			Logger.getLogger(UserStatiscics.class.getName()).log(Level.SEVERE, null, ex);
		}
		if(pollsInYear.size() == 0 || !currentTimePeriodInPolls()){
			pollsInYear.add(new PollsInYear(currentTimePeriod.getYear()));
			pollsInYear.get(pollsInYear.size() - 1).addMonth(currentTimePeriod.getMonth());
		}
	}
	
	private void setResults() throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		
		ResultSet setUserPollsGroupedByMonths = c.select(queryUserPollsGroupedByMonths());
		int year = -1;
		int month = -1;
		while(setUserPollsGroupedByMonths.next()){
			if(year != setUserPollsGroupedByMonths.getInt("YEAR")){
				year = setUserPollsGroupedByMonths.getInt("YEAR");
				pollsInYear.add(new PollsInYear(year));
				month = -1;
			}
			if(month != setUserPollsGroupedByMonths.getInt("MONTH")){
				month = setUserPollsGroupedByMonths.getInt("MONTH");
				pollsInYear.get(pollsInYear.size() - 1).addMonth(setUserPollsGroupedByMonths.getInt("MONTH"));
			}
			int tmp = pollsInYear.get(pollsInYear.size() - 1).getPollsInMonthSize() - 1;
			pollsInYear.get(pollsInYear.size() - 1).getPollsInMonth(tmp).addResult(setUserPollsGroupedByMonths.getString("Ankieta"), setUserPollsGroupedByMonths.getInt("IloscAnkiet"));
		}
		setUserPollsGroupedByMonths.close();
		
		c.close();
	}
	private String queryUserPollsGroupedByMonths(){
		return "SELECT YEAR, MONTH, Ankieta, COUNT(Grupa) AS IloscAnkiet FROM ("
				+ "	SELECT YEAR, MONTH, Ankieta, CONCAT(MonthNumber, Ankieta) AS Grupa FROM ("
				+ "		SELECT YEAR(WYPELNIENIA.Data) AS YEAR, MONTH(WYPELNIENIA.Data) AS MONTH, CONCAT(YEAR(WYPELNIENIA.Data), '-', MONTH(WYPELNIENIA.Data)) AS MonthNumber, ANKIETY.Nazwa AS Ankieta"

				+ "		FROM ODPOWIEDZI"

				+ "		INNER JOIN WYPELNIENIA"
				+ "		ON WYPELNIENIA.ID_Wypelnienia = ODPOWIEDZI.ID_Wypelnienia"
				+ "		INNER JOIN OPCJE"
				+ "		ON OPCJE.ID_Opcji = ODPOWIEDZI.ID_Opcji"
				+ "		INNER JOIN PYTANIA"
				+ "		ON OPCJE.ID_Pytania = PYTANIA.ID_Pytania"
				+ "		INNER JOIN ANKIETY"
				+ "		ON ANKIETY.ID_Ankiety = PYTANIA.ID_Ankiety"
				
				+ "		WHERE"
				+ "		WYPELNIENIA.Ankieter = '" + login + "'"

				+ "		GROUP BY ODPOWIEDZI.ID_Wypelnienia"
				+ "	) StatystykaAnkietera"
				+ ") StatystykaAnkietera "
				+ "GROUP BY Grupa "
				+ "ORDER BY YEAR, MONTH ";
	}
	
	private boolean currentTimePeriodInPolls(){
		int i;
		for(i = 0; i < pollsInYear.size(); i++)
			if(pollsInYear.get(i).getYear() == currentTimePeriod.getYear())
				break;
		if(i < pollsInYear.size())
			pollsInYear.get(i).insertCurrentMonth(currentTimePeriod.getMonth());
		
		
		return i < pollsInYear.size();
	}
	
	public void setTimePeriod(int month, int year){
		if(year != currentTimePeriod.getYear()){
			int i;
			for(i = 0; pollsInYear.get(i).getYear() != year; i++)
				;
			month = pollsInYear.get(i).getPollsInMonth(0).getMonth();
		}
		currentTimePeriod.setTimePeriod(month, year);
	}
	
	public StringBuilder getStatistics(){
		StringBuilder tmp = new StringBuilder(sqlExceptions + "<B>" + login + "</B><BR>");
		
		tmp.append(monthAndYear());
		
		try{
			tmp.append(setUserStatiscticsInMonth());
			tmp.append(setUserGeneralStatisctics());
		} catch (ClassNotFoundException ex)
        {
			tmp.append("<BR><BR>poleciał wyjątek ClassNotFoundException<BR><BR>" + ex.getMessage());
			Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
			tmp.append("<BR><BR>poleciał wyjątek SQLException<BR><BR>" + ex.getMessage());
			Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		
		return tmp;
	}
	
	private String monthAndYear(){
		String tmp = "<FORM ACTION = \"http://localhost:8080/ProjektAnkiety/UsersView/UserView\""
				+ " METHOD=\"POST\">";
		tmp += "Dane z: ";
		
		tmp += monthsSelector() + " " + yearsSelector();
		
		tmp += "<BUTTON ID=\"aktualizacja\" TYPE=\"submit\" STYLE=\"display:none;\"><B>Aktualizacja</B></BUTTON>"
				+ "</FORM>";
		tmp += "<BR>";
		
		
		return tmp;
	}
	private String monthsSelector(){
		String tmp = "<SELECT NAME=\"monthsSelector\" onChange=\"aktualizacja.click();\" style=\"font-size:3vmin;\">";
		int i;
		for(i = 0; i < pollsInYear.size(); i++)
			if(pollsInYear.get(i).getYear() == currentTimePeriod.getYear()){
				indexes.y = i;
				break;
			}
		for(int j = 0; j < pollsInYear.get(i).getPollsInMonthSize(); j++){
			tmp += "<OPTION";
			if(currentTimePeriod.getMonth() == pollsInYear.get(i).getPollsInMonth(j).getMonth()){
				indexes.m = j;
				tmp += " SELECTED";
			}
			tmp += ">" +  pollsInYear.get(i).getPollsInMonth(j).getMonth() + "</OPTION>";
		}
		tmp += "</SELECT>";
		
		
		return tmp;
	}
	private String yearsSelector(){
		String tmp = "<SELECT NAME=\"yearsSelector\" onChange=\"aktualizacja.click();\" style=\"font-size:3vmin;\">";
		for(PollsInYear year : pollsInYear){
			tmp += "<OPTION";
			if(currentTimePeriod.getYear() == year.getYear())
				tmp += " SELECTED";
			tmp += ">" +  year.getYear() + "</OPTION>";
		}
		tmp += "</SELECT>";
		
		
		return tmp;
	}
	
	private String setUserStatiscticsInMonth() throws ClassNotFoundException, SQLException{
		boolean emptyList = true;
		
		String tmp = "<TABLE>";
        for(Result r : pollsInYear.get(indexes.y).getPollsInMonth(indexes.m).getResults()){
			emptyList = false;
            tmp += "<TR>";
			
			tmp += "<TD>";
			tmp += r.title;
			tmp += "</TD>";
			tmp += "<TD>";
			tmp += r.count;
			tmp += "</TD>";
			
            tmp += "</TR>";
		}
		tmp += "<TD></TD><TD><B>" + pollsInYear.get(indexes.y).getPollsInMonth(indexes.m).getSumInMonth() + "</B></TD>";
		tmp += "</TABLE> ";
		
		if(emptyList)
			return "Brak ankiet.<BR><BR>";
		
		
		return tmp;
    }
	
	private String setUserGeneralStatisctics() throws ClassNotFoundException, SQLException{
		String tmp = "<BR><TABLE style=\"font-size:4vmin;\">";
		int sum = 0;
		int months = 0;
		for(PollsInYear year : pollsInYear){
			sum += year.getSumInYear();
			months += year.getPollsInMonthSize();
		}
        
		tmp += "<TR>";
		
		tmp += "<TD>Łącznie przeprowadzonych ankiet</TD>";
		tmp += "<TD>";
		tmp += sum;
		tmp += "</TD>";
		
		tmp += "</TR>";
		
		tmp += "<TR>";

		tmp += "<TD>Średnio na miesiąc</TD>";
		tmp += "<TD>";
		if(months != 0){
			double average = (double)sum / months;
			tmp +=  Math.ceil(100 * average) / 100;
		}
		else
			tmp += 0;
		tmp += "</TD>";

		tmp += "</TR>";
		
		tmp += "</TABLE> ";
		
		
		return tmp;
    }
}
