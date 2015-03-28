package handlers;

public class FileInfoNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3440596255504738225L;

    public FileInfoNotFoundException(String id) {
        super(String.format("FileInfo with id %s not found.", id));
    }
}
