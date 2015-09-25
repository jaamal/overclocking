package compressionservice.algorithms;

import avlTree.slpBuilders.IConcurrencyAvlTreeSLPBuilder;
import dataContracts.AlgorithmType;
import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.SLPModel;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.Statistics;
import storage.slpProductsRepository.ISlpProductsRepository;

public class ConcurrencyAvlSlpBuildAlgorithmRunner implements IAlgorithm {
    
    private final IConcurrencyAvlTreeSLPBuilder avlTreeSLPBuilder;
    private final ISlpProductsRepository slpProductsRepository;
    private final IResourceProvider resourceProvider;
    private final String sourceId;
    private final String resultId;
    private IStatistics statistics;

    public ConcurrencyAvlSlpBuildAlgorithmRunner(
            IConcurrencyAvlTreeSLPBuilder avlTreeSLPBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            String sourceId,
            String resultId) {
        this.avlTreeSLPBuilder = avlTreeSLPBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.sourceId = sourceId;
        this.resultId = resultId;
    }

    @Override
    public void run() {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        statistics = new Statistics();

        SLPModel slpModel = avlTreeSLPBuilder.buildSlp(factorization, statistics);

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

    @Override
    public AlgorithmType getType()
    {
        return AlgorithmType.avlSlpConcurrent;
    }
}
