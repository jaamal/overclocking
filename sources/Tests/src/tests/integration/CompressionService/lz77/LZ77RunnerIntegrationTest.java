package tests.integration.CompressionService.lz77;

import static junit.framework.Assert.assertEquals;
import java.nio.charset.Charset;
import java.util.UUID;
import org.junit.Test;
import compressionservice.algorithms.lz77.suffixTree.ISuffixTree;
import compressionservice.algorithms.lz77.suffixTree.ISuffixTreeBuilder;
import compressionservice.runner.IWorker;
import compressionservice.runner.parameters.IRunParamsFactory;
import dataContracts.AlgorithmType;
import dataContracts.ContentType;
import dataContracts.FactorDef;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.StatisticsObject;
import helpers.FactorizationScenarios;
import helpers.FileHelpers;
import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.statistics.IStatisticsRepository;
import tests.integration.StorageTestBase;

public class LZ77RunnerIntegrationTest extends StorageTestBase
{
    private IStatisticsRepository staisticsRepository;
    private IFactorsRepository factorsRepository;
    private IWorker worker;
    private IRunParamsFactory runParamsFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        
        container.get(ISchemeInitializer.class).setUpCluster();
        
        staisticsRepository = container.get(IStatisticsRepository.class);
        factorsRepository = container.get(IFactorsRepositoryFactory.class).find(AlgorithmType.lz77);
        runParamsFactory = container.get(IRunParamsFactory.class);
        worker = container.get(IWorker.class);
    }
    
    @Override
    public void tearDown() {
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.statistics.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.files.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.factorizations.name());
        super.tearDown();
    }
    
    @Test
    public void testSimpleDNASquared() {
        String fileId = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository).getId();
        
        worker.process(UUID.randomUUID(), runParamsFactory.create(fileId, AlgorithmType.lz77));
        
        StatisticsObject[] actuals = staisticsRepository.readAll(fileId);
        assertEquals(1, actuals.length);
        assertEquals("97", actuals[0].statistics.get(StatisticKeys.FactorizationLength));
        assertEquals("600", actuals[0].statistics.get(StatisticKeys.SourceLength));
        checkFactorization(actuals[0].getId(), "simpleDNA_twoSections_clean.txt");
    }

    @Test
    public void testSimpleDNA() {
        String fileId = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository).getId();
        
        worker.process(UUID.randomUUID(), runParamsFactory.create(fileId, AlgorithmType.lz77));

        StatisticsObject[] actuals = staisticsRepository.readAll(fileId);
        assertEquals(1, actuals.length);
        assertEquals("96", actuals[0].statistics.get(StatisticKeys.FactorizationLength));
        assertEquals("300", actuals[0].statistics.get(StatisticKeys.SourceLength));
        checkFactorization(actuals[0].getId(), "simpleDNA_clean.txt");
    }

    @Test
    public void testGZippedFile() {
        String fileId = FileHelpers.writeDnaToRepository("AAES.gz", ContentType.GZip, filesRepository).getId();

        worker.process(UUID.randomUUID(), runParamsFactory.create(fileId, AlgorithmType.lz77));
        
        StatisticsObject[] actuals = staisticsRepository.readAll(fileId);
        assertEquals(1, actuals.length);
        assertEquals("7165", actuals[0].statistics.get(StatisticKeys.FactorizationLength));
        assertEquals("51359", actuals[0].statistics.get(StatisticKeys.SourceLength));
    }
    
    @Test
    public void testSuffixTree() {
        ISuffixTree suffixTree = container.get(ISuffixTreeBuilder.class).build("abrakadabra");
        int x = 1;
    }

    private void checkFactorization(String statsId, String filePath) {
        String expected = FileHelpers.readTestFile(filePath, Charset.forName("cp1251"));
        String actual = FactorizationScenarios.stringify(factorsRepository.readAll(statsId).toArray(new FactorDef[0]));
        assertEquals(expected, actual);
    }
}