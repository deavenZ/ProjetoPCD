import java.io.File;

public class IscTorrent {

    public static final String PATH = new File((System.getProperty("user.dir"))).getParent()+ "\\";

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String folderName = args[1];
        Node node = new Node(port, folderName);
        GUI gui = new GUI(node);
    }
}
