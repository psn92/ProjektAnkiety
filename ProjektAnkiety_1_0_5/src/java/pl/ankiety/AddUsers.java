package pl.ankiety;

import pl.ankiety.pomocniczeKlasy.Conn;
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

public class AddUsers extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Dodawanie użytkownika</title>");			
		out.println("</head>");
		out.println("<body bgcolor=\"#76b852\">");
		
		HTML h = new HTML();
                
                out.println("<div style=\""+ h.wrapper() +"\">");
                out.println(h.menuHTML());
                out.println("<div style=\""+h.whiteSquare()+"\">"
                                + "<div style=\""+h.naglowek()+"\">");
                
		if(request.getParameter("log") == null && request.getParameter("pass1") == null && request.getParameter("pass2") == null)
			addData(out, request);
		else
			try {
				checkData(out, request);
		} catch (ClassNotFoundException | SQLException ex) {
			out.println(Logger.getLogger(ex.getMessage()));
			Logger.getLogger(AddUsers.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		out.println("</div></body>");
		out.println("</html>");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Dodawanie użytkownika</title>");			
		out.println("</head>");
		out.println("<body bgcolor=\"#76b852\">");
		
		HTML h = new HTML();
                
                out.println("<div style=\""+ h.wrapper() +"\">");
                out.println(h.menuHTML());
                out.println("<div style=\""+h.whiteSquare()+"\">"
                                + "<div style=\""+h.naglowek()+"\">");
		
		addData(out, request);
		
		out.println("</div></body>");
		out.println("</html>");
        
    }
	
	private void addData(PrintWriter out, HttpServletRequest request){
		HTML h = new HTML();
                out.println("</div><form style=\"padding: 1vh 0 2vh 0;\"><input name=\"log\" type=\"text\" placeholder=\"login\" value=\"");
                if(request.getParameter("log") != null)
			out.print(request.getParameter("log"));
                out.println("\" style=\""+ h.input() +"\" />"
                    + "<input name=\"pass1\" type=\"password\" placeholder=\"hasło\" style=\""+ h.input() +"\" />"
                    + "<input name=\"pass2\" type=\"password\" placeholder=\"hasło\" style=\""+ h.input() +"\" />"
                    + "<button style=\""+ h.bigButton() +"\">Dodaj</button></form></div>");
	}
	
	private void checkData(PrintWriter out, HttpServletRequest request)
			throws ClassNotFoundException, SQLException{
		boolean log = checkLogin(out, request.getParameter("log"));
		boolean pass = checkPassword(out, request.getParameter("pass1"), request.getParameter("pass2"));
		
		if(log && pass){
			addUser(request);
			out.print("Dodano użytkownika");
		}
		else{
                    HTML h = new HTML();
                    out.println("</div>"
                            + "<form ACTION = \"http://localhost:8080/ProjektAnkiety/AddUsers?log="+ request.getParameter("log") +""
                            + "\" METHOD=\"POST\">"
                            + "<button style=\""+ h.bigButton() +"\">Popraw dane</button></form></div></div>");
		}
	}
	
	private boolean checkLogin(PrintWriter out, String login)
			throws ClassNotFoundException, SQLException{
		if("".equals(login)){
			out.println("Nie wpisałeś loginu<BR>");
			return false;
		}
		Conn c = new Conn();
		c.open();
		ResultSet rs = c.select("SELECT COUNT(*) FROM UZYTKOWNICY WHERE ID_Uzytkownika='"
				+ login + "'");
		rs.next();
		if(rs.getInt(1)==1){
			out.println("Login zajęty.<BR>");
			return false;
		}
		c.close();
		
		return true;
	}
	
	private boolean checkPassword(PrintWriter out, String password1, String password2)
			throws ClassNotFoundException, SQLException{
		boolean passwordIsOK = true;
		
		if(password1.length() < 4 && password2.length() < 4){
			passwordIsOK = false;
			out.println("Hało musi mieć minimum cztery znaki.<BR>");
		}
		Pattern p1 = Pattern.compile("^[\\d\\p{Lu}\\p{Ll}]*$");
		Pattern p2 = Pattern.compile("^[\\d\\p{Lu}\\p{Ll}]*$");
		Matcher m1 = p1.matcher(password1);
		Matcher m2 = p2.matcher(password1);
		if(!m1.matches() || !m2.matches()){
			passwordIsOK = false;
			out.println("Hasło może skłać się wyłącznie z cyfr i liter.<BR>");
		}
		if(!password1.equals(password2)){
			passwordIsOK = false;
			out.println("Hasła są różne.<BR>");
		}
		
		return passwordIsOK;
	}
	
	private void addUser(HttpServletRequest request)
			throws ClassNotFoundException, SQLException{
		Conn c = new Conn();
		c.open();
		c.add("INSERT INTO UZYTKOWNICY (ID_Uzytkownika, Administrator, Haslo)"
				+ " VALUES ('"+request.getParameter("log")
				+ "',0,'" + request.getParameter("pass1") + "')");
		c.close();
	}

}
