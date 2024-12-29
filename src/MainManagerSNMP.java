import Controller.Controller;
import View.fenetrePrincipale;

public class MainManagerSNMP
{
    public static void main(String[] args)
    {

        fenetrePrincipale view = new fenetrePrincipale();
        Controller controller = new Controller(view);
    }
}