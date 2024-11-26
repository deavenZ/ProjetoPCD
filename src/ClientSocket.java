import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


//public class ClientSocket {
//    private BufferedReader in;
//    private PrintWriter out;
//    private Socket socket;
//    private int port;
//
//    public ClientSocket(int port) {
//        this.port = port;
//    }
//
//    public void runClient(int port){
//        try {
//            connectToServer(port);
//            System.out.println("Connected to server");
//            sendMessages();
//        } catch (IOException e) {
//            e.printStackTrace(); // Print the stack trace for debugging
//        }
//    }
//
//    void connectToServer(int port) throws IOException {
//        InetAddress addr = InetAddress.getByName(null);
//        socket = new Socket(addr, port);
//        in = new BufferedReader(new InputStreamReader((socket.getInputStream())));
//        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))), true);
//    }
//
//
//    void sendMessages() throws IOException {
//        for (int i = 0; i < 10; i++) {
//            out.println("Ola " + i);
//            String str = in.readLine();
//            out.println(str);
//        }
//        out.println("FIM");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {//...
//        }
//    }
//
//}
