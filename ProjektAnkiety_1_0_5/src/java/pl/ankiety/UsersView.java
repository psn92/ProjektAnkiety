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

public class UsersView extends HttpServlet 
{
	public Vector<String> usersOn;
	public Vector<String> usersOff;
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
        request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Edycja użytkownika</title>");			
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
			out.println("poleciał wyjątek ClassNotFoundException");
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) 
        {
			out.println("poleciał wyjątek SQLException");
            Logger.getLogger(UsersView.class.getName()).log(Level.SEVERE, null, ex);
        }

        out.println("</div></body>");
        out.println("</html>");
        
    }
    
    private void content(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
		if(!previousPages){
			if(request.getParameter("pageOn") != null && !"null".equalsIgnoreCase(request.getParameter("pageOn")))
				pOn = Integer.parseInt(request.getParameter("pageOn"));
			else
				pOn = 1;
			if(request.getParameter("pageOff") != null && !"null".equalsIgnoreCase(request.getParameter("pageOff")))
				pOff = Integer.parseInt(request.getParameter("pageOff"));
			else
				pOff = 1;
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
				+ "<INPUT TYPE=\"hidden\" NAME=\"markedUserSelectedStatus\" VALUE=\"" + request.getParameter("markedUserSelectedStatus") + "\">"
				+ "<INPUT TYPE=\"hidden\" NAME=\"markedUser\" VALUE=\"" + request.getParameter("markedUser") + "\">");
        
		if(request.getParameter("markedUser") != null && !"null".equals(request.getParameter("markedUser")))
			changeUser(request);
		
		setUsers();
		viewUsers(out, request);
		
		out.println("<SCRIPT type=\"text/javascript\">"
				+ "	function chooseUser(user, status, On){"
                + "		document.forms['formularz1'].elements['markedUser'].value = user;"
                + "		document.forms['formularz1'].elements['markedUserSelectedStatus'].value = status;"
				+ "		if(document.forms['formularz1'].elements['pageOn'].value == " + (usersOn.size() + 9) / 10
				+ " && On && " + (usersOn.size() > 10 && usersOn.size() % 10 == 1) + ")"
				+ "			document.forms['formularz1'].elements['pageOn'].value = document.forms['formularz1'].elements['pageOn'].value - 1;"
				+ "		if(document.forms['formularz1'].elements['pageOff'].value == " + (usersOff.size() + 9) / 10
				+ " && !On && " + (usersOff.size() > 10 && usersOff.size() % 10 == 1) + ")"
				+ "			document.forms['formularz1'].elements['pageOff'].value = document.forms['formularz1'].elements['pageOff'].value - 1;"
				+ "	};"
				+ "	function setPageOn(k){"
				+ "		if(k >= 1 && k <= " + (usersOn.size() + 9) / 10 + ")"
                + "			document.forms['formularz1'].elements['pageOn'].value = k;"
				+ "	};"
				+ "	function setPageOff(k){"
				+ "		if(k >= 1 && k <= " + (usersOff.size() + 9) / 10 + ")"
                + "			document.forms['formularz1'].elements['pageOff'].value = k;"
				+ "	};"
                + "</SCRIPT>");
		
		out.println("</FORM>");
    }
	
	private void setUsers() throws ClassNotFoundException, SQLException
    {
		usersOn = new Vector<>();
		usersOff = new Vector<>();
		
        Conn c = new Conn();
        c.open();
        ResultSet off = c.select("SELECT * FROM UZYTKOWNICY WHERE Zatrudniony='" + 0 + "' AND Administrator = '0'");
        while(off.next())  
            usersOff.add(off.getString(1));
		off.close();
		
        ResultSet on = c.select("SELECT * FROM UZYTKOWNICY WHERE Zatrudniony='" + 1 + "' AND Administrator = '0'");
        while(on.next())
            usersOn.add(on.getString(1));
        on.close();
		
        c.close();
    }
    
    private void viewUsers(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
	Paging usersPaging = new Paging(usersOn.size(), usersOff.size());
        HTML h = new HTML();
        out.println("<div style=\"width:80vw; margin: auto;\">");
        out.println("<div style=\""+ h.whiteSquare() +"width: 40vw; background:white; float: left;\"><div style=\""+h.naglowek()+"\">Zatrudnieni</div>");
        showUsersOn(out, request);
        out.print(usersPaging.pagesOn(pOn));
	out.println("</div><div style=\""+ h.whiteSquare() +"width: 40vw; background:white; float: left;\"><div style=\""+h.naglowek()+"\">Zwolnieni</div>");
	showUsersOff(out, request);
	out.print(usersPaging.pagesOff(pOff) + "</div></div>");
    }
	
    private void showUsersOn(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
        HTML h = new HTML();
        	
		int i = 10 * (pOn - 1);
		int lim = (usersOn.size() >= i + 10) ? i + 10 : usersOn.size();
		
		if(lim == 0)
			out.println("Brak!");
		else
			for(; i < lim; i++){
                            
                            out.println("<div style=\"padding: 1vh 0 0 0;height: 4vh;width:8vw; float:left; margin: 0 0 0 4vw; font-size: 2vmin;text-align:right;\">"+ usersOn.get(i) + "</div>");
                            out.println("<div><button onclick=\"chooseUser(this.id, 0, true)\" id=\""+ usersOn.get(i) + "\""
                                    + "style=\""+h.smallButton()+"border: 0.2vh solid white;width:10vw; float:left; margin: 0 0 0 1vw; background:#F05D5D\">" + "Zwolnij</button></div>");
                            out.println("<div style=\"padding: 1vh 0 1vh 0;height: 4vh;width:10vw; float:left; margin: -1vh 4vw 0 1vw; font-size: 2vmin;\">"
                                    + "<A style=\""+h.smallButton()+"background: grey; width:8vw; display:block; text-decoration:none; display:table-cell; vertical-align:middle;\" "
                                    + "HREF=\"http://localhost:8080/ProjektAnkiety/UsersView/UserView"
                                    + "?userID=" + usersOn.get(i)
                                    + "&pageOn=" + pOn
                                    + "&pageOff=" + pOff
                                    + "\">Podgląd wyników</A>"
                                    + "</div><BR>");
			}
    }
	private void showUsersOff(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException
    {
		int i = 10 * (pOff - 1);
		int lim = (usersOff.size() >= i + 10) ? i + 10 : usersOff.size();
		HTML h = new HTML();
		if(lim == 0)
			out.println("Brak!");
		else
			for(; i < lim; i++){
                            
                            out.println("<div style=\"padding: 1vh 0 0 0;height: 4vh;width:8vw; float:left; margin: 0 0 0 4vw; font-size: 2vmin;text-align:right;\">"+ usersOff.get(i) + "</div>");
                            out.println("<div><button onclick=\"chooseUser(this.id, 1, false)\" id=\""+ usersOff.get(i) + "\""
                                    + "style=\""+h.smallButton()+"border: 0.2vh solid white;width:10vw; float:left; margin: 0 0 0 1vw;\">" + "Zatrudnij</button></div>");
                            out.println("<div style=\"padding: 1vh 0 1vh 0;height: 4vh;width:10vw; float:left; margin: -1vh 4vw 0 1vw; font-size: 2vmin;\"><A style=\""+h.smallButton()+"background: grey; width:8vw; display:block; text-decoration:none; display:table-cell; vertical-align:middle;\" HREF=\"http://localhost:8080/ProjektAnkiety/UsersView/UserView"
                                    + "?userID=" + usersOff.get(i)
                                    + "&pageOn=" + pOn
                                    + "&pageOff=" + pOff
                                    + "\">Podgląd wyników</A>"
                                    + "</div><BR>");
			}
    }

    private void changeUser(HttpServletRequest request) throws ClassNotFoundException, SQLException 
    {
        Conn c = new Conn();
		c.open();
        ResultSet rs = c.select("SELECT Zatrudniony FROM UZYTKOWNICY WHERE ID_Uzytkownika='"+ request.getParameter("markedUser") +"'");
        if(rs.next())
            if(!rs.getString(1).equals(request.getParameter("markedUserSelectedStatus")))
                c.update("UPDATE UZYTKOWNICY SET Zatrudniony = " + request.getParameter("markedUserSelectedStatus") + " WHERE ID_Uzytkownika ='"+ request.getParameter("markedUser") +"'");
		rs.close();
		c.close();
    }
}