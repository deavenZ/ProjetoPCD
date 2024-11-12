import java.util.ArrayList;
import java.util.List;

public class Network {

    private List<Node> network;

    public Network() {
        network = new ArrayList<Node>();
    }

    public Node searchInNetwork(String endereco, String porta)  {
        for (Node node : network) {
            if(node.getEndereco().equals(endereco) && node.getPorta().equals(porta)) {
                return node;
            }
        }
        return null;
    }

    public void addNode(Node node) {
        network.add(node);
    }
}
