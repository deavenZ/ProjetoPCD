import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


public class SimpleClient {
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Socket socket;

    public SimpleClient() {
        runClient();
    }

    public void runClient() {
        try {
            connectToServer();
            sendMessages();
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {//...
            }
        }
    }

    void connectToServer() throws IOException {
        InetAddress endereco = InetAddress.getByName(null);
        System.out.println("Endereco:" + endereco);
        socket = new Socket(endereco, SimpleServer.PORTO);
        System.out.println("Socket:" + socket);
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    void sendMessages() throws IOException {
        for (int i = 0; i < 10; i++) {
            String str = in.readLine();
            System.out.println(str);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {//...
            }
        }
    }

}
