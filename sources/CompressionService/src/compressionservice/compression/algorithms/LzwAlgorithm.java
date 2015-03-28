package compressionservice.compression.algorithms;

import java.util.Arrays;
import java.util.HashMap;

import storage.filesRepository.IFilesRepository;
import commons.utils.ITimeCounter;
import commons.utils.TimeCounter;
import compressingCore.dataAccess.IReadableCharArray;
import compressionservice.compression.algorithms.lzw.ILZWFactorsAnalyzer;
import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class LzwAlgorithm implements ISlpBuildAlgorithm {
    
    private IResourceProvider resourceProvider;
    private ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private IFilesRepository filesRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;
    
    public LzwAlgorithm(
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
    public StatisticsObject build(ICompressionRunParams runParams) {
        try(IReadableCharArray charArray = resourceProvider.getText(runParams))
        {
            ITimeCounter timeCounter = new TimeCounter();
            timeCounter.start();
            long lzwCodesCount = lzwFactorsAnalyzer.countLZWCodes(charArray);
            timeCounter.end();
            
            HashMap<CompressionStatisticKeys, String> statistics = new HashMap<>();
            statistics.put(CompressionStatisticKeys.SourceLength, String.valueOf(charArray.length()));
            statistics.put(CompressionStatisticKeys.FactorizationLength, String.valueOf(lzwCodesCount));
            statistics.put(CompressionStatisticKeys.RunningTime, String.valueOf(timeCounter.getTime()));
            StatisticsObject result = statisticsObjectFactory.create(runParams.toMap(), statistics);
            
            return result;
        }
    }
    
    @Override
    public Iterable<String> getAllSourceIds() {
        return Arrays.asList(filesRepository.getAllIds());
    }
}
