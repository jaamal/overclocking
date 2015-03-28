package compressionservice.compression.algorithms.lz77;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;

import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lz77.windows.IWindowFactory;

import dataContracts.FactorDef;

public class LZ77FactorizationProducer implements ILZ77FactorizationProducer
{
    private IFactorsRepository<FactorDef> lz77FactorsRepository;
    private IWindowFactory windowFactory;

    public LZ77FactorizationProducer(IFactorsRepositoryFactory factorsRepositoryFactory, IWindowFactory windowFactory)
    {
        this.lz77FactorsRepository = factorsRepositoryFactory.getLZ77Repository();
        this.windowFactory = windowFactory;
    }

    @Override
    public void run(String statisticsId, IReadableCharArray source, int windowSize)
    {
        IArrayItemsWriter<FactorDef> writer = lz77FactorsRepository.getWriter(statisticsId);
        LZ77FactorIterator lz77FactorIterator = new LZ77FactorIterator(windowFactory, source, windowSize);
        while (lz77FactorIterator.hasFactors())
        {
            FactorDef nextFactor = lz77FactorIterator.getNextFactor();
            writer.add(nextFactor);
        }
        writer.done();
    }
}
