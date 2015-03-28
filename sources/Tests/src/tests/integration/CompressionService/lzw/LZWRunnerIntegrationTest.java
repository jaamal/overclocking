package tests.integration.CompressionService.lzw;

import static junit.framework.Assert.assertEquals;
import helpers.FileHelpers;

import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.statistics.IStatisticsRepository;
import tests.integration.StorageTestBase;

import compressionservice.compression.parameters.CompressionRunParams;
import compressionservice.compression.running.LzwRunner;

import dataContracts.ContentType;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class LZWRunnerIntegrationTest extends StorageTestBase
{
    private IStatisticsRepository statisticsRepository;
    private LzwRunner lzwRunner;

    @Override
    public void setUp()
    {
        super.setUp();
        container.get(ISchemeInitializer.class).setUpCluster();
        
        lzwRunner = container.get(LzwRunner.class);
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
        
        CompressionRunParams runParams = new CompressionRunParams();
        lzwRunner.checkAndRefillParams(runParams);
        lzwRunner.run(runParams);
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("211", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
    }
    
    @Test
    public void testSimpleDNA() {
        String fileId = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository).getId();
        
        CompressionRunParams runParams = new CompressionRunParams();
        lzwRunner.checkAndRefillParams(runParams);
        lzwRunner.run(runParams);

        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("124", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
    }
}
