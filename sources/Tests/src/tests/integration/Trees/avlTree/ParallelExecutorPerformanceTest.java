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
import avlTree.slpBuilders.DelayParallelExecutor;
import avlTree.slpBuilders.IParallelExecutor;
import avlTree.slpBuilders.ParallelExecutor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import commons.utils.TimeCounter;

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
                Thread.sleep(10000);
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
    public void testParrallelExecutor() {
        final int ITER = 10;
        long totalElapsed = 0;
        for (int ii = 0; ii < ITER; ++ii) {
            final int threadsCount = 16;
            final int workerPerThread = 100;
            final int actionsCount = (int) 1e9;
            IParallelExecutor parallelExecutor = new ParallelExecutor(threadsCount);
            Worker[] workers = new Worker[threadsCount * workerPerThread];
            for (int i = 0; i < threadsCount; ++i) {
                for (int j = 0; j < workerPerThread; ++j) {
                    int num = i * workerPerThread + j;
                    workers[num] = new Worker(actionsCount / threadsCount / workerPerThread, num);
                    parallelExecutor.append(workers[num]);
                }
            }

            TimeCounter timeCounter = TimeCounter.start();
            parallelExecutor.await();
            timeCounter.finish();
            totalElapsed += timeCounter.getMillis();

            int totalCount = 0;
            for (Worker worker : workers)
                totalCount += worker.getValue();
            Assert.assertEquals(actionsCount, totalCount);
        }
        System.out.println("Mean elapsed " + totalElapsed / 1000.0 / ITER);
        System.gc();
    }
    
    @Test
    public void testDelayParrallelExecutor() {
        final int ITER = 10;
        long totalElapsed = 0;
        for (int ii = 0; ii < ITER; ++ii) {
            final int threadsCount = 16;
            final int workerPerThread = 100;
            final int actionsCount = (int) 1e9;
            IParallelExecutor parallelExecutor = new DelayParallelExecutor(threadsCount);
            Worker[] workers = new Worker[threadsCount * workerPerThread];
            for (int i = 0; i < threadsCount; ++i) {
                for (int j = 0; j < workerPerThread; ++j) {
                    int num = i * workerPerThread + j;
                    workers[num] = new Worker(actionsCount / threadsCount / workerPerThread, num);
                    parallelExecutor.append(workers[num]);
                }
            }

            TimeCounter timeCounter = TimeCounter.start();
            parallelExecutor.await();
            timeCounter.finish();
            totalElapsed += timeCounter.getMillis();

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

        private Worker(int incrementsPerRun, int number) {
            this.incrementsPerRun = incrementsPerRun;
        }

        public int getValue() {
            return counter.get();
        }

        @Override
        public void run() {
            for (int i = 0; i < incrementsPerRun; ++i)
                counter.incrementAndGet();
        }
    }
}
