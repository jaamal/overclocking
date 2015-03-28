package compressionservice.compression.algorithms.lzInf.suffixArray;

public class ExternalProcessCrashesException extends RuntimeException
{
	private static final long serialVersionUID = 5529988990045264481L;

	public ExternalProcessCrashesException(int exitCode)
    {
        super("The process has terminated with exit code " + exitCode);
    }

    public ExternalProcessCrashesException(Exception innerException)
    {
        super("Exception with external process", innerException);
    }
}
