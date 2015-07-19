package sorting;

public class MemoryQueueFactory<T> implements IQueueFactory<T>
{
    public long sum = 0;

    public IQueue<T> create(long capacity)
    {
        sum += capacity;
        return new MemoryQueue<T>((int)capacity);
    }
}
