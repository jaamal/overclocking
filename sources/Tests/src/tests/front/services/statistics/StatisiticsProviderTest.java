//package tests.front.services.statistics;
//
//import java.util.List;
//import models.CompressionAlgorithmType;
//import models.FileInfo;
//import models.statistics.LZ77Statistics;
//import models.statistics.SLPClassicStatistics;
//import models.statistics.SLPOptimalStatistics;
//import models.statistics.StatisticType;
//import org.junit.Assert;
//import org.junit.Test;
//import services.statistics.IStatisticProvider;
//import services.statistics.StatisticElement;
//import services.statistics.StatisticInfo;
//import tests.FrontIntegrationTestBase;
//import database.IDBContext;
//
//public class StatisiticsProviderTest extends FrontIntegrationTestBase
//{
//	private IDBContext dbContext;
//	private IStatisticProvider statisticdProvider;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//
//		clearDB();
//
//		dbContext = container.get(IDBContext.class);
//		statisticdProvider = container.get(IStatisticProvider.class);
//	}
//
//	@Test
//	public void testProvideLZ77StatisticInfo() throws Exception
//	{
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		LZ77Statistics statistics = new LZ77Statistics("1", 500, 1000, fileInfo);
//		dbContext.create(statistics);
//
//		statistics = new LZ77Statistics("2", 500, 1000, fileInfo);
//		dbContext.create(statistics);
//
//		StatisticInfo actual = statisticdProvider.provideStatiticInfo(new StatisticElement(LZ77Statistics.class, CompressionAlgorithmType.slpClassic), StatisticType.inputSize, StatisticType.factorizationSize);
//		Assert.assertNotNull(actual);
//
//		Assert.assertEquals(2, dbContext.select(LZ77Statistics.class).size());
//	}
//
//	@Test
//	public void testProvideLZ77StatisticInfoTwice() throws Exception
//	{
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		LZ77Statistics statistics = new LZ77Statistics("1", 500, 1000, fileInfo);
//		dbContext.create(statistics);
//
//		List<LZ77Statistics> list = dbContext.select(LZ77Statistics.class);
//		Assert.assertEquals(1, list.size());
//		LZ77Statistics actual = list.get(0);
//		Assert.assertEquals("1", actual.getId());
//		Assert.assertEquals(1000, actual.getCompressingTime());
//
//		LZ77Statistics statistics2 = new LZ77Statistics("1", 500, 1001, fileInfo);
//		dbContext.create(statistics2);
//		List<LZ77Statistics> list2 = dbContext.select(LZ77Statistics.class);
//		Assert.assertEquals(1, list2.size());
//		LZ77Statistics actual2 = list2.get(0);
//		Assert.assertEquals("1", actual2.getId());
//		Assert.assertEquals(1001, actual2.getCompressingTime());
//	}
//
//	@Test
//	public void testProvideSLPStatisticInfo() throws Exception
//	{
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		SLPClassicStatistics statistics = new SLPClassicStatistics("1", 1000, 1000, "memory", 14500, 10, fileInfo);
//		dbContext.create(statistics);
//		statistics = new SLPClassicStatistics("2", 2000, 2000, "memory", 24500, 20, fileInfo);
//		dbContext.create(statistics);
//
//		SLPOptimalStatistics optStatis = new SLPOptimalStatistics("3", 1000, 1000, "memory", 14500, 10, fileInfo);
//		dbContext.create(optStatis);
//		optStatis = new SLPOptimalStatistics("4", 2000, 2000, "memory", 24500, 20, fileInfo);
//		dbContext.create(optStatis);
//
//		StatisticInfo actual = statisticdProvider.provideStatiticInfo(new StatisticElement(SLPClassicStatistics.class, CompressionAlgorithmType.slpClassic), StatisticType.inputSize, StatisticType.slpHeight);
//		Assert.assertNotNull(actual);
//	}
//}
