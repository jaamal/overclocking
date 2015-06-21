package compressionservice.algorithms.lzInf.arrayMinSearching;

import data.IDataFactory;
import data.longArray.ILongArray;
import dataContracts.DataFactoryType;

public class ArrayMinSearcherFactory implements IArrayMinSearcherFactory
{
    private IDataFactory dataFactory;

    public ArrayMinSearcherFactory(IDataFactory dataFactory)
    {
        this.dataFactory = dataFactory;
    }

    @Override
    public IArrayMinSearcher createSearcher(DataFactoryType dataFactoryType, ILongArray longArray)
    {
        return new IntervalTreeArrayMinSearcher(longArray, dataFactoryType, dataFactory);
    }
}
