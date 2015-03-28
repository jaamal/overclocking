package avlTree.slpBuilders;

public class Stopwatch {
    private long elapsedNanoseconds = 0;
    private long startNanoseconds;
    private boolean isStarted = false;

    public void start() {
        if (!isStarted) {
            isStarted = true;
            startNanoseconds = System.nanoTime();
        }
    }

    public void stop() {
        if (isStarted) {
            isStarted = false;
            elapsedNanoseconds += System.nanoTime() - startNanoseconds;
        }
    }

    public long getElapsedNanoseconds() {
        if (isStarted)
            return elapsedNanoseconds + System.nanoTime() - startNanoseconds;
        return elapsedNanoseconds;
    }

    public double getElapsedSeconds() {
        return getElapsedNanoseconds() / 1e9;
    }
}
