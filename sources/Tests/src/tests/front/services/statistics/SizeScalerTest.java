//package tests.front.services.statistics;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import java.util.ArrayList;
//import java.util.List;
//import org.junit.Test;
//import services.statistics.scaling.ScalingResult;
//import services.statistics.scaling.SizeScaler;
//import tests.UnitTestBase;
//
//public class SizeScalerTest extends UnitTestBase
//{
//	private SizeScaler sizeScaler;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		sizeScaler = new SizeScaler();
//	}
//
//	@Test
//	public void testScaleNullAndEmpty() throws Exception
//	{
//		assertEquals(ScalingResult.Empty(), sizeScaler.scale(null));
//		assertEquals(ScalingResult.Empty(), sizeScaler.scale(new ArrayList<Object[]>()));
//	}
//	
//	@Test
//	public void testScaleInKb() throws Exception
//	{
//		List<Object[]> values = new ArrayList<Object[]>();
//		values.add(new Object[] { 1024L, 1024 * 1024L, 2 * 1024 * 1024L });
//		ScalingResult actuals = sizeScaler.scale(values);
//		assertEquals(1, actuals.values.size());
//		assertArrayEquals(new int[] {1, 1024, 2048}, actuals.values.get(0));
//		assertEquals("Kb", actuals.unit);
//	}
//	
//	@Test
//	public void testScaleInMb() throws Exception
//	{
//		List<Object[]> values = new ArrayList<Object[]>();
//		values.add(new Object[] { 1024L, 1024 * 1000L, 11 * 1024 * 1024L });
//		ScalingResult actuals = sizeScaler.scale(values);
//		assertEquals(1, actuals.values.size());
//		assertArrayEquals(new int[] {0, 1, 11}, actuals.values.get(0));
//		assertEquals("Mb", actuals.unit);
//	}
//}
