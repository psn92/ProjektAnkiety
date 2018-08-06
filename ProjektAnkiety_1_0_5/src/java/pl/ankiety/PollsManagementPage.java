package pl.ankiety;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.ankiety.pomocniczeKlasy.Conn;
import pl.ankiety.pomocniczeKlasy.HTML;
import pl.ankiety.pomocniczeKlasy.PollsViewPollInformation;


public class PollsManagementPage extends HttpServlet {
	private int previousPagePageOn;
	private int previousPagePageOff;
	PollsViewPollInformation markedPoll;
	private boolean saved;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
		previousPagePageOn = (request.getParameter("pageOn") != null && !"null".equalsIgnoreCase(request.getParameter("pageOn"))) ? Integer.parseInt(request.getParameter("pageOn")) : 1;
		previousPagePageOff = (request.getParameter("pageOff") != null && !"null".equalsIgnoreCase(request.getParameter("pageOff"))) ? Integer.parseInt(request.getParameter("pageOff")) : 1;
		
		try {
			setPoll(Integer.parseInt(request.getParameter("pollID")));
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PollsManagementPage.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			Logger.getLogger(PollsManagementPage.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		saved = false;
		
		doPost(request, response);
    }
	private void setPoll(int ID) throws ClassNotFoundException, SQLException{
        Conn c = new Conn();
        c.open();
        
		ResultSet poll = c.select("SELECT ID_Ankiety, Nazwa, Priorytet, Wymagana_liczba, Ilosc_wypelnien FROM TRWAJACE WHERE ID_Ankiety = '" + ID + "'");
        if(poll.next())
            markedPoll = new PollsViewPollInformation(Integer.parseInt(poll.getString("ID_Ankiety"))
													, poll.getString("Nazwa")
													, Integer.parseInt(poll.getString("priorytet"))
													, Integer.parseInt(poll.getString("wymagana_liczba"))
													, Integer.parseInt(poll.getString("Ilosc_wypelnien")));
		poll.close();
		
		c.close();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Edycja ankiety</title>");			
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
			out.println("poleciał wyjątek ClassNotFoundException\n" + ex.getMessage());
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
			out.println("poleciał wyjątek SQLException\n" + ex.getMessage());
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.println("</div></body>");
        out.println("</html>");
        
    }
	
	private void content(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException
    {        
        HTML h = new HTML();
        if(!saved){
			out.println("<FORM name=\"formularz1\" METHOD=\"POST\">");
			out.println(markedPoll.toStringOneRowVersion());
			out.println("</FORM>");
			saved = true;
		}
		else{
			changePoll(request);
			out.println("<div style=\""+ h.whiteSquare() +"\">"
					+ "<div style=\"color: #4CAF50; font-size: 4vmin;\">");
			out.print("<Center>Zmiany zostały zapisane.</Center></div>");
		}
		out.print(" <BR><Center><A style=\""+h.bigButton()+"display:block; "
                        + "text-decoration:none; display:table-cell; vertical-align:middle;\""
                        + "HREF=\"http://localhost:8080/ProjektAnkiety/PollsView?admin=1"
			+ "&previousPagePageOn=" + previousPagePageOn
			+ "&previousPagePageOff=" + previousPagePageOff
			+ "\">Powrót do ankiet</A></Center>");
		out.print("</div>");
    }
	
	private void changePoll(HttpServletRequest request) throws ClassNotFoundException, SQLException 
    {
		Conn c = new Conn();
		c.open();
		ResultSet rs = c.select("SELECT priorytet, wymagana_liczba FROM ANKIETY WHERE ID_Ankiety = " + markedPoll.getID());
		c.add("UPDATE ANKIETY"
				+ " SET priorytet = " + request.getParameter("pools_priority")
				+ ", wymagana_liczba = " + request.getParameter("pools_n")
				+ " WHERE ID_Ankiety = " + markedPoll.getID());
		rs.close();
		c.close();
	}
}
