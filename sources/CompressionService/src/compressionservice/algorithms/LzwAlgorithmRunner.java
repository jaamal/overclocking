package compressionservice.algorithms;

import storage.filesRepository.IFilesRepository;
import commons.utils.TimeCounter;
import compressionservice.algorithms.lzw.ILZWFactorsAnalyzer;
import data.charArray.IReadableCharArray;
import dataContracts.AlgorithmType;
import dataContracts.DataFactoryType;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;

public class LzwAlgorithmRunner implements IAlgorithm {
    
    private final IResourceProvider resourceProvider;
    private final ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private final String sourceId;
    private final DataFactoryType dataFactoryType;
    private final String resultId;
    IStatistics statistics;
    
    public LzwAlgorithmRunner(
            ILZWFactorsAnalyzer lzwFactorsAnalyzer,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository, 
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId, 
            String resultId,
            DataFactoryType dataFactoryType) {
        this.lzwFactorsAnalyzer = lzwFactorsAnalyzer;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
        this.resultId = resultId;
        this.dataFactoryType = dataFactoryType;
    }
    
    //TODO: this algorithm only counts number of factors, but doesnt create any factorization
    @Override
    public void run() {
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
        if (statistics == null)
            throw new RuntimeException("Statistics is empty since algorithm does not running.");
        return statistics;
    }

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.lzw;
    }
}
