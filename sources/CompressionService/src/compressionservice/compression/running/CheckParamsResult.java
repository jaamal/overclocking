package compressionservice.compression.running;

public class CheckParamsResult {
    private CheckParamsResult(State state, String message) {
        this.state = state;
        this.message = message;
    }

    public static final CheckParamsResult OK =  new CheckParamsResult(State.ok, null);

    public static CheckParamsResult failed(String message) {
        return new CheckParamsResult(State.failed, message);
    }

    public final State state;
    public final String message;

    public enum State {
        ok, failed
    }
}
