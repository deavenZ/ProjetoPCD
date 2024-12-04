import java.net.InetAddress;
import java.net.UnknownHostException;

public class NewConnectionRequest {

    private InetAddress endereco;
    private int porta;

    public NewConnectionRequest(InetAddress endereco, int porta) {
        this.endereco = endereco;
        this.porta = porta;
    }

    public InetAddress getEndereco() {
        return endereco;
    }

    public int getPorta() {
        return porta;
    }
}
