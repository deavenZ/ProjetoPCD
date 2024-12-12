package Download;

import Messages.FileBlockAnswerMessage;
import Messages.FileBlockRequestMessage;
import Network.Node;
import Network.NodeAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DownloadTasksManager {

    private class DownloadTasksManagerHandler extends Thread {

        @Override
        public void run() {
            while (!fileRequests.isEmpty()) {
                NodeAgent na;
                synchronized (DownloadTasksManager.this) {
                    while (availableAgents.isEmpty()) {
                        System.out.println("Waiting for an agent to be available");
                        try {
                            DownloadTasksManager.this.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    na = availableAgents.removeFirst();
                    unavailableAgents.add(na);
                }
                na.sendData(fileRequests.removeFirst());
                synchronized (DownloadTasksManager.this) {
                    try {
                        DownloadTasksManager.this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if(fileContent.size() >= blockAmount) {
                FileWriter.writeFile(targetFolder, fileContent);
            }
        }
    }

    private class FileWriter {

        private static void writeFile(String targetFolder, List<FileBlockAnswerMessage> fileContent) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(targetFolder)) {
                fileContent.sort(Comparator.comparingInt(FileBlockAnswerMessage::getOffset));
                for (FileBlockAnswerMessage block : fileContent) {
                    fileOutputStream.write(block.getChunk());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<FileBlockRequestMessage> fileRequests;
    private int blockAmount;
    private List<FileBlockAnswerMessage> fileContent = new ArrayList<>();
    private List<NodeAgent> availableAgents;
    private List<NodeAgent> unavailableAgents = new ArrayList<NodeAgent>();
    private String targetFolder;
    private List<byte[]> chunkList;
    private Node node;

    public DownloadTasksManager(List<FileBlockRequestMessage> fileRequests, List<NodeAgent> availableAgents, String targetFolder, Node node) {
        this.targetFolder = targetFolder;
        this.fileRequests = fileRequests;
        this.blockAmount = fileRequests.size();
        this.availableAgents = availableAgents;
        this.node = node;
        startDownload();
    }

    public synchronized void startDownload() {
        System.out.println("Starting Download");
        DownloadTasksManagerHandler handler = new DownloadTasksManagerHandler();
        handler.start();
        try {
            handler.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        node.completeDownload(this);
    }

    public synchronized void responseReceived(FileBlockAnswerMessage fileData, NodeAgent na) {
        fileContent.add(fileData);
        System.out.println(fileData);
        unavailableAgents.remove(na);
        availableAgents.add(na);
        notifyAll();
    }
}
