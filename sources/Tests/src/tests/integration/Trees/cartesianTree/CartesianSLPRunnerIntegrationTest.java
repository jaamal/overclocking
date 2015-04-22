package tests.integration.Trees.cartesianTree;

import static org.junit.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.slpProductsRepository.ISlpProductsRepository;
import storage.statistics.IStatisticsRepository;
import tests.integration.AlgorithmRunnerTestBase;

import compressionservice.compression.parameters.RunParams;
import compressionservice.compression.running.CartesianSlpRunner;
import compressionservice.compression.running.LzInfRunner;

import dataContracts.ContentType;
import dataContracts.Product;
import dataContracts.files.FileMetadata;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class CartesianSLPRunnerIntegrationTest extends AlgorithmRunnerTestBase {
    private IStatisticsRepository statisticsRepository;
    private ISlpProductsRepository slpProductsRepository;
    private CartesianSlpRunner cartesianSlpRunner;
    private LzInfRunner lzInfRunner;

    @Override
    public void setUp() {
        super.setUp();
        container.get(ISchemeInitializer.class).setUpCluster();

        lzInfRunner = container.get(LzInfRunner.class);
        statisticsRepository = container.get(IStatisticsRepository.class);
        slpProductsRepository = container.get(ISlpProductsRepository.class);
        cartesianSlpRunner = container.get(CartesianSlpRunner.class);
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
    @Ignore
    public void testBigDNA() {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("AATT.gz", ContentType.GZip, filesRepository);

        RunParams runParams = new RunParams();
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);

        BuildSLPs();

        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        List<Product> slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(fileMetadata), getText(slp));
    }

    @Test
    public void testSimpleDNA() {
        FileMetadata simpleDnaFile = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository);
        FileMetadata twoSectionsDnaFile = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository);

        RunParams runParams = new RunParams();
        lzInfRunner.checkAndRefillParams(runParams);
        lzInfRunner.run(runParams);

        BuildSLPs();

        StatisticsObject[] actuals = statisticsRepository.readAll(simpleDnaFile.getId());
        assertEquals(1, actuals.length);
        assertEquals("300", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        List<Product> slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(simpleDnaFile), getText(slp));

        actuals = statisticsRepository.readAll(twoSectionsDnaFile.getId());
        assertEquals(1, actuals.length);
        assertEquals("600", actuals[0].statistics.get(CompressionStatisticKeys.SourceLength));
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(twoSectionsDnaFile), getText(slp));
    }

    private void BuildSLPs() {
        RunParams runParams = new RunParams();
        cartesianSlpRunner.checkAndRefillParams(runParams);
        cartesianSlpRunner.run(runParams);
    }

    private String getText(List<Product> products) {
        ArrayList<Product> arrayList = new ArrayList<Product>();
        for (Product productDef : products) {
            arrayList.add(productDef);
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
