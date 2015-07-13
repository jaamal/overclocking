package compressionservice.algorithms.lzInf.suffixArray;

import data.charArray.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface ISuffixArrayBuilder
{
    ISuffixArray build(DataFactoryType dataFactoryType, IReadableCharArray source);
}
