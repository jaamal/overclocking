//package tests.front.services.statistics;
//
//import static org.junit.Assert.assertArrayEquals;
//import static org.junit.Assert.assertEquals;
//import java.util.ArrayList;
//import java.util.List;
//import models.statistics.UnitType;
//import org.junit.Test;
//import services.statistics.scaling.PercentScaler;
//import services.statistics.scaling.ScalingResult;
//import tests.UnitTestBase;
//
//public class PercentScalerTest extends UnitTestBase
//{
//	private PercentScaler percentScaler;
//
//	@Override
//	public void setUp()
//	{
//		super.setUp();
//		percentScaler = new PercentScaler();
//	}
//
//	@Test
//	public void testScale() throws Exception
//	{
//		assertEquals(UnitType.percent, percentScaler.getSupportedType());
//		List<Object[]> values = new ArrayList<Object[]>(); 
//		values.add(new Object[] {1.2F, 1.6F, Float.MAX_VALUE});
//		ScalingResult actuals = percentScaler.scale(values);
//		assertEquals(1, actuals.values.size());
//		assertArrayEquals(new int[] {1, 2, Integer.MAX_VALUE}, actuals.values.get(0));
//	}
//	
//	@Test
//	public void testScaleEmptyAndNull() throws Exception
//	{
//		assertEquals(ScalingResult.Empty(), percentScaler.scale(null));
//		assertEquals(ScalingResult.Empty(), percentScaler.scale(new ArrayList<Object[]>()));
//	}
//}
