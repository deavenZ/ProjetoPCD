import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class SimpleServer {

    public class DealWithClient extends Thread {

        private BufferedReader in;
        private PrintWriter out;


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
            in = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter((socket.getOutputStream()))), true);
            System.out.println("Client " + socket.getPort() + " connected");
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

    private int port;

    public SimpleServer(int port) {
        this.port = port;
//        try {
//            startServing();
//        } catch (IOException e) {}
    }

    public void startServing() throws IOException {
        ServerSocket ss = new ServerSocket(port);
        try {
            while(true){
                Socket s = ss.accept();
                new DealWithClient(s).start();
            }
        } finally {
            ss.close();
        }
    }

    public int getPort() {
        return port;
    }
}
