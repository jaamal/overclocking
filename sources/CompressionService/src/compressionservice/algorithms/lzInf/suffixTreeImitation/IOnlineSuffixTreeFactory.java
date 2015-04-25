package compressionservice.algorithms.lzInf.suffixTreeImitation;

import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface IOnlineSuffixTreeFactory
{
    public IOnLineSuffixTree create(DataFactoryType dataFactoryType, IReadableCharArray source);
}
