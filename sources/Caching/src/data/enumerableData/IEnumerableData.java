package data.enumerableData;

public interface IEnumerableData<T>
{
    T load(long index);
    void save(long index, T obj);
    void close();
}
