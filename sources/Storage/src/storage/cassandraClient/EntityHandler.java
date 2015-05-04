package storage.cassandraClient;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import storage.KeySpaces;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.entitystore.DefaultEntityManager;
import com.netflix.astyanax.entitystore.EntityManager;
import com.netflix.astyanax.model.ColumnFamily;

public class EntityHandler implements IEntityHandler {

    private ICassandraConnectionFactory cassandraConnectionFactory;

    public EntityHandler(ICassandraConnectionFactory cassandraConnectionFactory) {
        this.cassandraConnectionFactory = cassandraConnectionFactory;
    }
    
    private final static ConcurrentHashMap<Class<?>, EntityManager<?, String>> managersMap = new ConcurrentHashMap<Class<?>, EntityManager<?,String>>();
    
    @Override
    public <T> void write(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, T entity) {
        EntityManager<T, String> entityManager = getOrCreateManager(keyspace, columnFamily, clazz);
        entityManager.put(entity);
    }

    @Override
    public <T> T read(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, String id) {
        EntityManager<T, String> entityManager = getOrCreateManager(keyspace, columnFamily, clazz);
        return entityManager.get(id);
    }
    
    @Override
    public <T> List<T> read(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, Collection<String> ids) {
        EntityManager<T, String> entityManager = getOrCreateManager(keyspace, columnFamily, clazz);
        return entityManager.get(ids);
    }
    
    public <T> List<T> readAll(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz) {
        EntityManager<T, String> entityManager = getOrCreateManager(keyspace, columnFamily, clazz);
        return entityManager.getAll();
    }

    @Override
    public <T> void delete(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz, String id) {
        EntityManager<T, String> entityManager = getOrCreateManager(keyspace, columnFamily, clazz);
        entityManager.delete(id);
    }

    private <T> EntityManager<T, String> getOrCreateManager(KeySpaces keyspace, ColumnFamily<String, String> columnFamily, Class<T> clazz) {
        if (!managersMap.containsKey(clazz)){
            Keyspace _keyspace = cassandraConnectionFactory.getKeyspace(keyspace.toString());
            EntityManager<T, String> entityManager = new DefaultEntityManager.Builder<T, String>()
                                                                             .withEntityType(clazz)
                                                                             .withKeyspace(_keyspace)
                                                                             .withColumnFamily(columnFamily)
                                                                             .build();
            managersMap.put(clazz, entityManager);
        }
        return (EntityManager<T, String>) managersMap.get(clazz);
    }    
}
