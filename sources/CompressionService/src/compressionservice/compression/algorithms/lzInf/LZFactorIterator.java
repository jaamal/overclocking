package compressionservice.compression.algorithms.lzInf;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lzInf.suffixTreeImitation.IOnLineSuffixTree;
import compressionservice.compression.algorithms.lzInf.suffixTreeImitation.ISuffixTreeNavigator;

import dataContracts.FactorDef;

public class LZFactorIterator implements ILZFactorIterator
{
    private IOnLineSuffixTree suffixTree;
    private IReadableCharArray charArray;
    private long currentPosition;
    private long arrayLength;

    public LZFactorIterator(IOnLineSuffixTree suffixTree, IReadableCharArray charArray)
    {
        this.suffixTree = suffixTree;
        this.charArray = charArray;
        currentPosition = 0;
        arrayLength = charArray.length();
    }

    @Override
    public boolean hasFactors()
    {
        return currentPosition < arrayLength;
    }

    @Override
    public FactorDef getNextFactor()
    {
        if (!hasFactors())
            throw new IllegalAccessError("The method called when all factors scanned.");
        return extractFactor(arrayLength);
    }

    @Override
    public void close() {
        suffixTree.close();
    }
    
    private FactorDef extractFactor(long maxPosition)
    {
        ISuffixTreeNavigator navigator = moveNavigatorWhileCan(maxPosition);
        long length = navigator.pathLength();
        if (length <= 1)
        {
            addChars(1L);
            return new FactorDef(charArray.get(currentPosition - 1));
        }
        addChars(length);
        long leftmostPosition = navigator.getLeftmostPosition();
        return new FactorDef(leftmostPosition - length, length);
    }

    private ISuffixTreeNavigator moveNavigatorWhileCan(long maxPosition)
    {
        ISuffixTreeNavigator navigator = suffixTree.getNavigator();
        long index = currentPosition;
        while (index < maxPosition && navigator.tryMove(charArray.get(index)))
            index++;
        return navigator;
    }

    private void addChars(long charsCount)
    {
        suffixTree.addCharsToPrefix(charsCount);
        currentPosition += charsCount;
    }
}
