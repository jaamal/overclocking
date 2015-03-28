package tests.unit.Trees.cartesianTree.heapKeyResolvers;

import cartesianTree.nodes.CartesianTreeNode;
import cartesianTree.heapKeyResolvers.HeapKeyResolution;
import cartesianTree.heapKeyResolvers.RandomHeapKeyResolver;
import cartesianTree.heapKeyResolvers.IRandomGenerator;
import cartesianTree.heapKeyResolvers.IRandomGeneratorFactory;
import junit.framework.Assert;

import org.junit.Test;

import tests.unit.UnitTestBase;
import static org.easymock.EasyMock.expect;

public class RandomHeapKeyResolverTest extends UnitTestBase
{
	private IRandomGenerator mockRandomGenerator;
	private RandomHeapKeyResolver heapKeyResolver;

	@Override
	public void setUp()
	{
		super.setUp();
		mockRandomGenerator = newMock(IRandomGenerator.class);
		IRandomGeneratorFactory mockRandomGeneratorFactory = newMock(IRandomGeneratorFactory.class);

		expect(mockRandomGeneratorFactory.create()).andReturn(mockRandomGenerator);
        replayAll();
        heapKeyResolver = new RandomHeapKeyResolver(mockRandomGeneratorFactory);
        resetAll();
	}

	@Test
	public void test_1_1_Center()
	{
		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, 0, 1);
		CartesianTreeNode right = new CartesianTreeNode(0, -1, -1, 0, 1);
		expect(mockRandomGenerator.nextLong(0, 0)).andReturn(0L);
		
		replayAll();
		Assert.assertEquals(HeapKeyResolution.Center, heapKeyResolver.resolve(left, right));
	}
	
	@Test
	public void test_3_4_Left1()
	{
		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, 0, 3);
		CartesianTreeNode right = new CartesianTreeNode(1, -1, -1, 0, 4);
		expect(mockRandomGenerator.nextLong(0, 5)).andReturn(0L);
		
		replayAll();
	
		Assert.assertEquals(HeapKeyResolution.Left, heapKeyResolver.resolve(left, right));
	}
	
	@Test
	public void test_3_4_Left2()
	{
		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, 0, 3);
		CartesianTreeNode right = new CartesianTreeNode(1, -1, -1, 0, 4);
		expect(mockRandomGenerator.nextLong(0, 5)).andReturn(1L);
		
		replayAll();
		
		Assert.assertEquals(HeapKeyResolution.Left, heapKeyResolver.resolve(left, right));
	}
	
	@Test
	public void test_3_4_Right1()
	{
		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, 0, 3);
		CartesianTreeNode right = new CartesianTreeNode(1, -1, -1, 0, 4);
		expect(mockRandomGenerator.nextLong(0, 5)).andReturn(2L);
		
		replayAll();
		
		Assert.assertEquals(HeapKeyResolution.Right, heapKeyResolver.resolve(left, right));
	}
	
	@Test
	public void test_3_4_Right2()
	{
		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, 0, 3);
		CartesianTreeNode right = new CartesianTreeNode(1, -1, -1, 0, 4);
		expect(mockRandomGenerator.nextLong(0, 5)).andReturn(4L);
		
		replayAll();
	
		Assert.assertEquals(HeapKeyResolution.Right, heapKeyResolver.resolve(left, right));
	}
	
	@Test
	public void test_3_4_Center()
	{
		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, 0, 3);
		CartesianTreeNode right = new CartesianTreeNode(1, -1, -1, 0, 4);
		expect(mockRandomGenerator.nextLong(0, 5)).andReturn(5L);
		
		replayAll();
		
		Assert.assertEquals(HeapKeyResolution.Center, heapKeyResolver.resolve(left, right));
	}
}
