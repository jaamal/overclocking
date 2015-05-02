package compressionservice.algorithms;

import storage.filesRepository.IFilesRepository;

import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lzw.ILZWFactorsAnalyzer;

import dataContracts.DataFactoryType;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.Statistics;

public class LzwAlgorithmRunner implements IAlgorithmRunner {
    
    private IResourceProvider resourceProvider;
    private ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private String sourceId;
    private DataFactoryType dataFactoryType;
    
    public LzwAlgorithmRunner(
            ILZWFactorsAnalyzer lzwFactorsAnalyzer,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository, 
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId, 
            DataFactoryType dataFactoryType) {
        this.lzwFactorsAnalyzer = lzwFactorsAnalyzer;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }
    
    //TODO: this algorithm only counts number of factors, but doesnt create any factorization
    @Override
    public IStatistics run(String resultId) {
        try(IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType))
        {
            TimeCounter timeCounter = TimeCounter.start();
            long lzwCodesCount = lzwFactorsAnalyzer.countLZWCodes(charArray);
            timeCounter.finish();
            
            IStatistics statistics = new Statistics();
            statistics.putParam(StatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.putParam(StatisticKeys.FactorizationLength, String.valueOf(lzwCodesCount));
            statistics.putParam(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            return statistics;
        }
    }
}
