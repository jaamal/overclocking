package compressionservice.compression.algorithms.factorization;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.ITextWindow;
import compressionservice.compression.algorithms.lz77.TextWindow;
import compressionservice.compression.algorithms.lzInf.suffixTreeImitation.IOnlineSuffixTreeFactory;
import compressionservice.compression.parameters.IRunParams;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;

public class FactorIteratorFactory implements IFactorIteratorFactory
{
    private IOnlineSuffixTreeFactory suffixTreeFactory;

    public FactorIteratorFactory(
            IOnlineSuffixTreeFactory suffixTreeFactory) {
        this.suffixTreeFactory = suffixTreeFactory;
    }

    @Override
    public IFactorIterator create(IRunParams runParams, IReadableCharArray charArray) {
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, CompressionRunKeys.AlgorithmType);
        switch (algorithmType) {
        case lzInf:
            DataFactoryType dataFactoryType = runParams.getEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType);
            return new LzInfFactorIterator(suffixTreeFactory.create(dataFactoryType, charArray), charArray);
        case lz77:
            int windowSize = runParams.getInt(CompressionRunKeys.WindowSize);
            ITextWindow textWindow = TextWindow.create(windowSize);
            return new LZ77FactorIterator(textWindow, charArray);
        default:
            throw new RuntimeException(String.format("Factorization for algorithm type %s is not supported.", algorithmType));
        }
    }

}
