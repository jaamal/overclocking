package avlTree.slpBuilders;

public class ParallelExecutorFactory implements IParallelExecutorFactory {
    private final int threadCount;

    public ParallelExecutorFactory(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public IParallelExecutor create() {
        return new DelayParallelExecutor(threadCount);
    }

}
