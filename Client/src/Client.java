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
        Scanner sc = new Scanner(System.in);
        // TODO Demander le port & l'adresse et les vérifier
        //int nbPort = sc.nextInt();
        Socket socket = new Socket("127.0.0.1",5050);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
        ObjectInputStream inputStream = new ObjectInputStream((socket.getInputStream()));
        Boolean isConnected = true;
        while(isConnected){
            String message = sc.nextLine();
            switch(message){
                case "dc":
                    isConnected = false;
                    out.println("dc");
                    System.out.println(in.readLine());
                    //socket.close();
                    break;
                case "ls":
                    out.println("ls");
                    String temp = in.readLine();
                    while(!("".equals(temp))){
                        System.out.println(temp);
                        temp = in.readLine();
                    }
                    //System.out.println("fin du dossier");
                    break;
                default:
                    Pattern patternUpload = Pattern.compile("^upload\\s(.*)");
                    Pattern patternDownload = Pattern.compile("^download\\s(.*)");
                    Matcher matcherUpload = patternUpload.matcher(message);
                    Matcher matcherDownload = patternDownload.matcher(message);
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
                                    File fileToDownload = new File("./"+matcherDownload.group(1));
                                    if("STARTINGTODOWNLOAD".equals(in.readLine())){
                                        byte[] contentToRead = (byte []) inputStream.readObject();
                                        Files.write(fileToDownload.toPath(),contentToRead);
                                        System.out.println("Téléchargement de "+ matcherDownload.group(1)+" terminé");
                                    }else{
                                        System.out.println("Le fichier n'existe pas");
                                    }
                                }

                    }else{
                        System.out.println("Veuillez rentrer une commande valide");
                    }
            }

            //outputStream.write(toBytes(message.toCharArray()));
            //System.out.println(in.readLine());
        }
        in.close();
        out.close();
        outputStream.close();
        socket.close();
    }

    static byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }
}
