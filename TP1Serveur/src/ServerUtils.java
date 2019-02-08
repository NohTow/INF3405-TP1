import java.net.Socket;
import java.time.LocalDateTime;

public class ServerUtils {

    public static void AfficherCommandeRecue(String socketAddress, String commande)
    {
        System.out.print("[" + socketAddress + " - " + LocalDateTime.now() + "]: " + commande);
    }
}
