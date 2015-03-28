package httpservice.server;

public class StopParameters {
	public final StopType type;
	public final int timeout;
	public final Exception reason;

	private StopParameters(StopType type, int timeout, Exception reason) {
		this.type = type;
		this.timeout = timeout;
		this.reason = reason;
	}
	
	public static StopParameters createLazy(int timeout) {
		return new StopParameters(StopType.lazy, timeout, null);
	}
	
	public static StopParameters createImmediate(Exception reason) {
		return new StopParameters(StopType.immediate, 0, reason);
	}
}
