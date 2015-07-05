package compressionservice.algorithms;

import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import SLPs.ConcurrentSLPExtractor;
import SLPs.ProductsSerializer4;
import SLPs.SLPExtractor;
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
import compressionservice.runner.parameters.IRunParams;

import dataContracts.AlgorithmType;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.RunParamKeys;

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
    public Iterable<String> getAllSourceIds(IRunParams runParams) {
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, RunParamKeys.AlgorithmType);
        switch (algorithmType) {
            case avlSlpConcurrent: 
            case avlSlp:
            case cartesianSlp: {
                return factorsRepositoryFactory.getLZRepository().getDoneStatisticIds();
            }
            case lzw: 
            case lz77: 
            case lzInf:
            case lcaOnlineSlp: {
                return filesRepository.getFileIds();
            }
            default:
                throw new RuntimeException(String.format("Algorithm of type %s is not supported", algorithmType));
        }
    }
    
    @Override
    public IAlgorithmRunner create(IRunParams runParams) {
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, RunParamKeys.AlgorithmType);
        switch (algorithmType) {
            case avlSlpConcurrent: {
                return createAvlSlpConcurrent(runParams);
            }
            case avlSlp: {
                return createAvlSlpRunner(runParams);
            }
            case cartesianSlp: {
                return createCartesianSlpRunner(runParams);
            }
            case lcaOnlineSlp: {
                return createLCAOnlineRunner(runParams);
            }
            case lzw: {
                return createLZWRunner(runParams);
            }
            case lz77: {
                return createLZ77Runner(runParams);
            }
            case lzInf: {
                return createLZInfRunner(runParams);
            }
            default:
                throw new RuntimeException(String.format("Algorithm of type %s is not supported", algorithmType));
        }
    }

    private IAlgorithmRunner createAvlSlpConcurrent(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        final AvlMergePattern defaultAvlMergePattern = AvlMergePattern.sequential;
        final int defaultThreadCount = 4;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        AvlMergePattern avlMergePattern = runParams.getOrDefaultEnum(AvlMergePattern.class, RunParamKeys.AvlMergePattern, defaultAvlMergePattern);
        int threadCount = runParams.getOrDefaultInt(RunParamKeys.ThreadCount, defaultThreadCount);
        
        IAvlTreeArrayMerger avlTreeArrayMerger = avlTreeArrayMergerFactory.create(avlMergePattern);
        IAvlTreeManagerFactory avlTreeManagerFactory = new ConcurrentAvlTreeManagerFactory(dataFactoryType);
        IParallelExecutorFactory parallelExecutorFactory = new ParallelExecutorFactory(threadCount);
        ConcurrentSLPExtractor slpExtractor = new ConcurrentSLPExtractor(threadCount);
        IConcurrencyAvlTreeSLPBuilder concurrencyAvlTreeSLPBuilder = new ConcurrencyAvlTreeSLPBuilder(avlTreeManagerFactory, new AvlTreeSetFactory(avlTreeArrayMerger), parallelExecutorFactory, factorizationIndexer, slpExtractor, new ProductsSerializer4());
        return new ConcurrencyAvlSlpBuildAlgorithmRunner(concurrencyAvlTreeSLPBuilder, slpProductsRepository, resourceProvider, 
                factorsRepositoryFactory, statisticsObjectFactory, sourceId);
    }

    private IAlgorithmRunner createAvlSlpRunner(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        final AvlMergePattern defaultAvlMergePattern = AvlMergePattern.block;
        final AvlSplitPattern defaultAvlSplitPattern = AvlSplitPattern.fromMerged;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        AvlMergePattern avlMergePattern = runParams.getOrDefaultEnum(AvlMergePattern.class, RunParamKeys.AvlMergePattern, defaultAvlMergePattern);
        AvlSplitPattern avlSplitPattern = runParams.getOrDefaultEnum(AvlSplitPattern.class, RunParamKeys.AvlSplitPattern, defaultAvlSplitPattern);
        
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(settings, dataFactoryType);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(avlTreeArrayMergerFactory, avlMergePattern, avlSplitPattern);
        IAvlTreeSLPBuilder avlTreeSLPBuilder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), new ProductsSerializer4());
        return new AvlSlpBuildAlgorithmRunner(avlTreeSLPBuilder, slpProductsRepository, resourceProvider, factorsRepositoryFactory, statisticsObjectFactory, sourceId);
    }

    private IAlgorithmRunner createCartesianSlpRunner(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        
        CartesianTreeManagerFactory cartesianTreeManagerFactory = new CartesianTreeManagerFactory(settings, dataFactoryType);
        CartesianSlpTreeBuilder cartesianSLPTreeBuilder = new CartesianSlpTreeBuilder(cartesianTreeManagerFactory, new SLPExtractor(), new ProductsSerializer4());
        return new CartesianSlpBuildAlgorithmRunner(cartesianSLPTreeBuilder, slpProductsRepository, resourceProvider, factorsRepositoryFactory, 
                statisticsObjectFactory, sourceId);
    }
    
    private IAlgorithmRunner createLZ77Runner(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        final int defaultWindowSize = 32 * 1024;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        int windowSize = runParams.getOrDefaultInt(RunParamKeys.WindowSize, defaultWindowSize);
        
        return new Lz77AlgorithmRunner(resourceProvider, filesRepository, factorsRepositoryFactory.getLZ77Repository(), factorIteratorFactory, 
                statisticsObjectFactory, sourceId, dataFactoryType, windowSize);
    }
    
    private IAlgorithmRunner createLZInfRunner(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        
        return new LzInfAlgorithmRunner(resourceProvider, filesRepository, factorIteratorFactory, factorsRepositoryFactory.getLZRepository(), 
                 statisticsObjectFactory, sourceId, dataFactoryType);
    }
    
    private IAlgorithmRunner createLZWRunner(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        
        return new LzwAlgorithmRunner(lzwFactorsAnalyzer, resourceProvider, filesRepository, statisticsObjectFactory, sourceId, dataFactoryType);
    }
    
    private IAlgorithmRunner createLCAOnlineRunner(IRunParams runParams) {
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        
        String sourceId = runParams.get(RunParamKeys.SourceId);
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        
        return new LCAOnlineSlpBuildAlgorithmRunner(lcaOnlineCompressor, slpProductsRepository, resourceProvider, filesRepository, 
                statisticsObjectFactory, new ProductsSerializer4(), sourceId, dataFactoryType);
    }
}
