package tests.integration.Storage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import serialization.ISerializer;
import storage.IArrayItemsRepository;
import storage.IArrayItemsWriter;
import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.LZ77FactorsRepository;
import storage.factorsRepository.LZFactorsRepository;
import storage.slpProductsRepository.ISlpProductsRepository;
import storage.slpProductsRepository.SlpProductsRepository;
import tests.integration.StorageTestBase;

import com.google.common.collect.Iterables;

import dataContracts.FactorDef;
import dataContracts.LZFactorDef;
import dataContracts.Product;

public class FactorsRepositoryTest extends StorageTestBase {
	
    private static Random random = new Random();
	private ISerializer serializer;
    private ISchemeInitializer schemaInitializer;

	@Override
	public void setUp()
	{
		super.setUp();
		serializer = container.get(ISerializer.class);
		schemaInitializer = container.get(ISchemeInitializer.class);
		
		schemaInitializer.setUpCluster();
	}

	@Override
    public void tearDown() {
	    schemaInitializer.truncateKeyspace(KeySpaces.factorizations.name());
	    schemaInitializer.truncateKeyspace(KeySpaces.statistics.name());
	    schemaInitializer.truncateKeyspace(KeySpaces.slps.name());
	    
        super.tearDown();
    }
	
	@Test
    public void TestWriteSLP() {
        String slpId = "jkhjkh";
        Product[] products = genSLP(random);
        final ISlpProductsRepository slpProductsRepository = container.create(SlpProductsRepository.class);
        writeItems(slpProductsRepository, slpId, products);

        String[] statistics = Iterables.toArray(slpProductsRepository.getDoneStatisticIds(), String.class);
        Assert.assertEquals(1, statistics.length);
        Assert.assertEquals(slpId, statistics[0]);
        
        List<Product> actualFactorization = slpProductsRepository.readItems(slpId);
        Assert.assertEquals(products.length, actualFactorization.size());
        for (int i = 0; i < products.length; i++) {
            String expectedFactorStr = serializer.stringify(products[i]);
            String actualFactorStr = serializer.stringify(actualFactorization.get(i));
            Assert.assertEquals(expectedFactorStr, actualFactorStr);
        }
    }
	
	@Test
    public void TestWriteLZFactorization() {
        String factorizationId = "fhfhf";
        LZFactorDef[] factorization = genLZFactorization(random);
        final IFactorsRepository<LZFactorDef> lzFactorsRepository = container.create(LZFactorsRepository.class);
        writeItems(lzFactorsRepository, factorizationId, factorization);

        String[] statistics = Iterables.toArray(lzFactorsRepository.getDoneStatisticIds(), String.class);
        Assert.assertEquals(1, statistics.length);
        Assert.assertEquals(factorizationId, statistics[0]);
        
        List<LZFactorDef> actualFactorization = lzFactorsRepository.readItems(factorizationId);
        Assert.assertEquals(factorization.length, actualFactorization.size());
        for (int i = 0; i < factorization.length; i++) {
            String expectedFactorStr = serializer.stringify(factorization[i]);
            String actualFactorStr = serializer.stringify(actualFactorization.get(i));
            Assert.assertEquals(expectedFactorStr, actualFactorStr);
        }
    }
	
	@Test
	public void TestWriteLZ77Factorization() {
        String factorizationId = "zzzz";
        FactorDef[] factorization = genLZ77Factorization(random);
        final IFactorsRepository<FactorDef> lz77FactorsRepository = container.create(LZ77FactorsRepository.class);
        writeItems(lz77FactorsRepository, factorizationId, factorization);

        String[] statistics = Iterables.toArray(lz77FactorsRepository.getDoneStatisticIds(), String.class);
        Assert.assertEquals(1, statistics.length);
        Assert.assertEquals(factorizationId, statistics[0]);
        
        List<FactorDef> actualFactorization = lz77FactorsRepository.readItems(factorizationId);
        Assert.assertEquals(factorization.length, actualFactorization.size());
        for (int i = 0; i < factorization.length; i++) {
            String expectedFactorStr = serializer.stringify(factorization[i]);
            String actualFactorStr = serializer.stringify(actualFactorization.get(i));
            Assert.assertEquals(expectedFactorStr, actualFactorStr);
        }
	}

    @Test
	public void TestWriteSeveralLZ77Factorizations() {
		final String[] ids = new String[]{"id1", "id2", "id3"};
		final FactorDef[][] testFactorizations = new FactorDef[ids.length][];
		for (int i = 0; i < ids.length; ++i) {
		    testFactorizations[i] = genLZ77Factorization(random);
		}
		
		final IFactorsRepository<FactorDef> lz77FactorsRepository = container.create(LZ77FactorsRepository.class);
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		for (int i = 0; i < ids.length; ++i) {
		    executorService.execute(createWriteFactorizationTask(lz77FactorsRepository, ids[i], testFactorizations[i]));
		}
		try {
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Fail to await while all tasks are finished.", e);
        }
		
		int num = 0;
		Iterable<String> statistics = lz77FactorsRepository.getDoneStatisticIds();
		for (String id : statistics) {
			Assert.assertEquals(ids[num], id);
			
			List<FactorDef> actualFactorization = lz77FactorsRepository.readItems(id);
			Assert.assertEquals(testFactorizations[num].length, actualFactorization.size());
			for (int i = 0; i < testFactorizations[num].length; i++) {
			    String expectedFactorStr = serializer.stringify(testFactorizations[num][i]);
                String actualFactorStr = serializer.stringify(actualFactorization.get(i));
                Assert.assertEquals(expectedFactorStr, actualFactorStr);
			}
			num++;
		}
		Assert.assertEquals(ids.length, num);
	}
    
    private static <T> void writeItems(final IArrayItemsRepository<T> itemsRepository, final String id, final T[] items) {
        IArrayItemsWriter<T> writer = itemsRepository.getWriter(id);
        for (int j = 0; j < items.length; ++j)
            writer.add(items[j]);
        writer.done();
    }
    
    private static <T extends FactorDef> Runnable createWriteFactorizationTask(final IFactorsRepository<T> factorsRepository, final String factorizationId, final T[] factorization) {
        return new Runnable() {
            public void run() {
                writeItems(factorsRepository, factorizationId, factorization);
            }
        };
    }
    
    private static Product[] genSLP(Random random) {
        int length = random.nextInt(2000) + 1;
        Product[] result = new Product[length];
        for (int j = 0; j < length; ++j)
            result[j] = new Product((char)(random.nextInt(26) + 'a'));
        return result;
    }
    
    private static LZFactorDef[] genLZFactorization(Random random) {
        int length = random.nextInt(2000) + 1;
        LZFactorDef[] result = new LZFactorDef[length];
        for (int j = 0; j < length; ++j)
            result[j] = new LZFactorDef(random.nextBoolean(), random.nextLong(), random.nextLong(), (char)(random.nextInt(26) + 'a'));
        return result;
    }
    
    private static FactorDef[] genLZ77Factorization(Random random) {
        int length = random.nextInt(2000) + 1;
        FactorDef[] result = new FactorDef[length];
        for (int j = 0; j < length; ++j)
            result[j] = new FactorDef(random.nextBoolean(), random.nextLong(), random.nextLong(), (char)(random.nextInt(26) + 'a'));
        return result;
    }
}
