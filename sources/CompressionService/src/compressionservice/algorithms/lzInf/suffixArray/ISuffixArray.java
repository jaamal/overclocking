package compressionservice.algorithms.lzInf.suffixArray;

import compressingCore.dataAccess.IReadableCharArray;
import data.longArray.ILongArray;

public interface ISuffixArray
{
    public IReadableCharArray getSource();
    public long get(long pos);
    public long length();
    public void dispose();
    public ILongArray getInnerArray();
}
