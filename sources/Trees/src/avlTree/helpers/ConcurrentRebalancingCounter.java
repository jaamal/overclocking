package avlTree.helpers;

import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentRebalancingCounter implements IRebalancingCounter {
    private AtomicInteger cnt = new AtomicInteger(0);

    @Override
    public void inc() {
        cnt.getAndIncrement();
    }

    @Override
    public int getCount() {
//        return 0;
        return cnt.get();
    }
}
