import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Node {

    private String endereco;
    private String porta;
    private List<File> fileList;
    private List<Node> connectedNodes;

    public Node(String endereco, String porta) {
        this.endereco = endereco;
        this.porta = porta;
        fileList = new ArrayList<File>();
        connectedNodes = new ArrayList<Node>();
    }

    public Node(String endereco, String porta, List<File> fileList) {
        this.endereco = endereco;
        this.porta = porta;
        this.fileList = fileList;
        connectedNodes = new ArrayList<Node>();
    }

    public void addFile(File file) {
        fileList.add(file);
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void addConnectedNode(Node node) {
        connectedNodes.add(node);
    }
}
