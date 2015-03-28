package cartesianTree.heapKeyResolvers;

public class RandomHeapKeyResolverFactory implements IHeapKeyResolverFactory
{
    private IRandomGeneratorFactory randomGeneratorFactory;

    public RandomHeapKeyResolverFactory(IRandomGeneratorFactory randomGeneratorFactory)
    {
        this.randomGeneratorFactory = randomGeneratorFactory;
    }

    @Override
	public IHeapKeyResolver create()
	{
		return new RandomHeapKeyResolver(randomGeneratorFactory);
	}

}
