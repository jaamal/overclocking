package compressionservice.compression.algorithms.lzw;

import compressingCore.dataAccess.IReadableCharArray;

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
