package compressionservice.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.IAvlTreeSLPBuilder;
import avlTree.slpBuilders.ISLPBuilder;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.Statistics;

public class AvlSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private IAvlTreeSLPBuilder avlTreeSLPBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private String sourceId;

    public AvlSlpBuildAlgorithmRunner(
            IAvlTreeSLPBuilder avlTreeSLPBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IStatisticsObjectFactory statisticsObjectFactory,
            String sourceId)
    {
        this.avlTreeSLPBuilder = avlTreeSLPBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
    }

    @Override
    public IStatistics run(String resultId) {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        IStatistics statistics = new Statistics();

        ISLPBuilder slp = avlTreeSLPBuilder.buildSlp(factorization, statistics);

        IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(resultId);
        Product[] products = slp.toNormalForm();
        writer.addAll(products);
        writer.done();

        return statistics;
    }
}
