import java.io.Serializable;

public class FileBlockAnswerMessage implements Serializable {

    private int offset;
    private int length;
    private int fileHash;
    private byte[] chunk;

    public FileBlockAnswerMessage(int fileHash, int offset, int length, byte[] chunk) {
        this.offset = offset;
        this.length = length;
        this.fileHash = fileHash;
        this.chunk = chunk;
    }

    public int getFileHash() {
        return fileHash;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public byte[] getChunk() {
        return chunk;
    }

    @Override
    public String toString() {
        return "Answering FileBlock [ fileHash= " + fileHash  + " | length= " + length + " | offset= " + offset + " ]";
    }
}
