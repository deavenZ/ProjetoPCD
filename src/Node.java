import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Node {

    private InetAddress address;
    private int port;
    private List<File> fileList = new ArrayList<>();
    private List<Integer> connectedPorts = new ArrayList<>();
    private List<NodeAgent> nodeAgents = new ArrayList<>();
    private DownloadTasksManager downloadManager;

    public Map<FileSearchResult, Integer> searchedFiles = new HashMap<>();


    public Node(int port, String folderName) {
        this.port = port;
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
        if(port == this.port)
            throw new IllegalArgumentException("Can't connect to yourself!");
        System.out.println("Connecting to server: " + address.getHostName() + ":" + port);
        Socket socket;
        try {
            socket = new Socket(address, port);
            NodeAgent na = new NodeAgent(this, socket);
            System.out.println("Starting agent for port: " + socket.getPort());
            na.start();
            na.sendConnectionRequest(new NewConnectionRequest(this.address, this.port));
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

    public void updateSearchedFiles(List<FileSearchResult> wantedFiles) {
        for(FileSearchResult wantedFile : wantedFiles) {
            searchedFiles.put(wantedFile, searchedFiles.get(wantedFile)+1);
        }
    }

    private void createFileList(String folderName) {
        File[] files = new File(folderName).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file != null && file.getName().endsWith("mp3")) {
                    addFile(file);
                }
            }
        }
    }


    private void initializeAddress() {
        try {
            address = InetAddress.getByName("localhost");
        } catch (Exception e) {
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
}
