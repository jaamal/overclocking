package compressionservice.algorithms;

import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.IAvlTreeSLPBuilder;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.Statistics;

public class AvlSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private IAvlTreeSLPBuilder avlTreeSLPBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private String sourceId;
    private String resultId;

    public AvlSlpBuildAlgorithmRunner(
            IAvlTreeSLPBuilder avlTreeSLPBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId,
            String resultId)
    {
        this.avlTreeSLPBuilder = avlTreeSLPBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
        this.resultId = resultId;
    }

    @Override
    public IStatistics run() {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        IStatistics statistics = new Statistics();

        SLPModel slpModel = avlTreeSLPBuilder.buildSlp(factorization, statistics);
        
        Product[] products = slpModel.toNormalForm();
        slpProductsRepository.writeAll(resultId, products);
        return statistics;
    }
}
