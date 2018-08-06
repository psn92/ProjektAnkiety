package pl.ankiety;

import pl.ankiety.pomocniczeKlasy.Conn;
import pl.ankiety.pomocniczeKlasy.PollsWizardQuestion;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.ankiety.pomocniczeKlasy.HTML;
import pl.ankiety.pomocniczeKlasy.JavaScripts;


public class PollsWizard extends HttpServlet {
	JavaScripts PollsWizardJS;
	private int numberOFquestions;
	private PollsWizardQuestion[] questions;
	static private int[] numberOFoptions;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Kreator ankiet</title>");
		out.println("</head>");
		out.println("<body bgcolor=\"#76b852\">");
                HTML h = new HTML();
                out.println("<div style=\""+ h.wrapper() +"\">");
                out.println(h.menuHTML());
		
		out.print("<FORM ACTION = \"http://localhost:8080/ProjektAnkiety/PollsWizard\""
				+ " METHOD=\"POST\">"
				+ "<BUTTON ID=\"klik\" style=\"display: none;\"></BUTTON>"
				+ "</FORM>");
		out.print("<SCRIPT>"
				+ "klik.click();"
				+ "</SCRIPT>");
		
		out.println("</div></body>");
		out.println("</html>");
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PollsWizardJS = new JavaScripts();
		
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Kreator ankiet</title>");			
		out.println("</head>" + PollsWizardJS.setSleepFunction());
		out.println("<body bgcolor=\"#76b852\"");
		HTML h = new HTML();
		if(request.getParameter("done") != null && "1".equals(request.getParameter("done"))){
			out.println(">");
                        out.println("<div style=\""+ h.wrapper() +"\">");
                        out.println(h.menuHTML());
			setQUESTIONS(request);
			try {
				pollsAPROVAL(out, request);
			} catch (ClassNotFoundException | SQLException ex) {
				Logger.getLogger(PollsWizard.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else{
			out.println(" onload=\"document.getElementById('" + (request.getParameter("focus") == null ? "title" : getFocus(request.getParameter("focus"))) + "').focus();\">");
			out.println("<div style=\""+ h.wrapper() +"\">");
                        out.println(h.menuHTML());
                        pollsCONTENT(out, request);
		}
		
		out.println("</div></body>");
		out.println("</html>");
		
	}
	
	private String getFocus(String a){
		Pattern p1 = Pattern.compile("^dodanie.*$");
		Matcher m = p1.matcher(a);
		if(m.matches())
			return a;
		Pattern p2 = Pattern.compile("^question.*$");
		m = p2.matcher(a);
		if(m.matches())
			return a;

		return "title";
	}
	
	private void pollsCONTENT(PrintWriter out, HttpServletRequest request){
		out.println("<FORM name=\"formularz1\""
				+ " METHOD=\"POST\">"
				/*
				Kontrolka ukryta - tutaj będzie zmianna  techniczna przechowywująca stotus ankiety (gotowości do zapisania).
				*/
				+ "<INPUT TYPE=\"hidden\" NAME=\"done\" VALUE=\"" + request.getParameter("done") + "\">"
				/*
				Kontrolka ukryta - tutaj będzie przekierowanie na focusa.
				*/
				+ "<INPUT TYPE=\"hidden\" NAME=\"focus\" VALUE=\"" + request.getParameter("focus") + "\">");
		HTML h = new HTML();
		out.println("<div style=\""+ h.whiteSquare2() +"\">"
                    + "<input placeholder=\"tytuł ankiety\" ID=\"title\" NAME=\"title\" style=\""+ h.input2() +"\" value=\""
                    + getRequest(request, "title")
                    + "\"/><SCRIPT>" + PollsWizardJS.PollsWizardPoolsNumberValidation() + "</SCRIPT>"   
                    + "<div style=\"width:45vw; margin:auto; font-size:1vw;\"><input ID=\"pools_n\" NAME=\"pools_n\" placeholder=\"Wymagana liczba ankiet\" style=\""+ h.input2() +"width: 30vw;\""
                    + "VALUE=\"" + getRequest(request, "pools_n") + "\" onChange=\"poolsNumberValidation();\" />"
                    + " Priorytet ankiety <SELECT NAME=\"pools_priority\" style=\""+h.sele()+"\">");
		for(int priority = 1; priority <= 5; priority++){
			out.print("<OPTION");
			if(getRequest(request, "pools_priority") != null && !"".equals(getRequest(request, "pools_priority")) && Integer.parseInt(getRequest(request, "pools_priority")) == priority)
				out.print(" SELECTED");
			out.print(">" +  priority + "</OPTION>");
		}
                out.println("</div>");
		questionsMENAGEMENT(out, request);
		/*
		Po naciśnięciu przycisku wykona się skrypt zmieniający status ankiety na gotową do zapisania,
		ale tylko jeśli przejdzie walidację.
		*/
		out.print("</SELECT><button ID=\"zapisAnkiety\" style=\""+ h.bigButton() +"\">"
				+ "Zapisz ankietę</button></div>");
		out.print("<SCRIPT>"
				+ PollsWizardJS.PollsWizardPoolsValidation());
		out.print("document.getElementById('zapisAnkiety').onclick = function(){"
				+ "		if(poolsValidation())"
				+ "			document.forms['formularz1'].elements['done'].value = '1';"
				+ "};"
				+ "</SCRIPT>");
		
		out.println("</FORM>");
	}
	private void questionsMENAGEMENT(PrintWriter out, HttpServletRequest request){
		numberOFquestions = ("".equals(getRequest(request, "n"))) ? 0 : Integer.parseInt(getRequest(request, "n"));
		questions = new PollsWizardQuestion[numberOFquestions];
		numberOFoptions = new int[numberOFquestions];
		for(int i = 0; i < numberOFquestions; i++){
			numberOFoptions[i] = ("".equals(getRequest(request, "n" + i))) ? 0 : Integer.parseInt(getRequest(request, "n" + i));
			questions[i] = new PollsWizardQuestion((request.getParameter("widocznePytanie" + i) == null ? "true" : getRequest(request, "widocznePytanie" + i))
					, getRequest(request, "question" + i), i, numberOFoptions[i]);
			
			for(int j = 0; j < numberOFoptions[i]; j++){
				String tmp = "option" + i + "_" + j;
				questions[i].addOption((request.getParameter("widocznaOpcja" + tmp) == null ? "true" : getRequest(request, "widocznaOpcja" + tmp)),
						tmp, getRequest(request, tmp), j);
			}
			
			out.println(questions[i].toStringBuilder());
		}
		HTML h = new HTML();
		out.println("<INPUT TYPE=\"hidden\" NAME=\"n\" VALUE=\"" + numberOFquestions + "\">"
			+ "<BUTTON ID=\"dodaniePytania\" TYPE=\"submit\" style=\""+ h.smallButton2() +"\"><B>Dodaj pytanie</B></BUTTON><br>"
				+ "<SCRIPT>"
				+ "document.getElementById('dodaniePytania').onclick = function(){"
				+ "		document.forms['formularz1'].elements['n'].value = '" + ++numberOFquestions + "';"
				+ "		document.forms['formularz1'].elements['focus'].value = 'dodaniePytania';"
				+ "};"
				+ "</SCRIPT>");
	}
	
	
	private void setQUESTIONS(HttpServletRequest request){
		numberOFquestions = ("".equals(getRequest(request, "n"))) ? 0 : Integer.parseInt(getRequest(request, "n"));
		questions = new PollsWizardQuestion[numberOFquestions];
		numberOFoptions = new int[numberOFquestions];
		for(int i = 0; i < numberOFquestions; i++){
			numberOFoptions[i] = ("".equals(getRequest(request, "n" + i))) ? 0 : Integer.parseInt(getRequest(request, "n" + i));
			questions[i] = new PollsWizardQuestion((request.getParameter("widocznePytanie" + i) == null ? "true" : getRequest(request, "widocznePytanie" + i))
					, getRequest(request, "question" + i), i, numberOFoptions[i]);
			
			for(int j = 0; j < numberOFoptions[i]; j++){
				String tmp = "option" + i + "_" + j;
				questions[i].addOption((request.getParameter("widocznaOpcja" + tmp) == null ? "true" : getRequest(request, "widocznaOpcja" + tmp)),
						tmp, getRequest(request, tmp), j);
			}
		}
	}

	
	private void pollsAPROVAL(PrintWriter out, HttpServletRequest request)
			throws ClassNotFoundException, SQLException{
            HTML h = new HTML();
            out.println("<div style=\""+ h.whiteSquare2() +"\">"
                            + "<div style=\"margin: auto; color: #4CAF50; font-size: 4vmin; word-wrap:break-word;\">");
            
		out.print("Tytuł: <B>"
				+ getRequest(request, "title")
				+ "</B><BR>[wymaganych ankiet: "
				+ getRequest(request, "pools_n")
				+ "] [priorytet: "
				+ getRequest(request, "pools_priority")
				+ "]"
				+ "<BR>");
		
		int currnetID = getPOOLSid();
		insertPRIMARYvalues(currnetID, getRequest(request, "title"), getRequest(request, "pools_n"), getRequest(request, "pools_priority"));
		
		int realORDER = 1;
		for(int i = 0; i < numberOFquestions; i++)
			if(questions[i].getVisibility())
				out.print("<div style=\"width: 35vw; margin: auto; text-align: left; padding: 1vh 0 1vh 0;\">" + 
                                        realORDER + ". " + questions[i].readOnlyForm(currnetID, realORDER++) + "<BR>");
        }
	private int getPOOLSid()
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		ResultSet rs = c.select("SELECT MAX(ID_Ankiety) AS KOL"
				+ " FROM ANKIETY"
				+ " UNION ALL"
				+ " SELECT 0 AS KOL");
		rs.next();
		int tmp = rs.getInt(1);
		c.close();
		
		return ++tmp;
	}
	private void insertPRIMARYvalues(int ID, String title, String pools_n, String pools_priority)
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		c.add("INSERT INTO ANKIETY (ID_Ankiety, Nazwa, priorytet, wymagana_liczba, Zakonczona)"
				+ " VALUES (" + ID + ", '" + title + "', " + pools_priority + ", " + pools_n + ", 0);");
		c.close();
	}
	
	
	private String getRequest(HttpServletRequest request, String x){
		if(request.getParameter(x) != null)
			return request.getParameter(x);
		else
			return "";
	}

}
