package compressionservice.algorithms.lzInf.suffixArray;

import compressingCore.dataAccess.ILongArray;
import compressingCore.dataAccess.IReadableCharArray;

//TODO it seems its obsolete abstraction
public class SuffixArray implements ISuffixArray
{
    private ILongArray suffixArray;
    private IReadableCharArray charArray;
    private long length;

    public SuffixArray(
            ILongArray suffixArray,
            IReadableCharArray source)
    {
        this.suffixArray = suffixArray;
        this.charArray = source;
        length = charArray.length();
    }

    @Override
    public IReadableCharArray getSource()
    {
        return charArray;
    }

    @Override
    public long get(long pos)
    {
        if (pos < 0 || pos >= length)
            throw new IndexOutOfBoundsException(Long.toString(pos));
        return suffixArray.get(pos);
    }

    @Override
    public long length()
    {
        return length;
    }

    @Override
    public void dispose()
    {
        suffixArray.close();
    }

    @Override
    public ILongArray getInnerArray()
    {
        return suffixArray;
    }
}
