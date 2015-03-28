package storage.cassandraClient;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.model.ColumnFamily;

public interface ISchemeInitializer {
    boolean isKeyspaceExists(String name);
    Keyspace createKeyspace(String name, int replicationFactor);
    void truncateKeyspace(String name);
    
    <K, C> boolean isColumnFamilyExisits(Keyspace keyspace, ColumnFamily<K, C> columnFamily);
    <K, C> void createColumnFamily(Keyspace keyspace, ColumnFamily<K, C> columnFamily, CFValidationClass validationClass); 
    
    void setUpCluster();
}
