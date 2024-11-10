public class IscTorrent {

    public static void main(String[] args) {
        GUI gui = new GUI();
        DownloadTasksManager downloadTasksManager = new DownloadTasksManager();

        gui.initialize(downloadTasksManager);
    }
}
