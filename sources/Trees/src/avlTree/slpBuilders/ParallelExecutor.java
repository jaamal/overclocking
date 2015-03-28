package avlTree.slpBuilders;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelExecutor implements IParallelExecutor {

    private final ArrayList<Future<?>> futures = new ArrayList<>();
    private final ExecutorService localExecutor;

    public ParallelExecutor(int threadCount) {
        localExecutor = Executors
                .newFixedThreadPool(threadCount,
                        new ThreadFactoryBuilder().setDaemon(true)
                                .setNameFormat("ParallelExecutor-%d")
                                .build());
    }

    @Override
    public void append(Runnable task) {
        futures.add(localExecutor.submit(task));
    }

    @Override
    public void await() {
        try {
            System.out.println("Tasks count " + futures.size());
            waitForTasksToFinish();
        } finally {
            localExecutor.shutdownNow();
        }
    }

    private void waitForTasksToFinish() {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}


