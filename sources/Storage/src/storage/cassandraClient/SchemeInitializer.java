package storage.cassandraClient;

import storage.KeySpaces;

import com.google.common.collect.ImmutableMap;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.BadRequestException;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;

public class SchemeInitializer implements ISchemeInitializer {

    private ICassandraConnectionFactory connectionFactory;

    public SchemeInitializer(ICassandraConnectionFactory keyspaceFactory) {
        this.connectionFactory = keyspaceFactory;
    }
    
    @Override
    public boolean isKeyspaceExists(String name) {
        try {
            connectionFactory.getKeyspace(name).describeKeyspace();
            return true;
        }
        catch (BadRequestException e) {
            return false;
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Fail to connect to keyspace %s", name), e);
        }
    }

    @Override
    public Keyspace createKeyspace(String name, int replicationFactor) {
        try {
            Keyspace result = connectionFactory.getKeyspace(name);
            result.createKeyspace(ImmutableMap.<String, Object>builder()
                    .put("strategy_options", ImmutableMap.<String, Object>builder()
                        .put("replication_factor", String.valueOf(replicationFactor))
                        .build())
                    .put("strategy_class", "SimpleStrategy")
                    .build());
            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Fail to connect and create keyspace %s", name), e);
        }
    }
    
    @Override
    public void truncateKeyspace(String name) {
        if (isKeyspaceExists(name)) {
            try {
                connectionFactory.getCluster().dropKeyspace(name);
            }
            catch (ConnectionException e) {
                throw new RuntimeException(String.format("Fail to connect and drop keyspace %s", name), e);
            }
        }
    }

    @Override
    public <K, C> boolean isColumnFamilyExisits(Keyspace keyspace, ColumnFamily<K, C> columnFamily) {
        try {
            return keyspace.describeKeyspace().getColumnFamily(columnFamily.getName()) != null;
        } catch (ConnectionException e) {
            return false;
        }
    }

    @Override
    public <K, C> void createColumnFamily(Keyspace keyspace, ColumnFamily<K, C> columnFamily, CFValidationClass validationClass) {
        try {
            keyspace.createColumnFamily(columnFamily, ImmutableMap.<String, Object>builder()
                    .put("default_validation_class", validationClass.name())
                    .put("key_validation_class", "UTF8Type")
                    .put("comparator_type", "UTF8Type")
                    .build());
        } catch (ConnectionException e) {
            throw new RuntimeException(String.format("Fail to create column family %s in keyspace %s", columnFamily.getName(), keyspace.getKeyspaceName()), e);
        }
    }

    @Override
    public void setUpCluster() {
        Keyspace keyspace = safeCreateKeyspace(KeySpaces.files.toString(), 1);
        safeCreateColumnFamily(keyspace, ColumnFamilies.FileMetas, CFValidationClass.UTF8Type);
        safeCreateColumnFamily(keyspace, ColumnFamilies.FileDatas, CFValidationClass.BytesType);
        
        keyspace = safeCreateKeyspace(KeySpaces.factorizations.toString(), 1);
        safeCreateColumnFamily(keyspace, ColumnFamilies.LZFactors, CFValidationClass.UTF8Type);
        safeCreateColumnFamily(keyspace, ColumnFamilies.LZ77Factors, CFValidationClass.UTF8Type);
        
        keyspace = safeCreateKeyspace(KeySpaces.slps.toString(), 1);
        safeCreateColumnFamily(keyspace, ColumnFamilies.SLPs, CFValidationClass.BytesType);
        
        keyspace = safeCreateKeyspace(KeySpaces.statistics.toString(), 1);
        safeCreateColumnFamily(keyspace, ColumnFamilies.Statistics, CFValidationClass.UTF8Type);
    }
    
    private Keyspace safeCreateKeyspace(String name, int replicationFactor) {
        if (!isKeyspaceExists(name))
            return createKeyspace(name, replicationFactor);
        return connectionFactory.getKeyspace(name);
    }
    
    private <K,C> void safeCreateColumnFamily(Keyspace keyspace, ColumnFamily<K, C> columnFamily, CFValidationClass validationClass) {
        if (!isColumnFamilyExisits(keyspace, columnFamily))
            createColumnFamily(keyspace, columnFamily, validationClass);
    }

}
