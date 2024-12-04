import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class IscTorrent {

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String folderName = args[1];
        Node node = new Node(port, folderName);
        GUI gui = new GUI(node);
    }
}
