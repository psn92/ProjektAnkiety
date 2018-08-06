package pl.ankiety.pomocniczeKlasy;

public class Option {
	private int ID;
	private String content;
	private class Gender{
		public int women;
		public int men;
		
		public Gender(){
			women = 0;
			men = 0;
		}
		
		public void add(int g){
			if(g == 0)
				women++;
			else
				men++;
		}
	}
	private Gender gender;
	private class Age{
		public int interval_0_23;
		public int interval_24_40;
		public int interval_41_60;
		public int interval_61;
		
		public Age(){
			interval_0_23 = 0;
			interval_24_40 = 0;
			interval_41_60 = 0;
			interval_61 = 0;
		}
		
		public void add(int a){
			if(a <= 23)
				interval_0_23++;
			else if(a <=40)
				interval_24_40++;
			else if(a <=60)
				interval_41_60++;
			else
				interval_61++;
		}
	}
	private Age age;
	private int sum;
	
	public Option(int id, String content){
		this.ID = id;
		this.content = content;
		gender = new Gender();
		age = new Age();
		sum = 0;
	}
	
	public int getID(){
		return ID;
	}
	
	public int getSum(){
		return sum;
	}
	
	public int[] getGender(){
		return new int[]{gender.women, gender.men};
	}
	
	public int[] getAge(){
		return new int[]{age.interval_0_23, age.interval_24_40, age.interval_41_60, age.interval_61};
	}
	
	public void addPoint(int plec, int wiek){
		sum++;
		gender.add(plec);
		age.add(wiek);
	}
	
	public String toString(int mode){
		return "<I>" + content + "</I> </td>" + statiscics(mode);
	}
	private String statiscics(int mode){
		switch(mode){
			case 0:
				return "<td><b>" + sum + "</b></td><tr>";
			case 1:
				return "<td><b>" + gender.women + "</b></td><td><b>" + gender.men + "</b></td><tr>";
			default:
				return "<td><b>" + age.interval_0_23
						+ "</b></td><td><b>" + age.interval_24_40
						+ "</b></td><td><b>" + age.interval_41_60
						+ "</b></td><td><b>" + age.interval_61 + "</b></td><tr>";
		}
	}
}
