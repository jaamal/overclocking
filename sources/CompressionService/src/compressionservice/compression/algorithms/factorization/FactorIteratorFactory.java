package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.windows.IWindowFactory;
import compressionservice.compression.algorithms.lzInf.suffixTreeImitation.IOnlineSuffixTreeFactory;
import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;

public class FactorIteratorFactory implements IFactorIteratorFactory
{
    private IOnlineSuffixTreeFactory suffixTreeFactory;
    private IWindowFactory windowFactory;

    public FactorIteratorFactory(
            IOnlineSuffixTreeFactory suffixTreeFactory,
            IWindowFactory windowFactory) {
        this.suffixTreeFactory = suffixTreeFactory;
        this.windowFactory = windowFactory;
    }

    @Override
    public IFactorIterator create(ICompressionRunParams runParams, IReadableCharArray charArray) {
        AlgorithmType algorithmType = runParams.getEnumValue(AlgorithmType.class, CompressionRunKeys.AlgorithmType);
        switch (algorithmType) {
        case lzInf:
            DataFactoryType dataFactoryType = runParams.getEnumValue(DataFactoryType.class, CompressionRunKeys.DataFactoryType);
            return new LzInfFactorIterator(suffixTreeFactory.create(dataFactoryType, charArray), charArray);
        case lz77:
            int windowSize = runParams.getIntValue(CompressionRunKeys.WindowSize);
            return new LZ77FactorIterator(windowFactory, charArray, windowSize);
        default:
            throw new RuntimeException(String.format("Factorization for algorithm type %s is not supported.", algorithmType));
        }
    }

}
