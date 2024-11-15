import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class SimpleClient {
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public SimpleClient(int PORTO) {
        runClient(PORTO);
    }

    public void runClient(int PORTO) {
        try {
            connectToServer(PORTO);
            sendMessages();
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {//...
            }
        }
    }

    void connectToServer(int PORTO) throws IOException {
        InetAddress addr = InetAddress.getByName(null);
        System.out.println("Endereço: " + addr);
        socket = new Socket(addr, PORTO);
        System.out.println("Socket: " + socket);
        in = new BufferedReader(new InputStreamReader((socket.getInputStream())));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))), true);
    }


    void sendMessages() throws IOException {
        for (int i = 0; i < 10; i++) {
            out.println("Ola " + i);
            String str = in.readLine();
            out.println(str);
        }
        out.println("FIM");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {//...
        }
    }

}
