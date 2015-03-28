package cartesianTree.heapKeyResolvers;

import cartesianTree.nodes.CartesianTreeNode;

public class RandomHeapKeyResolver implements IHeapKeyResolver
{
	private final IRandomGenerator randomGenerator;

	public RandomHeapKeyResolver(IRandomGeneratorFactory randomGeneratorFactory) {
		randomGenerator = randomGeneratorFactory.create();
	}

	@Override
	public HeapKeyResolution resolve(CartesianTreeNode leftRoot, CartesianTreeNode rightRoot)
	{
		long weightLeft = leftRoot.count - 1;
		long weightRight = rightRoot.count - 1;
		long weightCenter = 1;

		long number = randomGenerator.nextLong(0, weightLeft + weightRight + weightCenter - 1);

		if (number < weightLeft)
			return HeapKeyResolution.Left;
		number -= weightLeft;

		if (number < weightRight)
			return HeapKeyResolution.Right;

		return HeapKeyResolution.Center;
	}
}
