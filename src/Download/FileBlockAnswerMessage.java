package Download;

public class FileBlockAnswerMessage {

    private int offset;
    private int size;
    private int fileHash;

    public FileBlockAnswerMessage(int offset, int size, int fileHash) {
        this.offset = offset;
        this.size = size;
        this.fileHash = fileHash;
    }

    public int getFileHash() {
        return fileHash;
    }

}
