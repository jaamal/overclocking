package compressionservice.algorithms;

import java.util.Arrays;
import java.util.HashMap;

import storage.filesRepository.IFilesRepository;

import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.algorithms.lzw.ILZWFactorsAnalyzer;
import compressionservice.runner.parameters.IRunParams;

import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class LzwAlgorithmRunner implements IAlgorithmRunner {
    
    private final static DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
    
    private IResourceProvider resourceProvider;
    private ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private IFilesRepository filesRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;
    
    public LzwAlgorithmRunner(
            ILZWFactorsAnalyzer lzwFactorsAnalyzer,
            IResourceProvider resourceProvider,
            IFilesRepository filesRepository, 
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.lzwFactorsAnalyzer = lzwFactorsAnalyzer;
        this.resourceProvider = resourceProvider;
        this.filesRepository = filesRepository;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }
    
    //TODO: this algorithm only counts number of factors, but doesnt create any factorization
    @Override
    public StatisticsObject run(IRunParams runParams) {
        String sourceId = runParams.get(CompressionRunKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType, defaultDataFactoryType);
        
        try(IReadableCharArray charArray = resourceProvider.getText(sourceId, dataFactoryType))
        {
            TimeCounter timeCounter = TimeCounter.start();
            long lzwCodesCount = lzwFactorsAnalyzer.countLZWCodes(charArray);
            timeCounter.finish();
            
            HashMap<CompressionStatisticKeys, String> statistics = new HashMap<>();
            statistics.put(CompressionStatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.put(CompressionStatisticKeys.FactorizationLength, String.valueOf(lzwCodesCount));
            statistics.put(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getMillis()));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics);
            
            return result;
        }
    }
    
    @Override
    public Iterable<String> getAllSourceIds() {
        return Arrays.asList(filesRepository.getAllIds());
    }
}
