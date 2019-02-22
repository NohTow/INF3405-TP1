import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Semaphore;

public class ServerDB {

    final static String DatabaseName = "Database.txt"; //nom du fichier où sont stocker les user:pass

    static File file;

    public static String ObtenirPasswordUsager(String inUsername) {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(DatabaseName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String input = null;
            while ((input = br.readLine()) != null) {
                String[] parts = input.split(":");
                if (parts[0].equals(inUsername)){
                    return parts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static Boolean enregisterNouvelUtilisateur(String inUsername, String inPassword) {
        try {
            Files.write(Paths.get(DatabaseName), (inUsername + ":" + inPassword +'\n').getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
        }
        return true;
    }

    public static Boolean InitDB() {
        file = new File(DatabaseName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File storage = new File("storage");
        if(!(storage.exists())){
            storage.mkdir();
        }
        return true;
    }
    public static void createUser(String name){
		File dossierUtilisateur = new File("./storage/"+name+"/");
		dossierUtilisateur.mkdir();
	}
}
