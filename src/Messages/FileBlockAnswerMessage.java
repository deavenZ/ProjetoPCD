package Messages;

import Network.NodeAgent;

public class FileBlockAnswerMessage {

    private String fileName;
    private int offset;
    private int size;
    private int fileHash;
    private byte[] chunk;

    public FileBlockAnswerMessage(String fileName, int offset, int fileHash, byte[] chunk) {
        this.fileName = fileName;
        this.offset = offset;
        this.size = NodeAgent.SIZE;
        this.fileHash = fileHash;
        this.chunk = chunk;
    }

    public int getFileHash() {
        return fileHash;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getChunk() {
        return chunk;
    }

    @Override
    public String toString() {
        return "Sending FileBlock [offset=" + offset + ", size=" + size + ", fileHash=" + fileHash + "]";
    }

}
