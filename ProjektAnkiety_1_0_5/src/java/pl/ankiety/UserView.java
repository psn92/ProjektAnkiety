package pl.ankiety;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.ankiety.pomocniczeKlasy.HTML;
import pl.ankiety.pomocniczeKlasy.UserStatiscics;


public class UserView extends HttpServlet {
	private int previousPagePageOn;
	private int previousPagePageOff;
	private UserStatiscics markedUser;
	HTML h = new HTML();

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
		previousPagePageOn = (request.getParameter("pageOn") != null && !"null".equalsIgnoreCase(request.getParameter("pageOn"))) ? Integer.parseInt(request.getParameter("pageOn")) : 1;
		previousPagePageOff = (request.getParameter("pageOff") != null && !"null".equalsIgnoreCase(request.getParameter("pageOff"))) ? Integer.parseInt(request.getParameter("pageOff")) : 1;
		
		markedUser = new UserStatiscics(request.getParameter("userID"));
		
		doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Edycja ankiety</title>");			
        out.println("</head>");
        out.println("<body bgcolor=\"#76b852\">");
                
        out.println("<div style=\""+ h.wrapper() +"\">");
        out.println(h.menuHTML());
		
		if(request.getParameter("monthsSelector") != null && !"null".equals(request.getParameter("monthsSelector"))
				&& request.getParameter("yearsSelector") != null && !"null".equals(request.getParameter("yearsSelector")))
			markedUser.setTimePeriod(Integer.parseInt(request.getParameter("monthsSelector")),
					Integer.parseInt(request.getParameter("yearsSelector")));
        content(out);

        out.println("</div></body>");
        out.println("</html>");
        
    }
	
	private void content(PrintWriter out){
		
		out.println("<div style=\""
				+ h.whiteSquare()
				+ "width: 50vw;\">"
				+ "<div style=\"margin: 0 0 10px 0; font-size: 4vmin;\">");
		out.print("<CENTER>"
				+ markedUser.getStatistics()
				+ "<BR><BR>");
		out.print(" <BR><A style=\""+h.bigButton()+"display:block; "
                        + "text-decoration:none; display:table-cell; vertical-align:middle;\" HREF=\"http://localhost:8080/ProjektAnkiety/UsersView?admin=1"
				+ "&previousPagePageOn=" + previousPagePageOn
				+ "&previousPagePageOff=" + previousPagePageOff
				+ "\">Powrót do listy użytkowników</A>");
		out.print("</CENTER></div>"
				+ "</div></div>");
    }
}
