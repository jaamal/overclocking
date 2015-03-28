package database;

public class DBException extends RuntimeException {
    private static final long serialVersionUID = 8549538849978533132L;

    public DBException(String message) {
        super(message);
    }
}
