package data.charArray;

import java.io.IOException;

import commons.files.IFile;

public interface IReadableCharArray extends AutoCloseable
{
    public char get(long idx);
    public long length();
    IReadableCharArray subArray(long inclusiveStartIndex, long exclusiveEndIndex);
    void close();
    
    default String toString(long start, long end)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (long i = start; i < end; i++)
            stringBuffer.append(this.get(i));
        return stringBuffer.toString();
    }
    
    default void saveToFile(IFile file) {
        final int bufferSize = 16 * 1024;
        final long charArrayLength = this.length();
        String bufferStr = "";
        
        try {
            for (long i = 0; i < charArrayLength; i++)
            {
                if (bufferStr.length() == bufferSize)
                {
                    file.append(bufferStr.getBytes());
                    bufferStr = "";
                }
                bufferStr += this.get(i);
            }
            if (bufferStr.length() > 0)
            {
                file.append(bufferStr.getBytes());
            }
        }
        catch (IOException e) {
            throw new RuntimeException(String.format("Fail to save char array to file %s.", file.getPathStr()), e);
        }
    }
}
