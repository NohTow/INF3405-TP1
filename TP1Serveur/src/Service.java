import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class Service extends Thread {
    Socket socket;
    int clientNumber;
    public Service(Socket socket, int nb){
        this.socket = socket;
        this.clientNumber = nb;

    }

    public void run(){
        System.out.println("Connexion d'un client");
        Boolean isConnected = true;
        BufferedReader in = null;
        PrintWriter out=null;
        ObjectInputStream inByte=null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            inByte = new ObjectInputStream(socket.getInputStream());
        }catch(Exception e){
            e.printStackTrace();
        }
        while(isConnected){
            String input = null;
			String nomFichier;
            try {
                input = in.readLine();
                switch(input){
                    case("dc"):
                        isConnected = false;
                        out.println("Deconnexion");
                        System.out.println("Deconnection d'un client");
                        socket.close();
                        break;
                    case("ls"):
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
                        System.out.println(nomFichier);
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
                        System.out.println("fin du transfert");
                        //fichierOutput.close();
                        break;
					case ("download"):
						nomFichier = in.readLine();
						File fileToSend = new File("./"+nomFichier);
						if(fileToSend.exists()){
							out.println("STARTINGTODOWNLOAD");
						}else{
							out.println("DOESNTEXIST");
						}

                    default:
                        out.println("non prévu");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
        // TODO : fermer tout les flux/sockets
    }

}
