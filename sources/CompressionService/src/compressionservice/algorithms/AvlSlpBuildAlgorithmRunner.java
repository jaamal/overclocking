package compressionservice.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.IAvlTreeSLPBuilder;
import avlTree.slpBuilders.ISLPBuilder;

import compressionservice.runner.parameters.IRunParams;

import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.Statistics;
import dataContracts.statistics.IStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class AvlSlpBuildAlgorithmRunner implements IAlgorithmRunner {

    private IAvlTreeSLPBuilder avlTreeSLPBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private IStatisticsObjectFactory statisticsObjectFactory;
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
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.sourceId = sourceId;
    }

    @Override
    public StatisticsObject run(IRunParams runParams) {
        FactorDef[] factorization = resourceProvider.getFactorization(sourceId);
        IStatistics statistics = new Statistics();

        ISLPBuilder slp = avlTreeSLPBuilder.buildSlp(factorization, statistics);

        StatisticsObject statisticsObject = statisticsObjectFactory.create(runParams.toMap(), statistics.toMap());

        String statisticsId = statisticsObject.getId();
        IArrayItemsWriter<Product> writer = slpProductsRepository.getWriter(statisticsId);
        Product[] products = slp.toNormalForm();
        writer.addAll(products);
        writer.done();

        return statisticsObject;
    }
}
