import com.sun.jdi.event.ThreadStartEvent;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NodeAgent extends Thread{

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Node node;
    private Socket socket;

    public NodeAgent(Node node, Socket socket) {
        this.node = node;
        this.socket = socket;
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
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());

            while (true) {
                Object message = in.readObject(); // Receive a message from the connected node
                if (message instanceof String) {
                    String keywords = (String) message;
                    askForFiles(keywords); // Process the message
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection closed: " + e.getMessage());
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

    private void handleRequest(String message) {
        if (message.equals("GET_FILES")) {
            try {
                out.writeObject(node.getFileList()); // Send the list of files
            } catch (IOException e) {
                System.err.println("Failed to send file list: " + e.getMessage());
            }
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

    private void askForFiles(WordSearchMessage keywords) {
        List<FileSearchResult> filesWanted = new ArrayList<FileSearchResult>();
        for(File file : node.getFileList()) {
            String fileName = file.getName();
            if(fileName.contains(keywords.getKeyword())) {
                FileSearchResult wantedFile = new FileSearchResult(keywords, file.length(), fileName, node.getAddress(), node.getPort());
                filesWanted.add(wantedFile);
            }
        }
        if(!filesWanted.isEmpty()) {
            try {
                out.writeObject(filesWanted);
            } catch (IOException e) {}
        }
    }

    private void sendData(Object object)
}
