package compressionservice.compression.algorithms;

import storage.IArrayItemsWriter;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.slpProductsRepository.ISlpProductsRepository;
import avlTree.slpBuilders.IAvlTreeSLPBuilder;
import avlTree.slpBuilders.ISLPBuilder;

import compressionservice.compression.parameters.ICompressionRunParams;

import dataContracts.FactorDef;
import dataContracts.Product;
import dataContracts.statistics.CompressionStatistics;
import dataContracts.statistics.ICompressionStatistics;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticsObject;

public class AvlSlpBuildAlgorithm implements ISlpBuildAlgorithm {

    private IAvlTreeSLPBuilder avlTreeSLPBuilder;
    private ISlpProductsRepository slpProductsRepository;
    private IResourceProvider resourceProvider;
    private IFactorsRepository factorsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;

    public AvlSlpBuildAlgorithm(
            IAvlTreeSLPBuilder avlTreeSLPBuilder,
            ISlpProductsRepository slpProductsRepository,
            IResourceProvider resourceProvider,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IStatisticsObjectFactory statisticsObjectFactory)
    {
        this.avlTreeSLPBuilder = avlTreeSLPBuilder;
        this.slpProductsRepository = slpProductsRepository;
        this.resourceProvider = resourceProvider;
        this.statisticsObjectFactory = statisticsObjectFactory;
        this.factorsRepository = factorsRepositoryFactory.getLZRepository();
    }

    @Override
    public StatisticsObject build(ICompressionRunParams runParams) {
        FactorDef[] factorization = resourceProvider.getFactorization(runParams);
        ICompressionStatistics statistics = new CompressionStatistics();

        ISLPBuilder slp = avlTreeSLPBuilder.buildSlp(factorization, statistics);

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
