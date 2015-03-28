package compressionservice.compression.algorithms.lzInf.suffixTreeImitation;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lzInf.arrayMinSearching.IArrayMinSearcherFactory;
import compressionservice.compression.algorithms.lzInf.suffixArray.ISuffixArray;
import compressionservice.compression.algorithms.lzInf.suffixArray.ISuffixArrayFactory;
import dataContracts.DataFactoryType;

public class OnLineSuffixTreeFactory implements IOnlineSuffixTreeFactory
{
    private ISuffixArrayFactory suffixArrayFactory;
    private IArrayMinSearcherFactory arrayMinSearcherFactory;

    public OnLineSuffixTreeFactory(ISuffixArrayFactory suffixArrayFactory, IArrayMinSearcherFactory arrayMinSearcherFactory)
    {
        this.suffixArrayFactory = suffixArrayFactory;
        this.arrayMinSearcherFactory = arrayMinSearcherFactory;
    }

    @Override
    public IOnLineSuffixTree create(DataFactoryType dataFactoryType, IReadableCharArray source)
    {
        ISuffixArray suffixArray = suffixArrayFactory.create(dataFactoryType, source);
        return new OnLineSuffixTreeFromSuffixArray(suffixArray, arrayMinSearcherFactory, dataFactoryType);
    }
}
