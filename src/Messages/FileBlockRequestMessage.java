package Messages;

public class FileBlockRequestMessage {

    private int fileHash;
    private int offset;
    private long size;

    public FileBlockRequestMessage(int fileHash, int offset, long size) {
        this.fileHash = fileHash;
        this.offset = offset;
        this.size = size;
    }

    public int getFileHash() {
        return fileHash;
    }

    public long getSize() {
        return size;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "Requesting FileBlock [offset=" + offset + ", size=" + size + ", fileHash=" + fileHash + "]";
    }
}
