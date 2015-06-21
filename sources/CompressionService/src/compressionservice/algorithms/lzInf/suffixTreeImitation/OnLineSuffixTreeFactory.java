package compressionservice.algorithms.lzInf.suffixTreeImitation;

import compressionservice.algorithms.lzInf.arrayMinSearching.IArrayMinSearcherFactory;
import compressionservice.algorithms.lzInf.suffixArray.ISuffixArray;
import compressionservice.algorithms.lzInf.suffixArray.ISuffixArrayBuilder;
import data.charArray.IReadableCharArray;
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
