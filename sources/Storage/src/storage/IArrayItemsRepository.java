package storage;

import java.util.List;

public interface IArrayItemsRepository<T> {

    void writeAll(String statisticId, T[] items);
    void writeAll(String statisticId, List<T> items);
    List<T> readAll(String statisticId);
    
    Iterable<String> getDoneStatisticIds();

    void remove(String statisticId);
}
