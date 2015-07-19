package sorting;

import java.util.ArrayList;

public class FunnelSorter<T extends Comparable<T>> implements IFunnelSorter<T>
{
    private IFunnelMergerFactory<T> funnelMergerFactory;
    private IQueueFactory<T> queueFactory;

    public FunnelSorter(IFunnelMergerFactory<T> funnelMergerFactory, IQueueFactory<T> queueFactory)
    {
        this.funnelMergerFactory = funnelMergerFactory;
        this.queueFactory = queueFactory;
    }

    @Override
    public void Sort(IQueue<T> in, IQueue<T> out)
    {
        long n = in.count();
        if (n <= 1)
        {
            out.copyFrom(in, in.count());
            return ;
        }
        long batchCount = (long)Math.ceil(Math.pow(n, 1.0 / 3.0));
        long batchSize = (n + batchCount - 1) / batchCount;
        IQueue<T>[] batches = new IQueue[(int)batchCount];
        for (int i = 0; i < batches.length; ++i)
        {
            long batchLength = Math.min(in.count(), batchSize);
            IQueue<T> notSorted = queueFactory.create(batchLength);
            notSorted.copyFrom(in, batchLength);
            IQueue<T> sorted = queueFactory.create(batchLength);
            Sort(notSorted, sorted);

            batches[i] = sorted;
        }
        IFunnelMerger<T> merger = funnelMergerFactory.create(batches);
        while(merger.invoke(out) != 0);
    }

    private void BubbleSort(ArrayList<T> arrayList)
    {
        for (int i = 0; i < arrayList.size(); ++i)
            for (int j = 0; j < arrayList.size(); ++j)
                if (arrayList.get(i).compareTo(arrayList.get(j)) > 0)
                {
                    T temp = arrayList.get(i);
                    arrayList.set(i, arrayList.get(j));
                    arrayList.set(j, temp);
                }
    }
}
