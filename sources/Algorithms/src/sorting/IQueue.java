package sorting;

public interface IQueue<T>
{
    void push(T obj);
    T pop();
    T front();
    long count();
    void copyFrom(IQueue<T> destination, long count);
}

