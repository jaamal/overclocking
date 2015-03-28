package storage.cassandraClient;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Cluster;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import commons.settings.ISettings;
import commons.settings.KnownKeys;

public class CassandraConnectionFactory implements ICassandraConnectionFactory {

    private ISettings settings;

    public CassandraConnectionFactory(ISettings settings) {
        this.settings = settings;
    }
    
    @Override
    public Keyspace getKeyspace(String name) {
        AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
        .forCluster(settings.getString(KnownKeys.CassandraClusterName))
        .forKeyspace(name)
        .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
            .setCqlVersion(settings.getString(KnownKeys.CassandraCQLVersion))
            .setTargetCassandraVersion(settings.getString(KnownKeys.CassandraVersion))
//            .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
        )
        .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
            .setPort(parsePort(settings))
            .setMaxConnsPerHost(settings.getInt(KnownKeys.CassandraMaxConnectionPerHost))
            .setSeeds(settings.getString(KnownKeys.CassandraSeeds))
        )
        .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
        .buildKeyspace(ThriftFamilyFactory.getInstance());

        context.start();
        return context.getClient();
    }

    @Override
    public Cluster getCluster() {
        AstyanaxContext<Cluster> context = new AstyanaxContext.Builder()
        .forCluster(settings.getString(KnownKeys.CassandraClusterName))
        .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()
                    .setCqlVersion(settings.getString(KnownKeys.CassandraCQLVersion))
                    .setTargetCassandraVersion(settings.getString(KnownKeys.CassandraVersion))
        //            .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
        )
        .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
            .setPort(parsePort(settings))
            .setMaxConnsPerHost(settings.getInt(KnownKeys.CassandraMaxConnectionPerHost))
            .setSeeds(settings.getString(KnownKeys.CassandraSeeds))
        )
        .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
        .buildCluster(ThriftFamilyFactory.getInstance());

        context.start();
        return context.getClient();
    }
    
    private static int parsePort(ISettings settings) {
        String seeds = settings.getString(KnownKeys.CassandraSeeds);
        String[] splits = seeds.split(":");
        if (splits.length != 2)
            throw new RuntimeException(String.format("Fail to parse cassandra port from %s.", seeds));
        return Integer.parseInt(splits[1]);
    }

}
