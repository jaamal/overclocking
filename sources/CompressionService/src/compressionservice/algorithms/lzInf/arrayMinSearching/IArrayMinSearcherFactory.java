package compressionservice.algorithms.lzInf.arrayMinSearching;

import compressingCore.dataAccess.ILongArray;
import dataContracts.DataFactoryType;

public interface IArrayMinSearcherFactory
{
    public IArrayMinSearcher createSearcher(DataFactoryType dataFactoryType, ILongArray longArray);
}
