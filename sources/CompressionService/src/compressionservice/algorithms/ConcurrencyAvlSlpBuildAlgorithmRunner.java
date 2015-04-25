package compressionservice.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.ConcurrentAvlBuilderStopwatches;
import avlTree.slpBuilders.IConcurrencyAvlTreeSLPBuilder;
import avlTree.slpBuilders.ISLPBuilder;
import compressionservice.runner.parameters.IRunParams;
import dataContracts.AvlMergePattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatistics;
import dataContracts.statistics.ICompressionStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class ConcurrencyAvlSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    //TODO: check where we should use it
    private final static AvlMergePattern defaultAVLMergePattern = AvlMergePattern.sequential;
    private final static DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
    private final static int defaultThreadCount = 4;
    
    private IConcurrencyAvlTreeSLPBuilder avlTreeSLPBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private IFactorsRepository factorsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;

    public ConcurrencyAvlSlpBuildAlgorithmRunner(
            IConcurrencyAvlTreeSLPBuilder avlTreeSLPBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.avlTreeSLPBuilder = avlTreeSLPBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.factorsRepository = factorsRepositoryFactory.getLZRepository();
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        String sourceId = runParams.get(CompressionRunKeys.SourceId);
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        ICompressionStatistics statistics = new CompressionStatistics();

        ConcurrentAvlBuilderStopwatches stopwatches = new ConcurrentAvlBuilderStopwatches();
        ISLPBuilder slp = avlTreeSLPBuilder.buildSlp(factorization, statistics, stopwatches);
        stopwatches.printTimes();

        StatisticsObject statisticsObject = statisticsObjectFactory.create(runParams.toMap(), statistics.toMap());

        String statisticsId = statisticsObject.getId();
        IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(statisticsId);
        Product[] products = slp.toNormalForm();
        writer.addAll(products);
        writer.done();

        return statisticsObject;
    }

    @Override
    public Iterable<String> getAllSourceIds() {
        return factorsRepository.getDoneStatisticIds();
    }
}
