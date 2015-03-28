//package tests.front.services.statistics;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.Test;
//import services.statistics.scaling.ScalingResult;
//import services.statistics.scaling.TimeScaler;
//import tests.UnitTestBase;
//
//public class TimeScalerTest extends UnitTestBase
//{
//
//	private TimeScaler timeScaler;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		timeScaler = new TimeScaler();
//	}
//	
//	@Test
//	public void testScaleNullAndEmpty() throws Exception
//	{
//		assertEquals(ScalingResult.Empty(), timeScaler.scale(null));
//		assertEquals(ScalingResult.Empty(), timeScaler.scale(new ArrayList<Object[]>()));
//	}
//	
//	@Test
//	public void testScaleInMin() throws Exception
//	{
//		List<Object[]> values = new ArrayList<Object[]>(); 
//		values.add(new Object[] { 10L, 60000L, (long) 11 * 60000 });
//		ScalingResult actuals = timeScaler.scale(values);
//		assertEquals(1, actuals.values.size());
//		assertArrayEquals(new int[] {0, 1, 11}, actuals.values.get(0));
//		assertEquals("min", actuals.unit);
//	}
//	
//	@Test
//	public void testScaleInSeconds() throws Exception
//	{
//		List<Object[]> values = new ArrayList<Object[]>(); 
//		values.add(new Object[] { 900L, 10000L, 20000L });
//		ScalingResult actuals = timeScaler.scale(values);
//		assertEquals(1, actuals.values.size());
//		assertArrayEquals(new int[] {1, 10, 20}, actuals.values.get(0));
//		assertEquals("s", actuals.unit);
//	}
//	
//}
