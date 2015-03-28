package compressionservice.compression.algorithms.lz77.suffixTree.structures;

public class Place implements IPlace
{
    private long position;
    private long length;

    public Place(long position, long length)
    {
        this.position = position;
        this.length = length;
    }

    @Override
    public long getLength()
    {
        return this.length;
    }

    @Override
    public long getPosition()
    {
        return this.position;
    }

}
