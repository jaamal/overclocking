package storage.cassandraClient;

import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.serializers.IntegerSerializer;
import com.netflix.astyanax.serializers.StringSerializer;

public class ColumnFamilies {
    public final static ColumnFamily<String, String> FileMetas = ColumnFamily.newColumnFamily("metas", StringSerializer.get(), StringSerializer.get());
    public final static ColumnFamily<String, Integer> FileDatas = ColumnFamily.newColumnFamily("bodies", StringSerializer.get(), IntegerSerializer.get());
    
    public final static ColumnFamily<String, String> LZ77Factors = ColumnFamily.newColumnFamily("lz77factors", StringSerializer.get(), StringSerializer.get());
    public final static ColumnFamily<String, String> LZFactors = ColumnFamily.newColumnFamily("lzfactors", StringSerializer.get(), StringSerializer.get());
    
    public final static ColumnFamily<String, String> SLPs = ColumnFamily.newColumnFamily("slps", StringSerializer.get(), StringSerializer.get());
    
    public final static ColumnFamily<String, String> Statistics = ColumnFamily.newColumnFamily("statistics", StringSerializer.get(), StringSerializer.get());
}
