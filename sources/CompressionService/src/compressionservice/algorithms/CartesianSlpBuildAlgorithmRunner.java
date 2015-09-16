package compressionservice.algorithms;

import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import cartesianTree.slpBuilders.ICartesianSlpTreeBuilder;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.Statistics;

public class CartesianSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private final ICartesianSlpTreeBuilder cartesianSLPTreeBuilder;
    private final ISlpProductsRepository slpProductsRepository;
    private final IResourceProvider resourceProvider;
    private final String sourceId;
    private final String resultId;
    private IStatistics statistics;

    public CartesianSlpBuildAlgorithmRunner(
            ICartesianSlpTreeBuilder cartesianSLPTreeBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider, 
            IFactorsRepositoryFactory factorsRepositoryFactory, 
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId,
            String resultId)
    {
        this.cartesianSLPTreeBuilder = cartesianSLPTreeBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
        this.resultId = resultId;
    }

    @Override
    public void run() {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        statistics = new Statistics();

        SLPModel slpModel = cartesianSLPTreeBuilder.buildSlp(factorization, statistics);

        Product[] products = slpModel.toNormalForm();
        slpProductsRepository.writeAll(resultId, products);
    }
    
    @Override
    public IStatistics getStats()
    {
        if (statistics == null)
            throw new RuntimeException("Statistics is empty since algorithm does not running.");
        return statistics;
    }
}
