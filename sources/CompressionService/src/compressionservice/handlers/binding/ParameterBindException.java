package compressionservice.handlers.binding;

public class ParameterBindException extends RuntimeException {
    private static final long serialVersionUID = 8261149563129501512L;
    
    public ParameterBindException(String message) {
        super(message);
    }
    
    public ParameterBindException(String message, Throwable e) {
        super(message, e);
    }

}
