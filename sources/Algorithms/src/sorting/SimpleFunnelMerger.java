package sorting;

public class SimpleFunnelMerger<T extends Comparable<T>> implements IFunnelMerger<T>
{
    private IQueue<T> first;
    private IQueue<T> second;
    private final int resultSize = 8;

    public SimpleFunnelMerger(IQueue<T> first, IQueue<T> second)
    {

        this.first = first;
        this.second = second;
    }

    @Override
    public long invoke(IQueue<T> queue)
    {
        int count;
        for (count = 0; count < resultSize; ++count)
        {
            if (first.count() == 0 && second.count() == 0)
                break;
            if (first.count() != 0 && (second.count() == 0 || first.front().compareTo(second.front()) < 0))
                queue.push(first.pop());
            else
                queue.push(second.pop());
        }
        return count;
    }

    @Override
    public long getResultSize()
    {
        return resultSize;
    }
}
