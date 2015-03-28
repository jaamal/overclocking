package cartesianTree.heapKeyResolvers;

import java.util.Random;

public class RandomGenerator implements IRandomGenerator
{
	private Random random;

	public RandomGenerator()
	{
		this.random = new Random();
	}

	@Override
	public long nextLong(long left, long right)
	{
		return Math.abs(random.nextLong()) % (right - left + 1) + left;
	}

}
