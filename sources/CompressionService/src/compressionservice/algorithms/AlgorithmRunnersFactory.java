package compressionservice.algorithms;

import serialization.products.ProductsSerializer4;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import SLPs.ConcurrentSLPExtractor;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.AvlTreeManagerFactory;
import avlTree.ConcurrentAvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.AvlTreeBufferFactory;
import avlTree.mergers.IAvlTreeArrayMerger;
import avlTree.mergers.IAvlTreeArrayMergerFactory;
import avlTree.slpBuilders.AvlTreeSLPBuilder;
import avlTree.slpBuilders.ConcurrencyAvlTreeSLPBuilder;
import avlTree.slpBuilders.IAvlTreeSLPBuilder;
import avlTree.slpBuilders.IConcurrencyAvlTreeSLPBuilder;
import avlTree.slpBuilders.IFactorizationIndexer;
import avlTree.slpBuilders.IParallelExecutorFactory;
import avlTree.slpBuilders.ParallelExecutorFactory;
import avlTree.treeSets.AvlTreeSetFactory;
import cartesianTree.CartesianTreeManagerFactory;
import cartesianTree.slpBuilders.CartesianSlpTreeBuilder;
import commons.settings.ISettings;
import compressionservice.algorithms.factorization.IFactorIteratorFactory;
import compressionservice.algorithms.lcaOnlineSlp.ILCAOnlineCompressor;
import compressionservice.algorithms.lzw.ILZWFactorsAnalyzer;
import compressionservice.profile.Analysator;
import compressionservice.runner.parameters.IRunParams;
import dataContracts.AlgorithmType;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.IStatisticsObjectFactory;

public class AlgorithmRunnersFactory implements IAlgorithmRunnersFactory {

    private ISettings settings;
    private final IAvlTreeArrayMergerFactory avlTreeArrayMergerFactory;
    private ISlpProductsRepository slpProductsRepository;
    private IFactorizationIndexer factorizationIndexer;
    private IResourceProvider resourceProvider;
    private ILCAOnlineCompressor lcaOnlineCompressor;
    private IFactorsRepositoryFactory factorsRepositoryFactory;
    private IFilesRepository filesRepository;
    private ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private IFactorIteratorFactory factorIteratorFactory;
    private IStatisticsObjectFactory statisticsObjectFactory;

    public AlgorithmRunnersFactory(
            ISettings settings,
            IAvlTreeArrayMergerFactory avlTreeArrayMergerFactory,
            ISlpProductsRepository slpProductsRepository,
            IFactorizationIndexer factorizationIndexer,
            IResourceProvider resourceProvider,
            ILCAOnlineCompressor lcaOnlineCompressor,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IFilesRepository filesRepository,
            ILZWFactorsAnalyzer lzwFactorsAnalyzer,
            IFactorIteratorFactory factorIteratorFactory,
            IStatisticsObjectFactory statisticsObjectFactory) {
        this.settings = settings;
        this.avlTreeArrayMergerFactory = avlTreeArrayMergerFactory;
        this.slpProductsRepository = slpProductsRepository;
        this.factorizationIndexer = factorizationIndexer;
        this.resourceProvider = resourceProvider;
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.factorsRepositoryFactory = factorsRepositoryFactory;
        this.filesRepository = filesRepository;
        this.lzwFactorsAnalyzer = lzwFactorsAnalyzer;
        this.factorIteratorFactory = factorIteratorFactory;
        this.statisticsObjectFactory = statisticsObjectFactory;
    }

    @Override
    public IAlgorithmRunner create(IRunParams runParams) {
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, CompressionRunKeys.AlgorithmType);
        switch (algorithmType) {
            case avlSlpConcurrent: {
                DataFactoryType dataFactoryType = runParams.getEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType);
                AvlMergePattern avlMergePattern = runParams.getEnum(AvlMergePattern.class, CompressionRunKeys.AvlMergePattern);
                IAvlTreeArrayMerger avlTreeArrayMerger = avlTreeArrayMergerFactory.create(avlMergePattern);
                IAvlTreeManagerFactory avlTreeManagerFactory = new ConcurrentAvlTreeManagerFactory(dataFactoryType);
                IParallelExecutorFactory parallelExecutorFactory = new ParallelExecutorFactory(runParams.getInt(CompressionRunKeys.ThreadCount));
                SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
                ConcurrentSLPExtractor slpExtractor = new ConcurrentSLPExtractor(runParams.getInt(CompressionRunKeys.ThreadCount));
                IConcurrencyAvlTreeSLPBuilder concurrencyAvlTreeSLPBuilder = new ConcurrencyAvlTreeSLPBuilder(avlTreeManagerFactory, new AvlTreeSetFactory(avlTreeArrayMerger), parallelExecutorFactory, factorizationIndexer, slpExtractor, slpByteSizeCounter);
                return new ConcurrencyAvlSlpBuildAlgorithmRunner(concurrencyAvlTreeSLPBuilder, slpProductsRepository, resourceProvider, factorsRepositoryFactory, statisticsObjectFactory);
            }
            case avlSlp: {
                DataFactoryType dataFactoryType = runParams.getEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType);
                AvlMergePattern avlMergePattern = runParams.getEnum(AvlMergePattern.class, CompressionRunKeys.AvlMergePattern);
                AvlSplitPattern avlSplitPattern = runParams.getEnum(AvlSplitPattern.class, CompressionRunKeys.AvlSplitPattern);
                IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(settings, dataFactoryType);
                AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(avlTreeArrayMergerFactory, avlMergePattern, avlSplitPattern);
                SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
                IAvlTreeSLPBuilder avlTreeSLPBuilder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), slpByteSizeCounter);
                return new AvlSlpBuildAlgorithmRunner(avlTreeSLPBuilder, slpProductsRepository, resourceProvider, factorsRepositoryFactory, statisticsObjectFactory);
            }
            case cartesianSlp: {
                DataFactoryType dataFactoryType = runParams.getEnum(DataFactoryType.class, CompressionRunKeys.DataFactoryType);
                CartesianTreeManagerFactory cartesianTreeManagerFactory = new CartesianTreeManagerFactory(settings, dataFactoryType);
                SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
                CartesianSlpTreeBuilder cartesianSLPTreeBuilder = new CartesianSlpTreeBuilder(cartesianTreeManagerFactory, new SLPExtractor(), slpByteSizeCounter);
                return new CartesianSlpBuildAlgorithmRunner(cartesianSLPTreeBuilder, slpProductsRepository, resourceProvider, factorsRepositoryFactory, statisticsObjectFactory);
            }
            case lcaOnlineSlp: {
                SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
                return new LCAOnlineSlpBuildAlgorithmRunner(lcaOnlineCompressor, slpProductsRepository, resourceProvider, filesRepository, statisticsObjectFactory, slpByteSizeCounter);
            }
            case lzw: {
                return new LzwAlgorithmRunner(lzwFactorsAnalyzer, resourceProvider, filesRepository, statisticsObjectFactory);
            }
            case lz77: {
                return new Lz77AlgorithmRunner(resourceProvider, filesRepository, factorsRepositoryFactory, factorIteratorFactory, new Analysator(), statisticsObjectFactory);
            }
            case lzInf: {
                return new LzInfAlgorithmRunner(resourceProvider, filesRepository, factorIteratorFactory, factorsRepositoryFactory, new Analysator(), statisticsObjectFactory);
            }
            default:
                throw new RuntimeException(String.format("Algorithm of type %s is not supported", algorithmType));
        }
    }

}
