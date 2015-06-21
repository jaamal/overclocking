package compressionservice.algorithms.lzw;

import data.charArray.IReadableCharArray;

public interface ILZWFactorsAnalyzer
{
    long countLZWCodes(IReadableCharArray charArray);
}
