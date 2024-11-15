import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private int porta;
    private List<File> fileList = new ArrayList<>();
    private List<Node> connectedNodes = new ArrayList<>();
    private DownloadTasksManager downloadManager;
    public SimpleClient cliente;
    public SimpleServer servidor;


    public Node(int porta, String folderName) {
        this.porta = porta;
        createFileList(folderName);

        this.cliente = new SimpleClient(porta);
        this.servidor = new SimpleServer();
        new Thread(() -> {
            try {
                servidor.startServing();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void connectNode(/*String addr,*/int porta) {
        if (getPorta() == porta) {
            throw new IllegalArgumentException("Não te podes ligar a ti mesmo!!");
        }
    }

//    public List<File> getFilteredFiles(WordSearchMessage filter) {
//
//    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public int getPorta() {
        return porta;
    }

    public void addFile(File file) {
        fileList.add(file);
    }

    public void addConectedNode(Node node) {
        connectedNodes.add(node);
        //System.out.println(this.getEndereco() + " is now conected to " + node.getEndereco());
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
