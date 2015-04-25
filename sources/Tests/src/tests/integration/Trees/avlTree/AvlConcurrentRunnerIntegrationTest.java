package tests.integration.Trees.avlTree;

import static org.junit.Assert.assertEquals;
import helpers.FileHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;

import storage.slpProductsRepository.ISlpProductsRepository;
import storage.slpProductsRepository.SlpProductsRepository;
import storage.statistics.IStatisticsRepository;
import tests.integration.AlgorithmRunnerTestBase;
import compressionservice.runner.IWorker;
import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.parameters.RunParams;
import dataContracts.AlgorithmType;
import dataContracts.ContentType;
import dataContracts.Product;
import dataContracts.files.FileMetadata;
import dataContracts.statistics.CompressionRunKeys;
import dataContracts.statistics.CompressionStatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class AvlConcurrentRunnerIntegrationTest extends AlgorithmRunnerTestBase {
    private ISlpProductsRepository slpProductsRepository;
    private IStatisticsRepository statisticsRepository;
    private IWorker worker;

    @Override
    public void setUp() {
        super.setUp();

        worker = container.get(IWorker.class);
        slpProductsRepository = container.create(SlpProductsRepository.class);
        statisticsRepository = container.get(IStatisticsRepository.class);
    }

    @Test
    public void testSimpleDNA() {
        FileMetadata simpleDnaFile = FileHelpers.writeDnaToRepository("simpleDNA.txt", ContentType.PlainText, filesRepository);
        FileMetadata twoSectionsDnaFile = FileHelpers.writeDnaToRepository("simpleDNA_twoSections.txt", ContentType.PlainText, filesRepository);

        RunParams runParams = new RunParams();
        runParams.put(CompressionRunKeys.AlgorithmType, AlgorithmType.lzInf);
        worker.process(UUID.randomUUID(), runParams);

        BuildSLPs();

        StatisticsObject[] actuals = statisticsRepository.readAll(simpleDnaFile.getId());
        assertEquals(1, actuals.length);
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        assertEquals("168", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        System.out.println(actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        List<Product> slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(simpleDnaFile), getText(slp));


        actuals = statisticsRepository.readAll(twoSectionsDnaFile.getId());
        assertEquals(1, actuals.length);
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        assertEquals("184", actuals[0].statistics.get(CompressionStatisticKeys.SlpCountRules));
        slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(twoSectionsDnaFile), getText(slp));
    }

    @Test
    @Ignore
    public void testBig() {
        FileMetadata fileMetadata = FileHelpers.writeDnaToRepository("AAOO.gz", ContentType.GZip, filesRepository);

        RunParams runParams = new RunParams();
        runParams.put(CompressionRunKeys.AlgorithmType, AlgorithmType.lzInf);
        worker.process(UUID.randomUUID(), runParams);

        BuildSLPs();

        StatisticsObject[] actuals = statisticsRepository.readAll(fileMetadata.getId());
        assertEquals(1, actuals.length);
        actuals = statisticsRepository.readAll(actuals[0].getId());
        assertEquals(1, actuals.length);
        List<Product> slp = slpProductsRepository.readItems(actuals[0].getId());
        assertEquals(readFileText(fileMetadata), getText(slp));
    }

    private void BuildSLPs() {
        IRunParams runParams = new RunParams();
        runParams.put(CompressionRunKeys.AlgorithmType, AlgorithmType.avlSlpConcurrent);
        worker.process(UUID.randomUUID(), runParams);
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
