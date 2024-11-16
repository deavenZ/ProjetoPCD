public class IscTorrent {

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String folderName = args[1];
        Node node = new Node(port, folderName);
    }
}
