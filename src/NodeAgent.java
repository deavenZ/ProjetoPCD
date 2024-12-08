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
    private void serve() throws IOException {
        try {
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Object message = in.readObject();// Receive a message from the connected node
                System.out.println("Received object of type: " + message.getClass().getName());
                switch (message) {
                    case NewConnectionRequest request -> {
                        clientAddress = request.getEndereco();
                        clientPort = request.getPorta();
                        System.out.println("Now connected to: " + clientAddress.getHostAddress() + ":" + clientPort);
                    }
                    case WordSearchMessage search -> {
                        giveWantedFiles(search);
                    }
                    case List<?> wantedFiles -> {
                        mainNode.updateSearchedFiles((List<FileSearchResult>) wantedFiles);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + message);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
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
