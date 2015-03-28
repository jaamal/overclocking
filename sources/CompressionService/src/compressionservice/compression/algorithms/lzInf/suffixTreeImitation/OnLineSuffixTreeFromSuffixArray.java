package compressionservice.compression.algorithms.lzInf.suffixTreeImitation;

import compressionservice.compression.algorithms.lzInf.arrayMinSearching.IArrayMinSearcher;
import compressionservice.compression.algorithms.lzInf.arrayMinSearching.IArrayMinSearcherFactory;
import compressionservice.compression.algorithms.lzInf.suffixArray.ISuffixArray;

import dataContracts.DataFactoryType;

public class OnLineSuffixTreeFromSuffixArray implements IOnLineSuffixTree
{
    private ISuffixArray suffixArray;
    private IArrayMinSearcher minSearcher;
    private long currentPrefix;
    private long length;

    public OnLineSuffixTreeFromSuffixArray(ISuffixArray suffixArray, IArrayMinSearcherFactory arrayMinSearcherFactory, DataFactoryType dataFactoryType)
    {
        this.suffixArray = suffixArray;
        minSearcher = arrayMinSearcherFactory.createSearcher(dataFactoryType, suffixArray.getInnerArray());
        currentPrefix = 0;
        length = suffixArray.length();
    }

    @Override
    public ISuffixTreeNavigator getNavigator()
    {
        return new SuffixTreeFromSuffixArrayNavigator(suffixArray,currentPrefix,minSearcher);
    }

    @Override
    //TODO: to throw exception if we try to add too much
    public void addCharsToPrefix(long charsCount)
    {
        currentPrefix = Math.min(currentPrefix + charsCount, length);
    }

    @Override
    //TODO: to throw exception if we try to delete too much
    public void removeCharsFromPrefix(long charsCount)
    {
        currentPrefix = Math.max(currentPrefix - charsCount, 0);
    }

    @Override
    public void close()
    {
        minSearcher.dispose();
        suffixArray.dispose();
    }
}
