package compressionservice.compression.algorithms.lzInf;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lzInf.suffixTreeImitation.IOnlineSuffixTreeFactory;
import dataContracts.DataFactoryType;

public class LZFactorIteratorFactory implements ILZFactorIteratorFactory
{
    private IOnlineSuffixTreeFactory suffixTreeFactory;

    public LZFactorIteratorFactory(IOnlineSuffixTreeFactory suffixTreeFactory)
    {
        this.suffixTreeFactory = suffixTreeFactory;
    }

    @Override
    public ILZFactorIterator create(DataFactoryType dataFactoryType, IReadableCharArray charArray)
    {
        return new LZFactorIterator(suffixTreeFactory.create(dataFactoryType, charArray), charArray);
    }
}
