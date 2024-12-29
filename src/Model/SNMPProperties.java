package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class SNMPProperties
{
    private static Properties properties;

    // Bloc d'initialisation statique -> s'execute une seule fois lorsque la classe est chargee en memoire
    static
    {
        properties = new Properties();
        try
        {
            properties.load(new FileInputStream("src/snmp.properties"));
        }
        catch (IOException e)
        {
            System.err.println("Impossible de charger snmp.properties. Mode synchrone par défaut.");
        }
    }

    public static boolean isAsynchronous()
    {
        String mode = properties.getProperty("mode", "sync");
        if(mode.toLowerCase().equals("async"))
        {
            System.err.println("Mode asynchrone choisi.");
            return true;
        }
        else
        {
            System.err.println("Mode synchrone choisi.");

            return false;
        }
    }
}