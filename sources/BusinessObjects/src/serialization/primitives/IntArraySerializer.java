package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import commons.utils.NumericUtils;
/*
 * This class serializes array of integers in the following format:
 *   1. length of array
 *   Each 4 integers joined into single block
 *   2.  for each block 
 *   2.1 mask of lengths for each integer in the block
 *   2.2 4 integers
 */
public class IntArraySerializer implements IIntArraySerializer {

    public void serialize(OutputStream stream, int[] array) throws IOException {
        stream.write(array.length);
        for (int i = 0; i < array.length; i += 4) {
            byte[] lenghtsMask = new byte[4];
            byte[][] blockBytes = new byte[4][];
            for (int j = 0; j < 4 && i + j < array.length; ++j) {
                byte[] buffer = NumericUtils.toBytes(array[i + j]);
                lenghtsMask[j] = (byte) buffer.length;
                blockBytes[j] = buffer;
            }

            stream.write(lenghtsMask);
            for (int j = 0; j < 4 && i + j < array.length; ++j)
                stream.write(blockBytes[j]);
        }
    }

    public int[] deserialize(InputStream stream) throws IOException {
        int count = stream.read();
        int[] array = new int[count];
        for (int i = 0; i < count; i += 4) {
            byte[] lengthsMask = new byte[4];
            stream.read(lengthsMask);
            for (int j = 0; j < 4 && i + j < count; ++j) {
                byte[] bytes = new byte[lengthsMask[j]];
                stream.read(bytes);
                array[i + j] = NumericUtils.fromBytes(bytes);
            }
        }
        return array;
    }
}

