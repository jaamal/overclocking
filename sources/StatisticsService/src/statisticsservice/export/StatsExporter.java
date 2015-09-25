package statisticsservice.export;

import java.util.List;

import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;
import storage.statistics.IStatisticsRepository;
import dataContracts.AlgorithmType;
import dataContracts.statistics.StatisticsObject;


public class StatsExporter implements IStatsExporter {

    private IFilesRepository filesRepository;
    private IStatisticsRepository statisticsRepository;
    private IStatisticsConverterFactory statisticsConverterFactory;
    private IFactorsRepositoryFactory factorsRepositoryFactory;

    public StatsExporter(IFilesRepository filesRepository,
                         IFactorsRepositoryFactory factorsRepositoryFactory,
                         IStatisticsRepository statisticsRepository, 
                         IStatisticsConverterFactory statisticsConverterFactory) {
        this.filesRepository = filesRepository;
        this.factorsRepositoryFactory = factorsRepositoryFactory;
        this.statisticsRepository = statisticsRepository;
        this.statisticsConverterFactory = statisticsConverterFactory;
    }
    
    @Override
    public String exportAll() {
        IStatisticsConverter statisticsConverter = statisticsConverterFactory.create();
        List<String> fileIds = filesRepository.getFileIds();
        for (String fileId : fileIds) {
            StatisticsObject[] stats = statisticsRepository.readAll(fileId);
            statisticsConverter.append(stats);
        }
        
        IFactorsRepository lzFactorsRepository = factorsRepositoryFactory.find(AlgorithmType.lzInf);
        Iterable<String> factorizationIds = lzFactorsRepository.getDoneStatisticIds();
        for (String factorizationId : factorizationIds) {
            StatisticsObject[] stats = statisticsRepository.readAll(factorizationId);
            statisticsConverter.append(stats);
        }
        
        IFactorsRepository lz77FactorsRepository = factorsRepositoryFactory.find(AlgorithmType.lz77);
        factorizationIds = lz77FactorsRepository.getDoneStatisticIds();
        for (String factorizationId : factorizationIds) {
            StatisticsObject[] stats = statisticsRepository.readAll(factorizationId);
            statisticsConverter.append(stats);
        }
        
        return statisticsConverter.run();
    }

}
