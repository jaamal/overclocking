package compressingCore.dataFiltering.automatas;

public class UnknownAlphabiteTypeException extends RuntimeException
{
	private static final long serialVersionUID = 4252775437932680456L;

	public UnknownAlphabiteTypeException(AlphabiteType alphabiteType)
    {
        super(alphabiteType.name());
    }
}
