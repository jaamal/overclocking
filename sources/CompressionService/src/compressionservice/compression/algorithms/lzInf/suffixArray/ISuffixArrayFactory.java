package compressionservice.compression.algorithms.lzInf.suffixArray;

import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface ISuffixArrayFactory
{
    public ISuffixArray create(DataFactoryType dataFactoryType, IReadableCharArray string);
}
