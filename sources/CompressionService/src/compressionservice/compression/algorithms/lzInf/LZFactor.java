package compressionservice.compression.algorithms.lzInf;

public class LZFactor
{
    public final char value;
    public final long startPosition;
    public final long endPosition;
    public final boolean isTerminal;

    public LZFactor(long startPosition, long endPosition)
    {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        isTerminal = false;
        value = 0;
    }

    public LZFactor(char value)
    {
        startPosition = 0;
        endPosition = 1;
        isTerminal = true;
        this.value = value;
    }
}
