package pl.ankiety.pomocniczeKlasy;

public class HTML 
{
    public String menuHTML()
    {
        return "<div style=\""+ menu() +"\">"
                        + "<ul style=\""+ ul() +"\">"
                        + "<li style=\""+li()+"\"><a style=\""+ aMenu() +"\" href=\"http://localhost:8080/ProjektAnkiety/PollsWizard\" >Kreator ankiet</a></li>"
                        + "<li style=\""+li()+"\"><a style=\""+ aMenu() +"\" href=\"http://localhost:8080/ProjektAnkiety/AddUsers\" >Dodawanie użytkownika</a></li>"
                        + "<li style=\""+li()+"\"><a style=\""+ aMenu() +"\" href=\"http://localhost:8080/ProjektAnkiety/UsersView\" >Przegląd użytkowników</a></li>"
                        + "<li style=\""+li()+"\"><a style=\""+ aMenu() +"\" href=\"http://localhost:8080/ProjektAnkiety/PollsView\" >Przegląd ankiet</a></li>"
                        + "<li style=\""+li()+"\"><a style=\""+ aMenu() +"\" href=\"http://localhost:8080/ProjektAnkiety/\" >Wyloguj</a></li></ul></div>";
    }
    
    public String menu()
    {
        return "width: 80vw; margin: auto; padding: 2vh 0 0 0;";
    }
    
    public String aMenu()
    {
        return "border: 0.1vw solid #4CAF50; display: block; color: #4CAF50; text-align: center;"
                + " width: 15vw; text-decoration: none; font-size: 2.4vmin; height:7vh;"
                + "display:table-cell; vertical-align:middle; box-shadow: 0 0 2vw 0 rgba(0, 0, 0, 0.2), 0 1vw 1vw 0 rgba(0, 0, 0, 0.24);";
    }
    
    public String input()
    {
        return "margin: 1.5vh 0 0 0; width: 20vw; height: 5vh; font-size: 2vmin; "
                + "padding: 0 0 0 1vw; outline: 0; background: #f2f2f2; border: 0;";
    }
    
    public String input2()
    {
        return "margin: 2.5vh 0 0 0; width: 41vw; height: 5vh; font-size: 2vmin; "
                + "padding: 0 0 0 1vw; outline: 0; background: #f2f2f2; border: 0;";
    }
    
    public String sele()
    {
        return "width:3vw; height:3.5vh; font-size: 2vmin;";
    }
    
    public String whiteSquare()
    {
        return "width:30vw; background: #ffffff;margin: 15vh auto; padding: 3vh 0 5vh 0;"
                + "box-shadow: 0 0 0.5vw 0 rgba(0, 0, 0, 0.2), 0 0.5vw 0.5vw 0 rgba(0, 0, 0, 0.24);";
    }
    
    public String naglowek()
    {
        return "text-align:center; color: #4CAF50; padding: 1vh; font-size: 3.5vmin;";
    }
    
    public String whiteSquare2()
    {
        return "width:50vw; background: #ffffff;margin: 10vh auto; padding: 3vh 0 5vh 0;"
                + "box-shadow: 0 0 0.5vw 0 rgba(0, 0, 0, 0.2), 0 0.5vw 0.5vw 0 rgba(0, 0, 0, 0.24);";
    }
    
    public String bigButton()
    {
        return "margin: 1.5vh 0 0 0; width: 21vw; height: 6vh; font-size: 3vmin; padding: 0 0 0 1vw; "
                + "background: #4CAF50; outline: 0;border: 0; color: #FFFFFF; cursor: pointer;";
    }
    public String smallButton()
    {
        return "margin: 0 0 0 1vw; width: 8vw; height: 4vh; font-size: 2vmin;"
                + "background: #4CAF50; outline: 0;border: 0; color: #FFFFFF; cursor: pointer;";
    }
    
    public String smallButton2()
    {
        return "margin: 1vh 0 0 0; width: 15vw; height: 4vh; font-size: 2vmin; padding: 0 0 0 1vw; "
                + "background: #4CAF50; outline: 0;border: 0; color: #FFFFFF; cursor: pointer;";
    }
    
    public String wrapper()
    {
        return "margin: 0 auto; text-align: center; font-family: sans-serif;";
    }
    
    public String ul()
    {
        return "list-style-type: none;margin: 0;padding: 0;overflow: hidden;";
    }
    
    public String li()
    {
        return "float: left; background:#ffffff;";
    }
}
