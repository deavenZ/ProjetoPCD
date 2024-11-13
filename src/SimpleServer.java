import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer {

    public class DealWithClient extends Thread {

        private ObjectOutputStream out;
        private ObjectInputStream in;

        public DealWithClient(Socket socket) throws IOException {
            doConnections(socket);
        }

        @Override
        public void run() {
            try {
                serve();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void doConnections(Socket socket) throws IOException {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }

        private void serve() throws IOException {
            while (true) {
                String str = in.readLine();
                if (str.equals("FIM"))
                    break;
                System.out.println("Eco:" + str);
            }
        }
    }

    public static final int PORTO = 8080;

    public SimpleServer() {
        try {
            startServing();
        } catch (IOException e) {}
    }

    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(PORTO);
        try {
            while(true){
                Socket socket = ss.accept();
                new DealWithClient(socket).start();
            }
        } finally {
            ss.close();
        }
    }
}
