package Download;

import Messages.FileSearchResult;

import java.io.File;
import java.util.List;

public class DownloadTasksManager {

    private static final int NUM_CHUNKS = 10024;

    private List<FileBlockRequestMessage> chunkList;

    public DownloadTasksManager(FileSearchResult fileSearchResult) {

    }

    public void downloadFile(File file) {

    }
}
