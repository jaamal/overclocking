package models.statistics;

public class InvalidParameterException extends RuntimeException {
    private static final long serialVersionUID = -3252636621423151572L;

    public InvalidParameterException(String name, String value) {
        super("Invalid parameter " + name + " with value " + value + " was passed.");
    }
}
