import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadTasksManager {

    private List<FileBlockRequestMessage> chunkList;
    private int numChunks;

    public DownloadTasksManager(File file, int chunkSize) {
        chunkList = new ArrayList<FileBlockRequestMessage>();
        for(int i = 0; i < file.length(); i++) {}
    }
}
