import java.io.*;
import java.net.Socket;

public class Service extends Thread {
    Socket socket;
    int clientNumber;
    public Service(Socket socket, int nb){
        this.socket = socket;
        this.clientNumber = nb;

    }

    public void run(){
        Boolean isConnected = true;
        while(isConnected){

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                DataInputStream inByte = new DataInputStream(socket.getInputStream());


                // Send a welcome message to the client.
                out.println("Hello, you are client #" + clientNumber);
                Boolean isReading = true;
                // Get messages from the client, line by line; return them capitalized
                while (isReading) {
                    String input = in.readLine();
                    byte[] msg= new byte[512];
                    inByte.read(msg);
                    System.out.println(msg[1]);

                    if (input == null || input.isEmpty()) {
                        isReading = false;
                    }
                    out.println(input.toUpperCase());

                }
            } catch (IOException e) {
                System.out.println("Error handling client #" + clientNumber);
                e.printStackTrace();
            } finally {
                try { socket.close(); isConnected=false; } catch (IOException e) {}
                System.out.println("Connection with client # " + clientNumber + " closed");
            }
        }
    }

}
