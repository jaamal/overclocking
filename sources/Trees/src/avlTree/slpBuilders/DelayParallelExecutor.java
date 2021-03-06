package avlTree.slpBuilders;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class DelayParallelExecutor implements IParallelExecutor {

    private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("DelayParallelExecutor-%d").build();
    private final ArrayList<Runnable> queue = new ArrayList<>();
    private final int threadCount;

    public DelayParallelExecutor(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public void append(Runnable task) {
        queue.add(task);
    }

    @Override
    public void await() {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount, threadFactory);
        try {
            final Runnable[] tasks = queue.toArray(new Runnable[0]);
            int approxBatchCount = Math.min(tasks.length, 50 * threadCount);
            final int batchSize = (tasks.length + approxBatchCount - 1) / approxBatchCount;
            final int batchCount = (tasks.length + batchSize - 1) / batchSize;
            Future<?>[] futures = new Future[batchCount];
            for (int i = 0; i < batchCount; ++i) {
                final int start = i * batchSize;
                futures[i] = executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        for (int j = 0; j < batchSize && start + j < tasks.length; ++j)
                            tasks[start + j].run();
                    }
                });
            }
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            executor.shutdownNow();
        }
    }
}
