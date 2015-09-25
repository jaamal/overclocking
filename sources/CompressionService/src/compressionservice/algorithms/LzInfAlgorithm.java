package compressionservice.algorithms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import commons.utils.TimeCounter;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;
import data.charArray.IReadableCharArray;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;
import serialization.factors.IFactorSerializer;

public class LzInfAlgorithm extends Algorithm implements ICompressionAlgorithm {

    private static Logger logger = LogManager.getLogger(LzInfAlgorithm.class);

    private final IResourceProvider resourceProvider;
    private final IFactorIteratorFactory factorIteratorFactory;
    private final IFactorSerializer factorSerializer;
    private final String sourceId;
    private final DataFactoryType dataFactoryType;
    IStatistics statistics;
    ArrayList<FactorDef> factorization;

    public LzInfAlgorithm(
            IResourceProvider resourceProvider,
            IFactorIteratorFactory factorIteratorFactory,
            IFactorSerializer factorSerializer,
            String sourceId,
            DataFactoryType dataFactoryType) {
        this.resourceProvider = resourceProvider;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorSerializer = factorSerializer;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }
    
    @Override
    protected void runInternal()
    {
        try (IReadableCharArray source = resourceProvider.getText(sourceId, dataFactoryType)) {
            TimeCounter timeCounter = TimeCounter.start();
            factorization = new ArrayList<>();
            try (IFactorIterator factorIterator = factorIteratorFactory.createInfiniteIterator(source, dataFactoryType)) {
                while (factorIterator.any()) {
                    if (factorization.size() % 10000 == 0)
                        logger.info(String.format("Produced %d factors", factorization.size()));

                    FactorDef factor = factorIterator.next();
                    factorization.add(factor);
                }
            } catch (Exception e) {
                logger.error(String.format("Fail to run lzInf algorithm."), e);
            }
            timeCounter.finish();

            statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(source.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(factorization.size()));
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            statistics.putParam(StatisticKeys.FactorizationByteSize, String.valueOf(factorSerializer.calcSizeInBytes(factorization)));
        }
    }

    @Override
    public IStatistics getStats()
    {
        checkIsFinished();
        return statistics;
    }

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.lzInf;
    }

    @Override
    public byte[] getCompressedRepresentation()
    {
        checkIsFinished();
        
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            factorSerializer.serialize(stream, factorization.toArray(new FactorDef[0]));
            return stream.toByteArray();
        }
        catch (IOException ex) {
            throw new RuntimeException("Fail to serialize factorization.", ex);
        }
    }

    @Override
    public boolean supportFactorization()
    {
        return true;
    }

    @Override
    public List<FactorDef> getFactorization()
    {
        checkIsFinished();
        return factorization;
    }
}

