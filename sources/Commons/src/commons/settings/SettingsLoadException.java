package commons.settings;

public class SettingsLoadException extends RuntimeException
{
	private static final long serialVersionUID = 4442522375103590139L;
	
	public SettingsLoadException(String msg, Exception e) {
		super(msg, e);
	}
}
