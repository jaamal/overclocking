package serialization.lzFactorizations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import serialization.primitives.IIntArraySerializer;
import dataContracts.FactorDef;

public class LzFactorizationSerializer implements ILzFactorizationSerializer {
    private final IIntArraySerializer intArraySerializer;

    public LzFactorizationSerializer(IIntArraySerializer intArraySerializer) {
        this.intArraySerializer = intArraySerializer;
    }
    
    @Override
    public void serialize(OutputStream stream, FactorDef[] factors) throws IOException {
        intArraySerializer.serialize(stream, toIntArray(factors));
    }

    @Override
    public FactorDef[] deserialize(InputStream stream) throws IOException {
        return fromIntArray(intArraySerializer.deserialize(stream));
    }
    
    private static <T extends FactorDef> int[] toIntArray(FactorDef[] factors) {
        int[] array = new int[factors.length * 2];
        for (int index = 0; index < factors.length; ++index) {
            if (factors[index].isTerminal) {
                array[index * 2] = factors[index].symbol;
                array[index * 2 + 1] = 0;
            } else {
                array[index * 2] = (int) factors[index].beginPosition;
                array[index * 2 + 1] = (int) factors[index].length;
            }
        }
        return array;
    }

    private static FactorDef[] fromIntArray(int[] intArray) {
        if (intArray.length % 2 != 0)
            throw new RuntimeException("Array length must be even!");
        FactorDef[] factors = new FactorDef[intArray.length / 2];
        for (int index = 0; index < factors.length; ++index) {
            if (intArray[2 * index + 1] == 0)
                factors[index] = new FactorDef((char) intArray[2 * index]);
            else
                factors[index] = new FactorDef(intArray[2 * index], intArray[2 * index + 1]);
        }
        return factors;
    }
}
