import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private InetAddress ip;
    private int port;
    private List<File> fileList = new ArrayList<>();
    private List<Integer> connectedPorts = new ArrayList<>();
    private List<NodeAgent> nodeAgents = new ArrayList<>();
    private DownloadTasksManager downloadManager;


    public Node(int port, String folderName) {
        this.port = port;
//      createFileList(folderName);
        startServing();
    }

    public void startServing() {
        new Thread(() -> {
            try (ServerSocket ss = new ServerSocket(port)) {
                while (true) {
                    Socket socket = ss.accept();
                    System.out.println("Received a request");
                    NodeAgent na = new NodeAgent(this, socket);
                    nodeAgents.add(na);
                    na.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void connectClient(String addr, int port){
 //       if (getPort() == port) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(addr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(address);
        System.out.println(port);

        Socket socket = null;
        try {
            socket = new Socket(address, port);
            NodeAgent na = new NodeAgent(this, socket);
            nodeAgents.add(na);
            na.start();
        } catch (IOException e) {
            System.out.println("Socket");
            e.printStackTrace();
        }
    }

    public List<String> searchFiles(WordSearchMessage keywords) {
        String keyword = keywords.getKeyword();
        List<String> files = new ArrayList<>();
        for (NodeAgent na : nodeAgents) {
            na.sendData(keywords);
        }
        return files;
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

    public void broadcast() {

    }

    private void initializeAddress() {

    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<Integer> getConnectedPorts() {
        return connectedPorts;
    }

    public InetAddress getAddress() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void addFile(File file) {
        fileList.add(file);
    }

    public void addConectedPorts(int port) {
        connectedPorts.add(port);
    }

    private class FileSearchResult {

        private WordSearchMessage keywords;
        private long size;
        private String fileName;
        private String endereco;
        private String porta;
        private Node node;

        public FileSearchResult(WordSearchMessage keywords, long size, String fileName, String endereco, String porta) {
            this.keywords = keywords;
            this.size = size;
            this.fileName = fileName;
            this.endereco = endereco;
            this.porta = porta;
        }
    }
}
