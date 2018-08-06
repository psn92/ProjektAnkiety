package pl.ankiety.pomocniczeKlasy;

public class User extends Person implements LoggedPerson{
	protected int pollID;

	public User(String login){
		super(login, false);
		setPool();
	}
	private void setPool(){
		pollID = 0;
	}
	
	public int getPollID(){
		return pollID;
	}
	
}
