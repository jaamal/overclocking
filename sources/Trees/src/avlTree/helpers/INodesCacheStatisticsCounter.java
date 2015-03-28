package avlTree.helpers;

import avlTree.IAvlTree;
import avlTree.nodes.AvlTreeNode;

import java.util.Collection;

public interface INodesCacheStatisticsCounter
{
    void flushEvent(Collection<AvlTreeNode> nodesCache);
    void flushEvent(IAvlTree[] treesCache);
}
