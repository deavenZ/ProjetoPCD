package Runnable;

import Network.GUI;
import Network.Node;

import java.io.File;

public class IscTorrent {

    public static final String PATH = new File((System.getProperty("user.dir")))+ "\\";

    public static void main(String[] args) {
        int port = Integer.parseInt("808" + args[0]);
        String folderName = "dl" + args[0];
        Node node = new Node(port, folderName);
        GUI gui = new GUI(node);
    }
}
