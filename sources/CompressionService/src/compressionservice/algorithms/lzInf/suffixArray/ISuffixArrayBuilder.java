package compressionservice.algorithms.lzInf.suffixArray;

import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface ISuffixArrayBuilder
{
    ISuffixArray build(DataFactoryType dataFactoryType, IReadableCharArray source);
    ISuffixArray build(DataFactoryType dataFactoryType, String pathToFile);
}
