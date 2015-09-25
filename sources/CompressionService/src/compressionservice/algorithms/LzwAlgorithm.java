package compressionservice.algorithms;

import java.util.List;
import commons.utils.TimeCounter;
import compressionservice.algorithms.lzw.ILZWFactorsAnalyzer;
import data.charArray.IReadableCharArray;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;

public class LzwAlgorithm extends Algorithm implements ICompressionAlgorithm {
    
    private final IResourceProvider resourceProvider;
    private final ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private final String sourceId;
    private final DataFactoryType dataFactoryType;
    IStatistics statistics;
    
    public LzwAlgorithm(
            ILZWFactorsAnalyzer lzwFactorsAnalyzer,
            IResourceProvider resourceProvider,
            String sourceId, 
            DataFactoryType dataFactoryType) {
        this.lzwFactorsAnalyzer = lzwFactorsAnalyzer;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }
    
    @Override
    protected void runInternal()
    {
        try(IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType))
        {
            TimeCounter timeCounter = TimeCounter.start();
            long lzwCodesCount = lzwFactorsAnalyzer.countLZWCodes(charArray);
            timeCounter.finish();
            
            statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(lzwCodesCount));
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
    public AlgorithmType getType()
    {
        return AlgorithmType.lzw;
    }

    @Override
    public byte[] getCompressedRepresentation()
    {
        // TODO Implement factorization
        return new byte[0];
    }

    @Override
    public boolean supportFactorization()
    {
        return false;
    }

    @Override
    public List<FactorDef> getFactorization()
    {
        return null;
    }
}
