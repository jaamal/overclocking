package tests.integration.Trees.cartesianTree;

import static org.junit.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import storage.KeySpaces;
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

public class CartesianSLPRunnerIntegrationTest extends AlgorithmRunnerTestBase {
    private IStatisticsRepository statisticsRepository;
    private ISlpProductsRepository slpProductsRepository;
    private IWorker worker;
    private IRunParamsFactory runParamsFactory;

    @Override
    public void setUp() {
        super.setUp();
        container.get(ISchemeInitializer.class).setUpCluster();

        worker = container.get(IWorker.class);
        statisticsRepository = container.get(IStatisticsRepository.class);
        slpProductsRepository = container.get(ISlpProductsRepository.class);
        runParamsFactory = container.get(IRunParamsFactory.class);
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
        worker.process(UUID.randomUUID(), runParamsFactory.create(fileMetadata.getId(), AlgorithmType.lzInf));
        worker.process(UUID.randomUUID(), runParamsFactory.create( AlgorithmType.cartesianSlp));
        
        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        List<Product> slp = slpProductsRepository.readAll(actuals[0].getId());
        assertEquals(readFileText(fileMetadata), getText(slp));
    }

    @Test
    public void testSimpleDNA() {
        FileMetadata simpleDnaFile = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository);
        worker.process(UUID.randomUUID(), runParamsFactory.create(simpleDnaFile.getId(), AlgorithmType.lzInf));
        FileMetadata twoSectionsDnaFile = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository);
        worker.process(UUID.randomUUID(), runParamsFactory.create(twoSectionsDnaFile.getId(), AlgorithmType.lzInf));
        worker.process(UUID.randomUUID(), runParamsFactory.create(AlgorithmType.cartesianSlp));
        
        StatisticsObject[] actuals = statisticsRepository.readAll(simpleDnaFile.getId());
        assertEquals(1, actuals.length);
        assertEquals("300", actuals[0].statistics.get(StatisticKeys.SourceLength));
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        List<Product> slp = slpProductsRepository.readAll(actuals[0].getId());
        assertEquals(readFileText(simpleDnaFile), getText(slp));

        actuals = statisticsRepository.readAll(twoSectionsDnaFile.getId());
        assertEquals(1, actuals.length);
        assertEquals("600", actuals[0].statistics.get(StatisticKeys.SourceLength));
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        slp = slpProductsRepository.readAll(actuals[0].getId());
        assertEquals(readFileText(twoSectionsDnaFile), getText(slp));
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
