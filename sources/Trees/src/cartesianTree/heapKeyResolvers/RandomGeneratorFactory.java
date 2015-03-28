package cartesianTree.heapKeyResolvers;

public class RandomGeneratorFactory implements IRandomGeneratorFactory
{

	@Override
	public IRandomGenerator create()
	{
		return new RandomGenerator();
	}

}
