package commons.utils;

import java.time.Duration;

public class TimeCounter
{
    private final long beginMillis;

    private TimeCounter() {
        beginMillis = System.currentTimeMillis();
    }
    
    public static TimeCounter start() {
        return new TimeCounter();
    }
    
    public Duration finish() {
        return Duration.ofMillis(getMillis());
    }
    
    public long getMillis() {
        return System.currentTimeMillis() - beginMillis;
    }
}
