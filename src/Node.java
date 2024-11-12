import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private String endereco;
    private String porta;
    private List<File> fileList;
    private List<Node> connectedNodes;
    private DownloadTasksManager downloadManager;
    private String folderName;

    // Main Node
    public Node(String endereco, String porta) {
        this.endereco = endereco;
        this.porta = porta;
        fileList = new ArrayList<File>();
        connectedNodes = new ArrayList<Node>();
    }

    // Neighbor Nodes
    public Node(String endereco, String porta, String folderName) {
        this.endereco = endereco;
        this.porta = porta;
        this.folderName = folderName;
        fileList = new ArrayList<File>();
        connectedNodes = new ArrayList<Node>();
    }

    public List<File> getFileList() {
        File[] files = new File(folderName).listFiles();
        if (files != null) {
            for (File file : files) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    public void addConnectedNode(Node node) {
        connectedNodes.add(node);
    }
}
