package tests.integration.CompressionService.lcaOnline;

import static junit.framework.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.slpProductsRepository.ISlpProductsRepository;
import storage.statistics.IStatisticsRepository;
import tests.integration.AlgorithmRunnerTestBase;
import compressionservice.compression.parameters.RunParams;
import compressionservice.compression.running.LCAOnlineRunner;
import dataContracts.AlgorithmType;
import dataContracts.ContentType;
import dataContracts.Product;
import dataContracts.files.FileMetadata;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class LCAOnlineRunnerIntegrationTest extends AlgorithmRunnerTestBase
{
    private ISlpProductsRepository slpProductsRepository;
    private IStatisticsRepository statisticsRepository;
    private LCAOnlineRunner lcaOnlineRunner;

    @Override
    public void setUp()
    {
        super.setUp();
        container.get(ISchemeInitializer.class).setUpCluster();
        
        statisticsRepository = container.get(IStatisticsRepository.class);
        slpProductsRepository = container.get(ISlpProductsRepository.class);
        lcaOnlineRunner = container.get(LCAOnlineRunner.class);
    }
    
    @Override
    public void tearDown() {
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.statistics.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.files.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.factorizations.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.slps.name());
        super.tearDown();
    }

    @Test
    public void testSimpleDNASquared() {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository);
        
        RunParams runParams = new RunParams();
        runParams.put(CompressionRunKeys.AlgorithmType, AlgorithmType.lcaOnlineSlp);
        lcaOnlineRunner.run(runParams);
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SlpWidth));
        assertEquals("176", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        checkSLP(actuals[0].getId(), fileMetadata);
    }
    
    @Test
    public void testSimpleDNA()
    {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository);

        RunParams runParams = new RunParams();
        runParams.put(CompressionRunKeys.AlgorithmType, AlgorithmType.lcaOnlineSlp);
        lcaOnlineRunner.run(runParams);
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SlpWidth));
        assertEquals("164", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        checkSLP(actuals[0].getId(), fileMetadata);
    }

    @Test
    public void testGZippedFile()
    {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("AAES.gz", ContentType.GZip, filesRepository);

        RunParams runParams = new RunParams();
        runParams.put(CompressionRunKeys.AlgorithmType, AlgorithmType.lcaOnlineSlp);
        lcaOnlineRunner.run(runParams);

        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        assertEquals("51359", actuals[0].statistics.get(CompressionStatisticKeys.SlpWidth));
        assertEquals("12605", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        checkSLP(actuals[0].getId(), fileMetadata);
    }

    private void checkSLP(String slpId, FileMetadata fileMetadata)
    {
        String expected = readFileText(fileMetadata);
        String actual = unpack(slpProductsRepository.readItems(slpId));
        assertEquals(expected, actual);
    }

    private String unpack(List<Product> products) {
        ArrayList<Product> arrayList = new ArrayList<Product>();
        for (Product productDef : products)
        {
            arrayList.add(productDef);
        }
        Product[] array = arrayList.toArray(new Product[0]);
        StringBuilder stringBuilder = new StringBuilder();
        buildText(array, array.length - 1, stringBuilder);
        return stringBuilder.toString();
    }

    private void buildText(Product[] array, int index, StringBuilder stringBuilder) {
        if (array[index].isTerminal)
            stringBuilder.append((char)array[index].symbol);
        else
        {
            buildText(array, (int) array[index].first, stringBuilder);
            buildText(array, (int) array[index].second, stringBuilder);
        }
    }
}
