package serialization.products;

import java.io.IOException;
import java.io.OutputStream;

public class LengthCalculatorOutputStream extends OutputStream
{
    private long streamLengthInBytes = 0;
    
    
    @Override
    public void write(int b) throws IOException
    {
        streamLengthInBytes++;
    }
    
    public long getLengthInBytes() {
        return streamLengthInBytes;
    }

}
