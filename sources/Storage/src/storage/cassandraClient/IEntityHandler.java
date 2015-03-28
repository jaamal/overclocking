package storage.cassandraClient;

import java.util.List;

import storage.KeySpaces;

import com.netflix.astyanax.model.ColumnFamily;

public interface IEntityHandler {
    <T> void write(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, T entity);
    <T> T read(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, String id);
    <T> List<T> readAll(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz);
    <T> void delete(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, String id);
}
