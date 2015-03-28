//package tests.front.services.statistics;
//
//import static org.easymock.EasyMock.expect;
//import static org.easymock.EasyMock.replay;
//import static org.junit.Assert.*;
//import helpers.ReflectionHelper;
//import java.util.ArrayList;
//import java.util.List;
//import models.CompressionAlgorithmType;
//import models.statistics.LZ77Statistics;
//import models.statistics.LZMAStatistics;
//import models.statistics.LZWStatistics;
//import models.statistics.StatisticType;
//import org.junit.Test;
//import services.statistics.PlotParameters;
//import services.statistics.StatisticElement;
//import services.statistics.mapping.IStatisticsMapping;
//import services.statistics.mapping.StatisticsMapping;
//import tests.UnitTestBase;
//
//public class PlotParametersTest extends UnitTestBase
//{
//
//	private IStatisticsMapping mockStatsMapping;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		mockStatsMapping = newMock(IStatisticsMapping.class);
//	}
//	
//	@Test
//	public void testGetStatClassesWithEmptyCompressionAlgorithmTypes() throws Exception
//	{
//		List<StatisticElement> statsClasses1 = new ArrayList<StatisticElement>();
//		statsClasses1.add(new StatisticElement(LZ77Statistics.class, CompressionAlgorithmType.lz77));
//		List<StatisticElement> statsClasses2 = new ArrayList<StatisticElement>();
//		statsClasses2.add(new StatisticElement(LZ77Statistics.class, CompressionAlgorithmType.lz77));
//		statsClasses2.add(new StatisticElement(LZWStatistics.class, CompressionAlgorithmType.lzw));
//		
//        expect(mockStatsMapping.getClasses(StatisticType.inputSize)).andReturn(statsClasses1);
//        expect(mockStatsMapping.getClasses(StatisticType.slpHeight)).andReturn(statsClasses2);
//        replay(mockStatsMapping);
//		
//		PlotParameters plotParameters = new PlotParameters(mockStatsMapping, StatisticType.inputSize, StatisticType.slpHeight, new CompressionAlgorithmType[0]);
//		List<StatisticElement> actuals = plotParameters.getStatElements();
//		assertEquals(statsClasses1, actuals);
//	}
//	
//	@Test
//	public void testGetStatClasses() throws Exception
//	{
//		List<StatisticElement> mockStatClasses1 = newMock(List.class);
//		List<StatisticElement> mockStatClasses2 = newMock(List.class);
//		expect(mockStatsMapping.getClasses(StatisticType.inputSize)).andReturn(mockStatClasses1);
//        expect(mockStatsMapping.getClasses(StatisticType.slpHeight)).andReturn(mockStatClasses2);
//        expect(mockStatsMapping.getAlgorithmClass(CompressionAlgorithmType.lz77)).andReturn(LZ77Statistics.class);
//        expect(mockStatsMapping.getAlgorithmClass(CompressionAlgorithmType.lzma)).andReturn(LZMAStatistics.class);
//        expect(mockStatClasses1.contains(new StatisticElement(LZ77Statistics.class, null))).andReturn(true);
//        expect(mockStatClasses2.contains(new StatisticElement(LZ77Statistics.class, null))).andReturn(true);
//        expect(mockStatClasses1.contains(new StatisticElement(LZMAStatistics.class, null))).andReturn(false);
//        replayAll();
//		
//        CompressionAlgorithmType[] algorithmTypes = new CompressionAlgorithmType[] { CompressionAlgorithmType.lz77, CompressionAlgorithmType.lzma };
//		PlotParameters plotParameters = new PlotParameters(mockStatsMapping, StatisticType.inputSize, StatisticType.slpHeight, algorithmTypes);
//		List<StatisticElement> actuals = plotParameters.getStatElements();
//		assertEquals(1, actuals.size());
//		assertEquals(LZ77Statistics.class, actuals.get(0).statisticClass);
//		assertEquals(CompressionAlgorithmType.lz77, actuals.get(0).algorithmType);
//	}
//	
//	@Test
//	public void testDoubleGetStatClasses() throws Exception
//	{
//		List<StatisticElement> mockStatClasses1 = newMock(List.class);
//		List<StatisticElement> mockStatClasses2 = newMock(List.class);
//		expect(mockStatsMapping.getClasses(StatisticType.inputSize)).andReturn(mockStatClasses1);
//        expect(mockStatsMapping.getClasses(StatisticType.slpHeight)).andReturn(mockStatClasses2);
//        expect(mockStatsMapping.getAlgorithmClass(CompressionAlgorithmType.lz77)).andReturn(LZ77Statistics.class);
//        expect(mockStatsMapping.getAlgorithmClass(CompressionAlgorithmType.lzma)).andReturn(LZMAStatistics.class);
//        expect(mockStatClasses1.contains(new StatisticElement(LZ77Statistics.class, null))).andReturn(true);
//        expect(mockStatClasses2.contains(new StatisticElement(LZ77Statistics.class, null))).andReturn(true);
//        expect(mockStatClasses1.contains(new StatisticElement(LZMAStatistics.class, null))).andReturn(false);
//        replayAll();
//		
//        CompressionAlgorithmType[] algorithmTypes = new CompressionAlgorithmType[] { CompressionAlgorithmType.lz77, CompressionAlgorithmType.lzma };
//		PlotParameters plotParameters = new PlotParameters(mockStatsMapping, StatisticType.inputSize, StatisticType.slpHeight, algorithmTypes);
//		List<StatisticElement> actuals = plotParameters.getStatElements();
//		assertEquals(1, actuals.size());
//		assertEquals(LZ77Statistics.class, actuals.get(0).statisticClass);
//		
//		actuals = plotParameters.getStatElements();
//		assertEquals(1, actuals.size());
//		assertEquals(LZ77Statistics.class, actuals.get(0).statisticClass);
//	}
//	
//	@Test
//	public void testEqualsSimple() throws Exception
//	{
//		replayAll();
//		
//		assertFalse(new PlotParameters(null, null, null, null).equals(null));
//		assertFalse(new PlotParameters(null, null, null, null).equals(new Object()));
//	}
//	
//	@Test
//	public void testEquals() throws Exception
//	{
//		replayAll();
//		
//		IStatisticsMapping statsMapping = new StatisticsMapping(new ReflectionHelper());
//		PlotParameters plotParams1 = new PlotParameters(statsMapping, StatisticType.inputSize, StatisticType.slpHeight, 
//				new CompressionAlgorithmType[] { CompressionAlgorithmType.slpCartesian, CompressionAlgorithmType.slpModern });
//		PlotParameters plotParams2 = new PlotParameters(statsMapping, StatisticType.inputSize, StatisticType.slpHeight, 
//				new CompressionAlgorithmType[] { CompressionAlgorithmType.slpModern, CompressionAlgorithmType.slpCartesian });
//		PlotParameters plotParams3 = new PlotParameters(statsMapping, StatisticType.inputSize, StatisticType.slpHeight, 
//				new CompressionAlgorithmType[] { CompressionAlgorithmType.slpModern, CompressionAlgorithmType.slpClassic });
//		
//		assertTrue(plotParams1.equals(plotParams2));
//		assertFalse(plotParams1.equals(plotParams3));
//		assertFalse(plotParams2.equals(plotParams3));
//	}
//}
