import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class ServerDB {

    final static String DatabasePath = "";
    final static String DatabaseName = "Database.txt";
    static File file;

    public static Boolean UtilisateurExisteDeja(String inUsername) {
        RandomAccessFile in = null;
        try {
            in = new RandomAccessFile(file, "rw");
            FileLock lock = in.getChannel().lock();
            try {

                // Ici lire les noms d'utilisateur et renvoyer true si le nom existe
            } finally {
                lock.release();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean TenterEnregisterNouvelUtilisateur(String inUsername, String inPassword) {
        File file = new File(DatabasePath + DatabaseName);
        RandomAccessFile in = null;
        try {
            in = new RandomAccessFile(file, "rw");
            FileLock lock = in.getChannel().lock();
            try {

                // Ici lire les noms d'utilisateur, si le nom existe déjà renvoyer false,
                // sinon créer le nouveau nom et le nouveau password
                // Enregistrer le fichier également
            } finally {
                lock.release();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Boolean InitDB() {
        file = new File(DatabasePath + DatabaseName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
