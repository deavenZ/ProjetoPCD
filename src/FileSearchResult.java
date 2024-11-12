import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearchResult {

    private WordSearchMessage keywords;
    private int size;
    private String fileName;
    private String endereco;
    private String porta;

    public FileSearchResult(WordSearchMessage keywords, int size, String fileName, String endereco, String porta) {
        this.keywords = keywords;
        this.size = size;
        this.fileName = fileName;
        this.endereco = endereco;
        this.porta = porta;
    }

    public List<File> search() {
        List<File> files = new ArrayList<File>();

    }
}
