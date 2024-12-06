import java.net.InetAddress;
import java.net.UnknownHostException;

public class NewConnectionRequest {

    private String endereco;
    private int porta;

    public NewConnectionRequest(String endereco, int porta) {
        this.endereco = endereco;
        this.porta = porta;
    }

    public InetAddress getEndereco() {
        try {
            return InetAddress.getByName(endereco);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPorta() {
        return porta;
    }
}
