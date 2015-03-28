package compressionservice.upload;

public class UploadException extends RuntimeException {

    private static final long serialVersionUID = -8731644044772975553L;
    
    public UploadException(String message, Throwable e) {
        super(message, e);
    }
}
