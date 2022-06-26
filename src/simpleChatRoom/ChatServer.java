package simpleChatRoom;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

public class ChatServer {
    private static final int PORT = 9001;
    private static HashSet<String> names = new HashSet<String>();
    
    private static HashSet<PrintWriter> writers = new HashSet<PrintWriter>();
    
    public static void main(String[] args) throws Exception{
        System.out.println("The Chat Service is running");
        ServerSocket listener = new ServerSocket(PORT);
        try {
            while (true) {                
                Socket socket = listener.accept();
                Thread handlerThread = new Thread(new Handler(socket));
                handlerThread.start();
            }
        } catch (Exception e) { 
            System.out.println(e);
        }finally{
            listener.close();
        }
    }
    
    private static class Handler implements Runnable{
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        public Handler(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            try{
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                while(true){
                    out.println("SubmitName");
                    name = in.readLine();
                    if(name == null) return;
                    if(!names.contains(name)){
                        names.add(name);
                        break;
                    }
                }
                
                out.println("NameAccepted");
                writers.add(out);
                
                while(true){
                    String input = in.readLine();
                    if(input == null){
                        return;
                    }
                    for(PrintWriter writer: writers){
                        writer.println("Message "+ name +" : "+ input );
                    }
                }
            }catch(Exception e){
                System.out.println(e);
            }finally{
                if(names != null){
                    names.remove(name);
                }
                if(out != null){
                    writers.remove(out);
                }
                try{
                    socket.close();
                }catch(Exception e){
                    System.out.println(e);
                }
            }
        }
    }
}
