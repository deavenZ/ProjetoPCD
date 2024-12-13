import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUI {

    private Node node;
    private JList<String> fileList = new JList<>();
    private List<FileSearchResult> fileInList = new ArrayList<>();

    public GUI(Node node) {
        this.node = node;
        node.setGui(this);
        setup();
    }

    public void setup() {
        // Setup Inicial da GUI
        JFrame frame = new JFrame("IscTorrent: IP: " + node.getAddress().getHostName() + " Porta: " + node.getPort());
        frame.setPreferredSize(new Dimension(1000, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Search Bar
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(1, 3));
        panel.add(new JLabel("Texto a procurar: "));
        JTextField textoAProcurar = new JTextField();
        panel.add(textoAProcurar);
        JButton searchButton = new JButton("Procurar");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WordSearchMessage search = new WordSearchMessage(textoAProcurar.getText());
                node.searchFiles(search);
            }
        });
        panel.add(searchButton);

        // List of Torrents
        fileList.setSize(1000, 600);
        frame.add(fileList, BorderLayout.CENTER);

        // Download and Connect Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(280, 600);
        frame.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setLayout(new GridLayout(2, 1));
        JButton downloadButton = new JButton("Download");
        downloadButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(String selectedFile : fileList.getSelectedValuesList()) {
                    node.seperateFileInBlocks(getSelectedFile(selectedFile));
                }
            }
        });
        JButton connectButton = new JButton("Ligar a Nó");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connectNode();
            }
        });
        buttonPanel.add(downloadButton);
        buttonPanel.add(connectButton);

        // Final Configuration
        frame.setResizable(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void connectNode() {
        // Window Configuration
        JFrame frame = new JFrame("Connect");
        frame.setPreferredSize(new Dimension(400, 90));
        frame.setLayout(new BorderLayout());

        // TextFields Configuration
        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(1, 4));
        panel.add(new JLabel("Endereço: "));
        JTextField endereco = new JTextField("127.0.0.1");
        panel.add(endereco);
        panel.add(new JLabel("Porta: "));
        JTextField porta = new JTextField("8080");
        panel.add(porta);


        // Buttons Configuration
        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        buttonPanel.add(cancelButton);
        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                String address = endereco.getText();
                int port = Integer.parseInt(porta.getText());
                connectClient(address, port);
            }
        });
        buttonPanel.add(connectButton);

        // Final Configuration
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void downloadComplete(Map<NodeAgent, Integer> blocksPerAgents, long elapsedTime) {
        JFrame frame = new JFrame("Download Complete");
        frame.setPreferredSize(new Dimension(600, 200));
        frame.setLayout(new BorderLayout());
        JLabel label = new JLabel("<html><br> Descarregamento Completo <br>");
        for(Map.Entry<NodeAgent, Integer> entry : blocksPerAgents.entrySet()) {
            String textWanted = "Fornecedor[endereco=/" + entry.getKey().getClientAddress().getHostName() + ", porto=" + entry.getKey().getClientPort() + "]:" + entry.getValue();
            label.setText(label.getText() + textWanted + "<br>");
        }
        double elapsedTimeSeconds = (double) ((int) (elapsedTime / 100))/10;
        label.setText(label.getText() + "<br> Tempo decorrido: " + elapsedTimeSeconds + " s<br></html>");
        frame.add(label, BorderLayout.CENTER);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        frame.add(okButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void connectClient(String address, int port) {
        try {
            node.connectClient(InetAddress.getByName(address), port);
        } catch (UnknownHostException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setSearchedFiles(Map<FileSearchResult, Integer> searchedFiles) {
        DefaultListModel<String> files = new DefaultListModel<>();
        List<String> filesString = new ArrayList<>();
        fileInList.clear();
        for(Map.Entry<FileSearchResult, Integer> entry : searchedFiles.entrySet()) {
            filesString.add(entry.getKey().getFileName() + " <" + entry.getValue() + ">");
            fileInList.add(entry.getKey());
        }
        filesString.sort(String.CASE_INSENSITIVE_ORDER);
        for(String file : filesString) {
            files.addElement(file);
        }
        fileList.setModel(files);
    }

    private FileSearchResult getSelectedFile(String fileWanted) {
        System.out.println("Wanted File: " + fileWanted.split(" ")[0]);
        for(FileSearchResult file : fileInList) {
            System.out.println("Searching... FILE: " + file);
            if(file.getFileName().equals(fileWanted.split(" ")[0])) {
                System.out.println("Found File: " + file);
                return file;
            }
        }
        return null;
    }
}
