package tests.unit.Caching;

import junit.framework.Assert;

import org.junit.Test;

import data.enumerableData.ConcurrentInMemoryEnumerableData;
import data.enumerableData.IEnumerableData;
import tests.unit.UnitTestBase;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class ConcurrentInMemoryEnumerableDataTest extends UnitTestBase {
    private IEnumerableData<Long> storage;
    
    @Override
    public void setUp() {
        super.setUp();
        storage = new ConcurrentInMemoryEnumerableData<Long>(Long.class, 100);
    }

    @Test
    public void TestOneThread() {
        for (long i = 0; i < 1000000; ++i)
            storage.save(i, i * i);
        for (long i = 0; i < 1000000; ++i)
            Assert.assertEquals(i * i, (long) storage.load(i));
    }

    @Test
    public void TestMultipleThread() throws InterruptedException {
        final int totalCount = 200000;
        Thread[] threads = new Thread[10];
        for (int i = 0; i < threads.length; ++i)
            threads[i] = new Thread(new ThreadWriterRunner(storage, i, threads.length, totalCount));
        for (int i = 0; i < threads.length; ++i)
            threads[i].start();
        for (int i = 0; i < threads.length; ++i)
            threads[i].join();
        for (long i = 0; i < totalCount; ++i)
            Assert.assertEquals(i * i, (long) storage.load(i));
    }

    private static class ThreadWriterRunner implements Runnable {
        private IEnumerableData<Long> storage;
        private int number;
        private int threadsCount;
        private int totalCount;
        private Random random;

        private ThreadWriterRunner(IEnumerableData<Long> storage, int number, int threadsCount, int totalCount) {
            this.storage = storage;
            this.number = number;
            this.threadsCount = threadsCount;
            this.totalCount = totalCount;
            random = new Random(UUID.randomUUID().hashCode());
        }

        @Override
        public void run() {
            ArrayList<Long> indexes = new ArrayList<Long>();
            for (long i = number; i < totalCount; i += threadsCount) {
                indexes.add(new Long(i));
            }
            for (int i = 1; i < indexes.size(); ++i) {
                int j = random.nextInt(i);
                Long temp = indexes.get(j);
                indexes.set(j, indexes.get(i));
                indexes.set(i, temp);
            }
            for (int j = 0; j < indexes.size(); ++j) {
                long i = indexes.get(j);
                storage.save(i, i * i);
            }
            System.out.println(String.format("Thread %d ended", number));
        }
    }
}
