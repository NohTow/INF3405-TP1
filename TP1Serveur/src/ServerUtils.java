import java.net.Socket;
import java.time.LocalDateTime;

public class ServerUtils {

    public static void AfficherCommandeRecue(String socketAddress, String commande, String username)
    {
        System.out.println("[" + socketAddress + " ("+username+") - " + LocalDateTime.now() + "]: " + commande);
    }

    public static Boolean CheckWhitespace(String in)
    {
        return !in.contains(" "); // check for whitespace
    }

}
