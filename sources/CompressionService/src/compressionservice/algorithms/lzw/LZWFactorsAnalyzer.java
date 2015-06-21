package compressionservice.algorithms.lzw;

import data.charArray.IReadableCharArray;

public class LZWFactorsAnalyzer implements ILZWFactorsAnalyzer
{
    public long countLZWCodes(IReadableCharArray charArray)
    {
        LZWFactorIterator factorIterator = new LZWFactorIterator(charArray);
        long result = 0;
        while (factorIterator.hasFactors())
        {
            result++;
            factorIterator.getNextFactor();
        }
        return result;
    }
}
