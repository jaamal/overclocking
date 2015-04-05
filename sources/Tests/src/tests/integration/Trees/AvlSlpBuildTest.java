package tests.integration.Trees;

import java.util.ArrayList;

import org.junit.Test;

import serialization.products.ProductsSerializer4;
import tests.integration.IntegrationTestBase;
import SLPs.SLPExtractor;
import SLPs.SlpByteSizeCounter;
import avlTree.AvlTreeManagerFactory;
import avlTree.IAvlTreeManagerFactory;
import avlTree.buffers.AvlTreeBufferFactory;
import avlTree.mergers.AvlTreeArrayMergerFactory;
import avlTree.slpBuilders.AvlTreeSLPBuilder;
import avlTree.slpBuilders.ISLPBuilder;
import commons.settings.ISettings;
import compressingCore.dataAccess.MemoryReadableCharArray;
import compressionservice.compression.algorithms.factorization.IFactorIterator;
import compressionservice.compression.algorithms.factorization.IFactorIteratorFactory;
import compressionservice.compression.parameters.CompressionRunParams;
import compressionservice.compression.parameters.ICompressionRunParams;
import dataContracts.AlgorithmType;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatistics;

public class AvlSlpBuildTest extends IntegrationTestBase {

    @Test
    public void Test()
    {
        IAvlTreeManagerFactory avlTreeManagerFactory = new AvlTreeManagerFactory(container.get(ISettings.class), DataFactoryType.file);
        AvlTreeBufferFactory avlTreeBufferFactory = new AvlTreeBufferFactory(new AvlTreeArrayMergerFactory(), AvlMergePattern.block, AvlSplitPattern.fromFirst);
        SlpByteSizeCounter slpByteSizeCounter = new SlpByteSizeCounter(new ProductsSerializer4());
        AvlTreeSLPBuilder builder = new AvlTreeSLPBuilder(avlTreeManagerFactory, avlTreeBufferFactory, new SLPExtractor(), slpByteSizeCounter);
        ISLPBuilder slp = builder.buildSlp(getFactorization("abacaba"), new CompressionStatistics());
        System.out.println(slp.getStatistics().countRules);
    }

    private FactorDef[] getFactorization(String text) {
        ICompressionRunParams runParams = new CompressionRunParams();
        runParams.putParam(CompressionRunKeys.AlgorithmType, AlgorithmType.lzInf);
        runParams.putParam(CompressionRunKeys.DataFactoryType, DataFactoryType.memory);
        
        IFactorIterator iterator = container.get(IFactorIteratorFactory.class).create(runParams, new MemoryReadableCharArray(text));
        ArrayList<FactorDef> factors = new ArrayList<FactorDef>();
        while(iterator.any())
        {
            FactorDef nextFactor = iterator.next();
            factors.add(nextFactor);
        }
        return factors.toArray(new FactorDef[0]);
    }
}
