package tests.unit.Trees.cartesianTree;

import cartesianTree.nodes.CartesianTreeNode;

import org.junit.Assert;
import org.junit.Test;

import tests.unit.UnitTestBase;

public class CartesianTreeNodeTest extends UnitTestBase
{
	@Test
	public void testEquals() throws Exception
	{
		CartesianTreeNode node1 = new CartesianTreeNode(0, 1, 2, 3, 4);
		CartesianTreeNode node2 = new CartesianTreeNode(0, 1, 2, 3, 4);

		Assert.assertEquals(true, node1.equals(node2));
	}

	@Test
	public void testDifferent1() throws Exception
	{
		CartesianTreeNode node1 = new CartesianTreeNode(0, 1, 2, 3, 4);
		CartesianTreeNode node2 = new CartesianTreeNode(-1, 1, 2, 3, 4);

		Assert.assertEquals(false, node1.equals(node2));
	}

	@Test
	public void testDifferent2() throws Exception
	{
		CartesianTreeNode node1 = new CartesianTreeNode(0, 1, 2, 3, 4);
		CartesianTreeNode node2 = new CartesianTreeNode(0, -1, 2, 3, 4);

		Assert.assertEquals(false, node1.equals(node2));
	}

	@Test
	public void testDifferent3() throws Exception
	{
		CartesianTreeNode node1 = new CartesianTreeNode(0, 1, 2, 3, 4);
		CartesianTreeNode node2 = new CartesianTreeNode(0, 1, -2, 3, 4);

		Assert.assertEquals(false, node1.equals(node2));
	}

	@Test
	public void testDifferent4() throws Exception
	{
		CartesianTreeNode node1 = new CartesianTreeNode(0, 1, 2, 3, 4);
		CartesianTreeNode node2 = new CartesianTreeNode(0, 1, 2, -3, 4);

		Assert.assertEquals(false, node1.equals(node2));
	}

	@Test
	public void testDifferent5() throws Exception
	{
		CartesianTreeNode node1 = new CartesianTreeNode(0, 1, 2, 3, 4);
		CartesianTreeNode node2 = new CartesianTreeNode(0, 1, 2, 3, -4);

		Assert.assertEquals(false, node1.equals(node2));
	}
}