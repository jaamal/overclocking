package compressionservice.algorithms.lzw;

import compressingCore.dataAccess.IReadableCharArray;

public interface ILZWFactorsAnalyzer
{
    long countLZWCodes(IReadableCharArray charArray);
}
