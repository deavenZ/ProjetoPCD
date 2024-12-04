import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NodeAgent extends Thread{

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Node mainNode;
    private Socket socket;
    private InetAddress clientAddress;
    private int clientPort;

    public NodeAgent(Node node, Socket socket) {
        this.mainNode = node;
        this.socket = socket;
        try {
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            serve();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void serve() throws IOException {
        try {
            while (true) {
                Object message = in.readObject(); // Receive a message from the connected node
                if(message instanceof NewConnectionRequest) {
                    NewConnectionRequest request = (NewConnectionRequest) message;
                    clientAddress = request.getEndereco();
                    clientPort = request.getPorta();
                    System.out.println("Now connected to: " + clientAddress.getHostAddress() + ":" + clientPort);
                }
                if(message instanceof WordSearchMessage) {
                    WordSearchMessage search = (WordSearchMessage) message;
                    giveWantedFiles(search);
                }
                if(message instanceof List<?> list) {
                    if (!list.isEmpty() && list.get(0) instanceof FileSearchResult) {
                        List<FileSearchResult> wantedFiles = (List<FileSearchResult>) list;
                        mainNode.updateSearchedFiles(wantedFiles);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeThreads(in, out, socket);
        }
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

    public void sendData(Object data) {
        try {
            out.writeObject(data);  // Send the data to the client
            out.flush();  // Ensure the data is sent immediately
        } catch (IOException e) {
            System.err.println("Failed to send data: " + e.getMessage());
        }
    }

    private void giveWantedFiles(WordSearchMessage keywords) {
        List<FileSearchResult> filesWanted = new ArrayList<FileSearchResult>();
        for(File file : mainNode.getFileList()) {
            String fileName = file.getName();
            if(fileName.contains(keywords.getKeyword())) {
                FileSearchResult wantedFile = new FileSearchResult(keywords, file.length(), fileName, mainNode.getAddress(), mainNode.getPort());
                filesWanted.add(wantedFile);
            }
        }
        if(!filesWanted.isEmpty()) {
            sendData(filesWanted);
        }
    }

    public void sendConnectionRequest(NewConnectionRequest request) {
        System.out.println("Sending Connection request to " + request.getEndereco().getHostAddress() + ":" + request.getPorta());
        sendData(request);
    }

}
