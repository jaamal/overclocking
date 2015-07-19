package sorting;

public interface IFunnelMergerFactory<T extends Comparable<T>>
{
    IFunnelMerger<T> create(IQueue<T>[] arrays);
}
