package compressionservice.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.ISLPBuilder;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.Statistics;

public class CartesianSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private ICartesianSlpTreeBuilder cartesianSLPTreeBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
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
        this.sourceId = sourceId;
    }

    @Override
    public IStatistics run(String resultId) {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        IStatistics statistics = new Statistics();

        ISLPBuilder slp = cartesianSLPTreeBuilder.buildSlp(factorization, statistics);

        IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(resultId);
        Product[] products = slp.toNormalForm();
        writer.addAll(products);
        writer.done();

        return statistics;
    }
}
