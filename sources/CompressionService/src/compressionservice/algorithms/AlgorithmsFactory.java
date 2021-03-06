package compressionservice.algorithms;

import SLPs.ConcurrentSLPExtractor;
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
import dataContracts.statistics.RunParamKeys;
import serialization.factors.IFactorSerializer;
import serialization.products.IProductSerializer;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.filesRepository.IFilesRepository;

public class AlgorithmsFactory implements IAlgorithmsFactory {

    private ISettings settings;
    private final IAvlTreeArrayMergerFactory avlTreeArrayMergerFactory;
    private IFactorizationIndexer factorizationIndexer;
    private IResourceProvider resourceProvider;
    private ILCAOnlineCompressor lcaOnlineCompressor;
    private IFactorsRepositoryFactory factorsRepositoryFactory;
    private IFilesRepository filesRepository;
    private ILZWFactorsAnalyzer lzwFactorsAnalyzer;
    private IFactorIteratorFactory factorIteratorFactory;
    private IFactorSerializer factorSerializer;
    private IProductSerializer productSerializer;

    public AlgorithmsFactory(
            ISettings settings,
            IAvlTreeArrayMergerFactory avlTreeArrayMergerFactory,
            IFactorizationIndexer factorizationIndexer,
            IResourceProvider resourceProvider,
            ILCAOnlineCompressor lcaOnlineCompressor,
            IFactorsRepositoryFactory factorsRepositoryFactory,
            IFilesRepository filesRepository,
            ILZWFactorsAnalyzer lzwFactorsAnalyzer,
            IFactorIteratorFactory factorIteratorFactory,
            IFactorSerializer factorSerializer,
            IProductSerializer productSerializer) {
        this.settings = settings;
        this.avlTreeArrayMergerFactory = avlTreeArrayMergerFactory;
        this.factorizationIndexer = factorizationIndexer;
        this.resourceProvider = resourceProvider;
        this.lcaOnlineCompressor = lcaOnlineCompressor;
        this.factorsRepositoryFactory = factorsRepositoryFactory;
        this.filesRepository = filesRepository;
        this.lzwFactorsAnalyzer = lzwFactorsAnalyzer;
        this.factorIteratorFactory = factorIteratorFactory;
        this.factorSerializer = factorSerializer;
        this.productSerializer = productSerializer;
    }

    @Override
    public Iterable<String> getAllSourceIds(IRunParams runParams) {
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, RunParamKeys.AlgorithmType);
        switch (algorithmType) {
            case avlSlpConcurrent: 
            case avlSlp:
            case cartesianSlp: {
                return factorsRepositoryFactory.find(AlgorithmType.lzInf).getDoneStatisticIds();
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
    public IAlgorithm create(IRunParams runParams) {
        AlgorithmType algorithmType = runParams.getEnum(AlgorithmType.class, RunParamKeys.AlgorithmType);
        String sourceId = runParams.get(RunParamKeys.SourceId);
        
        final DataFactoryType defaultDataFactoryType = DataFactoryType.memory;
        DataFactoryType dataFactoryType = runParams.getOrDefaultEnum(DataFactoryType.class, RunParamKeys.DataFactoryType, defaultDataFactoryType);
        
        switch (algorithmType) {
            case avlSlpConcurrent: {
                return createAvlSlpConcurrent(sourceId, dataFactoryType, runParams);
            }
            case avlSlp: {
                return createAvlSlpRunner(sourceId, dataFactoryType, runParams);
            }
            case cartesianSlp: {
                return createCartesianSlpRunner(sourceId, dataFactoryType, runParams);
            }
            case lcaOnlineSlp: {
                return createLCAOnlineRunner(sourceId, dataFactoryType, runParams);
            }
            case lzw: {
                return createLZWRunner(sourceId, dataFactoryType, runParams);
            }
            case lz77: {
                return createLZ77Runner(sourceId, dataFactoryType, runParams);
            }
            case lzInf: {
                return createLZInfRunner(sourceId, dataFactoryType, runParams);
            }
            default:
                throw new RuntimeException(String.format("Algorithm of type %s is not supported", algorithmType));
        }
    }

    private IAlgorithm createAvlSlpConcurrent(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        final AvlMergePattern defaultAvlMergePattern = AvlMergePattern.sequential;
        AvlMergePattern avlMergePattern = runParams.getOrDefaultEnum(AvlMergePattern.class, RunParamKeys.AvlMergePattern, defaultAvlMergePattern);
        final int defaultThreadCount = 4;
        int threadCount = runParams.getOrDefaultInt(RunParamKeys.ThreadCount, defaultThreadCount);
        
        IAvlTreeArrayMerger avlTreeArrayMerger = avlTreeArrayMergerFactory.create(avlMergePattern);
        IAvlTreeManagerFactory avlTreeManagerFactory = new ConcurrentAvlTreeManagerFactory(dataFactoryType);
        IParallelExecutorFactory parallelExecutorFactory = new ParallelExecutorFactory(threadCount);
        ConcurrentSLPExtractor slpExtractor = new ConcurrentSLPExtractor(threadCount);
        IConcurrencyAvlTreeSLPBuilder concurrencyAvlTreeSLPBuilder = new ConcurrencyAvlTreeSLPBuilder(avlTreeManagerFactory, new AvlTreeSetFactory(avlTreeArrayMerger), parallelExecutorFactory, factorizationIndexer, slpExtractor, productSerializer);
        return new ConcurrencyAvlSlpAlgorithm(concurrencyAvlTreeSLPBuilder, resourceProvider, productSerializer, sourceId);
    }

    private IAlgorithm createAvlSlpRunner(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        final AvlMergePattern defaultAvlMergePattern = AvlMergePattern.block;
        final AvlSplitPattern defaultAvlSplitPattern = AvlSplitPattern.fromMerged;

        AvlMergePattern avlMergePattern = runParams.getOrDefaultEnum(AvlMergePattern.class, RunParamKeys.AvlMergePattern, defaultAvlMergePattern);
        AvlSplitPattern avlSplitPattern = runParams.getOrDefaultEnum(AvlSplitPattern.class, RunParamKeys.AvlSplitPattern, defaultAvlSplitPattern);
        
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(settings, dataFactoryType);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(avlTreeArrayMergerFactory, avlMergePattern, avlSplitPattern);
        IAvlTreeSLPBuilder avlTreeSLPBuilder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), productSerializer);
        return new AvlSlpAlgorithm(avlTreeSLPBuilder, resourceProvider, productSerializer, sourceId);
    }

    private IAlgorithm createCartesianSlpRunner(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        CartesianTreeManagerFactory cartesianTreeManagerFactory = new CartesianTreeManagerFactory(settings, dataFactoryType);
        CartesianSlpTreeBuilder cartesianSLPTreeBuilder = new CartesianSlpTreeBuilder(cartesianTreeManagerFactory, new SLPExtractor(), productSerializer);
        return new CartesianSlpAlgorithm(cartesianSLPTreeBuilder, resourceProvider, productSerializer, sourceId);
    }
    
    private IAlgorithm createLZ77Runner(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        final int defaultWindowSize = 32 * 1024;
        int windowSize = runParams.getOrDefaultInt(RunParamKeys.WindowSize, defaultWindowSize);
        
        return new Lz77Algorithm(resourceProvider, factorIteratorFactory, factorSerializer, sourceId, dataFactoryType, windowSize);
    }
    
    private IAlgorithm createLZInfRunner(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        return new LzInfAlgorithm(resourceProvider, factorIteratorFactory, factorSerializer, sourceId, dataFactoryType);
    }
    
    private IAlgorithm createLZWRunner(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        return new LzwAlgorithm(lzwFactorsAnalyzer, resourceProvider, sourceId, dataFactoryType);
    }
    
    private IAlgorithm createLCAOnlineRunner(String sourceId, DataFactoryType dataFactoryType, IRunParams runParams) {
        return new LCAOnlineAlgorithm(lcaOnlineCompressor, resourceProvider, productSerializer, sourceId, dataFactoryType);
    }
}
