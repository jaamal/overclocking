package tests.integration.Trees;

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
import compressionservice.compression.algorithms.lzInf.ILZFactorIterator;
import compressionservice.compression.algorithms.lzInf.ILZFactorIteratorFactory;
import compressionservice.compression.algorithms.lzInf.LZFactor;
import dataContracts.AvlMergePattern;
import dataContracts.AvlSplitPattern;
import dataContracts.DataFactoryType;
import dataContracts.LZFactorDef;
import dataContracts.statistics.CompressionStatistics;

import org.junit.Test;

import serialization.products.ProductsSerializer4;
import tests.integration.IntegrationTestBase;

import java.util.ArrayList;

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

    private LZFactorDef[] getFactorization(String text) {
        ILZFactorIterator iterator = container.get(ILZFactorIteratorFactory.class).create(DataFactoryType.memory, new MemoryReadableCharArray(text));
        ArrayList<LZFactorDef> factors = new ArrayList<LZFactorDef>();
        while(iterator.hasFactors())
        {
            LZFactor nextFactor = iterator.getNextFactor();
            factors.add(new LZFactorDef(nextFactor.isTerminal, nextFactor.startPosition, nextFactor.endPosition - nextFactor.startPosition, nextFactor.value));
        }
        return factors.toArray(new LZFactorDef[0]);
    }
}
