package tests.integration.Storage;

import helpers.FactorizationScenarios;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;

import org.junit.Test;

import serialization.ISerializer;
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
        String slpId = UUID.randomUUID().toString();
        Product[] products = genSLP(random);
        final ISlpProductsRepository slpProductsRepository = container.create(SlpProductsRepository.class);
        slpProductsRepository.writeAll(slpId, products);

        String[] statistics = Iterables.toArray(slpProductsRepository.getDoneStatisticIds(), String.class);
        Assert.assertEquals(1, statistics.length);
        Assert.assertEquals(slpId, statistics[0]);
        
        List<Product> actualFactorization = slpProductsRepository.readAll(slpId);
        Assert.assertEquals(products.length, actualFactorization.size());
        for (int i = 0; i < products.length; i++) {
            String expectedFactorStr = serializer.stringify(products[i]);
            String actualFactorStr = serializer.stringify(actualFactorization.get(i));
            Assert.assertEquals(expectedFactorStr, actualFactorStr);
        }
    }
	
	@Test
    public void TestWriteLZFactorization() {
        String factorizationId = UUID.randomUUID().toString();
        LZFactorDef[] factorization = FactorizationScenarios.generate(2000);
        final IFactorsRepository lzFactorsRepository = container.create(LZFactorsRepository.class);
        lzFactorsRepository.writeAll(factorizationId, factorization);

        String[] statistics = Iterables.toArray(lzFactorsRepository.getDoneStatisticIds(), String.class);
        Assert.assertEquals(1, statistics.length);
        Assert.assertEquals(factorizationId, statistics[0]);
        
        List<FactorDef> actualFactorization = lzFactorsRepository.readAll(factorizationId);
        Assert.assertEquals(factorization.length, actualFactorization.size());
        for (int i = 0; i < factorization.length; i++) {
            String expectedFactorStr = serializer.stringify(factorization[i]);
            String actualFactorStr = serializer.stringify(actualFactorization.get(i));
            Assert.assertEquals(expectedFactorStr, actualFactorStr);
        }
    }
	
	@Test
	public void TestWriteLZ77Factorization() {
	    String factorizationId = UUID.randomUUID().toString();
        FactorDef[] factorization = FactorizationScenarios.generate2(2000);
        final IFactorsRepository lz77FactorsRepository = container.create(LZ77FactorsRepository.class);
        lz77FactorsRepository.writeAll(factorizationId, factorization);

        String[] statistics = Iterables.toArray(lz77FactorsRepository.getDoneStatisticIds(), String.class);
        Assert.assertEquals(1, statistics.length);
        Assert.assertEquals(factorizationId, statistics[0]);
        
        List<FactorDef> actualFactorization = lz77FactorsRepository.readAll(factorizationId);
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
		    testFactorizations[i] = FactorizationScenarios.generate2(2000);
		}
		
		final IFactorsRepository lz77FactorsRepository = container.create(LZ77FactorsRepository.class);
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
			
			List<FactorDef> actualFactorization = lz77FactorsRepository.readAll(id);
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
    
    private static Runnable createWriteFactorizationTask(final IFactorsRepository factorsRepository, final String factorizationId, final FactorDef[] factorization) {
        return new Runnable() {
            public void run() {
                factorsRepository.writeAll(factorizationId, factorization);
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
}
