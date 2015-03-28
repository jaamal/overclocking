package sorting;

import java.util.ArrayList;

public class ComplexFunnelMerger<T extends Comparable<T>> implements IFunnelMerger<T>
{
    private IFunnelMerger<T>[] innerMergers;
    private IQueue<T>[] buffers;

    private IFunnelMerger<T> mainMerger;
    private long resultSize;

    public ComplexFunnelMerger(IFunnelMergerFactory<T> funnelMergerFactory, IQueueFactory<T> queueFactory, IQueue<T>[] arrays)
    {
        int k = arrays.length;
        ComplexFunnelMergerParameters parameters = ComplexFunnelMergerParametersGetter.instance.getParameters(k);

        int innerMergersCount = (int) parameters.innerMergersCount;
        int streamCountForMerger = (int) parameters.streamCountForMerger;
        resultSize = parameters.resultSize;
        innerMergers = new IFunnelMerger[innerMergersCount];
        buffers = new IQueue[innerMergersCount];

        long mainMergerResultSize = ComplexFunnelMergerParametersGetter.instance.getResultSize(innerMergersCount);
        for (int i = 0, j = 0; i < innerMergersCount; ++i, j += streamCountForMerger)
        {
            ArrayList<IQueue<T>> current = new ArrayList<IQueue<T>>();
            for (int l = 0; l < streamCountForMerger && j + l < arrays.length; ++l)
                current.add(arrays[j + l]);
            innerMergers[i] = funnelMergerFactory.create(current.toArray(new IQueue[0]));

            buffers[i] = queueFactory.create(innerMergers[i].getResultSize() + mainMergerResultSize);
        }
        mainMerger = funnelMergerFactory.create(buffers);
    }

    @Override
    public long invoke(IQueue<T> queue)
    {
        long count = 0;
        while (count < resultSize)
        {
            for (int i = 0; i < innerMergers.length; ++i)
            {
                while (buffers[i].count() < mainMerger.getResultSize() && innerMergers[i].invoke(buffers[i]) != 0) ;
            }
            long delta = mainMerger.invoke(queue);
            if (delta == 0)
                break;
            count += delta;
        }
        return count;
    }

    @Override
    public long getResultSize()
    {
        return resultSize;
    }
}

