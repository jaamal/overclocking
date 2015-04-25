package compressionservice.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lz77.TextWindow;
import compressionservice.algorithms.lzInf.suffixTreeImitation.IOnlineSuffixTreeFactory;
import dataContracts.DataFactoryType;

public class FactorIteratorFactory implements IFactorIteratorFactory
{
    private IOnlineSuffixTreeFactory suffixTreeFactory;

    public FactorIteratorFactory(IOnlineSuffixTreeFactory suffixTreeFactory) {
        this.suffixTreeFactory = suffixTreeFactory;
    }
    
    @Override
    public IFactorIterator createWindowIterator(IReadableCharArray charArray, int windowSize) {
        return new LZ77FactorIterator(TextWindow.create(windowSize), charArray);
    }
    
    @Override
    public IFactorIterator createInfiniteIterator(IReadableCharArray charArray, DataFactoryType dataFactoryType) {
        return new LzInfFactorIterator(suffixTreeFactory.create(dataFactoryType, charArray), charArray);
    }

}
