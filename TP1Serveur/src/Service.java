import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.Semaphore;

public class Service extends Thread {
    Socket socket;
    int clientNumber;
    String user;
    Semaphore mutex;
    public Service(Socket socket, int nb){
        this.socket = socket;
        this.clientNumber = nb;
    }

    public void run(){
        System.out.println("Connexion d'un client");
        Boolean isConnected = false;
        BufferedReader in = null;
        PrintWriter out=null;
        ObjectInputStream inByte=null;
        ObjectOutputStream outstream = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            inByte = new ObjectInputStream(socket.getInputStream());
            outstream = new ObjectOutputStream(socket.getOutputStream());
        }catch(Exception e){
            e.printStackTrace();
        }

        while (!isConnected) {
            String input = null;
            try {
                input = in.readLine();
                this.user = input;
                String password = ServerDB.ObtenirPasswordUsager(input);
                System.out.println(password);
                if ("".equals(password)) {
                    out.println("NEW");
                    input = in.readLine();
                    ServerDB.TenterEnregisterNouvelUtilisateur(this.user, input);
                    isConnected = true;
                    System.out.println("Connexion de l'utilisateur "+this.user+" réussie");
                    File dossierUtilisateur = new File("./"+this.user);
                    dossierUtilisateur.mkdir();
                }
                else {
                    out.println("EXIST");
                    int i = 0;
                    while (i < 3 && !(isConnected)) {
                        input = in.readLine();
                        if (password.equals(input)) {
                            out.println("VALIDE");
                            isConnected = true;
                            System.out.println("Connexion de l'utilisateur "+this.user+" réussie");
                        }
                        else {
                            out.println("INVALIDE");
                        }
                        i++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while(isConnected){
            String input = null;
			String nomFichier;
            try {
                input = in.readLine();
				System.out.println(input);
                switch(input){
                    case("dc"):
                        isConnected = false;
                        out.println("Deconnexion");
                        System.out.println("Deconnection d'un client");
                        socket.close();
                        break;
                    case("ls"):
						System.out.println("ls");
                        final File dossier = new File("./");
                        final File[] listFiles = dossier.listFiles();
                        for(File file : listFiles){
                            if(file.isDirectory()){
                                out.println("[Directory] "+file.getName());
                            }else{
                                out.println("[File] "+file.getName());
                            }
                        }
                        out.println("");
                        break;
                    case ("upload"):
                        nomFichier = in.readLine();
                        System.out.println("upload : "+nomFichier);
                        File fileToCreate = new File("./"+nomFichier);
                        //byte buffer[] = new byte[1024];
                        //FileOutputStream fichierOutput = new FileOutputStream("./"+nomFichier);
                        int n;
                        byte[] content = (byte[]) inByte.readObject();
                        Files.write(fileToCreate.toPath(),content);
                        //System.out.println(inByte.read(buffer));
                        /*while((n=inByte.read(buffer))!=-1){
                            System.out.println(buffer);
                            //fichierOutput.write(buffer,0,n);
                        }*/
                        System.out.println("Fin du transfert");
                        //fichierOutput.close();
                        break;
					case ("download"):
						nomFichier = in.readLine();
						System.out.println("download : "+nomFichier);
						File fileToSend = new File("./"+nomFichier);
						if(fileToSend.exists()){
							out.println("STARTINGTODOWNLOAD");
							byte[] contentToSend = Files.readAllBytes(fileToSend.toPath());
							outstream.writeObject(contentToSend);
							System.out.println("Envoi du fichier "+nomFichier);

						}else{
							out.println("DOESNTEXIST");
							System.out.println("Le fichier "+nomFichier+" n'existe pas");
						}
						break;
					case ("delete"):
						nomFichier = in.readLine();
						System.out.println("delete : "+nomFichier);
						File fileToDelete = new File("./"+nomFichier);
						if(fileToDelete.exists()){
							if(fileToDelete.delete()){ // on vérifie que le fichier à été supprimé
								out.println("Le fichier "+nomFichier+" a été supprimé");
							}else{
								out.println("La suppression de "+nomFichier+" a échoué");
							}
						}else{
							out.println("Le fichier "+nomFichier+" n'existe pas");
						}
						break;
                    default:
                        out.println("Cette commande n'a pas été prévue");
						System.out.println("Commande incorrecte");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
				System.out.println("La classe n'a pu être lu ligne lors d'un read object");
                e.printStackTrace();
            }


        }
        // TODO : fermer tout les flux/sockets
    }

}
