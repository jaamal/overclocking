package tests.integration.CompressionService.lcaOnline;

import static junit.framework.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import storage.cassandraClient.ISchemeInitializer;
import storage.slpProductsRepository.ISlpProductsRepository;
import storage.statistics.IStatisticsRepository;
import tests.integration.AlgorithmRunnerTestBase;

import compressionservice.runner.IWorker;
import compressionservice.runner.parameters.IRunParamsFactory;

import dataContracts.AlgorithmType;
import dataContracts.ContentType;
import dataContracts.Product;
import dataContracts.files.FileMetadata;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class LCAOnlineRunnerIntegrationTest extends AlgorithmRunnerTestBase
{
    private ISlpProductsRepository slpProductsRepository;
    private IStatisticsRepository statisticsRepository;
    private IWorker worker;
    private IRunParamsFactory runParamsFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        container.get(ISchemeInitializer.class).setUpCluster();
        
        statisticsRepository = container.get(IStatisticsRepository.class);
        slpProductsRepository = container.get(ISlpProductsRepository.class);
        runParamsFactory = container.get(IRunParamsFactory.class);
        worker = container.get(IWorker.class);
    }

    @Test
    public void testSimpleDNASquared() {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository);
        
        worker.process(UUID.randomUUID(), runParamsFactory.create(fileMetadata.getId(), AlgorithmType.lcaOnlineSlp));
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        assertEquals("600", actuals[0].statistics.get(StatisticKeys.SlpWidth));
        assertEquals("176", actuals[0].statistics.get(StatisticKeys.SlpCountRules));
        checkSLP(actuals[0].getId(), fileMetadata);
    }
    
    @Test
    public void testSimpleDNA()
    {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository);

        worker.process(UUID.randomUUID(), runParamsFactory.create(fileMetadata.getId(), AlgorithmType.lcaOnlineSlp));
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        assertEquals("300", actuals[0].statistics.get(StatisticKeys.SlpWidth));
        assertEquals("164", actuals[0].statistics.get(StatisticKeys.SlpCountRules));
        checkSLP(actuals[0].getId(), fileMetadata);
    }

    @Test
    public void testGZippedFile()
    {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("AAES.gz", ContentType.GZip, filesRepository);

        worker.process(UUID.randomUUID(), runParamsFactory.create(fileMetadata.getId(), AlgorithmType.lcaOnlineSlp));

        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        assertEquals("51359", actuals[0].statistics.get(StatisticKeys.SlpWidth));
        assertEquals("12605", actuals[0].statistics.get(StatisticKeys.SlpCountRules));
        checkSLP(actuals[0].getId(), fileMetadata);
    }

    private void checkSLP(String slpId, FileMetadata fileMetadata)
    {
        String expected = readFileText(fileMetadata);
        String actual = unpack(slpProductsRepository.readAll(slpId));
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
