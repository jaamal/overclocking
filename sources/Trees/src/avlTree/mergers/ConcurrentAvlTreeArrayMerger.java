package avlTree.mergers;

import avlTree.IAvlTree;
import avlTree.helpers.IRebalancingCounter;
import avlTree.slpBuilders.IParallelExecutor;
import avlTree.slpBuilders.IParallelExecutorFactory;

public class ConcurrentAvlTreeArrayMerger extends AvlTreeArrayMergerWithStatisticsCollecting {
    private final IParallelExecutorFactory parallelExecutorFactory;

    public ConcurrentAvlTreeArrayMerger(IParallelExecutorFactory parallelExecutorFactory) {
        this.parallelExecutorFactory = parallelExecutorFactory;
    }

    @Override
    protected IAvlTree merge(final IAvlTree[] trees, final IRebalancingCounter rebalancingCounter) {
        int step = 1;
        while (step < trees.length) {
            final int localStep = step;

            IParallelExecutor parallelExecutor = parallelExecutorFactory.create();
            for (int index = step; index < trees.length; index += 2 * step) {
                final int localIndex = index;
                parallelExecutor.append(new Runnable() {

                    @Override
                    public void run() {
                        trees[localIndex - localStep] = trees[localIndex - localStep].merge(trees[localIndex], rebalancingCounter);
                    }
                });
            }
            parallelExecutor.await();
            step *= 2;
        }
        return trees[0];
    }
}
