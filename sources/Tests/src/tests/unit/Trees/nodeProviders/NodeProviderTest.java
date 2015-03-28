package tests.unit.Trees.nodeProviders;

import tests.unit.UnitTestBase;

public class NodeProviderTest extends UnitTestBase
{
    //TODO
//	private TreeNodeProvider nodeProvider;
//	private IStorage<ITreeNode> nodeStorage;
//	private IIndexSet indexSet;
//    private Random random;
//    private ITreeNodeBuilder treeNodeBuilder;
//
//    @Override
//	public void setUp() {
//		super.setUp();
//
//        IIndexSetFactory indexSetFactory = newMock(IIndexSetFactory.class);
//        expect(indexSetFactory.createOneElement()).andReturn(indexSet);
//
//		nodeStorage = newMock(IStorage.class);
//		indexSet = newMock(IIndexSet.class);
//        treeNodeBuilder = newMock(ITreeNodeBuilder.class);
//
//        replayAll();
//        nodeProvider = new TreeNodeProvider(nodeStorage, indexSetFactory, treeNodeBuilder);
//		random = new Random();
//    }
//
//	private void expectCreate(ITreeNode node, ITreeNode left,
//			ITreeNode right, ITreeNode newLeft,
//			ITreeNode newRight) {
//		resetAll();
//		if (random.nextInt(2) == 0) {
//			expect(indexSet.isEmpty()).andReturn(false);
//			expect(indexSet.popAny()).andReturn(node.getNumber());
//		} else {
//			expect(indexSet.isEmpty()).andReturn(true);
//		}
//		expectChangeInner(node.getLeftSonNumber(), left, newLeft);
//		expectChangeInner(node.getRightSonNumber(), right, newRight);
//		expect(nodeStorage.load(node.getLeftSonNumber())).andReturn(left);
//		expect(nodeStorage.load(node.getRightSonNumber())).andReturn(right);
//		nodeStorage.save(node.getNumber(), node);
//		replayAll();
//
//		Assert.assertEquals(node, nodeProvider.createOneElement(node.getLeftSonNumber(), node.getRightSonNumber(), node.getValue()));
//	}
//
//	private void expectChangeInner(long number, ITreeNode child,
//			ITreeNode newChild) {
//		expect(nodeStorage.load(number)).andReturn(child);
//		if (child == null)
//			return;
//		nodeStorage.save(number, newChild);
//	}
//
//	@Test
//	public void testGet() throws Exception {
//		for (int i = 0; i < 100; ++i)
//		{
//			resetAll();
//			ITreeNode node = getTreeNode();
//			expect(nodeStorage.load(i)).andReturn(node);
//			replayAll();
//			Assert.assertEquals(node, nodeProvider.get(i));
//		}
//	}
//
//    private ITreeNode getTreeNode()
//    {
//        final long left = random.nextLong();
//        final long right = random.nextLong();
//        final long number = random.nextLong();
//        final long value = random.nextLong();
//        final long countInner = random.nextLong();
//        return new ITreeNode()
//            {
//                @Override
//                public long getLeftSonNumber()
//                {
//                    return left;
//                }
//
//                @Override
//                public long getRightSonNumber()
//                {
//                    return right;
//                }
//
//                @Override
//                public long getNumber()
//                {
//                    return number;
//                }
//
//                @Override
//                public long getValue()
//                {
//                    return value;
//                }
//
//                @Override
//                public long getCountInnerReferences()
//                {
//                    return countInner;
//                }
//            };
//    }
//
//    @Test
//	public void testCreateLeaf() throws Exception {
//		ITreeNode expectedNode = getTreeNode();
//		expectCreate(expectedNode, null, null, null, null);
//	}
//
//	@Test
//	public void testCreateNonLeaf() throws Exception {
//        expectCreate(getTreeNode(), getTreeNode(), getTreeNode(), getTreeNode(), getTreeNode());
//	}
//
//	@Test
//	public void testCreateSeveralNodes() throws Exception {
//		for (int i = 0; i < 10; i++) {
//			testCreateNonLeaf();
//		}
//	}
//
//	@Test
//	public void testDisposeAllBut()
//	{
//		CartesianTreeNode node = new CartesianTreeNode(2, 0, 1, -1, 2, 0);
//		CartesianTreeNode left = new CartesianTreeNode(0, -1, -1, -1, 1, 0);
//		CartesianTreeNode left1 = new CartesianTreeNode(0, -1, -1, -1, 1, 1);
//		CartesianTreeNode right = new CartesianTreeNode(1, -1, -1, -1, 1, 0);
//		CartesianTreeNode right1 = new CartesianTreeNode(1, -1, -1, -1, 1, 1);
//		CartesianTreeNode right2 = new CartesianTreeNode(1, -1, -1, -1, 1, 2);
//		expectCreate(left, null, null, null, null);
//		expectCreate(right, null, null, null, null);
//		expectCreate(node, left, right, left1, right1);
//
//		resetAll();
//		expect(nodeStorage.get(1)).andReturn(right1);
//		nodeStorage.set(1, right2);
//
//		expect(nodeStorage.get(2)).andReturn(node);
//		expect(nodeStorage.get(0)).andReturn(left1);
//		nodeStorage.set(0, left);
//		expect(nodeStorage.get(1)).andReturn(right2);
//		nodeStorage.set(1, right1);
//		indexSet.add(2);
//
//		expect(nodeStorage.get(0)).andReturn(left);
//		expect(nodeStorage.get(-1)).andReturn(null);
//		expect(nodeStorage.get(-1)).andReturn(null);
//		indexSet.add(0);
//
//		expect(nodeStorage.get(1)).andReturn(right1);
//		nodeStorage.set(1, right);
//		replayAll();
//
//		nodeProvider.disposeAllBut(1);
//	}
}
