package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import commons.utils.StreamHelpers;
/*
 * This class serializes array of integers in the following format:
 *   1. length of the array
 *   Each 4 integers joined into a single block
 *   2.  for each block 
 *   2.1 mask of lengths for each integer in the block
 *   2.2 4 integers
 */
public class IntArraySerializer implements IIntArraySerializer {

    public void serialize(OutputStream stream, int[] array) throws IOException {
        StreamHelpers.writeInt(stream, array.length);
        
        int fullBlocksCount = array.length >> 2;
        for (int i = 0; i < fullBlocksCount; i ++) {
            int pos = i << 2;
            StreamHelpers.writeIntsBlock(stream, array[pos], array[pos+1], array[pos+2], array[pos+3]);
        }
        
        if (array.length > 0) {
            int tailLength = array.length - (fullBlocksCount << 2);
            int[] tail = new int[tailLength];
            System.arraycopy(array, fullBlocksCount << 2, tail, 0, tailLength);
            StreamHelpers.writeIntsBlock(stream, tail);
        }
        
        stream.flush();
    }

    public int[] deserialize(InputStream stream) throws IOException {
        int count = StreamHelpers.readInt(stream);
        int[] array = new int[count];
        
        int fullBlocksCount = count >> 2;
        for (int i = 0; i < fullBlocksCount; i++) {
            int[] block = StreamHelpers.readIntsBlock(stream);
            System.arraycopy(block, 0, array, i << 2, 4);
        }
        
        int tailLength = count - (fullBlocksCount << 2);
        if (tailLength > 0) {
            int[] block = StreamHelpers.readIntsBlock(stream);
            System.arraycopy(block, 0, array, fullBlocksCount << 2, tailLength);
        }
        
        return array;
    }
}

