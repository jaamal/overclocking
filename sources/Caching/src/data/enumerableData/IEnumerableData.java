package data.enumerableData;

public interface IEnumerableData<T>
{
    T load(long index);
    void save(long index, T value);
    
    default void saveAll(long fromIndex, T[] values) {
        for (int diff = 0; diff < values.length; diff++) {
            save(fromIndex + diff, values[diff]);
        }
    }
    
    void close();
}
