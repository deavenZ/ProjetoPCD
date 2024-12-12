package Network;

import Download.DownloadTasksManager;
import Messages.*;
import Runnable.IscTorrent;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Node {

    public static final int SIZE = 10024;

    private InetAddress address;
    private int port;
    private GUI gui;
    private String folderName;
    private List<File> fileList = new ArrayList<>();
    private List<Integer> connectedPorts = new ArrayList<>();
    private List<NodeAgent> nodeAgents = new ArrayList<>();
    private List<DownloadTasksManager> activeDownloads = new ArrayList<>();

    private Map<FileSearchResult, Integer> searchedFiles = new HashMap<>();
    private Map<FileSearchResult, NodeAgent> agentPerFile = new HashMap<>();


    public Node(int port, String folderName) {
        this.port = port;
        this.folderName = IscTorrent.PATH + folderName;
        createFileList();
        initializeAddress();
        startServing();
    }

    public void startServing() {
        new Thread(() -> {
            try (ServerSocket ss = new ServerSocket(port)) {
                while (true) {
                    Socket socket = ss.accept();
                    NodeAgent na = new NodeAgent(this, socket);
                    na.start();
                    nodeAgents.add(na);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void connectClient(InetAddress address, int port){
        if(port == this.port) {
            System.err.println("Can't connect to yourself!");
            return;
        }
        if(connectedPorts.contains(port)) {
            System.err.println("Already Connected!");
            return;
        }
        System.out.println("Connecting to server: " + address.getHostName() + ":" + port);
        Socket socket;
        try {
            socket = new Socket(address, port);
            System.out.println("Starting agent for port: " + socket.getPort());
            NodeAgent na = new NodeAgent(this, socket);
            na.start();
            na.sendConnectionRequest(new NewConnectionRequest(this.address.getHostAddress(), this.port));
            addConnectedPort(socket.getPort());
            nodeAgents.add(na);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchFiles(WordSearchMessage keywords) {
        for (NodeAgent na : nodeAgents) {
            na.sendData(keywords);
        }
    }

    public synchronized void updateSearchedFiles(List<FileSearchResult> wantedFiles, NodeAgent agent) {
        searchedFiles.clear();
        agentPerFile.clear();
        for (FileSearchResult wantedFile : wantedFiles) {
            searchedFiles.merge(wantedFile, 1, Integer::sum);
            agentPerFile.put(wantedFile, agent);
        }
        gui.setSearchedFiles(searchedFiles);
    }

    public File getFileByHash(int hash) {
        for (File file : fileList) {
            if (file.hashCode() == hash) {
                return file;
            }
        }
        return null;
    }

    public List<NodeAgent> getWantedAgents(FileSearchResult file) {
        List<NodeAgent> wantedAgents = new ArrayList<>();
        for(Map.Entry<FileSearchResult, NodeAgent> entry : agentPerFile.entrySet()) {
            if(entry.getKey().equals(file)) {
                wantedAgents.add(entry.getValue());
            }
        }
        return wantedAgents;
    }

    public void seperateFileInBlocks(FileSearchResult file) {
        System.out.println("Divinding File in Chunks");
        List<FileBlockRequestMessage> fileRequests = new ArrayList<>();
        long size = file.getSize();
        int offset = 0;
        while(size > SIZE) {
            FileBlockRequestMessage fileBlockRequest = new FileBlockRequestMessage(file.getHash(), offset, SIZE);
            fileRequests.add(fileBlockRequest);
            offset += SIZE;
            size -= SIZE;
        }
        FileBlockRequestMessage fileBlockRequest = new FileBlockRequestMessage(file.getHash(), offset, (int) size);
        fileRequests.add(fileBlockRequest);
        List<NodeAgent> wantedAgents = getWantedAgents(file);
        createDTM(fileRequests, wantedAgents);
    }

    private void createDTM(List<FileBlockRequestMessage> fileRequests, List<NodeAgent> nodeAgents) {
        System.out.println("Creating DTM!");
        activeDownloads.add(new DownloadTasksManager(fileRequests, nodeAgents, folderName));
    }

    public void completeDownload(DownloadTasksManager dtm) {
        activeDownloads.remove(dtm);
//        System.out.println("File: " + fileHash + " - Download Completed!");
    }

    private void createFileList() {
        System.out.println("Creating file list: " + folderName);
        File[] files = new File(folderName).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith("mp3")) {
                    System.out.println("Adding file: " + file.getName());
                    addFile(file);
                }
            }
        }
    }


    private void initializeAddress() {
        try {
            address = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void addFile(File file) {
        fileList.add(file);
    }

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    public String getFolderName() {
        return folderName;
    }

    public void addConnectedPort(int port) {
        connectedPorts.add(port);
    }
}
