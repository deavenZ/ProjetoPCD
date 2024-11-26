import java.io.File;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class FileSearchResult {

    private WordSearchMessage keywords;
    private long size;
    private String fileName;
    private InetAddress endereco;
    private int porta;

    public FileSearchResult(WordSearchMessage keywords, long size, String fileName, InetAddress endereco, int porta) {
        this.keywords = keywords;
        this.size = size;
        this.fileName = fileName;
        this.endereco = endereco;
        this.porta = porta;
    }

//    public List<File> search() {
//        List<File> files = new ArrayList<File>();
//
//    }
}
