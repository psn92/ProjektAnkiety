package pl.ankiety.pomocniczeKlasy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PollsViewPollInformation{
	private int ID;
	private String Nazwa;
	private int priorytet;
	private int wymagana_liczba;
	private int ilosc_wypelnien;
	
	public PollsViewPollInformation(int ID, String Nazwa, int priorytet, int wymagana_liczba, int ilosc_wypelnien){
		this.ID = ID;
		this.Nazwa = Nazwa;
		this.priorytet = priorytet;
		this.wymagana_liczba = wymagana_liczba;
		this.ilosc_wypelnien = ilosc_wypelnien;
	}
	
	@Override
	public String toString(){
		return ID + "</TD><TD style=\"word-wrap:break-word;\">"
				+ Nazwa + "</TD><TD>"
				+ priorytet + "</TD><TD>"
				+ wymagana_liczba + "</TD><TD>"
				+ ilosc_wypelnien + "</TD><TD>"
				+ "<div style=\"width:8vw; float:left;\"><input id=\"" + ID;
	}
	
	public int getID(){
		return ID;
	}
	
	public StringBuilder toStringOneRowVersion(){
            HTML h = new HTML();
            StringBuilder tmp = new StringBuilder("<div style=\"" + h.whiteSquare2() +"\">");
		tmp.append("<TABLE align=\"center\" style=\"font-size:2.5vmin;\"><TR style=\"font-size: 2.5vmin;\"><TH style=\"width:5vw;\">ID ankiety</TH><TH style=\"width:13vw;\">Nazwa ankiety</TH><TH style=\"width:5vw;\">Priorytet</TH>"
                        + "<TH style=\"width:5vw;\">Wymagana liczba</TH><TH style=\"width:5vw;\">Ilość wypełnień</TH><TH style=\"width:16vw;\"></TH></TR>");
		
		tmp.append("<TR align=\"CENTER\"><TD>"
				+ ID + "</TD><TD>"
				+ Nazwa + "</TD><TD>");
		
		tmp.append("<SELECT NAME=\"pools_priority\" style=\"width:3vw; height: 3vh; font-size: 2vmin;\">");
		for(int priority = 1; priority <= 5; priority++){
			tmp.append("<OPTION");
			if(priorytet == priority)
				tmp.append(" SELECTED");
			tmp.append(">" +  priority + "</OPTION>");
		}
		tmp.append("</SELECT></TD><TD>");
		tmp.append("<INPUT ID=\"pools_n\" NAME=\"pools_n\" VALUE=\"" + wymagana_liczba + "\" onChange=\"poolsNumberValidation();\" style=\"width:8vw; height: 2vh;font-size: 2vmin;\"></TD><TD>"
				+ ilosc_wypelnien + "</TD><TD>");
		tmp.append("<input TYPE=\"submit\" value=\"Zapisz\" style=\"" + h.smallButton() + "\"" 
                        + "></TD></TR></TABLE>");
		
		tmp.append("<SCRIPT>"
				+ new JavaScripts().PollsWizardPoolsNumberValidation()
				+ "</SCRIPT>");
		
		return tmp;
	}
}
