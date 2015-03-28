//package tests.front.configuration;
//
//import static tests.front.configuration.Generator.genStr;
//import static tests.front.configuration.Generator.genUInt;
//import java.util.ArrayList;
//import models.FileInfo;
//import models.statistics.LZ77Statistics;
//import models.statistics.LZMAStatistics;
//import models.statistics.LZWStatistics;
//import models.statistics.SLPCartesianStatistics;
//import models.statistics.SLPClassicStatistics;
//import models.statistics.SLPOptimalStatistics;
//import org.junit.Test;
//import tests.FrontIntegrationTestBase;
//import database.IDBContext;
//
//public class ConfigureDB extends FrontIntegrationTestBase
//{
//	private IDBContext dbContext;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		
//		truncateTable(LZMAStatistics.class);
//		truncateTable(LZ77Statistics.class);
//		truncateTable(LZWStatistics.class);
//		truncateTable(SLPCartesianStatistics.class);
//		truncateTable(SLPClassicStatistics.class);
//		truncateTable(SLPOptimalStatistics.class);
//		truncateTable(FileInfo.class);
//		
//		dbContext = container.get(IDBContext.class);
//	}
//
//	
//	@Test
//	public void runConfiguration() throws Exception
//	{
//		createRandomSLPOptimalStats(3);
//		createRandomSLPClassicStats(3);
//		createRandomLZ77Stats(3);
//		createRandomLZMAStats(3);
//		createRandomLZWStats(3);
//	}
//	
//	private FileInfo createRandomFileInfo() {
//		return new FileInfo(genStr(10), genStr(10), genUInt(), genStr(10));
//	}
//	
//	private SLPOptimalStatistics[] createRandomSLPOptimalStats(int itemsNumber)
//	{
//		ArrayList<SLPOptimalStatistics> list = new ArrayList<SLPOptimalStatistics>();
//		for (int i = 0; i < itemsNumber; i++) {
//			SLPOptimalStatistics stats = new SLPOptimalStatistics(genStr(10), genUInt(), genUInt(), genStr(3), genUInt(), genUInt(), createRandomFileInfo());
//			list.add(stats);
//			dbContext.create(stats);
//		}
//		SLPOptimalStatistics[] result = new SLPOptimalStatistics[itemsNumber];
//		return list.toArray(result);
//	}
//	
//	private SLPClassicStatistics[] createRandomSLPClassicStats(int itemsNumber) {
//		ArrayList<SLPClassicStatistics> list = new ArrayList<SLPClassicStatistics>();
//		for (int i = 0; i < itemsNumber; i++) {
//			SLPClassicStatistics stats = new SLPClassicStatistics(genStr(10), genUInt(), genUInt(), genStr(3), genUInt(), genUInt(), createRandomFileInfo());
//			list.add(stats);
//			dbContext.create(stats);
//		}
//		SLPClassicStatistics[] result = new SLPClassicStatistics[itemsNumber];
//		return list.toArray(result);
//	}
//	
//	private LZ77Statistics[] createRandomLZ77Stats(int itemsNumber) {
//		ArrayList<LZ77Statistics> list = new ArrayList<LZ77Statistics>();
//		for (int i = 0; i < itemsNumber; i++) {
//			LZ77Statistics stats = new LZ77Statistics(genStr(10), genUInt(), genUInt(), createRandomFileInfo());
//			list.add(stats);
//			dbContext.create(stats);
//		}
//		LZ77Statistics[] result = new LZ77Statistics[itemsNumber];
//		return list.toArray(result);
//	}
//	
//	private LZMAStatistics[] createRandomLZMAStats(int itemsNumber) {
//		ArrayList<LZMAStatistics> list = new ArrayList<LZMAStatistics>();
//		for (int i = 0; i < itemsNumber; i++) {
//			LZMAStatistics stats = new LZMAStatistics(genStr(10), genUInt(), genUInt(), createRandomFileInfo());
//			list.add(stats);
//			dbContext.create(stats);
//		}
//		LZMAStatistics[] result = new LZMAStatistics[itemsNumber];
//		return list.toArray(result);
//	}
//	
//	private LZWStatistics[] createRandomLZWStats(int itemsNumber) {
//		ArrayList<LZWStatistics> list = new ArrayList<LZWStatistics>();
//		for (int i = 0; i < itemsNumber; i++) {
//			LZWStatistics stats = new LZWStatistics(genStr(10), genUInt(), genUInt(), createRandomFileInfo());
//			list.add(stats);
//			dbContext.create(stats);
//		}
//		LZWStatistics[] result = new LZWStatistics[itemsNumber];
//		return list.toArray(result);
//	}
//}
