import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class SharedUtils {

    public static int DemanderPortAUtiliser(Scanner sc)
    {
        int nbPort = 0;
        Boolean obtenuPortValide = false;

        while(!obtenuPortValide) {
            System.out.println("Veuillez saisir un numéro de port entre 5000 et 5050");

            if (sc.hasNextInt()) {
                nbPort = sc.nextInt();
                if(nbPort >= 5000 && nbPort <= 5050) { // Vérifie si le port est entre 5000 et 5050
					//5050 compris ?
                    obtenuPortValide = true;
                    sc.nextLine();
                }
            }
            if (!obtenuPortValide) {
                System.out.println("Erreur: Port invalide");
                sc.nextLine();
            }
        }
        return nbPort;
    }

    public static String DemanderAdresseIPAUtiliser(Scanner sc)
    {
        String adresseIP = "";
        Boolean obtenuAdresseValide = false;

        while(!obtenuAdresseValide) {
            Boolean erreur = false;
            System.out.println("Veuillez saisir l'adresse IP du serveur");
            String input = sc.nextLine();

            // Adresse doit ne contenir que des nombres et le caractère point (.)
            if (Pattern.matches("[0-9][0-9.]*[0-9]", input)) {
                String[] parties = input.split("\\.");
                // Adresse doit contenir quatre nombres séparé d'un point (.)
                if (parties.length == 4) {
                    // Chaque octet doit être de 0 à 255 inclusivement pour exclure les adresses de broadcast
                    for (int i = 0; i < parties.length; i++) {
                        int nb = parseInt(parties[i]);
                        if (nb < 0 || nb > 255) {
                            erreur = true;
                            break;
                        }
                    }
                }
                else erreur = true;
            }
            else erreur = true;

            if (erreur)
                System.out.println("Erreur: Adresse IP saisie invalide");
            else {
                adresseIP = input;
                obtenuAdresseValide = true;
            }
        }
        return adresseIP;
    }
}
