public class IscTorrent {

    public static void main(String[] args) {
        DownloadTasksManager downloadManager = new DownloadTasksManager();
        Network network = new Network();
        GUI gui = new GUI("1.1.1.1", "8000", network, downloadManager);
        Node node = new Node("2.2.2.2", "8000", "sounds_1", network);
    }
}
