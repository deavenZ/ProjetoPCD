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

    private InetAddress address;
    private int port;
    private List<File> fileList = new ArrayList<>();
    private List<Integer> connectedPorts = new ArrayList<>();
    private List<NodeAgent> nodeAgents = new ArrayList<>();
    private GUI gui;
    private String folderName;

    private Map<FileSearchResult, Integer> searchedFiles = new HashMap<>();
    private Map<Integer, DownloadTasksManager> activeDownloads = new ConcurrentHashMap<>();


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

    public synchronized void updateSearchedFiles(List<FileSearchResult> wantedFiles) {
        searchedFiles.clear();
        for (FileSearchResult wantedFile : wantedFiles) {
            searchedFiles.merge(wantedFile, 1, Integer::sum);
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

    public DownloadTasksManager getOrCreateDTM(int fileHash, String fileName) {
        return activeDownloads.computeIfAbsent(fileHash, f-> {
            return new DownloadTasksManager(fileHash, fileName, folderName);
        });
    }

    public void completeDownload(int fileHash) {
        activeDownloads.remove(fileHash);
        System.out.println("File: " + fileHash + " - Download Completed!");
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
