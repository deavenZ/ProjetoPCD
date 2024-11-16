import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private int port;
    private List<File> fileList = new ArrayList<>();
    private List<Integer> connectedPorts = new ArrayList<>();
    private DownloadTasksManager downloadManager;
    public SimpleClient cliente;
    public SimpleServer servidor;


    public Node(int port, String folderName) {
        this.port = port;
        createFileList(folderName);
        new GUI(this);

        this.cliente = new SimpleClient(port);
        this.servidor = new SimpleServer(port);
        new Thread(() -> {
            try {
                servidor.startServing();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void connectClient(int port) {
        if (getPort() == port)
            throw new IllegalArgumentException("Não te podes ligar a ti mesmo!!");
        if (!isAlreadyConnected(port)) {
            addConectedPorts(port);
            cliente.runClient(port);
        }
    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<Integer> getConnectedPorts() {
        return connectedPorts;
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

    private boolean isAlreadyConnected(int port) {
        for (int p : connectedPorts) {
            if(port == p){
                System.out.println("Já estás conectado ao servidor de porta " + port);
                return true;
            }
        }
        return false;
    }

    private class FileSearchResult {

        private WordSearchMessage keywords;
        private int size;
        private String fileName;
        private String endereco;
        private String porta;
        private Node node;

        public FileSearchResult(WordSearchMessage keywords, int size, String fileName, String endereco, String porta) {
            this.keywords = keywords;
            this.size = size;
            this.fileName = fileName;
            this.endereco = endereco;
            this.porta = porta;
        }
    }
}
