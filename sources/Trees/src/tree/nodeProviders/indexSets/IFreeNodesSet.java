package tree.nodeProviders.indexSets;

public interface IFreeNodesSet {
    void addFreeNode(long index);
    long getAnyFreeNodeNumber();
    long getTotalCount();
    void close();
}
