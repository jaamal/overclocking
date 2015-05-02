package compressionservice.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.ISLPBuilder;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;

import compressionservice.runner.parameters.IRunParams;

import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.Statistics;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class CartesianSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private ICartesianSlpTreeBuilder cartesianSLPTreeBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private IStatisticsObjectFactory statisticsObjectFactory;
    private String sourceId;

    public CartesianSlpBuildAlgorithmRunner(
            ICartesianSlpTreeBuilder cartesianSLPTreeBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider, 
            IFactorsRepositoryFactory factorsRepositoryFactory, 
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId)
    {
        this.cartesianSLPTreeBuilder = cartesianSLPTreeBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.sourceId = sourceId;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        IStatistics statistics = new Statistics();

        ISLPBuilder slp = cartesianSLPTreeBuilder.buildSlp(factorization, statistics);

        StatisticsObject statisticsObject = statisticsObjectFactory.create(runParams.toMap(), statistics.toMap());

        String statisticsId = statisticsObject.getId();
        IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(statisticsId);
        Product[] products = slp.toNormalForm();
        writer.addAll(products);
        writer.done();

        return statisticsObject;
    }
}
