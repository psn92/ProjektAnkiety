package pl.ankiety.pomocniczeKlasy;

import java.util.Calendar;

public class TimePeriod{
	protected int fromMonth;
	protected int fromYear;
	protected int toMonth;
	protected int toYear;
	
	public TimePeriod(){
		Calendar calendar = Calendar.getInstance();
		fromMonth = calendar.get(Calendar.MONTH) + 1;
		fromYear = calendar.get(Calendar.YEAR);
		setTo();
	}
	
	public void setTimePeriod(int month, int year){
		fromMonth = month;
		fromYear = year;
		setTo();
	}
	
	public int getMonth(){
		return fromMonth;
	}
	
	public int getYear(){
		return fromYear;
	}
	
	public String toStringFrom(){
		String tmp = fromYear + "-";
		if(fromMonth < 10)
			tmp += "0";
		tmp += fromMonth + "-01";
		
		
		return tmp;
	}
	public String toStringTo(){
		String tmp = toYear + "-";
		if(toMonth < 10)
			tmp += "0";
		tmp += toMonth + "-01";
		
		
		return tmp;
	}
	
	private void setTo(){
		if(fromMonth < 12)
			toMonth = fromMonth + 1;
		else
			toMonth = 1;
		
		if(toMonth > 1)
			toYear = fromYear;
		else
			toYear = fromYear + 1;
	}
}
