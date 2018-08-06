package pl.ankiety.pomocniczeKlasy;

import java.util.Vector;


public class PollsInYear {
	private int year;
	private Vector<PollsInMonth> months;
	
	public PollsInYear(int year){
		this.year = year;
		months = new Vector();
	}
	
	public int getYear(){
		return year;
	}
	
	public int getPollsInMonthSize(){
		return months.size();
	}
	
	public PollsInMonth getPollsInMonth(int i){
		return months.get(i);
	}
	
	public int getSumInYear(){
		int sum = 0;
		for(PollsInMonth i : months)
			sum += i.getSumInMonth();
		
		
		return sum;
	}
	
	public void addMonth(int month){
		months.add(new PollsInMonth(month));
	}
	
	public void insertCurrentMonth(int currentMonth){
		int i;
		for(i = 0; i < months.size(); i++)
			if(months.get(i).getMonth() == currentMonth)
				break;
		if(i == months.size())
			months.add(new PollsInMonth(currentMonth));
	}
}
