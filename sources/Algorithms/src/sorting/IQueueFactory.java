package sorting;

public interface IQueueFactory<T>
{
   IQueue<T> create(long capacity);
}

