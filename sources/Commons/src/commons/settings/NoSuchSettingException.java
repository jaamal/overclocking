package commons.settings;

public class NoSuchSettingException extends RuntimeException {

	private static final long serialVersionUID = 5911897673961922278L;

	public NoSuchSettingException(String msg) {
        super(msg);
    }
}
