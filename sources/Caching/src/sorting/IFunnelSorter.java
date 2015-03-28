package sorting;

public interface IFunnelSorter<T extends Comparable<T>>
{
    void Sort(IQueue<T> in, IQueue<T> out);
}

