package commons.utils;

import java.time.Duration;

public class TimeCounter
{
    private final long beginMillis;
    private long endMillis = 0;
    
    private TimeCounter() {
        beginMillis = System.currentTimeMillis();
    }
    
    public static TimeCounter start() {
        return new TimeCounter();
    }
    
    public Duration finish() {
        endMillis = System.currentTimeMillis();
        return Duration.ofMillis(getMillis());
    }
    
    public long getMillis() {
        if (endMillis == 0)
            throw new RuntimeException("Fail to get counter value since it has not been finished.");
        return endMillis - beginMillis;
    }
}
