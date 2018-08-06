package pl.ankiety.pomocniczeKlasy;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PollsWizardQuestionOption {
	private String questionID;
	private boolean visibility;
	private String name;
	private String title;
	private int order;
	
	public PollsWizardQuestionOption(String questionID, String visibility, String name, String title, int order){
		this.questionID = questionID;
		if("true".equals(visibility))
			this.visibility = true;
		else
			this.visibility = false;
		this.name = name;
		this.title = title;
		this.order = order;
	}
	
	public StringBuilder toStringBuilder(){
		StringBuilder builder = new StringBuilder("");
		String hiddenOption = visibility ? "" : " TYPE=\"hidden\"";
		if(visibility){
			builder.append("<BR>");
		}
		HTML h = new HTML();
		builder.append("<INPUT placeholder=\"tytuł opcji\"" + hiddenOption + " ID=\"" + name + "\" NAME=\"" + name + "\" VALUE=\"" + title + "\""
                        + "style=\""+ h.input() +" \">"
				/*
				Kontrolka ukryta - tutaj będzie zmianna  techniczna przechowywująca widoczność opcji.
				*/
				+ "<INPUT TYPE=\"hidden\" NAME=\"widocznaOpcja" + name + "\" VALUE=\"" + visibility + "\">");
		
		if(visibility){
			builder.append(" <BUTTON ID=\"usuniecieOpcji" + name + "\" style=\""+ h.smallButton() +"background: #F05D5D;\">Usuń opcję</BUTTON>"
					+ "<SCRIPT>"
					+ "document.getElementById('usuniecieOpcji" + name + "').onclick = function(){"
					+ "		document.forms['formularz1'].elements['widocznaOpcja" + name + "'].value = 'false';"
					+ "		document.forms['formularz1'].elements['focus'].value = '" + questionID + "';"
					+ "};"
					+ "</SCRIPT>");
		}
		
		return builder;
	}
	
	public boolean getVisibility(){
		return visibility;
	}
	
	public StringBuilder readOnlyForm(int questionID, char realORDER)
			throws ClassNotFoundException, SQLException{
		int currnetID = getOPTIONid();
		insertOPTION(currnetID, questionID);
		
		return new StringBuilder("" + realORDER + ". " + title);
	}
	private int getOPTIONid()
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		ResultSet rs = c.select("SELECT MAX(ID_Opcji) AS KOL"
				+ " FROM OPCJE"
				+ " UNION ALL"
				+ " SELECT 0 AS KOL");
		rs.next();
		int tmp = rs.getInt(1);
		c.close();
		
		return ++tmp;
	}
	private void insertOPTION(int ID, int questionID)
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		c.add("INSERT INTO OPCJE (ID_Opcji, ID_Pytania, Tresc)"
				+ " VALUES (" + ID + ", " + questionID + ", '" + title + "');");
		c.close();
	}
}
