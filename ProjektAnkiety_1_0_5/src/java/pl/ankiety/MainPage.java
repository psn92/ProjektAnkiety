package pl.ankiety;

import pl.ankiety.pomocniczeKlasy.Conn;
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
import pl.ankiety.pomocniczeKlasy.Admin;
import pl.ankiety.pomocniczeKlasy.HTML;
import pl.ankiety.pomocniczeKlasy.LoggedPerson;
import pl.ankiety.pomocniczeKlasy.User;

public class MainPage extends HttpServlet {
	LoggedPerson signedUs;
	//User signedUser;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>Logowanie</title>");			
		out.println("</head>");
		out.println("<body bgcolor=\"#76b852\">");
		
		loginPAGEcontent(out);
		
		out.println("</body>");
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
		out.println("<title>Strona główna</title>");			
		out.println("</head>");
		out.println("<body bgcolor=\"#76b852\">");
		
		if(request.getParameter("status") != null && "-1".equals(request.getParameter("status"))){
			try {
				checkDATA(out, request);
			} catch (ClassNotFoundException | SQLException ex) {
				Logger.getLogger(MainPage.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		else{
			if(request.getParameter("admin") != null && "1".equals(request.getParameter("admin")))
				adminPAGEcontent(out);
			else
				userPAGEcontent(out);

			out.println("</body>");
			out.println("</html>");
		}
		
	}
	
	protected void loginPAGEcontent(PrintWriter out){
		HTML h = new HTML();
                out.println("<div style=\""+ h.wrapper() +"\">"
                    + "<div style=\""+ h.whiteSquare() +"\" >"
                    + "<form style=\"padding: 1vh 0 2vh 0;\" ACTION = \"http://localhost:8080/ProjektAnkiety/?status=-1\" METHOD=\"POST\">"
                    + "<input name=\"log\" type=\"text\" placeholder=\"login\" style=\""+ h.input() +"\" />"
                    + "<input name=\"pass\" type=\"password\" placeholder=\"hasło\" style=\""+ h.input() +"\" />"
                    + "<button style=\""+ h.bigButton() +"\">LOGIN</button></form></div></div>");
	}
	
	protected void checkDATA(PrintWriter out, HttpServletRequest request) throws ClassNotFoundException, SQLException{
		int mode = 2;
		
		Conn c = new Conn();
		c.open();

		ResultSet rs = c.select("SELECT Haslo, Administrator FROM UZYTKOWNICY WHERE ID_Uzytkownika='"
				+ request.getParameter("log") + "'"
				+ " AND Zatrudniony = 1");
		if(rs.next())
			if(rs.getString(1).equals(request.getParameter("pass")))
				mode = rs.getInt(2);
		c.close();

		HTML h = new HTML();
                
		switch(mode){
			case 0:
				signedUs = new User(request.getParameter("log"));
				out.print("<FORM ACTION = \"http://localhost:8080/ProjektAnkiety/?admin=0\""
						+ " METHOD=\"POST\">"
						+ "<BUTTON ID=\"klik\" STYLE=\"display:none;\">Sprawdzanie danych</BUTTON>"
						+ "</FORM>");
				break;
			case 1:
				signedUs = new Admin(request.getParameter("log"));
				out.print("<FORM ACTION = \"http://localhost:8080/ProjektAnkiety/AddUsers\""
						+ " METHOD=\"POST\">"
						+ "<BUTTON ID=\"klik\" onClick=\"tab_submit('klik')\" STYLE=\"display:none;\">Sprawdzanie danych</BUTTON>"
						+ "</FORM>");
				break;
			default:
				out.println("<div style=\""+ h.wrapper() +"\">"
                                + "<div style=\""+ h.whiteSquare() +"\">"
                                + "<div style=\""+h.naglowek()+"\">Niepoprawny login lub hasło.</div>"
                                + "<form ACTION = \"http://localhost:8080/ProjektAnkiety/\">"
                                + "<button style=\""+ h.bigButton() +"\">Powrót do logowania</button></form></div></div>");
                                
		}
		
		out.print("<SCRIPT>"
				+ "klik.click();"
				+ "</SCRIPT>");
	}
	
	protected void adminPAGEcontent(PrintWriter out){
		HTML h = new HTML();
                
                out.println("<div style=\""+ h.wrapper() +"\">");
                out.println(h.menuHTML());
                out.println("</div>");
	}
	
	protected void userPAGEcontent(PrintWriter out){
		HTML h = new HTML();
		out.print("<P ALIGN=\"center\">"
				+ " <A style=\""+h.bigButton()+"background: white; color: #4CAF50; display:block; text-decoration:none; display:table-cell; vertical-align:middle;\" "
				+ "HREF=\"http://localhost:8080/ProjektAnkiety/\"><b>Wyloguj<b><br>(" + signedUs.getLogin() + ")</A><br>"
				+ "Miejsce na stronę użytkownika. Zaloguj się w aplikacji mobilnej."
				+ "</P>");
		//signedUser = (User) signedUs;
		
		out.println("<MENU>"
					+ "<LI TYPE = \"square\"><A target=\"_blank\" HREF=\"https://www.google.pl/\">Google</A>"
					+ "</MENU>");
	}

}
