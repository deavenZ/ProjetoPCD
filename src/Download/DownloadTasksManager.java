package Download;

import Messages.FileBlockAnswerMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DownloadTasksManager {

    private class DownloadTaskManagerDownloader extends Thread {

        @Override
        public void run() {
            downloadChunks();
        }

        private void downloadChunks() {
            while(!isComplete()) {
                System.out.println("Downloading chunks...");
            }
            File outputFile = new File(targetFolder, fileName);
            try (FileOutputStream fileHandler = new FileOutputStream(outputFile)) {
                for (byte[] chunk : chunkList) {
                    fileHandler.write(chunk);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("File downloaded successfully: " + outputFile.getAbsolutePath());
        }
    }

    private List<byte[]> chunkList;
    private int fileHash;
    private int totalChunks;
    private String fileName;
    private String targetFolder;

    public DownloadTasksManager(int fileHash, String fileName, String targetFolder) {
        this.fileHash = fileHash;
        this.fileName = fileName;
        this.targetFolder = targetFolder;
    }

    public synchronized void saveChunk(FileBlockAnswerMessage chunk) {
        totalChunks++;
        chunkList.add(chunk.getChunk());
        System.out.println("Chunk received for file: " + fileName + " | Total received: " + totalChunks);
    }

    public synchronized boolean isComplete() {
        return totalChunks == chunkList.size();
    }

    public void runDownloader() {
        DownloadTaskManagerDownloader downloader = new DownloadTaskManagerDownloader();
        downloader.start();
        try {
            downloader.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
