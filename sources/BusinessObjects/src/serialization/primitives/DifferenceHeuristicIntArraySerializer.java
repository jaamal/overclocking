package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DifferenceHeuristicIntArraySerializer extends IntArraySerializer {
    private final int differenceStep;

    public DifferenceHeuristicIntArraySerializer() {
        this(1);
    }

    public DifferenceHeuristicIntArraySerializer(int differenceStep) {
        this.differenceStep = differenceStep;
    }

    @Override
    public void serialize(OutputStream stream, int[] array) throws IOException {
        super.serialize(stream, minimizeValuesUsingDifferences(array));
    }

    @Override
    public int[] deserialize(InputStream stream) throws IOException {
        return decodeFromArrayWithDifferences(super.deserialize(stream));
    }

    private int[] minimizeValuesUsingDifferences(int[] array) {
        int[] array2 = new int[array.length];
        for (int i = 0; i < differenceStep; ++i)
            array2[i] = array[i];
        for (int i = differenceStep; i < array.length; ++i) {
            int difference = array[i] - array[i - differenceStep];
            int encodedDifference;
            if (difference < 0)
                encodedDifference = (-difference) * 2 + 1;
            else
                encodedDifference = difference * 2;
            if (encodedDifference < array[i])
                array2[i] = encodedDifference * 2 + 1;
            else
                array2[i] = array[i] * 2;
        }
        return array2;
    }

    private int[] decodeFromArrayWithDifferences(int[] array) {
        int[] array2 = new int[array.length];
        for (int i = 0; i < differenceStep; ++i)
            array2[i] = array[i];
        for (int i = differenceStep; i < array.length; ++i) {
            if ((array[i] & 1) == 1) {
                int encodedDifference = (array[i] >> 1);
                int difference = (encodedDifference & 1) == 1 ? -(encodedDifference >> 1) : (encodedDifference >> 1);
                array2[i] = array2[i - differenceStep] + difference;
            } else
                array2[i] = array[i] >> 1;
        }
        return array2;
    }
}
