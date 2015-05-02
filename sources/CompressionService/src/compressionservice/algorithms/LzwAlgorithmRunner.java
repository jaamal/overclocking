package compressionservice.algorithms;

import java.util.HashMap;

import storage.filesRepository.IFilesRepository;

import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lzw.ILZWFactorsAnalyzer;
import compressionservice.runner.parameters.IRunParams;

import dataContracts.DataFactoryType;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class LzwAlgorithmRunner implements IAlgorithmRunner {
    
    private IResourceProvider resourceProvider;
    private ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private IStatisticsObjectFactory statisticsObjectFactory;
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
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.sourceId = sourceId;
        this.dataFactoryType = dataFactoryType;
    }
    
    //TODO: this algorithm only counts number of factors, but doesnt create any factorization
    @Override
    public StatisticsObject run(IRunParams runParams) {
        try(IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType))
        {
            TimeCounter timeCounter = TimeCounter.start();
            long lzwCodesCount = lzwFactorsAnalyzer.countLZWCodes(charArray);
            timeCounter.finish();
            
            HashMap<StatisticKeys, String> statistics = new HashMap<>();
            statistics.put(StatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.put(StatisticKeys.FactorizationLength, String.valueOf(lzwCodesCount));
            statistics.put(StatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics);
            
            return result;
        }
    }
}
