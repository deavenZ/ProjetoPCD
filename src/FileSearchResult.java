import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.InetAddress;
import java.nio.file.Files;
import java.security.MessageDigest;

public class FileSearchResult implements Serializable {

    private WordSearchMessage keywords;
    private long size;
    private String fileName;
    private int hash;
    private InetAddress endereco;
    private int porta;

    public FileSearchResult(WordSearchMessage keywords, long size, String fileName, InetAddress endereco, int porta) {
        this.keywords = keywords;
        this.size = size;
        this.fileName = fileName;
        this.hash = digestFile(new File(fileName));
        this.endereco = endereco;
        this.porta = porta;
    }

        private int digestFile(File file) {
            try {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                byte[] hash = MessageDigest.getInstance("SHA-256").digest(fileContent);
                return new BigInteger(1, hash).intValue();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    public String getFilePath() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public int getHash() {
        return hash;
    }

    public String getFileName() {
        String[] separatedFile = fileName.split("\\\\");
        return separatedFile[separatedFile.length - 1];
    }

    @Override
    public String toString() {
        String[] separatedFile = fileName.split("\\\\");
        return separatedFile[separatedFile.length - 1] + " " + hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FileSearchResult) {
            FileSearchResult other = (FileSearchResult) obj;
            if(other.hash == this.hash) {
                return true;
            }
        }
        return false;
    }
}
