import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String endereco;
    private String porta;
    private List<File> fileList;
    private List<User> connectedUsers;
    private DownloadTasksManager downloadManager;

    public User(String endereco, String porta) {
        this.endereco = endereco;
        this.porta = porta;
        fileList = new ArrayList<File>();
        connectedUsers = new ArrayList<User>();
    }

    public User(String endereco, String porta, String folderName) {
        this.endereco = endereco;
        this.porta = porta;
        createFileList(folderName);
        connectedUsers = new ArrayList<User>();
    }

    private void addFile(File file) {
        fileList.add(file);
    }

    public void createFileList(String folderName) {
        File[] files = new File(folderName).listFiles();
        if (files != null) {
            for (File file : files) {
                addFile(file);
            }
        }
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void addConnectedNode(User user) {
        connectedUsers.add(user);
    }
}
