import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerUtils {

    public static void AfficherCommandeRecue(String socketAddress, String commande, String username)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("[" + socketAddress + " ("+username+") - " + LocalDateTime.now().format(formatter) + "]: " + commande);
    }

    public static Boolean CheckWhitespace(String in)
    {
        return !in.contains(" "); // check for whitespace
    }

}
