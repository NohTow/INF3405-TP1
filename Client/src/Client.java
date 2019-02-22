import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int nbPort;
        String adresseIP="";
        Scanner sc = new Scanner(System.in);
        nbPort = SharedUtils.DemanderPortAUtiliser(sc);
        adresseIP = SharedUtils.DemanderAdresseIPAUtiliser(sc);
        Socket socket = new Socket(adresseIP,nbPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
        ObjectInputStream inputStream = new ObjectInputStream((socket.getInputStream()));
        boolean isConnected = false;

        while(!isConnected){
            String input = null;

            boolean nomUsagerValide = false;
            while(!nomUsagerValide){
                System.out.println("Veuillez entrez votre nom d'usager :");
                input = sc.nextLine();
                if (!("".equals(input))){
                    nomUsagerValide = true;
                    out.println(input);
                }
                else
                    System.out.println("Nom d'utilisateur avec un format incorrect");
                	//TODO on peut faire des dossiers avec des espaces
            }
            if("NEW".equals(in.readLine())){
				System.out.println("Création du compte "+input);
                Boolean passwordValide = false;
                while (!passwordValide) {
                    System.out.println("Veuillez entrez un nouveau password :");
                    input = sc.nextLine();
                    if (ClientUtils.CheckWhitespace(input) && !("".equals(input))) {
                        out.println(input);
                        passwordValide = true;
                        isConnected = true;
						System.out.println("Connexion réussie");
                    }
                    else
                        System.out.println("Format du Password invalide");
                }
            } else {
                int i = 0;
                while (i < 3 && !(isConnected)) {

                    System.out.println("Veuillez rentrer votre mot de passe ");
                    input = sc.nextLine();
                    if (ClientUtils.CheckWhitespace(input)) {
                        out.println(input);
                        if("VALIDE".equals(in.readLine())){
                            isConnected = true;
                            System.out.println("Connexion réussie");
                        }else{
							i++;
							System.out.println("Mauvais mot de passe "+i+"/3");

                        }
                    }else{
                        System.out.println("Format du password invalide");
                    }

                }
            }

        }

        while(isConnected){
            String message = sc.nextLine();
            switch(message){
                case "exit":
                    isConnected = false;
                    out.println("dc");
                    System.out.println(in.readLine());
                    //socket.close();
                    break;
                case "ls":
                    out.println("ls");
                    String temp = in.readLine();
                    if("".equals(temp)){
						System.out.println("Aucun fichier dans votre dossier");
					}
                    while(!("".equals(temp))){
                        System.out.println(temp);
                        temp = in.readLine();
                    }
                    //System.out.println("fin du dossier");
                    break;
                default:
                    Pattern patternUpload = Pattern.compile("^upload\\s(.*)");
                    Pattern patternDownload = Pattern.compile("^download\\s(.*)");
                    Pattern patternDelete = Pattern.compile("^delete\\s(.*)");
                    Matcher matcherUpload = patternUpload.matcher(message);
                    Matcher matcherDownload = patternDownload.matcher(message);
                    Matcher matcherDelete = patternDelete.matcher(message);
                    if(matcherUpload.matches()){
                        if(matcherUpload.group(1).isEmpty()){
                            System.out.println("Veuillez rentrer un nom de fichier");
                        }else{
                            File fileToUpload = new File("./"+matcherUpload.group(1));
                            if(fileToUpload.exists()) {
                                //FileInputStream fichierInput = new FileInputStream(new File("./"+matcher.group(1)));
                                byte[] contentToSend= Files.readAllBytes(fileToUpload.toPath());
                                out.println("upload");
                                out.println(matcherUpload.group(1));
                                outputStream.writeObject(contentToSend);
                                System.out.println("Fin du transfert de "+matcherUpload.group(1));
                            }else{
                                System.out.println("Le fichier n'existe pas");
                            }
                        }

                    }else if(matcherDownload.matches()){
                                if(matcherDownload.group(1).isEmpty()){
                                    System.out.println("Veuillez rentrer un nom de fichier");
                                }else{
                                    out.println("download");
                                    out.println(matcherDownload.group(1));
                                    if("STARTINGTODOWNLOAD".equals(in.readLine())){
										File fileToDownload = new File("./"+matcherDownload.group(1));
                                        byte[] contentToRead = (byte []) inputStream.readObject();
                                        Files.write(fileToDownload.toPath(),contentToRead);
                                        System.out.println("Téléchargement de "+ matcherDownload.group(1)+" terminé");
                                    }else{
                                        System.out.println("Le fichier n'existe pas");
                                    }
                                }

                    }else if(matcherDelete.matches()){
                    			if(matcherDelete.group(1).isEmpty()){
                    				System.out.println("Veuillez rentrer un nom de fichier");
								}else{
                    				out.println("delete");
                    				out.println(matcherDelete.group(1));
                    				System.out.println(in.readLine());
								}
					} else{
                        System.out.println("Veuillez rentrer une commande valide");
                    }
            }

        }
        in.close();
        out.close();
        outputStream.close();
        socket.close();
    }
}
