import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadTasksManager {

    private static final int MAX_THREADS = 5;
    private final ExecutorService executorService;

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
                FileWriter.writeFile(targetFolder, fileName, fileContent);
            }
            completeDownload();
        }
    }

    private static class FileWriter {

        private static void writeFile(String targetFolder, String fileName, List<FileBlockAnswerMessage> fileContent) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(targetFolder + File.separator + fileName);) {
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
    private Map<NodeAgent, Integer> blocksPerAgent = new HashMap<>();
    private String targetFolder;
    private String fileName;
    private Node node;
    private long startTime;

    public DownloadTasksManager(List<FileBlockRequestMessage> fileRequests, List<NodeAgent> availableAgents, String targetFolder, String fileName, Node node) {
        this.targetFolder = targetFolder;
        this.fileRequests = fileRequests;
        this.blockAmount = fileRequests.size();
        this.availableAgents = availableAgents;
        this.fileName = fileName;
        this.node = node;
        this.startTime = System.currentTimeMillis();
        initialiteBlocksPerAgent(availableAgents);
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
        startDownload();
    }

    public synchronized void startDownload() {
        System.out.println("Starting Download");
        executorService.submit(new DownloadTasksManagerHandler());
    }

    public synchronized void responseReceived(FileBlockAnswerMessage fileData, NodeAgent na) {
        fileContent.add(fileData);
        System.out.println(fileData);
        unavailableAgents.remove(na);
        availableAgents.add(na);
        addBlockToAgent(na);
        notifyAll();
    }

    private void completeDownload() {
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Completed Download in " + elapsedTime + " ms");
        executorService.shutdown();
        node.completeDownload(this, blocksPerAgent, elapsedTime);
    }

    private void initialiteBlocksPerAgent(List<NodeAgent> availableAgents) {
        for(NodeAgent agent : availableAgents) {
            blocksPerAgent.put(agent, 0);
        }
    }

    private void addBlockToAgent(NodeAgent agent) {
        blocksPerAgent.put(agent, blocksPerAgent.get(agent) + 1);
    }
}
