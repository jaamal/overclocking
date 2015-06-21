package compressionservice.algorithms.lzInf.arrayMinSearching;

import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

public interface IArrayMinSearcherFactory
{
    public IArrayMinSearcher createSearcher(DataFactoryType dataFactoryType, ILongArray longArray);
}
