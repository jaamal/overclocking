package database;

public class DBConfigurationException extends RuntimeException {
    private static final long serialVersionUID = -234581075793168755L;

    public DBConfigurationException(Throwable message) {
        super(message);
    }
}
