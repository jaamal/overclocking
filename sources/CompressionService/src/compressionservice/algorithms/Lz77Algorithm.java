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

public class Lz77Algorithm extends Algorithm implements ICompressionAlgorithm {

    private static Logger logger = LogManager.getLogger(Lz77Algorithm.class);

    private final IResourceProvider resourceProvider;
    private final IFactorIteratorFactory factorIteratorFactory;
    private final IFactorSerializer factorSerializer;

    private final String sourceId;
    private final DataFactoryType dataFactoryType;
    private final int windowSize;
    private IStatistics statistics;
    private ArrayList<FactorDef> factorization;

    public Lz77Algorithm(
            IResourceProvider resourceProvider,
            IFactorIteratorFactory factorIteratorFactory,
            IFactorSerializer factorSerializer,
            String sourceId, 
            DataFactoryType dataFactoryType,
            int windowSize) {
        this.resourceProvider = resourceProvider;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorSerializer = factorSerializer;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
        this.windowSize = windowSize;
    }

    @Override
    protected void runInternal()
    {
        try (IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType)) {
            TimeCounter timeCounter = TimeCounter.start();

            IFactorIterator factorIterator = factorIteratorFactory.createWindowIterator(charArray, windowSize);
            factorization = new ArrayList<>();
            while (factorIterator.any()) {
                if (factorization.size() % 10000 == 0)
                    logger.info(String.format("Produced %d factors", factorization.size()));
                factorization.add(factorIterator.next());
            }
            timeCounter.finish();

            statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(factorization.size()));
            statistics.putParam(StatisticKeys.FactorizationByteSize, String.valueOf(factorSerializer.calcSizeInBytes(factorization)));
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
        }
    }
    
    @Override
    public IStatistics getStats()
    {
        checkIsFinished();
        return statistics;
    }

    @Override
    public byte[] getCompressedRepresentation()
    {
        checkIsFinished();
        
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            factorSerializer.serialize(outputStream, getFactorization().toArray(new FactorDef[0]));
            return outputStream.toByteArray();
        }
        catch (IOException e) {
            throw new RuntimeException("Fail to build compressed representation.", e);
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

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.lz77;
    }
}
