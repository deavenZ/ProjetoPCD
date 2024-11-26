import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI {

    private Node node;
    private DownloadTasksManager downloadTasksManager;
    private String folderName;

    public GUI(Node node) {
        this.node = node;
        setup();
    }

    public void setup() {
        // Setup Inicial da GUI
        JFrame frame = new JFrame("IscTorrent - IP: localhost" + " Porta: " + node.getPort());
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

            }
        });
        panel.add(searchButton);

        // List of Torrents
        JList<String> list = new JList<String>();
        list.setSize(1000, 600);
        frame.add(list, BorderLayout.CENTER);

        // Download and Connect Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(280, 600);
        frame.add(buttonPanel, BorderLayout.EAST);
        buttonPanel.setLayout(new GridLayout(2, 1));
        JButton downloadButton = new JButton("Download");
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
        JTextField endereco = new JTextField("localhost");
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
                String addr = endereco.getText();
                int port = Integer.parseInt(porta.getText());
                node.connectClient(addr, port);
            }
        });
        buttonPanel.add(connectButton);

        // Final Configuration
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
