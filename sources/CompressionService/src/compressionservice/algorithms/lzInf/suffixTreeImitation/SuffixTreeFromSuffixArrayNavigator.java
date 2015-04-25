package compressionservice.algorithms.lzInf.suffixTreeImitation;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lzInf.arrayMinSearching.IArrayMinSearcher;
import compressionservice.algorithms.lzInf.suffixArray.ISuffixArray;

/**
 *The class is simulates navigator on suffix array.
 */
public class SuffixTreeFromSuffixArrayNavigator implements ISuffixTreeNavigator
{
    private ISuffixArray suffixArray;
    private long prefixLength;
    private long leftIndex;
    private long rightIndex;
    private IReadableCharArray string;
    private long stringPrefix;
    private IArrayMinSearcher arrayMinSearcher;

    /**
     * @param suffixArray      the suffix array
     * @param stringPrefix     the prefix of the string that controlled by the navigator
     * @param arrayMinSearcher min searcher constructed on suffixArray. We trust to arrayMinSearcher and to suffixArray!
     */
    public SuffixTreeFromSuffixArrayNavigator(ISuffixArray suffixArray, long stringPrefix, IArrayMinSearcher arrayMinSearcher)
    {
        this.stringPrefix = stringPrefix;
        this.arrayMinSearcher = arrayMinSearcher;
        string = suffixArray.getSource();
        this.suffixArray = suffixArray;
        this.prefixLength = 0;
        this.leftIndex = 0;
        this.rightIndex = suffixArray.length() - 1;
    }

    boolean validPosition(long position)
    {
        return position < string.length();
    }

    private long findLeftmostOccurence(char ch)
    {
        long left = leftIndex;
        long right = rightIndex;
        while (left < right)
        {
            long middle = ((left + right) >> 1);
            long position = prefixLength + suffixArray.get(middle);
            if (validPosition(position) && string.get(position) >= ch)
                right = middle;
            else
                left = middle + 1;
        }
        long position = prefixLength + suffixArray.get(left);
        if (!validPosition(position) || string.get(position) != ch)
            return -1;
        return left;
    }

    private long findRightMostOccurence(char ch)
    {
        long left = leftIndex;
        long right = rightIndex;
        while (left < right)
        {
            long middle = ((left + right + 1) >> 1);
            long position = prefixLength + suffixArray.get(middle);
            if (!validPosition(position) || string.get(position) <= ch)
                left = middle;
            else
                right = middle - 1;
        }
        long position = prefixLength + suffixArray.get(left);
        if (!validPosition(position) || string.get(position) != ch)
            return -1;
        return left;
    }

    @Override
    public boolean tryMove(char ch)
    {
        if (leftIndex > rightIndex)
            return false;

        long leftMostOccurence = findLeftmostOccurence(ch);
        if (leftMostOccurence == -1)
            return false;
        long rightMostOccurence = findRightMostOccurence(ch);
        if (rightMostOccurence == -1)
            throw new IllegalStateException("Methods findLeftmostOccurence and findRightMostOccurence are not consistent.");

        if (arrayMinSearcher.getMin(leftMostOccurence, rightMostOccurence + 1) + prefixLength + 1 > stringPrefix)
            return false;

        leftIndex = leftMostOccurence;
        rightIndex = rightMostOccurence;
        prefixLength++;
        return true;
    }

    @Override
    public long getLeftmostPosition()
    {
        return prefixLength + arrayMinSearcher.getMin(leftIndex, rightIndex + 1);
    }

    @Override
    public long pathLength()
    {
        return prefixLength;
    }
}
