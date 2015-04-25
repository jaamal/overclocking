package compressionservice.algorithms.lzw;

public class IncorrectUsageException extends RuntimeException
{
	private static final long serialVersionUID = 8573772549203977412L;

	public IncorrectUsageException(String message)
    {
        super(message);
    }
}
