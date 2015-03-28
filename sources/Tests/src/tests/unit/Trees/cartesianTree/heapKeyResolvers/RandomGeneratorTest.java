package tests.unit.Trees.cartesianTree.heapKeyResolvers;

import java.util.Random;

import cartesianTree.heapKeyResolvers.RandomGenerator;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;

public class RandomGeneratorTest extends UnitTestBase
{
	@Test
	public void testStress()
	{
		RandomGenerator randomGenerator = new RandomGenerator();
		Random random = new Random(1234567);
		for (int i = 0; i < 100000; ++i)
		{
			long left, right;
			do {
				left = random.nextLong();
				right = random.nextLong();
			} while (left > right);
			long result = randomGenerator.nextLong(left, right);
			Assert.assertTrue(left <= result && result <= right);
		}		
	}
	
	@Test
	public void testOne()
	{
		RandomGenerator randomGenerator = new RandomGenerator();
		long number = 1000;
		Assert.assertEquals(number, randomGenerator.nextLong(number, number));
	}
}
