package commons.utils;

public class TimeCounter implements ITimeCounter
{
    private long startTime;
    private long endTime;
    
    public TimeCounter()
    {
        startTime = 0;
        endTime = 0;
    }

    @Override
    public void start()
    {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void end()
    {
        endTime = System.currentTimeMillis();
    }

    @Override
    public long getTime()
    {
        return endTime - startTime;
    }
}
