import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.concurrent.Semaphore;

public class Service extends Thread {
	Socket socket;
	String user;

	public Service(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		Boolean isConnected = false;
		BufferedReader in = null;
		PrintWriter out = null;
		ObjectInputStream inByte = null;
		ObjectOutputStream outstream = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			inByte = new ObjectInputStream(socket.getInputStream());
			outstream = new ObjectOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

		while (!isConnected) {
			String input = null;
			try {
				input = in.readLine();
				this.user = input;
				ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Tentative de connexion de l'utilisateur",this.user);
				String password = ServerDB.ObtenirPasswordUsager(input);
				if ("".equals(password)) {
					out.println("NEW");
					input = in.readLine();
					ServerDB.enregisterNouvelUtilisateur(this.user, input);
					isConnected = true;
					ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Création de l'utilisateur réussie",this.user);
					ServerDB.createUser(this.user);
					ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Connexion de l'utilisateur réussie",this.user);
				} else {
					out.println("EXIST");
					int i = 0;
					while (i < 3 && !(isConnected)) {
						input = in.readLine();
						if (password.equals(input)) {
							out.println("VALIDE");
							isConnected = true;
							ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Connexion de l'utilisateur réussie",this.user);
						} else {
							out.println("INVALIDE");
						}
						i++;
						if(i==3){
							ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Tentative de connexion de l'utilisateur échoué",this.user);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		while (isConnected) {
			String input = null;
			String nomFichier;
			try {
				input = in.readLine();
				switch (input) {
					case ("dc"):
						isConnected = false;
						out.println("Deconnexion");
						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Deconnexion de l'utilisateur",this.user);
						socket.close();
						break;
					case ("ls"):
						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"ls",this.user);
						final File dossier = new File("./storage/" +user+"/");
						final File[] listFiles = dossier.listFiles();
						for (File file : listFiles) {
							if (file.isDirectory()) {
								out.println("[Directory] " + file.getName());
							} else {
								out.println("[File] " + file.getName());
							}
						}
						out.println("");
						break;
					case ("upload"):
						nomFichier = in.readLine();

						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"upload : "+nomFichier,this.user);
						File fileToCreate = new File("./storage/" +user+"/"+ nomFichier);
						//byte buffer[] = new byte[1024];
						//FileOutputStream fichierOutput = new FileOutputStream("./"+nomFichier);
						int n;
						byte[] content = (byte[]) inByte.readObject();
						Files.write(fileToCreate.toPath(), content);
						//System.out.println(inByte.read(buffer));
                        /*while((n=inByte.read(buffer))!=-1){
                            System.out.println(buffer);
                            //fichierOutput.write(buffer,0,n);
                        }*/
						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Fin du transfert de : "+nomFichier,this.user);
						//fichierOutput.close();
						break;
					case ("download"):
						nomFichier = in.readLine();
						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"download : "+nomFichier,this.user);
						File fileToSend = new File("./storage/" +user+"/" + nomFichier);
						if (fileToSend.exists()) {
							out.println("STARTINGTODOWNLOAD");
							byte[] contentToSend = Files.readAllBytes(fileToSend.toPath());
							outstream.writeObject(contentToSend);
							ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Envoi du fichier : "+nomFichier,this.user);

						} else {
							out.println("DOESNTEXIST");
							ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Le fichier n'existe pas : "+nomFichier,this.user);
						}
						break;
					case ("delete"):
						nomFichier = in.readLine();
						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"delete : "+nomFichier,this.user);
						File fileToDelete = new File("./storage/" +user+"/" + nomFichier);
						if (fileToDelete.exists()) {
							if (fileToDelete.delete()) { // on vérifie que le fichier à été supprimé
								out.println("Le fichier " + nomFichier + " a été supprimé");
							} else {
								out.println("La suppression de " + nomFichier + " a échoué");
							}
						} else {
							out.println("Le fichier " + nomFichier + " n'existe pas");
						}
						break;
					default:
						out.println("Cette commande n'a pas été prévue");
						ServerUtils.AfficherCommandeRecue(this.socket.getRemoteSocketAddress().toString().replace("\\",""),"Commande incorrecte",this.user);
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
