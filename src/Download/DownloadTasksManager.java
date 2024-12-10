package Download;

import Messages.FileSearchResult;

import java.io.File;
import java.util.List;

public class DownloadTasksManager {

    private List<FileBlockAnswerMessage> chunkList;
    private int fileHash;

    public DownloadTasksManager(int fileHash) {
        this.fileHash = fileHash;
    }

    public void downloadFile(File file) {

    }
}
