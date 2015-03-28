//package tests.front.services.statistics;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import java.util.ArrayList;
//import java.util.List;
//import models.statistics.UnitType;
//import org.junit.Test;
//import services.statistics.scaling.CounterScaler;
//import services.statistics.scaling.ScalingResult;
//import tests.UnitTestBase;
//
//public class CounterScalerTest extends UnitTestBase
//{
//
//	private CounterScaler counterScaler;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		counterScaler = new CounterScaler();
//	}
//
//	@Test
//	public void testScale() throws Exception
//	{
//		assertEquals(UnitType.counter, counterScaler.getSupportedType());
//		List<Object[]> values = new ArrayList<Object[]>();
//		values.add(new Object[] { 1, 6, Integer.MAX_VALUE });
//		ScalingResult actuals = counterScaler.scale(values);
//		assertEquals(1, actuals.values.size());
//		assertArrayEquals(new int[] {1, 6, Integer.MAX_VALUE}, actuals.values.get(0));
//	}
//	
//	@Test
//	public void testScaleEmptyAndNull() throws Exception
//	{
//		assertEquals(ScalingResult.Empty(), counterScaler.scale(null));
//		assertEquals(ScalingResult.Empty(), counterScaler.scale(new ArrayList<Object[]>()));
//	}
//}
