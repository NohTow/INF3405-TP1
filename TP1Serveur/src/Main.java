import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) throws Exception {
        int nbPort;
        String adresseIP="";

        System.out.println("Debut Serveur");
        Scanner sc = new Scanner(System.in);
        nbPort = SharedUtils.DemanderPortAUtiliser(sc);
        adresseIP = SharedUtils.DemanderAdresseIPAUtiliser(sc);

        // Initialize la DB
        ServerDB.InitDB();

        int clientNumber = 0;
        try (ServerSocket listener = new ServerSocket()) {
            listener.bind(new InetSocketAddress(adresseIP,nbPort));
            System.out.println(listener.getInetAddress());
            while (true) {
                new Service(listener.accept(), clientNumber++).start();
            }
        }
    }


}
