package caching;

public interface IStorage<T>
{
    T load(long number);
    void save(long number, T obj);
    void close();
}
