package pl.ankiety.pomocniczeKlasy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import pl.ankiety.PollsManagementPage;

public class Poll{
	private String sqlExceptions = "";
	private int ID;
	private String title;
	private Vector<Question> questions;
	
	public Poll(int ID){
		this.ID = ID;
		questions = new Vector();
		try {
			setPoll();
		} catch (ClassNotFoundException ex) {
			sqlExceptions += "<BR><BR>poleciał wyjątek ClassNotFoundException<BR><BR>" + ex.getMessage();
			Logger.getLogger(PollsManagementPage.class.getName()).log(Level.SEVERE, null, ex);
		} catch (SQLException ex) {
			sqlExceptions += "<BR><BR>poleciał wyjątek SQLException<BR><BR>" + ex.getMessage();
			Logger.getLogger(PollsManagementPage.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	private void setPoll() throws ClassNotFoundException, SQLException{
        Conn c = new Conn();
        c.open();
        
		ResultSet pollConstruction = c.select(
				"SELECT Nazwa, ID_Pytania, Tresc_pytania, ID_Opcji, Tresc_opcji"
						+ " FROM CALOSC"
						+ " WHERE ID_Ankiety = " + ID
						+ " ORDER BY ID_Pytania, ID_Opcji");
        int qstn = -1;
		while(pollConstruction.next()){
			title = pollConstruction.getString("Nazwa");
			if(qstn != pollConstruction.getInt("ID_Pytania")){
				qstn = pollConstruction.getInt("ID_Pytania");
				questions.add(new Question(pollConstruction.getInt("ID_Pytania"), pollConstruction.getString("Tresc_pytania")));
			}
			questions.get(questions.size() - 1).addOption(pollConstruction.getInt("ID_Opcji"), pollConstruction.getString("Tresc_opcji"));
		}
		pollConstruction.close();
		
		ResultSet pollResults = c.select(
			"SELECT ID_Pytania, ID_Opcji, Plec, Wiek"
					+ " FROM WYNIKI"
					+ " WHERE ID_Ankiety = " + ID
					+ " ORDER BY ID_Pytania, ID_Opcji");
		qstn = -1;
		int i = 0;
		int j = 0;
		while(pollResults.next()){
			if(qstn != pollResults.getInt("ID_Pytania")){
				qstn = pollResults.getInt("ID_Pytania");
				while(questions.get(i).getID() != qstn)
					i++;
				j = 0;
			}
			while(questions.get(i).getOptions(j).getID() != pollResults.getInt("ID_Opcji"))
				j++;
			
			questions.get(i).getOptions(j).addPoint(pollResults.getInt("Plec"), pollResults.getInt("Wiek"));
		}
		pollResults.close();
		
		c.close();
    }
	
	public String getTitle(){
		return title;
	}
	
	public void setMode(HttpServletRequest request){
		for(Question q : questions)
			if(request.getParameter("mode" + q.getID()) != null && !"null".equals(request.getParameter("mode" + q.getID())))
				q.setMode(request.getParameter("mode" + q.getID()));
	}
	
	public StringBuilder toStringBuilder(){
		StringBuilder tmp = new StringBuilder(sqlExceptions);
		for(int i = 0; i < questions.size(); i++)
			tmp.append(i + 1).append(". ").append(questions.get(i).toString());
		
		return tmp;
	}
}
