package services.blobs;

public class BlobServiceException extends RuntimeException
{
    private static final long serialVersionUID = -1513063381443984736L;

    public BlobServiceException(String message) {
		super(message);
	}
}
