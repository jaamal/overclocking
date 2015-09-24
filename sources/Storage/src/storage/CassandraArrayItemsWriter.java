package storage;

import serialization.ISerializer;

import com.netflix.astyanax.ColumnListMutation;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnFamily;

public class CassandraArrayItemsWriter<T> implements IArrayItemsWriter<T> {
    private static final int batchSize = 8192;
    
    private ISerializer serializer;
    private Keyspace keyspace;
    private ColumnFamily<String, String> columnFamily;
    
    private String rowId;
    private String rowListDone;
    
    private MutationBatch mutationBatch;
    private ColumnListMutation<String> rowMutations;
    private int currentBatchNumber;
    private int currentBatchSize;

    public CassandraArrayItemsWriter(
            ISerializer serializer,
            Keyspace keyspace,
            ColumnFamily<String, String> columnFamily,
            String rowId,
            String rowListDone) {
        this.serializer = serializer;
        this.keyspace = keyspace;
        this.columnFamily = columnFamily;
        this.rowId = rowId;
        this.rowListDone = rowListDone;
        
        mutationBatch = keyspace.prepareMutationBatch();
        rowMutations = mutationBatch.withRow(columnFamily, rowId);
        this.currentBatchNumber = 0;
        this.currentBatchSize = 0;
    }

    @Override
    public void add(T item) {
        String columnName = ColumnFormatter.asPositiveInt(++currentBatchNumber);
        byte[] value = serializer.stringify(item).getBytes();
        rowMutations.putColumn(columnName, value);
        currentBatchSize += value.length;
        if (currentBatchSize >= batchSize)
            flush();
    }

    
    @Override
    public void done() {
        flush();
        try {
            keyspace.prepareColumnMutation(columnFamily, rowListDone, rowId)
                    .putEmptyColumn(null)
                    .execute();
        } catch (ConnectionException e) {
            throw new RuntimeException("Fail to write batch done row.", e);
        }
    }

    private void flush() {
        try {
            mutationBatch.execute();
            currentBatchSize = 0;
            mutationBatch = keyspace.prepareMutationBatch();
            rowMutations = mutationBatch.withRow(columnFamily, rowId);
        } catch (ConnectionException e) {
            throw new RuntimeException("Fail to write batch.", e);
        }
    }
}
