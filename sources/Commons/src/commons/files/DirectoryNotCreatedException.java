package commons.files;

public class DirectoryNotCreatedException extends RuntimeException
{
	private static final long serialVersionUID = 8237151073452472399L;

	public DirectoryNotCreatedException(String message)
    {
        super(message);
    }

    public DirectoryNotCreatedException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
