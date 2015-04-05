package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lzInf.suffixTreeImitation.IOnlineSuffixTreeFactory;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;

public class FactorIteratorFactory implements IFactorIteratorFactory
{
    private IOnlineSuffixTreeFactory suffixTreeFactory;

    public FactorIteratorFactory(IOnlineSuffixTreeFactory suffixTreeFactory) {
        this.suffixTreeFactory = suffixTreeFactory;
    }
    
    @Override
    public IFactorIterator create(AlgorithmType algorithmType, DataFactoryType dataFactoryType, IReadableCharArray charArray) {
        switch (algorithmType) {
        case lzInf:
            return new LzInfFactorIterator(suffixTreeFactory.create(dataFactoryType, charArray), charArray);

        default:
            throw new RuntimeException(String.format("Factorization for algorithm type %s is not supported.", algorithmType));
        }
    }

}
