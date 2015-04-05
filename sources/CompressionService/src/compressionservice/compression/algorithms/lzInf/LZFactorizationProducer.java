package compressionservice.compression.algorithms.lzInf;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.factorization.IFactorIterator;
import compressionservice.compression.algorithms.factorization.IFactorIteratorFactory;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;

public class LZFactorizationProducer implements ILZFactorizationProducer
{
    private static Logger logger = LogManager.getLogger(LZFactorizationProducer.class);
    
    private IFactorsRepository<FactorDef> factorsRepository;
    private IFactorIteratorFactory factorIteratorFactory;

    public LZFactorizationProducer(IFactorsRepositoryFactory factorsRepositoryFactory, IFactorIteratorFactory factorIteratorFactory)
    {
        this.factorsRepository = factorsRepositoryFactory.getLZRepository();
        this.factorIteratorFactory = factorIteratorFactory;
    }

    @Override
    public void run(String statisticsId, IReadableCharArray source, DataFactoryType dataFactoryType)
    {
        logger.info("Start produce factors for statisticsId = " + statisticsId);

        try(IFactorIterator factorIterator = factorIteratorFactory.create(AlgorithmType.lzInf, dataFactoryType, source);) {
            IArrayItemsWriter<FactorDef> writer = factorsRepository.getWriter(statisticsId);
            int index = 0;
            while (factorIterator.any())
            {
                if (index % 100000 == 0 && index > 0)
                    logger.info("Produced " + index + " factors");
                index++;
                FactorDef factor = factorIterator.next();
                writer.add(factor);
            }
            writer.done();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("Finish produce factors for statisticsId = " + statisticsId);
    }
}
