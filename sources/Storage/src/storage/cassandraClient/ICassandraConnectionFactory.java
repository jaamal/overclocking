package storage.cassandraClient;

import com.netflix.astyanax.Cluster;
import com.netflix.astyanax.Keyspace;

public interface ICassandraConnectionFactory {
    Keyspace getKeyspace(String name);
    Cluster getCluster();
}
