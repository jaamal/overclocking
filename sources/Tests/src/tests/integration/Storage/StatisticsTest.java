package tests.integration.Storage;

import helpers.TestHelpers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import serialization.ISerializer;
import storage.KeySpaces;
import storage.cassandraClient.ISchemeInitializer;
import storage.statistics.IStatisticsRepository;
import tests.integration.StorageTestBase;

import compressionservice.runner.parameters.IRunParams;
import compressionservice.runner.parameters.IRunParamsFactory;

import dataContracts.AlgorithmType;
import dataContracts.statistics.IStatisticsObjectFactory;
import dataContracts.statistics.StatisticKeys;
import dataContracts.statistics.StatisticsObject;

public class StatisticsTest extends StorageTestBase {

    private ISerializer serializer;
    private ISchemeInitializer schemaInitializer;
    private IStatisticsRepository statisticsRepository;
    private IStatisticsObjectFactory statisticsObjectFactory;
    private IRunParamsFactory runParamsFactory;

    @Override
    public void setUp() {
        super.setUp();
        serializer = container.get(ISerializer.class);
        schemaInitializer = container.get(ISchemeInitializer.class);
        statisticsRepository = container.get(IStatisticsRepository.class);
        statisticsObjectFactory = container.get(IStatisticsObjectFactory.class);
        runParamsFactory = container.get(IRunParamsFactory.class);
        schemaInitializer.setUpCluster();
    }

    @Override
    public void tearDown() {
        schemaInitializer.truncateKeyspace(KeySpaces.statistics.name());

        super.tearDown();
    }

    @Test
    public void testRemoveStatistics() {
        String textId = "zzz";
        StatisticsObject stats = genStatistic();
        StatisticsObject stats2 = genStatistic();

        statisticsRepository.write(textId, stats);
        statisticsRepository.write(textId, stats2);
        Assert.assertTrue(statisticsRepository.exists(textId, stats.getId()));
        Assert.assertTrue(statisticsRepository.exists(textId, stats2.getId()));
        statisticsRepository.remove(textId, stats.getId());
        Assert.assertFalse(statisticsRepository.exists(textId, stats.getId()));
        Assert.assertTrue(statisticsRepository.exists(textId, stats2.getId()));
    }

    @Test
    public void testWriteStatistics() {
        String textId = "zzz";
        StatisticsObject stats = genStatistic();

        statisticsRepository.write(textId, stats);
        Assert.assertTrue(statisticsRepository.contains(textId, stats.runningParameters));

        String expectedStats = serializer.stringify(stats);
        String actualStats = serializer.stringify(statisticsRepository.readAll(textId)[0]);
        Assert.assertEquals(expectedStats, actualStats);
    }

    @Test
    public void testExistsStatistics() {
        String textId1 = "zzz";
        StatisticsObject stats1 = genStatistic();
        String textId2 = "qxx";
        StatisticsObject stats2 = genStatistic();

        statisticsRepository.write(textId1, stats1);
        statisticsRepository.write(textId2, stats2);
        Assert.assertTrue(statisticsRepository.exists(textId1, stats1.getId()));
        Assert.assertFalse(statisticsRepository.exists(textId1, stats2.getId()));
        Assert.assertFalse(statisticsRepository.exists(textId1, "qqq"));

        Assert.assertFalse(statisticsRepository.exists(textId2, stats1.getId()));
        Assert.assertTrue(statisticsRepository.exists(textId2, stats2.getId()));
        Assert.assertFalse(statisticsRepository.exists(textId2, "qqq"));

        Assert.assertFalse(statisticsRepository.exists("qzz", stats1.getId()));
        Assert.assertFalse(statisticsRepository.exists("qzz", stats1.getId()));
        Assert.assertFalse(statisticsRepository.exists("qzz", "qqq"));
    }

    @Test
    public void testWriteSeveralStatisticsAndContains() {
        String textId1 = TestHelpers.genString(14);
        String textId2 = TestHelpers.genString(14);
        StatisticsObject stats1 = genStatistic();
        StatisticsObject stats2 = genStatistic();
        StatisticsObject stats3 = genStatistic();

        statisticsRepository.write(textId1, stats1);
        statisticsRepository.write(textId1, stats2);
        statisticsRepository.write(textId2, stats3);
        Assert.assertTrue(statisticsRepository.contains(textId1, stats1.runningParameters));
        Assert.assertTrue(statisticsRepository.contains(textId1, stats2.runningParameters));
        Assert.assertTrue(statisticsRepository.contains(textId2, stats3.runningParameters));

        StatisticsObject[] allStats1 = statisticsRepository.readAll(textId1);
        assertArrayEquivalent(new StatisticsObject[]{stats1, stats2}, allStats1);

        StatisticsObject[] allStats2 = statisticsRepository.readAll(textId2);
        assertArrayEquivalent(new StatisticsObject[]{stats3}, allStats2);

        HashMap<String, StatisticsObject[]> allStats = statisticsRepository.readAll(new String[]{textId1, textId2, "zzz"});
        Assert.assertEquals(3, allStats.size());
        Assert.assertTrue(allStats.containsKey(textId1));
        Assert.assertTrue(allStats.containsKey(textId2));
        Assert.assertTrue(allStats.containsKey("zzz"));

        assertArrayEquivalent(new StatisticsObject[]{stats1, stats2}, allStats.get(textId1));
        assertArrayEquivalent(new StatisticsObject[]{stats3}, allStats.get(textId2));
        assertArrayEquivalent(new StatisticsObject[0], allStats.get("zzz"));
    }

    private void assertArrayEquivalent(StatisticsObject[] expected, StatisticsObject[] actual) {
        Assert.assertEquals(expected.length, actual.length);
        Comparator<StatisticsObject> comparator = new Comparator<StatisticsObject>() {
            @Override
            public int compare(StatisticsObject o1, StatisticsObject o2) {
                return o1.getId().compareTo(o2.getId());
            }
        };
        Arrays.sort(expected, comparator);
        Arrays.sort(actual, comparator);
        for (int i = 0; i < expected.length; ++i) {
            String expectedString = serializer.stringify(expected[i]);
            String actualString = serializer.stringify(actual[i]);
            Assert.assertEquals(expectedString, actualString);
        }
    }


    private StatisticsObject genStatistic() {
        IRunParams runParams = runParamsFactory.create(TestHelpers.genString(15), AlgorithmType.lcaOnlineSlp);
        Map<StatisticKeys, String> statistics = new HashMap<StatisticKeys, String>();
        statistics.put(StatisticKeys.RunningTime, String.valueOf(TestHelpers.genInt()));
        statistics.put(StatisticKeys.SourceLength, String.valueOf(TestHelpers.genInt()));
        return statisticsObjectFactory.create(runParams.getHashId(), runParams.toMap(), statistics);
    }

}
