package commons.settings;

public class KnownKeys {
    public final static String ServerPort = "server.port";
    public final static String ServerStopTimeout = "server.stop_timeout";
    public final static String ServerRunProfile = "server.run_profile";
    public final static String ServerWorkingDir = "server.working_dir";
    public final static String ServerSuffixArrayBuilderPath = "server.suffix_array_builder_path";

    public final static String CassandraVersion = "cassandra.version";
    public final static String CassandraCQLVersion = "cassandra.cql_version";
    public final static String CassandraClusterName = "cassandra.cluster_name";
    public final static String CassandraSeeds = "cassandra.seeds";
    public final static String CassandraMaxConnectionPerHost = "cassandra.max_connection_per_host";
    
    public final static String HttpClientHost = "httpclient.host";
    public final static String HttpClientPort = "httpclient.port";
    public final static String HttpClientScheme = "httpclient.scheme";
    public final static String HttpClientCertStorePath = "httpclient.cert_store_path";
    public final static String HttpClientCertStorePassword = "httpclient.cert_store_password";
    public final static String HttpClientCertTrustedStorePath = "httpclient.cert_trusted_store_path";
    public final static String HttpClientCertTrustedStorePassword = "httpclient.cert_trusted_store_password";

    public final static String MemoryMappedFileBatchSize = "memory_mapped_file.batch_size";
}
