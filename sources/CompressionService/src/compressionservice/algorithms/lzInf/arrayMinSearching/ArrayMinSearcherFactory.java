package compressionservice.algorithms.lzInf.arrayMinSearching;

import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.ILongArray;

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
