package Download;

public class FileBlockRequestMessage {

    private int offset;
    private int size;
    private int fileHash;

    public FileBlockRequestMessage(int offset, int size, int fileHash) {
        this.offset = offset;
        this.size = size;
        this.fileHash = fileHash;
    }

    @Override
    public String toString() {
        return "Requesting FileBlock [offset=" + offset + ", size=" + size + ", fileHash=" + fileHash + "]";
    }
}
