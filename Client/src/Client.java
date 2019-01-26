import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        // TODO Demander le port & l'adresse et les vérifier
        //int nbPort = sc.nextInt();
        Socket socket = new Socket("127.0.0.1",5050);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        DataOutputStream outputStream = new DataOutputStream((socket.getOutputStream()));

        Boolean isWriting = true;
        while(isWriting){
            String message = sc.nextLine();
            if(message==null || message.isEmpty()){
                isWriting = false;
            }
            out.println(message);
            outputStream.write(toBytes(message.toCharArray()));
            System.out.println(in.readLine());
        }

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
