package database;

import java.util.List;

public interface IDBContext {
    <T> void create(T entity) throws DBException;

    <T> List<T> select(Class<T> c) throws DBException;

    <T> void truncateTable(Class<T> c) throws DBException;
}
