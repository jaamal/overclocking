package compressionservice.algorithms.lzInf.arrayMinSearching;

import commons.utils.MathHelpers;
import compressingCore.dataAccess.IDataFactory;
import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

/**
 * The implementation of interface IArrayMinSearcher
 * based on data structure 'interval tree'.
 * About this structure you can see this articles:
 *
 * @link http://ru.wikipedia.org/wiki/%D0%94%D0%B5%D1%80%D0%B5%D0%B2%D0%BE_%D0%BE%D1%82%D1%80%D0%B5%D0%B7%D0%BA%D0%BE%D0%B2
 * @link http://e-maxx.ru/algo/segment_tree
 * @link http://sites.google.com/site/indy256/algo/maximizator_norec
 * It needs 2 * arraySize additional memory in average, and
 * each query work in O(log (size)) time.
 */
public class IntervalTreeArrayMinSearcher implements IArrayMinSearcher
{
    private ILongArray mainArray;
    private long size;
    private long halfSize;

    public IntervalTreeArrayMinSearcher(ILongArray longArray, DataFactoryType dataType, IDataFactory dataFactory)
    {
        this.size = longArray.size();
        halfSize = MathHelpers.minDeg2Bigger(size);
        mainArray = dataFactory.createLongArray(dataType, halfSize + halfSize);
        init();
        fillMainArray(longArray);
    }

    /**
     * Filling the array by infinity value
     */
    private void init()
    {
        final long arraySize = mainArray.size();
        final long infinity = Long.MAX_VALUE;
        for (long idx = 0; idx < arraySize; idx++)
        {
            mainArray.set(idx, infinity);
        }
    }

    /**
     * Fill the array by correct values
     */
    private void fillMainArray(ILongArray longArray)
    {
        final long arraySize = longArray.size();
        for (long idx = 0; idx < arraySize; idx++)
            set(idx, longArray.get(idx));
    }

    /**
     * Setting the array value and update mainArray.
     *
     * @param position array index (0-based)
     * @param value    the value of array in this position
     */
    private void set(long position, long value)
    {
        long index = position + halfSize;
        mainArray.set(index, value);
        while (index > 0)
        {
            long father = index / 2;
            mainArray.set(father, Math.min(mainArray.get(father + father), mainArray.get(father + father + 1)));
            index = father;
        }
    }

    private void checkStartIndex(long startIndex)
    {
        if (!(startIndex >= 0 && startIndex < size))
            throw new ArrayIndexOutOfBoundsException("Start index out of range:" + startIndex + ", array size is " + size);
    }

    private void checkEndIndex(long endIndex)
    {
        if (!(endIndex > 0 && endIndex <= size))
            throw new ArrayIndexOutOfBoundsException("End index out of range:" + endIndex + ", array size is " + size);
    }

    @Override
    public long getMin(final long startIndex, final long endIndex)
    {
        checkStartIndex(startIndex);
        checkEndIndex(endIndex);

        if (endIndex <= startIndex)
            throw new InvalidInputParametersError("Incorrect usage of method getMin: getMin(" + startIndex + "," + endIndex + ")");

        long leftIndex = startIndex + halfSize;
        long rightIndex = endIndex + halfSize - 1;
        long result = Long.MAX_VALUE;
        /***
         * For leftIndex, we must consider only those nodes, that are right sons of some other node.
         * Fo right index, conversely. 
         */
        while (leftIndex <= rightIndex)
        {
            if ((leftIndex & 1) != 0)
            {
                result = Math.min(result, mainArray.get(leftIndex));
            }
            if ((rightIndex & 1) == 0)
            {
                result = Math.min(result, mainArray.get(rightIndex));
            }
            leftIndex = ((leftIndex + 1) >> 1);
            rightIndex = ((rightIndex - 1) >> 1);
        }
        return result;
    }

    @Override
    public void dispose()
    {
        mainArray.close();
    }
}
