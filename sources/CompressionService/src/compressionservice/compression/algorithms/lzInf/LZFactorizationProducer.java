package compressionservice.compression.algorithms.lzInf;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import compressingCore.dataAccess.IReadableCharArray;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;

public class LZFactorizationProducer implements ILZFactorizationProducer
{
    private static Logger logger = LogManager.getLogger(LZFactorizationProducer.class);
    
    private IFactorsRepository<LZFactorDef> factorsRepository;
    private ILZFactorIteratorFactory lzFactorIteratorFactory;

    public LZFactorizationProducer(IFactorsRepositoryFactory factorsRepositoryFactory, ILZFactorIteratorFactory lzFactorIteratorFactory)
    {
        this.factorsRepository = factorsRepositoryFactory.getLZRepository();
        this.lzFactorIteratorFactory = lzFactorIteratorFactory;
    }

    @Override
    public void run(String statisticsId, IReadableCharArray source, DataFactoryType dataFactoryType)
    {
        logger.info("Start produce factors for statisticsId = " + statisticsId);

        try(ILZFactorIterator factorIterator = lzFactorIteratorFactory.create(dataFactoryType, source);) {
            IArrayItemsWriter<LZFactorDef> writer = factorsRepository.getWriter(statisticsId);
            int index = 0;
            while (factorIterator.hasFactors())
            {
                if (index % 100000 == 0 && index > 0)
                    logger.info("Produced " + index + " factors");
                index++;
                LZFactor factor = factorIterator.getNextFactor();
                long length = factor.endPosition - factor.startPosition;
                writer.add(new LZFactorDef(factor.isTerminal, factor.startPosition, length, factor.value));
            }
            writer.done();
        }
        logger.info("Finish produce factors for statisticsId = " + statisticsId);
    }
}
