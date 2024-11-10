public class FileSearchResult {

    private WordSearchMessage keywords;

    private int size;
    private String fileName;

    public FileSearchResult(WordSearchMessage keywords, int size, String fileName) {
        this.keywords = keywords;
        this.size = size;
        this.fileName = fileName;
    }
}
