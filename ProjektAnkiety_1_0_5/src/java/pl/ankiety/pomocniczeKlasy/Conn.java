package pl.ankiety.pomocniczeKlasy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn
{
	private String DBURL;
	private String DBUSER;
	private String DBPASS;
	private Connection connection;
	public Statement statement;
	
	public Conn() throws ClassNotFoundException
	{
            Class.forName("com.mysql.jdbc.Driver");
		DBURL = "jdbc:mysql://www.db4free.net:3306/ankiety?useSSL=false";
		DBUSER = "projank";
		DBPASS = "projank1";
	}
	
	public void open() throws SQLException
	{
		connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                statement = connection.createStatement();
	}
	
	public void close() throws SQLException
	{
		statement.close();
        connection.close();
	}
	
	public ResultSet select(String query) throws SQLException
	{
		return statement.executeQuery(query);
	}
	
	public void add(String query) throws SQLException
	{
		statement.execute(query); 
	}
	
	public int update(String query) throws SQLException
	{
		return statement.executeUpdate(query); 
	}
}