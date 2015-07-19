package tests.unit.Algorithms.sorting;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import sorting.FunnelMergerFactory;
import sorting.FunnelSorter;
import sorting.IQueue;
import sorting.MemoryQueueFactory;
import tests.unit.UnitTestBase;

import java.util.Arrays;
import java.util.Random;

public class FunnelSorterTest extends UnitTestBase
{
    private FunnelSorter<Integer> sorter;
    private MemoryQueueFactory<Integer> queueFactory;
    private FunnelMergerFactory<Integer> funnelMergerFactory;

    @Override
    public void setUp()
    {
        super.setUp();
        queueFactory = new MemoryQueueFactory<Integer>();
        funnelMergerFactory = new FunnelMergerFactory<Integer>(queueFactory);
        sorter = new FunnelSorter<Integer>(funnelMergerFactory, queueFactory);
    }

    @Test
    public void Test()
    {
        DoTest(new Integer[]{67, 23, 4, 53, 3, 4, 5, 6, 7, 123, 456876, 23, 4, 123, 54235, 434, 123, 123, 213, 123, 23});
    }

    @Test
    @Ignore
    public void TestBig()
    {
        Random random = new Random(42);
        Integer[] array = new Integer[5000000];
        for (int i = 0; i < array.length; ++i)
            array[i] = random.nextInt();
        DoTest(array);
    }

    private void DoTest(Integer[] array)
    {
        IQueue<Integer> in = queueFactory.create(array.length);
        IQueue<Integer> out = queueFactory.create(array.length);
        for (int i = 0; i < array.length; ++i)
            in.push(array[i]);

        long myStart = System.currentTimeMillis();
        sorter.Sort(in, out);
        long myFinish = System.currentTimeMillis();

        System.out.println("My time is " + (myFinish - myStart) / 1000.0);

        long standardStart = System.currentTimeMillis();
        Arrays.sort(array);
        long standardFinish = System.currentTimeMillis();

        System.out.println("Standard time is " + (standardFinish - standardStart) / 1000.0);

        Assert.assertEquals(array.length, out.count());
        for (int i = 0; i < array.length; ++i)
            Assert.assertEquals(array[i], out.pop());

        System.out.println(queueFactory.sum);
        System.out.flush();
    }
}
