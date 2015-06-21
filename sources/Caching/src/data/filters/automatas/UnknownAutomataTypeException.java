package data.filters.automatas;

public class UnknownAutomataTypeException extends RuntimeException
{
	private static final long serialVersionUID = -7270290045936650750L;

	public UnknownAutomataTypeException(AutomataType automataType)
    {
        super(automataType.name());
    }
}
