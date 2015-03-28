package patternMatching.logger;

public interface ILogger {
    public void logMessage(String message);
    public void logLSearchCall();
    public void logLSearchRecursiveCall();
    public void logLSearchEnd();
    public void logBuildStart();
    public void logBuildEnd();
    public void logNonEmptyLSearchCreation();
    public void logLSearchLeaf();
    public void logAddStart();
    public void logAddFinish();
}
