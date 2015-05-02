package tests.integration.CompressionService.lz77;

import static junit.framework.Assert.assertEquals;
import helpers.FileHelpers;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.factorsRepository.IFactorsRepository;
import storage.factorsRepository.IFactorsRepositoryFactory;
import storage.statistics.IStatisticsRepository;
import tests.integration.StorageTestBase;
import compressionservice.runner.IWorker;
import compressionservice.runner.parameters.RunParams;
import dataContracts.AlgorithmType;
import dataContracts.ContentType;
import dataContracts.FactorDef;
import dataContracts.statistics.RunParamKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class LZ77RunnerIntegrationTest extends StorageTestBase
{
    private IStatisticsRepository staisticsRepository;
    private IFactorsRepository factorsRepository;
    private IWorker worker;

    @Override
    public void setUp()
    {
        super.setUp();
        
        container.get(ISchemeInitializer.class).setUpCluster();
        
        staisticsRepository = container.get(IStatisticsRepository.class);
        factorsRepository = container.get(IFactorsRepositoryFactory.class).getLZ77Repository();
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
        
        RunParams runParams = new RunParams();
        runParams.put(RunParamKeys.AlgorithmType, AlgorithmType.lz77);
        worker.process(UUID.randomUUID(), runParams);
        
        StatisticsObject[] actuals = staisticsRepository.readAll(fileId);
        assertEquals(1, actuals.length);
        assertEquals("97", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        checkFactorization(actuals[0].getId(), "simpleDNA_twoSections_clean.txt");
    }

    @Test
    public void testSimpleDNA() {
        String fileId = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository).getId();
        
        RunParams runParams = new RunParams();
        runParams.put(RunParamKeys.AlgorithmType, AlgorithmType.lz77);
        worker.process(UUID.randomUUID(), runParams);

        StatisticsObject[] actuals = staisticsRepository.readAll(fileId);
        assertEquals(1, actuals.length);
        assertEquals("96", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        checkFactorization(actuals[0].getId(), "simpleDNA_clean.txt");
        

    }

    @Test
    public void testGZippedFile() {
        String fileId = FileHelpers.writeDnaToRepository("AAES.gz", ContentType.GZip, filesRepository).getId();

        RunParams runParams = new RunParams();
        runParams.put(RunParamKeys.AlgorithmType, AlgorithmType.lz77);
        worker.process(UUID.randomUUID(), runParams);
        
        StatisticsObject[] actuals = staisticsRepository.readAll(fileId);
        assertEquals(1, actuals.length);
        assertEquals("7165", actuals[0].statistics.get(CompressionStatisticKeys.FactorizationLength));
        assertEquals("51359", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
    }

    private static String unpack(List<FactorDef> factors) {
        StringBuffer result = new StringBuffer();
        for (FactorDef factor : factors)
        {
            if (factor.isTerminal)
            {
                result.append((char)factor.symbol);
            }
            else
            {
                String subString = result.substring((int) factor.beginPosition, (int) (factor.beginPosition + factor.length));
                result.append(subString);
            }
        }
        return result.toString();
    }

    private void checkFactorization(String statsId, String filePath) {
        String expected = FileHelpers.readTestFile(filePath, Charset.forName("cp1251"));
        String actual = unpack(factorsRepository.readItems(statsId));
        assertEquals(expected, actual);
    }
}