package pl.ankiety.pomocniczeKlasy;

import java.util.Vector;

public class Question {
	private int ID;
	private String content;
	private Vector<Option> options;
	private String[] mode = new String[]{"Ogólny", "Płeć", "Wiek"};
	private String selected;
	
	public Question(int id, String content){
		this.ID = id;
		this.content = content;
		options = new Vector();
		selected = "Ogólny";
	}
	
	public int getID(){
		return ID;
	}
	
	public void addOption(int optionID, String optionContent){
		options.add(new Option(optionID, optionContent));
	}
	
	public Option getOptions(int i){
		return options.get(i);
	}
	
	public void setMode(String m){
		selected = m;
	}
	
	@Override
	public String toString(){
		String tmp = "<B style=\"font-size: 2vmin;\">" + content + "</B><BR>";
		tmp += selector();
		tmp += "<BR>";
		tmp += "<table align=\"center\">";
		int m = 0;
                tmp+="<tr>";
		if("Płeć".equals(selected))
                {	
                    m = 1;
                    tmp+="<td style=\"width:10vw; text-align:center;\"></td><td>K</td><td>M</td></tr><tr>";    
                }
		else if("Wiek".equals(selected))
                {
                    m = 2;
                    tmp+="<td style=\"width:10vw; text-align:center;\"></td><td>0-23</td><td>24-40</td><td>41-60</td><td>61+</td></tr><tr>";
                }
		for(int i = 0; i < options.size(); i++)
			tmp += "<td style=\"width:10vw; text-align:center;\">" + (char)('A' + i) + ". " + options.get(i).toString(m);
		
		tmp += "</tr></table>";
		return tmp + "<BR>";
	}
	
	private String selector(){
		String tmp = "<SELECT style=\"width:5vw; height:3vh; font-size:2vmin;\" NAME=\"mode" + ID + "\" ID=\"mode" + ID + "\""
				+ " onChange=\""
				+ "document.forms['formularz1'].elements['focus'].value = 'mode" + ID + "';"
				+ "aktualizacja.click();"
				+ "\">";
		for(int i = 0; i < mode.length; i++){
			tmp += "<OPTION ";
			if(mode[i].equals(selected))
				tmp += " SELECTED";
			tmp += ">" +  mode[i] + "</OPTION>";
		}
		tmp += "</SELECT>";
		
		
		return tmp;
	}
}
