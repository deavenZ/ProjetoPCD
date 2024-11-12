import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadTasksManager {

    private static final int NUM_CHUNKS = 1024;

    private List<FileBlockRequestMessage> chunkList;

    public DownloadTasksManager() {
        chunkList = new ArrayList<FileBlockRequestMessage>();
    }

    public void downloadFile(File file) {

    }
}
