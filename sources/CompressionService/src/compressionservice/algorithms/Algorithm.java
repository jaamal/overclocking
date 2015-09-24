package compressionservice.algorithms;

import dataContracts.statistics.IStatistics;

public abstract class Algorithm implements IAlgorithm
{
    protected boolean isFinished;
    
    protected void checkIsFinished() throws RuntimeException
    {
        if (!isFinished)
            throw new RuntimeException("Algorithm does not running.");
    }
    
    @Override
    public void run()
    {
        isFinished = false;
        runInternal();
        isFinished = true;
    }
    
    protected abstract void runInternal();
    
    @Override
    public abstract IStatistics getStats();
}
