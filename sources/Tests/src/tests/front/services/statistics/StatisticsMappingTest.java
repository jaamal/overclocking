//package tests.front.services.statistics;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import java.util.List;
//import models.CompressionAlgorithmType;
//import models.statistics.LZ77Statistics;
//import models.statistics.LZMAStatistics;
//import models.statistics.LZWStatistics;
//import models.statistics.SLPCartesianStatistics;
//import models.statistics.SLPClassicStatistics;
//import models.statistics.SLPOptimalStatistics;
//import models.statistics.StatisticType;
//import org.junit.Test;
//import services.statistics.StatisticElement;
//import services.statistics.mapping.IStatisticsMapping;
//import tests.FrontIntegrationTestBase;
//
//public class StatisticsMappingTest extends FrontIntegrationTestBase
//{
//
//	@Test
//	public void getAlgorithmTypeTest() throws Exception
//	{
//		IStatisticsMapping mapping = container.get(IStatisticsMapping.class);
//		assertEquals(LZ77Statistics.class, mapping.getAlgorithmClass(CompressionAlgorithmType.lz77));
//		assertEquals(LZWStatistics.class, mapping.getAlgorithmClass(CompressionAlgorithmType.lzw));
//		assertEquals(LZMAStatistics.class, mapping.getAlgorithmClass(CompressionAlgorithmType.lzma));
//		assertEquals(SLPCartesianStatistics.class, mapping.getAlgorithmClass(CompressionAlgorithmType.slpCartesian));
//		assertEquals(SLPClassicStatistics.class, mapping.getAlgorithmClass(CompressionAlgorithmType.slpClassic));
//		assertEquals(SLPOptimalStatistics.class, mapping.getAlgorithmClass(CompressionAlgorithmType.slpModern));
//	}
//	
//	@Test
//	public void getClassesTest(){
//		IStatisticsMapping mapping = container.get(IStatisticsMapping.class);
//		List<StatisticElement> actuals = mapping.getClasses(StatisticType.avlRotationsNumber);
//		assertEquals(2, actuals.size());
//		assertTrue(actuals.contains(new StatisticElement(SLPClassicStatistics.class, null)));
//		assertTrue(actuals.contains(new StatisticElement(SLPOptimalStatistics.class, null)));
//		
//		actuals = mapping.getClasses(StatisticType.inputSize);
//		assertEquals(6, actuals.size());
//		assertTrue(actuals.contains(new StatisticElement(SLPClassicStatistics.class, null)));
//		assertTrue(actuals.contains(new StatisticElement(SLPOptimalStatistics.class, null)));
//		assertTrue(actuals.contains(new StatisticElement(LZ77Statistics.class, null)));
//		assertTrue(actuals.contains(new StatisticElement(LZWStatistics.class, null)));
//		assertTrue(actuals.contains(new StatisticElement(LZMAStatistics.class, null)));
//		assertTrue(actuals.contains(new StatisticElement(SLPCartesianStatistics.class, null)));
//	}
//}
