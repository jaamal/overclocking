//package tests.front.services.statistics;
//
//import junit.framework.Assert;
//import models.FileInfo;
//import models.PlotModel;
//import models.statistics.SLPClassicStatistics;
//import models.statistics.SLPOptimalStatistics;
//import models.statistics.StatisticType;
//import org.junit.Test;
//import services.statistics.IPlotParameters;
//import services.statistics.IPlotParametersFactory;
//import services.statistics.IPlotService;
//import tests.FrontIntegrationTestBase;
//import database.IDBContext;
//
//public class PlotServiceTest extends FrontIntegrationTestBase
//{
//	private IDBContext dbContext;
//	private IPlotService plotSerivce;
//	private IPlotParametersFactory plotParametersFactory;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		
//		clearDB();
//		
//		dbContext = container.get(IDBContext.class);
//        plotSerivce = container.get(IPlotService.class);
//        plotParametersFactory = container.get(IPlotParametersFactory.class);
//    }
//	
//	@Test
//	public void testGet() throws Exception
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
//        IPlotParameters plotParams = plotParametersFactory.create(StatisticType.inputSize, StatisticType.slpHeight);
//        PlotModel actual = plotSerivce.get(plotParams);
//        Assert.assertNotNull(actual);
//        
//		plotParams = plotParametersFactory.create(StatisticType.avlRotationsNumber, StatisticType.factorizationSize);
//		actual = plotSerivce.get(plotParams);
//		Assert.assertNotNull(actual);
//	}
//}
