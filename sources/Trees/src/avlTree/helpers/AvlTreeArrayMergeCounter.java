package avlTree.helpers;

public class AvlTreeArrayMergeCounter implements IAvlTreeArrayMergeCounter {
    @Override
    public void fixMerge(int arrayLength) {
        ++count;
        totalLength += arrayLength;
        maxLength = Math.max(maxLength, arrayLength);
    }

    @Override
    public double getMeanArrayLength() {
        return ((double)totalLength) / count;
    }

    @Override
    public void printStatistics() {
        System.out.println(String.format("AvlTreeArrayMergeCounter.Statistics: count = %d, totalLength = %d, mean length = %f, maxLength = %d", count, totalLength, getMeanArrayLength(), maxLength));
    }


    private long count = 0;
    private long totalLength = 0;
    private int maxLength = 0;
}
