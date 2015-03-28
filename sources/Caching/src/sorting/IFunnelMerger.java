package sorting;

public interface IFunnelMerger<T extends Comparable<T>> {
    long invoke(IQueue<T> queue);
    long getResultSize();
}

