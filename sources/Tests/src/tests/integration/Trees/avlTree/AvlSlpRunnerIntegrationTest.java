package tests.integration.Trees.avlTree;

import static org.junit.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.slpProductsRepository.ISlpProductsRepository;
import storage.statistics.IStatisticsRepository;
import tests.integration.AlgorithmRunnerTestBase;

import compressionservice.compression.parameters.CompressionRunParams;
import compressionservice.compression.running.AvlSlpRunner;
import compressionservice.compression.running.LzInfRunner;

import dataContracts.ContentType;
import dataContracts.Product;
import dataContracts.files.FileMetadata;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class AvlSlpRunnerIntegrationTest extends AlgorithmRunnerTestBase {
    private ISlpProductsRepository slpProductsRepository;
    private IStatisticsRepository statisticsRepository;
    private AvlSlpRunner avlSlpRunner;
    private LzInfRunner lzInfRunner;

    @Override
    public void setUp() {
        super.setUp();

        container.get(ISchemeInitializer.class).setUpCluster();

        statisticsRepository = container.get(IStatisticsRepository.class);
        lzInfRunner = container.get(LzInfRunner.class);
        avlSlpRunner = container.get(AvlSlpRunner.class);
        slpProductsRepository = container.get(ISlpProductsRepository.class);

        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.statistics.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.files.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.factorizations.name());
        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.slps.name());
        container.get(ISchemeInitializer.class).setUpCluster();
    }

    @Override
    public void tearDown() {
//        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.statistics.name());
//        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.files.name());
//        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.factorizations.name());
//        container.get(ISchemeInitializer.class).truncateKeyspace(KeySpaces.slps.name());
        super.tearDown();
    }

    @Test
    public void testSimpleDNA() {
        FileMetadata simlpeDnaFile = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository);
        FileMetadata twoSectionsDnaFile = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository);

        CompressionRunParams runParams = new CompressionRunParams();
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);

        BuildSLPs();

        StatisticsObject[] actuals = statisticsRepository.readAll(simlpeDnaFile.getId());
        assertEquals(1, actuals.length);
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        assertEquals("171", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        List<Product> slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(simlpeDnaFile), getText(slp));

        actuals = statisticsRepository.readAll(twoSectionsDnaFile.getId());
        assertEquals(1, actuals.length);
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        assertEquals("185", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(twoSectionsDnaFile), getText(slp));
    }

    @Test
    public void testBig() {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("AAES.gz", ContentType.GZip, filesRepository);

        CompressionRunParams runParams = new CompressionRunParams();
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);

        BuildSLPs();

        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        List<Product> slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals("51359", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        assertEquals("15036", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        assertEquals(readFileText(fileMetadata), getText(slp));
    }

    private void BuildSLPs() {
        CompressionRunParams runParams = new CompressionRunParams();
        avlSlpRunner.checkAndRefillParams(runParams);
        avlSlpRunner.run(runParams);
    }

    private String getText(List<Product> products) {
        ArrayList<Product> arrayList = new ArrayList<Product>();
        for (Product product : products) {
            arrayList.add(product);
        }
        Product[] array = arrayList.toArray(new Product[0]);
        StringBuilder stringBuilder = new StringBuilder();
        buildText(array, array.length - 1, stringBuilder);
        return stringBuilder.toString();
    }

    private void buildText(Product[] array, int index, StringBuilder stringBuilder) {
        if (array[index].isTerminal)
            stringBuilder.append((char) array[index].symbol);
        else {
            buildText(array, (int) array[index].first, stringBuilder);
            buildText(array, (int) array[index].second, stringBuilder);
        }
    }
}
