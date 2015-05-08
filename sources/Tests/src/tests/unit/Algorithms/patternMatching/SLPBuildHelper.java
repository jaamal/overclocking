package tests.unit.Algorithms.patternMatching;

import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import patternMatching.fcpm.preprocessing.Product;
import patternMatching.fcpm.preprocessing.ProductFactory;
import patternMatching.fcpm.preprocessing.ProductsPreprocessor;
import SLPs.ProductsSerializer4;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.AvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.AvlTreeBufferFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.AvlTreeSLPBuilder;
import avlTree.slpBuilders.ISLPBuilder;

import commons.files.FileManager;
import commons.files.IFileManager;
import commons.settings.ISettings;
import compressingCore.dataAccess.IDataFactory;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.algorithms.factorization.FactorIteratorFactory;
import compressionservice.algorithms.factorization.IFactorIterator;
import compressionservice.algorithms.lzInf.arrayMinSearching.ArrayMinSearcherFactory;
import compressionservice.algorithms.lzInf.arrayMinSearching.IArrayMinSearcherFactory;
import compressionservice.algorithms.lzInf.suffixArray.ExternalProcessExecutor;
import compressionservice.algorithms.lzInf.suffixArray.IExternalProcessExecutor;
import compressionservice.algorithms.lzInf.suffixArray.ISuffixArrayBuilder;
import compressionservice.algorithms.lzInf.suffixArray.SuffixArrayBuilder;
import compressionservice.algorithms.lzInf.suffixTreeImitation.IOnlineSuffixTreeFactory;
import compressionservice.algorithms.lzInf.suffixTreeImitation.OnLineSuffixTreeFactory;

import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.LZFactorDef;
import dataContracts.statistics.Statistics;

public class SLPBuildHelper {
    private IDataFactory dataFactory;

    public SLPBuildHelper(IDataFactory dataFactory) {
        this.dataFactory = dataFactory;

        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.OFF);
    }

    public Product[] buildSlp(ISettings settings, String text) {
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(settings, DataFactoryType.memory);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.recursiveBlock, AvlSplitPattern.fromFirst);
        SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
        AvlTreeSLPBuilder avlSlpTreeBuilder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), slpByteSizeCounter);
        ISLPBuilder slpBuilder = avlSlpTreeBuilder.buildSlp(getFactorization(settings, text), new Statistics());
        ProductsPreprocessor builder = new ProductsPreprocessor(new ProductFactory());
        return builder.execute(slpBuilder.toNormalForm());
    }

    private LZFactorDef[] getFactorization(ISettings settings, String text) {
        IFileManager fileManager = new FileManager();
        IExternalProcessExecutor externalProcessExecutor = new ExternalProcessExecutor();
        ISuffixArrayBuilder suffixArrayFactory = new SuffixArrayBuilder(dataFactory, fileManager, externalProcessExecutor, settings);
        IArrayMinSearcherFactory arrayMinSearcherFactory = new ArrayMinSearcherFactory(dataFactory);
        IOnlineSuffixTreeFactory onlineSuffixTreeFactory = new OnLineSuffixTreeFactory(suffixArrayFactory, arrayMinSearcherFactory);
        IFactorIterator iterator = new FactorIteratorFactory(onlineSuffixTreeFactory).createInfiniteIterator(new MemoryReadableCharArray(text), DataFactoryType.memory);
        
        ArrayList<FactorDef> factors = new ArrayList<FactorDef>();
        while (iterator.any()) {
            FactorDef nextFactor = iterator.next();
            factors.add(nextFactor);
        }
        return factors.toArray(new LZFactorDef[0]);
    }
}
