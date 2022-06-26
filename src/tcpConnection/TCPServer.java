package tcpConnection;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TCPServer {
    public static void main(String[] args) {
        String clientSentence;
        String capatalizedSentence;
        try {
            System.out.println("Waiting for clients");
            ServerSocket welcomeSocket = new ServerSocket(7777);
            while(true){
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Connection is Established");
                System.out.println("Getting client Data");
                
                //to read the string client sent
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outputClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                capatalizedSentence = clientSentence.toUpperCase()+ '\n';
                outputClient.writeBytes(capatalizedSentence);
            }
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
