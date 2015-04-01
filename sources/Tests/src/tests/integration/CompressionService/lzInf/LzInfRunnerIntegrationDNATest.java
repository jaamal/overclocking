package tests.integration.CompressionService.lzInf;

import static junit.framework.Assert.assertEquals;
import helpers.FileHelpers;

import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.statistics.IStatisticsRepository;
import tests.integration.StorageTestBase;

import compressionservice.compression.parameters.CompressionRunParams;
import compressionservice.compression.running.LzInfRunner;

import dataContracts.ContentType;
import dataContracts.LZFactorDef;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

//TODO: LzInf integration tests doesnt work at linux since we use windows program to build suffix tree
public class LzInfRunnerIntegrationDNATest extends StorageTestBase
{
    private IStatisticsRepository statisticsRepository;
    private IFactorsRepository<LZFactorDef> factorsRepository;
    private LzInfRunner lzInfRunner;

    @Override
    public void setUp()
    {
        super.setUp();
        
        container.get(ISchemeInitializer.class).setUpCluster();
        statisticsRepository = container.get(IStatisticsRepository.class);
        factorsRepository = container.get(IFactorsRepositoryFactory.class).getLZRepository();
        
        lzInfRunner = container.get(LzInfRunner.class);
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
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("97", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        checkFactorization(actuals[0].getId(), "simpleDNA_twoSections_clean.txt");
    }
    
    @Test
    public void testSimpleDNA() {
        String fileId = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository).getId();
        
        CompressionRunParams runParams = new CompressionRunParams();
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("96", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        checkFactorization(actuals[0].getId(), "simpleDNA_clean.txt");
    }

    @Test
    public void testGZippedFile() {
        String fileId = FileHelpers.writeDnaToRepository("AAES.gz", ContentType.GZip, filesRepository).getId();

        CompressionRunParams runParams = new CompressionRunParams();
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);

        StatisticsObject[] actuals = statisticsRepository.readAll(fileId);
        assertEquals(1,  actuals.length);
        assertEquals("7036", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("51359", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
    }


    private static String unpack(List<LZFactorDef> factors)
    {
        StringBuffer result = new StringBuffer();
        for (LZFactorDef factor : factors)
        {
            if (factor.isTerminal)
            {
                result.append((char) factor.symbol);
            }
            else
            {
                String subString = result.substring(
                        (int) factor.beginPosition, (int) (factor.beginPosition + factor.length));
                result.append(subString);
            }
        }
        return result.toString();
    }

    private void checkFactorization(String factorizationId, String filePath)
    {
        String expected = FileHelpers.readTestFile(filePath, Charset.forName("cp1251"));
        String actual = unpack(factorsRepository.readItems(factorizationId));
        assertEquals(expected, actual);
    }
}
