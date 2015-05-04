package storage.filesRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;

import storage.KeySpaces;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;
import storage.cassandraClient.IEntityHandler;

import com.google.common.base.Function;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.recipes.reader.AllRowsReader;

import dataContracts.ContentType;
import dataContracts.files.FileBatch;
import dataContracts.files.FileMetadata;

public class CassandraFilesRepository implements IFilesRepository {
    private ICassandraConnectionFactory cassandraConnectionFactory;
    private IEntityHandler entityHandler;

    public CassandraFilesRepository(ICassandraConnectionFactory cassandraConnectionFactory, IEntityHandler entityHandler) {
        this.cassandraConnectionFactory = cassandraConnectionFactory;
        this.entityHandler = entityHandler;
    }

    @Override
    public void saveMeta(FileMetadata fileMeta) {
        entityHandler.write(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, fileMeta);
    }

    @Override
    public FileMetadata getMeta(String fileId) {
        return entityHandler.read(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, fileId);
    }
    
    @Override
    public FileMetadata[] getMeta(Collection<String> fileIds) {
        return entityHandler.read(KeySpaces.files, ColumnFamilies.FileMetas, FileMetadata.class, fileIds)
                            .toArray(new FileMetadata[0]);
    }
    
    @Override
    public List<String> getFileIds() {
        return getFileIds(0, Integer.MAX_VALUE);
    }
    
    @Override
    public List<String> getFileIds(int from, int count) {
        try {
            ArrayList<String> rowKeys = new ArrayList<String>();
           
            Keyspace filesKeyspace = cassandraConnectionFactory.getKeyspace(KeySpaces.files.toString());
            Function<Row<String, String>, Boolean> rowIdFunc = new Function<Row<String, String>, Boolean>() {
                @Override
                public Boolean apply(Row<String, String> row) {
                    rowKeys.add(row.getKey());
                    return true;
                }
            };
            
            //TODO: handle false result
            new AllRowsReader.Builder<String, String>(filesKeyspace, ColumnFamilies.FileMetas)
                    .withColumnRange(null, null, false, 0)
                    .withPartitioner(null)
                    .forEachRow(rowIdFunc)
                    .build()
                    .call();
            
            if (from <= 0 && count == Integer.MAX_VALUE)
                return rowKeys;
            else{
                int rowKeysSize = rowKeys.size();
                return from + count > rowKeysSize 
                        ? rowKeys.subList(from, rowKeysSize)
                        : rowKeys.subList(from, from + count);
            }
                
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to enumerate row keys"), e);
        }
    }

    @Override
    public void saveBatch(FileBatch fileBatch) {
        try {
            cassandraConnectionFactory.getKeyspace(KeySpaces.files.name()).prepareColumnMutation(ColumnFamilies.FileDatas, fileBatch.fileId, fileBatch.batchNumber)
                    .putValue(fileBatch.batchData, null)
                    .execute();
        } catch (ConnectionException e) {
            throw new RuntimeException(String.format("Fail to add batch for file %s", fileBatch.fileId), e);
        }
    }

    @Override
    public FileBatch getBatch(String fileId, int batchNumber) {
        try {
            OperationResult<Column<Integer>> result = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name())
                    .prepareQuery(ColumnFamilies.FileDatas)
                    .getRow(fileId)
                    .getColumn(batchNumber)
                    .execute();
            return new FileBatch(fileId, batchNumber, result.getResult().getByteArrayValue());
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileBatch> getAllBatches(String fileId) {
        try {
            OperationResult<ColumnList<Integer>> queryResult = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name())
                    .prepareQuery(ColumnFamilies.FileDatas)
                    .getRow(fileId)
                    .execute();

            ArrayList<FileBatch> result = new ArrayList<FileBatch>();
            for (Column<Integer> column : queryResult.getResult()) {
                result.add(new FileBatch(fileId, column.getName(), column.getByteArrayValue()));
            }
            return result;
        } catch (ConnectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<FileBatch> getFile(String fileId) {
        return new FileBatchIterator(getColumnsIterator(fileId), fileId);
    }
    
    @Override
    public Iterable<FileBatch> getFileIterator(String fileId) {
        return new FileBatchIterable(fileId);
    }

    @Override
    public InputStream getFileStream(FileMetadata fileMeta) {

        try {
            return fileMeta.getContentType() == ContentType.GZip
                    ? new GZIPInputStream(new FileBatchStream(getColumnsIterator(fileMeta.getId())))
                    : new FileBatchStream(getColumnsIterator(fileMeta.getId()));
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to open input stream to file with id %s and content type %s.", fileMeta.getId(), fileMeta.getContentType()), e);
        }
    }

    @Override
    public void remove(String fileId) {
        try {
            ArrayList<ColumnFamily<String, Integer>> fileDataColumnFamily = new ArrayList<>();
            fileDataColumnFamily.add(ColumnFamilies.FileDatas);
            MutationBatch fileDataMutationBatch = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name())
                    .prepareMutationBatch();
            fileDataMutationBatch.deleteRow(fileDataColumnFamily, fileId);
            fileDataMutationBatch.execute();

            ArrayList<ColumnFamily<String, String>> fileMetaColumnFamily = new ArrayList<>();
            fileMetaColumnFamily.add(ColumnFamilies.FileMetas);
            MutationBatch fileMetaMutationBatch = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name())
                    .prepareMutationBatch();
            fileMetaMutationBatch.deleteRow(fileMetaColumnFamily, fileId);
            fileMetaMutationBatch.execute();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Unable to open input stream to file with id %s.", fileId), e);
        }
    }

    private Iterator<Column<Integer>> getColumnsIterator(String fileId) {
        try {
            OperationResult<ColumnList<Integer>> queryResult = cassandraConnectionFactory.getKeyspace(KeySpaces.files.name())
                    .prepareQuery(ColumnFamilies.FileDatas)
                    .getRow(fileId)
                    .execute();
            return queryResult.getResult().iterator();
        } catch (ConnectionException e) {
            throw new RuntimeException(String.format("Fail to get file with id %s.", fileId), e);
        }
    }

    private class FileBatchIterable implements Iterable<FileBatch> {
        private String fileId;

        public FileBatchIterable(String fileId) {
            this.fileId = fileId;
        }
        
        @Override
        public Iterator<FileBatch> iterator() {
            return new FileBatchIterator(getColumnsIterator(fileId), fileId);
        }
    }
    
    private class FileBatchIterator implements Iterator<FileBatch> {
        private String fileId;
        private Iterator<Column<Integer>> columns;

        public FileBatchIterator(Iterator<Column<Integer>> columns, String fileId) {
            this.columns = columns;
            this.fileId = fileId;
        }

        @Override
        public boolean hasNext() {
            return columns.hasNext();
        }

        @Override
        public FileBatch next() {
            Column<Integer> column = columns.next();
            return new FileBatch(fileId, column.getName(), column.getByteArrayValue());
        }

        @Override
        public void remove() {
            throw new RuntimeException("Method not implemented.");
        }

		@Override
		public void forEachRemaining(Consumer<? super FileBatch> arg0) {
			// TODO Auto-generated method stub	
		}
    }

    private class FileBatchStream extends InputStream {
        private Iterator<Column<Integer>> columnIterator;
        private int position;
        private byte[] body;

        public FileBatchStream(Iterator<Column<Integer>> columnIterator) {
            this.columnIterator = columnIterator;
            fillNext();
        }

        private void fillNext() {
            if (columnIterator.hasNext()) {
                body = columnIterator.next().getByteArrayValue();
                position = 0;
            } else {
                body = null;
                position = -1;
            }
        }

        @Override
        public int read() throws IOException {
            if (position == -1 || body == null)
                return -1;
            if (position == body.length) {
                fillNext();
                return read();
            }
            return body[position++] & 0xff;
        }
    }
}
