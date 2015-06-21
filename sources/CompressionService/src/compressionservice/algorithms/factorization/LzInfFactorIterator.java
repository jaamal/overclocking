package compressionservice.algorithms.factorization;

import compressionservice.algorithms.lzInf.suffixTreeImitation.IOnLineSuffixTree;
import compressionservice.algorithms.lzInf.suffixTreeImitation.ISuffixTreeNavigator;
import data.charArray.IReadableCharArray;
import dataContracts.FactorDef;

public class LzInfFactorIterator implements IFactorIterator
{
    private IOnLineSuffixTree suffixTree;
    private IReadableCharArray charArray;
    private long currentPosition;
    private long arrayLength;

    public LzInfFactorIterator(IOnLineSuffixTree suffixTree, IReadableCharArray charArray)
    {
        this.suffixTree = suffixTree;
        this.charArray = charArray;
        currentPosition = 0;
        arrayLength = charArray.length();
    }

    @Override
    public boolean any()
    {
        return currentPosition < arrayLength;
    }

    @Override
    public FactorDef next()
    {
        if (!any())
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
