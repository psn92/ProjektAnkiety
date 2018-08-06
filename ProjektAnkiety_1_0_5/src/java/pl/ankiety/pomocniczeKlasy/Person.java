package pl.ankiety.pomocniczeKlasy;


public class Person{
	protected String login;
	protected boolean admin;
	
	public Person(String login, boolean admin){
		this.login = login;
		this.admin = admin;
	}
	
	public String getLogin(){
		return login;
	}
	
	public boolean getAdmin(){
		return admin;
	}
}
