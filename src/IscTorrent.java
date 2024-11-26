import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class IscTorrent {

    public static void main(String[] args) {
//        int port = Integer.parseInt(args[0]);
//        String folderName = args[1];
        Node node = new Node(8081, "s1");
        GUI gui = new GUI(node);

        Node node2 = new  Node(8082, "s2");
        GUI gui2 = new GUI(node2);

        try {
            node2.connectClient(InetAddress.getLocalHost().getHostAddress(), 8081);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
