import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private String endereco;
    private String porta;
    private List<File> fileList = new ArrayList<>();
    private List<Node> connectedNodes = new ArrayList<>();
    private DownloadTasksManager downloadManager;

    // Main Node
    public Node(String endereco, String porta, Network network) {
        this.endereco = endereco;
        this.porta = porta;
        network.addNode(this);
    }

    // Neighbor Nodes
    public Node(String endereco, String porta, String folderName, Network network) {
        this.endereco = endereco;
        this.porta = porta;
        createFileList(folderName);
        network.addNode(this);
    }

    public void connectNode(String endereco, String porta) {
        if(getEndereco().equals(endereco) && getPorta().equals(porta)) {
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
