package storage;

import java.util.List;

public interface IArrayItemsRepository<T> {
    IArrayItemsWriter<T> getWriter(String statisticId);

    Iterable<String> getDoneStatisticIds();

    List<T> readItems(String statisticId);

    void remove(String statisticId);
}
