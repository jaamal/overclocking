package caching.doubleLinkedList;

//TODO test this package (take all tests from old repository)
public class List implements IList
{
    private final IListNodeProvider nodeProvider;
    private final int headKey;

    public List(IListNodeProviderFactory listNodeProviderFactory, int maxSize)
    {
        headKey = maxSize;

        nodeProvider = listNodeProviderFactory.create(maxSize + 1);
        nodeProvider.create(headKey);
    }

    @Override
    public void prepend(int key)
    {
        if (key == headKey)
            throw new RuntimeException(String.format("Fail to delete head key %d.", key));
        
        appendAfter(headKey, nodeProvider.create(key));
    }

    @Override
    public void moveToBegin(int key)
    {
        delete(key);
        prepend(key);
    }
    
    @Override
    public int getLastKey()
    {
        ListNode head = nodeProvider.get(headKey);
        return nodeProvider.get(head.prev).key;
    }

    private void delete(int key)
    {
        if (key == headKey)
            throw new RuntimeException(String.format("Fail to delete head key %d.", key));
        
        ListNode node = nodeProvider.get(key);
        nodeProvider.updateNext(nodeProvider.get(node.prev), node.next);
        nodeProvider.delete(node);
    }

    private void appendAfter(int prevKey, ListNode newNode)
    {
        int nextKey = nodeProvider.get(prevKey).next;
        newNode = nodeProvider.updateNext(newNode, nextKey);
        nodeProvider.updatePrev(newNode, prevKey);
    }
}
