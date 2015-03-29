package compressionservice.compression.algorithms.lzInf.suffixTreeImitation;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lzInf.arrayMinSearching.IArrayMinSearcherFactory;
import compressionservice.compression.algorithms.lzInf.suffixArray.ISuffixArray;
import compressionservice.compression.algorithms.lzInf.suffixArray.ISuffixArrayBuilder;
import dataContracts.DataFactoryType;

public class OnLineSuffixTreeFactory implements IOnlineSuffixTreeFactory
{
    private ISuffixArrayBuilder suffixArrayBuilder;
    private IArrayMinSearcherFactory arrayMinSearcherFactory;

    public OnLineSuffixTreeFactory(ISuffixArrayBuilder suffixArrayBuilder, IArrayMinSearcherFactory arrayMinSearcherFactory)
    {
        this.suffixArrayBuilder = suffixArrayBuilder;
        this.arrayMinSearcherFactory = arrayMinSearcherFactory;
    }

    @Override
    public IOnLineSuffixTree create(DataFactoryType dataFactoryType, IReadableCharArray source)
    {
        ISuffixArray suffixArray = suffixArrayBuilder.build(dataFactoryType, source);
        return new OnLineSuffixTreeFromSuffixArray(suffixArray, arrayMinSearcherFactory, dataFactoryType);
    }
}
