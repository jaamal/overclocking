package compressionservice.compression.algorithms.lzw;


public class LZWFactor
{
    public final long code;

    public LZWFactor(long code)
    {
        this.code = code;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LZWFactor lzwFactor = (LZWFactor) o;

        if (code != lzwFactor.code) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return (int) (code ^ (code >>> 32));
    }
}
