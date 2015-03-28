package tests.integration.Trees.avlTree;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;

import tests.integration.IntegrationTestBase;
import avlTree.slpBuilders.ParallelExecutor;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class ParallelExecutorPerformanceTest extends IntegrationTestBase {

    @Test
    public void test2() throws InterruptedException {
        final int threadCount = 4;
        ExecutorService executor = Executors
                .newFixedThreadPool(
                        threadCount,
                        new ThreadFactoryBuilder().setDaemon(true)
                                .setNameFormat("ParallelExecutor-%d")
                                .build());
        final Future<Integer> answer = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("Sleeping...");
                Thread.sleep(100000);
                return 42;
            }
        });

        for (int i = 0; i < 10; ++i) {
            final int local = i;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Run " + local);
                    try {
                        System.out.println(String.format("%dth answer is %d", local, answer.get()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        for (int i = 0; i < 10; ++i) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("La-la-la");
                }
            });
        }
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }


    @Test
    public void Test() {
        final int ITER = 10;
        long totalElapsed = 0;
        for (int ii = 0; ii < ITER; ++ii) {
            final int threadsCount = 16;
            final int workerPerThread = 100;
            final int actionsCount = (int) 1e9;
            ParallelExecutor parallelExecutor = new ParallelExecutor(threadsCount);
            Worker[] workers = new Worker[threadsCount * workerPerThread];
            for (int i = 0; i < threadsCount; ++i) {
                for (int j = 0; j < workerPerThread; ++j) {
                    int num = i * workerPerThread + j;
                    workers[num] = new Worker(actionsCount / threadsCount / workerPerThread, num);
                    parallelExecutor.append(workers[num]);
                }
            }

            Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            parallelExecutor.await();
            stopwatch.stop();
            System.out.println(stopwatch.elapsedMillis() / 1000.0);
            totalElapsed += stopwatch.elapsedMillis();

            int totalCount = 0;
            for (Worker worker : workers)
                totalCount += worker.getValue();
            Assert.assertEquals(actionsCount, totalCount);
        }
        System.out.println("Mean elapsed " + totalElapsed / 1000.0 / ITER);
        System.gc();
    }

    private class Worker implements Runnable {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final int incrementsPerRun;
        private final int number;

        private Worker(int incrementsPerRun, int number) {
            this.incrementsPerRun = incrementsPerRun;
            this.number = number;
        }

        public int getValue() {
            return counter.get();
        }

        @Override
        public void run() {
//            System.out.println(String.format("Start working %d...", number));
            for (int i = 0; i < incrementsPerRun; ++i)
                counter.incrementAndGet();
//            System.out.println(String.format("End working %d!", number));
        }
    }
}
