import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        // TODO Demander le port & l'adresse et les vérifier
        //int nbPort = sc.nextInt();
        Socket socket = new Socket("127.0.0.1",5050);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ObjectOutputStream outputStream = new ObjectOutputStream((socket.getOutputStream()));
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
                    Pattern pattern = Pattern.compile("^upload\\s(.*)");
                    Matcher matcher = pattern.matcher(message);
                    if(matcher.matches()){
                        System.out.println("UPLOAD RECOGNIZED");
                        System.out.println(matcher.group(1));
                        FileInputStream fichierInput = new FileInputStream(new File("./"+matcher.group(1)));
                        byte buffer[] = new byte[1024];
                        out.println("upload");
                        out.println(matcher.group(1));
                        int n;
                        while((n=fichierInput.read(buffer))!=-1){
                            System.out.println(buffer);
                            outputStream.write(buffer,0,n);
                        }
                        System.out.println("fin transfert");
                        fichierInput.close();
                    }else {
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
