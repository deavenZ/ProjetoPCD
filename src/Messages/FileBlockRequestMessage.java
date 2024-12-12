package Messages;

import java.io.Serializable;

public class FileBlockRequestMessage implements Serializable {

    private int fileHash;
    private int offset;
    private int length;

    public FileBlockRequestMessage(int fileHash, int offset, int length) {
        this.fileHash = fileHash;
        this.offset = offset;
        this.length= length;
    }

    public int getFileHash() {
        return fileHash;
    }

    public int getLength() {
        return length;
    }

    public int getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "Requesting FileBlock [ fileHash= " + fileHash  + " | length= " + length + " | offset= " + offset + " ]";
    }
}
