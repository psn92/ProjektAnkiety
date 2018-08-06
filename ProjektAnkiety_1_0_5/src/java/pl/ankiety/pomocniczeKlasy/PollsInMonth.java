package pl.ankiety.pomocniczeKlasy;

import java.util.Vector;


public class PollsInMonth {
	private int month;
	public class Result{
		public String title;
		public int count;
		
		public Result(String title, int count){
			this.title = title;
			this.count = count;
		}
	}
	private Vector<Result> results;
	
	public PollsInMonth(int month){
		this.month = month;
		results = new Vector();
	}
	
	public int getMonth(){
		return month;
	}
	
	public int getSumInMonth(){
		int sum = 0;
		for(Result i : results)
			sum += i.count;
		
		
		return sum;
	}
	
	public Vector<Result> getResults(){
		return results;
	}
	
	public void addResult(String title, int count){
		results.add(new Result(title, count));
	}
}
