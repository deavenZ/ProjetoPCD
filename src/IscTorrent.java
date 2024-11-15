public class IscTorrent {

    public static void main(String[] args) {
        DownloadTasksManager downloadManager = new DownloadTasksManager();
        //Network network = new Network();
        GUI gui = new GUI("1.1.1.1", 8000, downloadManager);
        //GUI node = new GUI("2.2.2.2", 8000, downloadManager);
    }
}
