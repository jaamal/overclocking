package compressionservice.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.ISLPBuilder;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;
import compressionservice.runner.parameters.IRunParams;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatistics;
import dataContracts.statistics.ICompressionStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class CartesianSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    //TODO: checkout where we should use it
    private final static DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
    
    private ICartesianSlpTreeBuilder cartesianSLPTreeBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private IFactorsRepository factorsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;

    public CartesianSlpBuildAlgorithmRunner(
            ICartesianSlpTreeBuilder cartesianSLPTreeBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider, 
            IFactorsRepositoryFactory factorsRepositoryFactory, 
            IStatisticsObjectFactory statisticsObjectFactory)
    {
        this.cartesianSLPTreeBuilder = cartesianSLPTreeBuilder;
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

        ISLPBuilder slp = cartesianSLPTreeBuilder.buildSlp(factorization, statistics);

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
