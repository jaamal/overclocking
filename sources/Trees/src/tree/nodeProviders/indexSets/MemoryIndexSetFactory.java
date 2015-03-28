package tree.nodeProviders.indexSets;

public class MemoryIndexSetFactory implements IIndexSetFactory
{
    @Override
    public IIndexSet create()
    {
        return new MemoryIndexSet();
    }
}
