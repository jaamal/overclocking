package avlTree.helpers;

public class RebalancingCounter implements IRebalancingCounter
{
    private int cnt = 0;

    @Override
    public void inc()
    {
        cnt++;
    }

    @Override
    public int getCount()
    {
        return cnt;
    }
}
