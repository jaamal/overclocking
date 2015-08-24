package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import commons.utils.NumericUtils;
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
        for (int i = 0; i < array.length; i += 4) {
            byte[] lenghtsMask = new byte[4];
            byte[][] blockBytes = new byte[4][];
            for (int j = 0; j < 4 && i + j < array.length; ++j) {
                byte[] buffer = NumericUtils.toFloatingBytes(array[i + j]);
                lenghtsMask[j] = (byte) buffer.length;
                blockBytes[j] = buffer;
            }

            stream.write(lenghtsMask);
            for (int j = 0; j < 4 && i + j < array.length; ++j)
                stream.write(blockBytes[j]);
        }
        stream.flush();
    }

    public int[] deserialize(InputStream stream) throws IOException {
        int count = StreamHelpers.readInt(stream);
        int[] array = new int[count];
        
        for (int i = 0; i < count; i += 4) {
            byte[] lengthsMask = new byte[4];
            stream.read(lengthsMask);
            for (int j = 0; j < 4 && i + j < count; ++j) {
                byte[] intBuffer = new byte[lengthsMask[j]];
                stream.read(intBuffer);
                array[i + j] = NumericUtils.intFromFloatingBytes(intBuffer);
            }
        }
        return array;
    }
}

