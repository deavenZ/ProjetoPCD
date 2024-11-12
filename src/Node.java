import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private String endereco;
    private String porta;
    private List<File> fileList = new ArrayList<File>();;
    private List<Node> connectedNodes = new ArrayList<Node>();;
    private DownloadTasksManager downloadManager;
    private Network network;

    // Main Node
    public Node(String endereco, String porta, Network network) {
        this.endereco = endereco;
        this.porta = porta;
        this.network = network;
        network.addNode(this);
    }

    // Neighbor Nodes
    public Node(String endereco, String porta, String folderName, Network network) {
        this.endereco = endereco;
        this.porta = porta;
        this.network = network;
        createFileList(folderName);
        network.addNode(this);
    }

    public void connectNode(String endereco, String porta) {
        if(getEndereco().equals(endereco) && getPorta().equals(porta)) {
            throw new IllegalArgumentException("Não te podes ligar a ti mesmo!!");
        }
        Node receiver = network.searchInNetwork(endereco, porta);
        if(receiver == null) {
            throw new IllegalArgumentException("Não foi possivel encontrar este Nó!!");
        } else {
            addConectedNode(receiver);
            receiver.addConectedNode(this);
        }
    }

    public List<File> getFilteredFiles(WordSearchMessage filter) {

    }

    public List<File> getFileList() {
        return fileList;
    }

    public List<Node> getConnectedNodes() {
        return connectedNodes;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getPorta() {
        return porta;
    }

    public void addFile(File file) {
        fileList.add(file);
    }

    public void addConectedNode(Node node) {
        connectedNodes.add(node);
        System.out.println(this.getEndereco() + " is now conected to " + node.getEndereco());
    }

    private void createFileList(String folderName) {
        File[] files = new File(folderName).listFiles();
        if (files != null) {
            for (File file : files) {
                addFile(file);
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
