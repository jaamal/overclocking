package avlTree.helpers;

import avlTree.IAvlTree;
import avlTree.nodes.AvlTreeNode;

import java.util.Collection;

public class NodesCacheStatisticsCounter implements INodesCacheStatisticsCounter
{
    private long totalWidth = 0;
    private int flushCount = 0;

    @Override
    public void flushEvent(Collection<AvlTreeNode> nodesCache)
    {
        for (AvlTreeNode node : nodesCache)
            totalWidth += node.getWidth();
        flushCount++;
    }

    @Override
    public void flushEvent(IAvlTree[] treesCache)
    {
        for (IAvlTree tree : treesCache)
            totalWidth += tree.getWidth();
        flushCount++;
    }

    public double averageCacheWidth()
    {
        return flushCount == 0 ? 0 : (double) totalWidth / flushCount;
    }
}

