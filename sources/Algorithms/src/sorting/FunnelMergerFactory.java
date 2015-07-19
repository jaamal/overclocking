package sorting;

public class FunnelMergerFactory<T extends Comparable<T>> implements IFunnelMergerFactory<T>
{
    private IQueueFactory<T> queueFactory;

    public FunnelMergerFactory(IQueueFactory<T> queueFactory)
    {
        this.queueFactory = queueFactory;
    }

    @Override
    public IFunnelMerger<T> create(IQueue<T>[] arrays)
    {
        if (arrays.length <= 2)
        {
            IQueue<T> first = (arrays.length >= 1) ? arrays[0] : new MemoryQueue<T>(0);
            IQueue<T> second = (arrays.length >= 2) ? arrays[1] : new MemoryQueue<T>(0);
            return new SimpleFunnelMerger<T>(first, second);
        }
        
        return new ComplexFunnelMerger<T>(this, queueFactory, arrays);
    }
}