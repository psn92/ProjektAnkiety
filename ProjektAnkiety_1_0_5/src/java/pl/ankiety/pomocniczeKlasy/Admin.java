package pl.ankiety.pomocniczeKlasy;


public class Admin extends Person implements LoggedPerson{

	public Admin(String login){
		super(login, true);
	}
	
}
