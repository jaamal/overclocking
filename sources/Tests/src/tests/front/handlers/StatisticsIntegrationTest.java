//package tests.front.handlers;
//
//import handlers.StatisticsHandler;
//import java.util.Collection;
//import models.FileInfo;
//import models.statistics.LZ77Statistics;
//import models.statistics.LZMAStatistics;
//import models.statistics.LZWStatistics;
//import models.statistics.SLPCartesianStatistics;
//import models.statistics.SLPClassicStatistics;
//import models.statistics.SLPOptimalStatistics;
//import org.junit.Assert;
//import org.junit.Test;
//import tests.FrontIntegrationTestBase;
//import database.IDBContext;
//
//public class StatisticsIntegrationTest extends FrontIntegrationTestBase{
//
//	private StatisticsHandler statisticsHandler;
//	private IDBContext dbContext;	
//	
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//				
//		clearDB();
//		
//		dbContext = container.get(IDBContext.class);
//		statisticsHandler = new StatisticsHandler(dbContext);
//	}
//
//	@Test
//	public void testGetLZ77Statistics() {
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		LZ77Statistics statistics = new LZ77Statistics("1", 500, 1000, fileInfo);
//		dbContext.create(statistics);
//		
//		statistics = new LZ77Statistics("2", 500, 1000, fileInfo); 
//		dbContext.create(statistics);
//		
//		Collection<LZ77Statistics> actuals = (Collection<LZ77Statistics>) statisticsHandler.getStatistics(LZ77Statistics.class);
//
//		Assert.assertEquals(2, actuals.size());
//		statistics = actuals.iterator().next();
//		Assert.assertEquals(500, statistics.getNumberOfFactors());
//		Assert.assertEquals(1000, statistics.getCompressingTime());
//		Assert.assertEquals("1", statistics.getId());
//		Assert.assertEquals(100, statistics.getFileInfo().getFileSize());
//		Assert.assertEquals("filezzz", statistics.getFileInfo().getId());
//	}
//
//	
//	@Test
//	public void testWriteLZWStatistics() {
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		LZWStatistics statistics = new LZWStatistics("1", 500, 1000, fileInfo); 
//		dbContext.create(statistics);
//		
//		Collection<LZWStatistics> actuals = (Collection<LZWStatistics>) statisticsHandler.getStatistics(LZWStatistics.class);
//		
//		Assert.assertEquals(1, actuals.size());
//		statistics = actuals.iterator().next();
//		Assert.assertEquals(500, statistics.getNumberOfFactors());
//		Assert.assertEquals(1000, statistics.getCompressingTime());
//		Assert.assertEquals("1", statistics.getId());
//		Assert.assertEquals(100, statistics.getFileInfo().getFileSize());
//		Assert.assertEquals("filezzz", statistics.getFileInfo().getId());
//	}
//
//	@Test
//	public void testWriteLZMAStatistics() {
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		LZMAStatistics statistics = new LZMAStatistics("1", 500, 1000, fileInfo); 
//		dbContext.create(statistics);
//		
//		Collection<LZMAStatistics> actuals = (Collection<LZMAStatistics>) statisticsHandler.getStatistics(LZMAStatistics.class);
//		
//		Assert.assertEquals(1, actuals.size());
//		statistics = actuals.iterator().next();
//		Assert.assertEquals(500, statistics.getNumberOfFactors());
//		Assert.assertEquals(1000, statistics.getCompressingTime());	
//		Assert.assertEquals("1", statistics.getId());
//		Assert.assertEquals(100, statistics.getFileInfo().getFileSize());
//		Assert.assertEquals("filezzz", statistics.getFileInfo().getId());
//	}
//
//	@Test
//	public void testWriteSLPClassicStatistics() {
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		SLPClassicStatistics statistics = new SLPClassicStatistics("1", 500, 1000, "memory", 100, 128, fileInfo); 	
//		dbContext.create(statistics);
//		
//		Collection<SLPClassicStatistics> actuals = (Collection<SLPClassicStatistics>) statisticsHandler.getStatistics(SLPClassicStatistics.class);
//		
//		Assert.assertEquals(1, actuals.size());
//		statistics = actuals.iterator().next();
//		Assert.assertEquals(500, statistics.getCopmressedFileSize());
//		Assert.assertEquals(1000, statistics.getSlpBuildingTime());		
//		Assert.assertEquals("1", statistics.getId());		
//		Assert.assertEquals("memory", statistics.getDataFactoryType());		
//		Assert.assertEquals(100, statistics.getRebalanceCount());		
//		Assert.assertEquals(128, statistics.getHeight());
//		Assert.assertEquals(100, statistics.getFileInfo().getFileSize());
//		Assert.assertEquals("filezzz", statistics.getFileInfo().getId());
//	}
//	
//	
//	@Test
//	public void testWriteSLPOptimalStatistics() {
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		SLPOptimalStatistics statistics = new SLPOptimalStatistics("1", 500, 1200, "memory", 100, 128, fileInfo); 	
//		dbContext.create(statistics);
//
//		Collection<SLPOptimalStatistics> actuals = (Collection<SLPOptimalStatistics>) statisticsHandler.getStatistics(SLPOptimalStatistics.class);
//		Assert.assertEquals(1, actuals.size());
//		statistics = actuals.iterator().next();
//		Assert.assertEquals(500, statistics.getCopmressedFileSize());
//		Assert.assertEquals(1200, statistics.getSlpBuildingTime());		
//		Assert.assertEquals("1", statistics.getId());		
//		Assert.assertEquals("memory", statistics.getDataFactoryType());		
//		Assert.assertEquals(100, statistics.getRebalanceCount());		
//		Assert.assertEquals(128, statistics.getHeight());
//		Assert.assertEquals(100, statistics.getFileInfo().getFileSize());
//		Assert.assertEquals("filezzz", statistics.getFileInfo().getId());
//	}
//
//	@Test
//	public void testWriteSLPCartesianStatistics(){
//		FileInfo fileInfo = new FileInfo("filezzz", "AAAA.txt", 100, "testType");
//		SLPCartesianStatistics statistics = new SLPCartesianStatistics("1", 2500, 1000, 200, 300, fileInfo);
//		dbContext.create(statistics);
//		
//		Collection<SLPCartesianStatistics> actuals = (Collection<SLPCartesianStatistics>) statisticsHandler.getStatistics(SLPCartesianStatistics.class);
//		
//		Assert.assertEquals(1, actuals.size());
//		statistics = actuals.iterator().next();
//		Assert.assertEquals("1", statistics.getId());
//		Assert.assertEquals(2500, statistics.getSlpBuildingTime());
//		Assert.assertEquals(1000, statistics.getLength());
//		Assert.assertEquals(200, statistics.getCountRules());
//		Assert.assertEquals(300, statistics.getHeight());
//		Assert.assertEquals(100, statistics.getFileInfo().getFileSize());
//		Assert.assertEquals("filezzz", statistics.getFileInfo().getId());
//	}
//	
//}
