package pl.ankiety.pomocniczeKlasy;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PollsWizardQuestion {
	private boolean visibility;
	private String title;
	private int order;
	private int numberOFoptions;
	private PollsWizardQuestionOption[] options;
	
	public PollsWizardQuestion(String visibility, String title, int order, int numberOFoptions){
		if("true".equals(visibility))
			this.visibility = true;
		else
			this.visibility = false;
		this.title = title;
		this.order = order;
		this.numberOFoptions = numberOFoptions;
		options = new PollsWizardQuestionOption[numberOFoptions];
	}
	
	public void addOption(String visibility, String name, String title, int order){
		options[order] = new PollsWizardQuestionOption("question" + this.order, visibility, name, title, order);
	}
	
	public StringBuilder toStringBuilder(){
		StringBuilder builder = new StringBuilder("");
		String hiddenQuestion = visibility ? "" : " STYLE=\"display:none;\"";
		if(visibility){
			builder.append("<BR>");		}
		HTML h = new HTML();
		builder.append("<input placeholder=\"tytuł pytania\"" + hiddenQuestion + " ID=\"question" + order + "\" "
                        + "NAME=\"question" + order + "\"  style=\""+ h.input() +"\" value=\""
				+ title
				/*
				Kontrolka ukryta - tutaj będzie zmianna  techniczna przechowywująca widoczność opcji.
				*/
				+ "\" /><INPUT TYPE=\"hidden\" NAME=\"widocznePytanie" + order + "\" VALUE=\"" + visibility + "\">");
		
		if(visibility){
			builder.append("<BUTTON ID=\"usunieciePytania" + order + "\" style=\""+h.smallButton()+"Background-color: #F05D5D;\">Usuń pytanie</BUTTON>"
					+ "<SCRIPT>"
					+ "document.getElementById('usunieciePytania" + order + "').onclick = function(){"
					+ "		document.forms['formularz1'].elements['widocznePytanie" + order + "'].value = 'false';"
					+ "		document.forms['formularz1'].elements['focus'].value = 'title';"
					+ "};"
					+ "</SCRIPT>"
					+ "<BR>");
			
			builder.append("<MENU>");
			for(int i = 0; i < numberOFoptions; i++)
				builder.append(options[i].toStringBuilder());
			builder.append("</MENU>");
			
			builder.append("<INPUT TYPE=\"hidden\" NAME=\"n" + order + "\" VALUE=\"" + numberOFoptions + "\">"
					+ "<BUTTON ID=\"dodanieOpcji" + order + "\" TYPE=\"submit\" style=\""+h.smallButton()+"color: #000000; background: #C0C0C0;\">Dodaj opcję</BUTTON><BR><BR>"
					+ "<SCRIPT>"
					+ "document.getElementById('dodanieOpcji" + order + "').onclick = function(){"
					+ "		document.forms['formularz1'].elements['n" + order + "'].value = '" + ++numberOFoptions + "';"
					+ "		document.forms['formularz1'].elements['focus'].value = 'dodanieOpcji" + order + "';"
					+ "};"
					+ "</SCRIPT>");
		}
		
		return builder;
	}
	
	public boolean getVisibility(){
		return visibility;
	}
	
	public boolean optionsExists(){
		int numberOfOptions = 0;
		for(int i = 0; i < options.length; i++)
			if(options[i].getVisibility())
				numberOfOptions++;
		
		if(numberOfOptions < 2)
			return false;
		return true;
	}
	
	public StringBuilder readOnlyForm(int poolsID, int realORDER)
			throws ClassNotFoundException, SQLException{
		StringBuilder builder = new StringBuilder(title + "<BR>");
		
		int currnetID = getQUESTIONid();
		insertQUESTION(currnetID, poolsID);
                builder.append("</div><div style=\"margin: auto;text-align:left; width: 30vw;\">");
		char realORDERQ = 'A';
		for(int i = 0; i < numberOFoptions; i++)
			if(options[i].getVisibility())
				builder.append(options[i].readOnlyForm(currnetID, realORDERQ++) + "<BR>");
		builder.append("</div>");
		return builder;
	}
	private int getQUESTIONid()
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		ResultSet rs = c.select("SELECT MAX(ID_Pytania) AS KOL"
				+ " FROM PYTANIA"
				+ " UNION ALL"
				+ " SELECT 0 AS KOL");
		rs.next();
		int tmp = rs.getInt(1);
		c.close();
		
		return ++tmp;
	}
	private void insertQUESTION(int ID, int poolsID)
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		c.add("INSERT INTO PYTANIA (ID_Pytania, ID_Ankiety, Tresc)"
				+ " VALUES (" + ID + ", " + poolsID + ", '" + title + "');");
		c.close();
	}
}
