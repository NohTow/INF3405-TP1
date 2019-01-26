import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        int nbPort=0;

        System.out.println("Debut Serveur");
        Scanner sc = new Scanner(System.in);

        // TODO demander adresse ip et vérifier

        System.out.println("Veuillez saisir un numéro de port");
        //sc.next(); // Ignorer le message envoyé par la console sd
        Boolean portIsOkay=false;
        while (!(portIsOkay)) {
            if(sc.hasNextInt()){
                nbPort = sc.nextInt();
                if(nbPort >= 5000 && nbPort <= 5050) {
                    portIsOkay = true;
                }
            }
            if (!(portIsOkay)) {
                System.out.println("Veuillez saisir un numéro de port correct");
                sc.nextLine(); // Ignorer le message envoyé par la console sd
            }
        }
        int clientNumber = 0;
        try (ServerSocket listener = new ServerSocket()) {
            listener.bind(new InetSocketAddress("127.0.0.1",nbPort));
            System.out.println(listener.getInetAddress());
            while (true) {
                new Service(listener.accept(), clientNumber++).start();
            }
        }
    }


}
