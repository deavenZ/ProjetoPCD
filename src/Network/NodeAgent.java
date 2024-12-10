package Network;

import Download.DownloadTasksManager;
import Messages.FileBlockAnswerMessage;
import Messages.FileBlockRequestMessage;
import Messages.FileSearchResult;
import Messages.NewConnectionRequest;
import Messages.WordSearchMessage;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeAgent extends Thread{

    public static final int SIZE = 1024;
    public static final int TIMEOUT = 300;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Node mainNode;
    private Socket socket;
    private InetAddress clientAddress;
    private int clientPort;

    public NodeAgent(Node node, Socket socket) throws IOException {
        this.mainNode = node;
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void run() {
        try {
            serve();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void serve() throws IOException {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object message = in.readObject();// Receive a message from the connected node
                System.out.println("Received object of type: " + message.getClass().getName());
                switch (message) {
                    case NewConnectionRequest request -> {
                        clientAddress = request.getEndereco();
                        clientPort = request.getPorta();
                        mainNode.addConnectedPort(clientPort);
                        System.out.println("Now connected to: " + clientAddress.getHostAddress() + ":" + clientPort);
                    }
                    case WordSearchMessage search -> {
                        giveWantedFiles(search);
                    }
                    case List<?> wantedFiles -> {
                        mainNode.updateSearchedFiles((List<FileSearchResult>) wantedFiles);
                    }
                    case FileBlockAnswerMessage download -> {
                        DownloadTasksManager dtm = mainNode.getOrCreateDTM(download.getFileHash(), download.getFileName());
                        System.out.println("Receiving file chunk: " + download);
                        dtm.saveChunk(download);
                        if(dtm.isComplete()) {
                            dtm.downloadChunks();
                            mainNode.completeDownload(download.getFileHash());
                        }
                    }
                    case FileBlockRequestMessage upload -> {
                        List<FileBlockAnswerMessage> wantedChunks = divideFileIntoChunks(upload);
                        for(FileBlockAnswerMessage wantedChunk : wantedChunks) {
                            sendData(wantedChunk);
                            try {
                                wait(TIMEOUT);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + message);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<FileBlockAnswerMessage> divideFileIntoChunks(FileBlockRequestMessage request) {
        List<FileBlockAnswerMessage> chunks = new ArrayList<>();
        File file = mainNode.getFileByHash(request.getFileHash()); // Retrieve the file using the file hash

        if (file == null) {
            System.err.println("Requested file not found for hash: " + request.getFileHash());
            return chunks; // Return an empty list if the file doesn't exist
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            int offset = request.getOffset();
            int size = request.getSize();

            // Ensure the offset is within the file size
            if (offset >= file.length()) {
                System.err.println("Offset exceeds file size for hash: " + request.getFileHash());
                return chunks;
            }

            // Ensure the requested size does not exceed the remaining file size
            int bytesToRead = (int) Math.min(size, file.length() - offset);

            raf.seek(offset); // Move the file pointer to the requested offset

            while (bytesToRead > 0) {
                int chunkSize = Math.min(SIZE, bytesToRead); // Use the fixed chunk size
                byte[] buffer = new byte[chunkSize];
                int bytesRead = raf.read(buffer);

                if (bytesRead == -1) break; // End of file reached

                chunks.add(new FileBlockAnswerMessage(
                        file.getName(),
                        offset,
                        request.getFileHash(),
                        Arrays.copyOf(buffer, bytesRead) // Adjust buffer size for the last chunk
                ));

                offset += bytesRead; // Update the offset for the next chunk
                bytesToRead -= bytesRead; // Decrease the remaining bytes to read
            }
        } catch (IOException e) {
            System.err.println("Error reading file for hash " + request.getFileHash() + ": " + e.getMessage());
        }

        return chunks;
    }

    private void closeThreads(ObjectInputStream in, ObjectOutputStream out, Socket socket) {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendData(Object data) {
        try {
            out.writeObject(data);
            out.flush();
            System.out.println("Sending data: " + data.getClass().getName());
            out.reset();
        } catch (IOException e) {
            System.err.println("Failed to send data: " + e.getMessage());
        }
    }

    private void giveWantedFiles(WordSearchMessage keywords) {
        List<FileSearchResult> filesWanted = new ArrayList<>();
        System.out.println("Searching for files...");
        for(File file : mainNode.getFileList()) {
            String fileName = file.getName();
            System.out.println("Searching for file: " + fileName);
            if(fileName.toLowerCase().contains(keywords.getKeyword().toLowerCase())) {
                System.out.println("Found file: " + fileName);
                FileSearchResult wantedFile = new FileSearchResult(keywords, file.length(), mainNode.getFolderName() + "\\" + fileName, mainNode.getAddress(), mainNode.getPort());
                filesWanted.add(wantedFile);
            }
        }
        if(!filesWanted.isEmpty()) {
            sendData(filesWanted);
        }
    }

    public void sendConnectionRequest(NewConnectionRequest request) {
        System.out.println("Sending Connection request to client.");
        sendData(request);
    }
}
