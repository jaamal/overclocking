package compressionservice.algorithms.lzInf.suffixTreeImitation;

import data.charArray.IReadableCharArray;
import dataContracts.DataFactoryType;

public interface IOnlineSuffixTreeFactory
{
    public IOnLineSuffixTree create(DataFactoryType dataFactoryType, IReadableCharArray source);
}
