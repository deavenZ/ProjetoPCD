import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {

    private DownloadTasksManager downloadTasksManager;

    public void initialize(DownloadTasksManager downloadTasksManager) {
        this.downloadTasksManager = downloadTasksManager;
        setup();
    }

    public void setup() {
        // Setup Inicial da GUI
        JFrame frame = new JFrame("IscTorrent");
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
        JFrame frame = new JFrame("Connect");
        frame.setPreferredSize(new Dimension(400, 90));
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        frame.add(panel, BorderLayout.CENTER);
        panel.setLayout(new GridLayout(1, 4));
        panel.add(new JLabel("Endereço: "));
        JTextField endereco = new JTextField();
        panel.add(endereco);
        panel.add(new JLabel("Porta: "));
        JTextField porta = new JTextField();
        panel.add(porta);

        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setLayout(new GridLayout(1, 2));
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        JButton connectButton = new JButton("Connect");
        buttonPanel.add(connectButton);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
