package tests.integration.CompressionService.lzw;

import static junit.framework.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.UUID;

import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.statistics.IStatisticsRepository;
import tests.integration.StorageTestBase;

import compressionservice.runner.IWorker;
import compressionservice.runner.parameters.IRunParamsFactory;

import dataContracts.AlgorithmType;
import dataContracts.ContentType;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class LZWRunnerIntegrationTest extends StorageTestBase
{
    private IStatisticsRepository statisticsRepository;
    private IWorker worker;
    private IRunParamsFactory runParamsFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        container.get(ISchemeInitializer.class).setUpCluster();
        runParamsFactory = container.get(IRunParamsFactory.class);
        worker = container.get(IWorker.class);
        statisticsRepository = container.get(IStatisticsRepository.class);
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
        
        worker.process(UUID.randomUUID(), runParamsFactory.create(fileId, AlgorithmType.lzw));
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("211", actuals[0].statistics.get(StatisticKeys.FactorizationLength));
        assertEquals("600", actuals[0].statistics.get(StatisticKeys.SourceLength));
    }
    
    @Test
    public void testSimpleDNA() {
        String fileId = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository).getId();
        
        worker.process(UUID.randomUUID(), runParamsFactory.create(fileId, AlgorithmType.lzw));

        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("124", actuals[0].statistics.get(StatisticKeys.FactorizationLength));
        assertEquals("300", actuals[0].statistics.get(StatisticKeys.SourceLength));
    }
}
