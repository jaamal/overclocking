package avlTree.slpBuilders;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ParallelExecutor implements IParallelExecutor {

    private final ArrayList<Future<?>> futures = new ArrayList<>();
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat("ParallelExecutor-%d").build();
    private final ExecutorService executor;

    public ParallelExecutor(int threadCount) {
        executor = Executors.newFixedThreadPool(threadCount, threadFactory);
    }

    @Override
    public void append(Runnable task) {
        futures.add(executor.submit(task));
    }

    @Override
    public void await() {
        try {
            System.out.println("Tasks count " + futures.size());
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


