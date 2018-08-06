package pl.ankiety;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.ankiety.pomocniczeKlasy.HTML;
import pl.ankiety.pomocniczeKlasy.Poll;

public class Poll_sResults extends HttpServlet {
	HTML h = new HTML();
	private int previousPagePageOn;
	private int previousPagePageOff;
	private Poll currentPoll;
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		previousPagePageOn = (request.getParameter("pageOn") != null && !"null".equalsIgnoreCase(request.getParameter("pageOn"))) ? Integer.parseInt(request.getParameter("pageOn")) : 1;
		previousPagePageOff = (request.getParameter("pageOff") != null && !"null".equalsIgnoreCase(request.getParameter("pageOff"))) ? Integer.parseInt(request.getParameter("pageOff")) : 1;
		
		currentPoll = new Poll(Integer.parseInt(request.getParameter("pollID")));
		
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
        out.println("<title>Edycja ankiety</title>");			
        out.println("</head>");
        out.println("<body bgcolor=\"#76b852\"");
		if(request.getParameter("focus") != null && !"null".equals(request.getParameter("focus")))
			out.println(" onload=\"document.getElementById('" + request.getParameter("focus") + "').focus();\"");
		out.println(">");

        out.println("<div style=\""+ h.wrapper() +"\">");
        out.println(h.menuHTML());
		currentPoll.setMode(request);
		content(out, request);
		
		out.println("</div></body>");
		out.println("</html>");
	}
	
	private void content(PrintWriter out, HttpServletRequest request){
		out.println("<div style=\""+ h.whiteSquare() +"\">"
					+ "<div style=\"margin: 0 0 10px 0; color: #4CAF50; font-size: 4vmin;\">");
		out.print("<CENTER><B>" + currentPoll.getTitle() + "</B></CENTER><BR></div>");
			
		out.println("<FORM name=\"formularz1\" METHOD=\"POST\">");
		
		out.println(currentPoll.toStringBuilder());
		
		out.print("<INPUT TYPE=\"hidden\" NAME=\"focus\" VALUE=\"" + request.getParameter("focus") + "\">");
		out.print("<BUTTON ID=\"aktualizacja\" TYPE=\"submit\" STYLE=\"display:none;\"><B>Aktualizacja</B></BUTTON>");
		out.println("</FORM>");
		
		out.print(" <BR><Center><A style=\""+h.bigButton()+"display:block; "
                        + "text-decoration:none; display:table-cell; vertical-align:middle;\""
                        + "HREF=\"http://localhost:8080/ProjektAnkiety/PollsView?admin=1"
			+ "&previousPagePageOn=" + previousPagePageOn
			+ "&previousPagePageOff=" + previousPagePageOff
			+ "\">Powrót do ankiet</A></Center>");
		out.print("</div>"
			+ "</div></div>");
    }
}
