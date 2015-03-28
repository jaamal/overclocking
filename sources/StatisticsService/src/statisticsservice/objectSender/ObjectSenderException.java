package statisticsservice.objectSender;

public class ObjectSenderException extends RuntimeException {
	private static final long serialVersionUID = 1784906043338443041L;
	
	public ObjectSenderException(Exception e) {
	    super(e);
    }
	
	public ObjectSenderException(String message) {
        super(message);
    }
}
