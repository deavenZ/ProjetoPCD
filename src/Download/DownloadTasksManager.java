package Download;

import Messages.FileBlockAnswerMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DownloadTasksManager {

    private List<byte[]> chunkList;
    private int fileHash;
    private int totalChunks;
    private String fileName;
    private String targetFolder;

    public DownloadTasksManager(int fileHash, String fileName, String targetFolder) {
        this.fileHash = fileHash;
        this.totalChunks = totalChunks;
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

    public void downloadChunks() {
        if (!isComplete()) {
            System.out.println("File not fully downloaded!");
            return;
        }
        File outputFile = new File(targetFolder, fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            for (byte[] chunk : chunkList) {
                fos.write(chunk);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("File downloaded successfully: " + outputFile.getAbsolutePath());
    }
}
