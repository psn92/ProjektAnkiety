package pl.ankiety;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.ankiety.pomocniczeKlasy.Conn;
import pl.ankiety.pomocniczeKlasy.HTML;
import pl.ankiety.pomocniczeKlasy.Paging;
import pl.ankiety.pomocniczeKlasy.PollsViewPollInformation;

public class PollsView extends HttpServlet 
{
	private Vector<PollsViewPollInformation> pollsOn;
	private Vector<PollsViewPollInformation> pollsOff;
	private boolean previousPages;
	private int pOn;
	private int pOff;
	
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
		if(request.getParameter("previousPagePageOn") != null && !"null".equals(request.getParameter("previousPagePageOn")))
			previousPages = true;
		else
			previousPages = false;
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Podgląd ankiet</title>");			
        out.println("</head>");
        out.println("<body bgcolor=\"#76b852\">");
        
        HTML h = new HTML();
                
        out.println("<div style=\""+ h.wrapper() +"\">");
        out.println(h.menuHTML());
        
        try 
        {
            content(out, request);
        } catch (ClassNotFoundException ex)
        {
			out.println("<BR><BR>poleciał wyjątek ClassNotFoundException<BR><BR>" + ex.getMessage());
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
			out.println("<BR><BR>poleciał wyjątek SQLException<BR><BR>" + ex.getMessage());
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.println("</div></body>");
        out.println("</html>");
        
    }
    
    private void content(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
        
		if(!previousPages){
			pOn = (request.getParameter("pageOn") != null && !"null".equalsIgnoreCase(request.getParameter("pageOn"))) ? Integer.parseInt(request.getParameter("pageOn")) : 1;
			pOff = (request.getParameter("pageOff") != null && !"null".equalsIgnoreCase(request.getParameter("pageOff"))) ? Integer.parseInt(request.getParameter("pageOff")) : 1;
		}
		else{
			pOn = Integer.parseInt(request.getParameter("previousPagePageOn"));
			pOff = Integer.parseInt(request.getParameter("previousPagePageOff"));
			previousPages = false;
		}
		
        out.println("<FORM name=\"formularz1\" METHOD=\"POST\">"
				/*
				Kontrolki ukryte - tutaj będą numery aktualnych stron.
				*/
				+ "<INPUT TYPE=\"hidden\" NAME=\"pageOn\" VALUE=\"" 
				+ pOn
				+ "\">"
                + "<INPUT TYPE=\"hidden\" NAME=\"pageOff\" VALUE=\""
				+ pOff
				+ "\">"
				/*
				Kontrolki ukryte - tutaj będą informacje na temat zwolniej/zatrudnień.
				*/
				+ "<INPUT TYPE=\"hidden\" NAME=\"markedPollSelectedStatus\" VALUE=\"" + request.getParameter("markedPollSelectedStatus") + "\">"
				+ "<INPUT TYPE=\"hidden\" NAME=\"markedPoll\" VALUE=\"" + request.getParameter("markedpoll") + "\">");
		
        if(request.getParameter("markedPoll") != null && !"null".equals(request.getParameter("markedPoll")))
			changePoll(request);
		
		setPolls();
		viewPolls(out);
		
		out.println("<SCRIPT type=\"text/javascript\">"
				+ "	function choosePoll(poll, status, On){"
                + "		document.forms['formularz1'].elements['markedPoll'].value = poll;"
                + "		document.forms['formularz1'].elements['markedPollSelectedStatus'].value = status;"
				+ "		if(document.forms['formularz1'].elements['pageOn'].value == " + (pollsOn.size() + 9) / 10
				+ " && On && " + (pollsOn.size() > 10 && pollsOn.size() % 10 == 1) + ")"
				+ "			document.forms['formularz1'].elements['pageOn'].value = document.forms['formularz1'].elements['pageOn'].value - 1;"
				+ "		if(document.forms['formularz1'].elements['pageOff'].value == " + (pollsOff.size() + 9) / 10
				+ " && !On && " + (pollsOff.size() > 10 && pollsOff.size() % 10 == 1) + ")"
				+ "			document.forms['formularz1'].elements['pageOff'].value = document.forms['formularz1'].elements['pageOff'].value - 1;"
				+ "	};"
				+ "	function setPageOn(k){"
				+ "		if(k >= 1 && k <= " + (pollsOn.size() + 9) / 10 + ")"
                + "			document.forms['formularz1'].elements['pageOn'].value = k;"
				+ "	};"
				+ "	function setPageOff(k){"
				+ "		if(k >= 1 && k <= " + (pollsOff.size() + 9) / 10 + ")"
                + "			document.forms['formularz1'].elements['pageOff'].value = k;"
				+ "	};"
                + "</SCRIPT>");
		
        out.println("</FORM>");
    }
	
	private void setPolls() throws ClassNotFoundException, SQLException
    {
		pollsOn = new Vector<>();
		pollsOff = new Vector<>();
		
        Conn c = new Conn();
        c.open();
		
        ResultSet off = c.select("SELECT ID_Ankiety, Nazwa, Priorytet, Wymagana_liczba, Ilosc_wypelnien FROM TRWAJACE WHERE Zakonczona = " + 1
				+ " ORDER BY ID_Ankiety");
        while(off.next())
            pollsOff.add(new PollsViewPollInformation(Integer.parseInt(off.getString("ID_Ankiety"))
													, off.getString("Nazwa")
													, Integer.parseInt(off.getString("Priorytet"))
													, Integer.parseInt(off.getString("Wymagana_liczba"))
													, Integer.parseInt(off.getString("Ilosc_wypelnien"))));
		off.close();
		
        ResultSet on = c.select("SELECT ID_Ankiety, Nazwa, Priorytet, Wymagana_liczba, Ilosc_wypelnien FROM TRWAJACE WHERE Zakonczona = " + 0 
				+ " ORDER BY ID_Ankiety");
        while(on.next())
            pollsOn.add(new PollsViewPollInformation(Integer.parseInt(on.getString("ID_Ankiety"))
													, on.getString("Nazwa")
													, Integer.parseInt(on.getString("Priorytet"))
													, Integer.parseInt(on.getString("Wymagana_liczba"))
													, Integer.parseInt(on.getString("Ilosc_wypelnien"))));
        on.close();
		
		c.close();
    }
    
    private void viewPolls(PrintWriter out) throws ClassNotFoundException, SQLException
    {
		Paging poolsPaging = new Paging(pollsOn.size(), pollsOff.size());
        HTML h = new HTML();
        out.println("<div style=\""+ h.whiteSquare2() +"width:70vw;\">");
        out.println("<div style=\""+h.naglowek()+"\">Trwające</div>");
        showPollsOn(out);
		out.print("<CENTER>" + poolsPaging.pagesOn(pOn) + "</CENTER>");
        out.println("<div style=\""+h.naglowek()+"\">Zakończone</div>");
        showPollsOff(out);
		out.print("<CENTER>" + poolsPaging.pagesOff(pOff) + "</CENTER></div></div>");
    }
	
	private void showPollsOn(PrintWriter out) throws ClassNotFoundException, SQLException
    {
        out.println("<TABLE align=\"center\" style=\"font-size:2.5vmin;\"><TR style=\"font-size: 2.5vmin;\"><TH style=\"width:5vw;\">ID ankiety</TH><TH style=\"width:13vw;\">Nazwa ankiety</TH><TH style=\"width:5vw;\">Priorytet</TH>"
                        + "<TH style=\"width:5vw;\">Wymagana liczba</TH><TH style=\"width:5vw;\">Ilość wypełnień</TH><TH style=\"width:16vw;\"></TH></TR>");
		HTML h = new HTML();
		int i = 10 * (pOn - 1);
		int lim = (pollsOn.size() >= i + 10) ? i + 10 : pollsOn.size();
		
		if(lim == 0)
			out.println("<TR><TD COLSPAN=\"6\"><CENTER>Brak!</CENTER></TD></TR>");
		else
			for(; i < lim; i++){
                                out.println("<TR style=\"border-bottom:1pt solid black;\"><TD>");
                                out.println(pollsOn.get(i).toString());
                                out.println("\" TYPE=\"submit\" value=\"Zakończ\" "
                                        + "onclick=\"choosePoll(this.id, 1, true)\" style=\"" + h.smallButton()+" background:#F05D5D;\"></div>");
                                out.println("<div style=\"height: 4vh;width:8vw; float:left; margin: -0.3vh 0 0 1vw; font-size: 2vmin;\">"
                                        + "<A style=\""+h.smallButton()+"background: #ccff33;display:block; text-decoration:none; display:table-cell; vertical-align:middle;\" "
                                        + "HREF=\"http://localhost:8080/ProjektAnkiety/PollsView/PoolsManagementPage"
						+ "?pageOn=" + pOn
						+ "&pageOff=" + pOff
						+ "&pollID=" + pollsOn.get(i).getID()
						+ "\">Edycja</A></div>");
                                out.println("<div style=\"height: 4vh;width:16vw; float:left;margin: 0.2vh 0 2vh -3vw; font-size: 2vmin;\">"
                                    + "<A style=\""+h.smallButton()+"background: grey;display:block; text-decoration:none; display:table-cell; vertical-align:middle;\" "
                                    + "HREF=\"http://localhost:8080/ProjektAnkiety/PollsView/Poll_sResults"
                                    + "?pageOn=" + pOn
                                    + "&pageOff=" + pOff
                                    + "&pollID=" + pollsOn.get(i).getID()
                                    + "\">Podgląd wyników</A>");
                                
                                out.println("</div></TD></TR>");
			}

        out.println("</TABLE>");
    }
	private void showPollsOff(PrintWriter out) throws ClassNotFoundException, SQLException
    {
        out.println("<TABLE align=\"center\" style=\"font-size:2.5vmin;\"><TR style=\"font-size: 2.5vmin;\"><TH style=\"width:5vw;\">ID ankiety</TH><TH style=\"width:15vw;\">Nazwa ankiety</TH><TH style=\"width:5vw;\">Priorytet</TH>"
                        + "<TH style=\"width:5vw;\">Wymagana liczba</TH><TH style=\"width:5vw;\">Ilość wypełnień</TH><TH style=\"width:17vw;\"></TH></TR>");
		HTML h = new HTML();
		int i = 10 * (pOff - 1);
		int lim = (pollsOff.size() >= i + 10) ? i + 10 : pollsOff.size();
		
		if(lim == 0)
			out.println("<TR><TD COLSPAN=\"6\"><CENTER>Brak!</CENTER></TD></TR>");
		else
			for(; i < lim; i++){
                                out.println("<TR><TD>");
                                out.println(pollsOff.get(i).toString());
                                out.println("\" TYPE=\"submit\" value=\"Wznów\" "
                                        + "onclick=\"choosePoll(this.id, 0, false)\" style=\"" + h.smallButton()+"\"></div>");
                                out.println("<div style=\"height: 4vh;width:8vw; float:left; margin: -0.3vh 0 2vh 1vw; font-size: 2vmin;\">"
                                        + "<A style=\""+h.smallButton()+"background: grey;display:block; text-decoration:none; display:table-cell; vertical-align:middle;\" "
                                        + "HREF=\"http://localhost:8080/ProjektAnkiety/PollsView/Poll_sResults"
						+ "?pageOn=" + pOn
						+ "&pageOff=" + pOff
						+ "&pollID=" + pollsOff.get(i).getID()
						+ "\">Podgląd wyników</A>");
                                out.println("</div></TD></TR>");
			}

        out.println("</TABLE>");
    }

    private void changePoll(HttpServletRequest request) throws ClassNotFoundException, SQLException 
    {
        Conn c = new Conn();
		c.open();
        ResultSet rs = c.select("SELECT Zakonczona FROM ANKIETY WHERE ID_Ankiety='" + request.getParameter("markedPoll")+"'");
        if(rs.next())
            if(!rs.getString(1).equals(request.getParameter("markedPollSelectedStatus")))
                c.add("UPDATE ANKIETY SET Zakonczona = " + request.getParameter("markedPollSelectedStatus") + " WHERE ID_Ankiety = '"+ request.getParameter("markedPoll") +"'");
		rs.close();
		c.close();
    }
}