package compressionservice.compression.algorithms.lzInf;

public interface ILZFactorIterator extends AutoCloseable
{
    public boolean hasFactors();
    public LZFactor getNextFactor();
    public void close();
}
