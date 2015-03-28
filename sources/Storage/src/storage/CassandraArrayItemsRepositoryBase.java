package storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

import com.netflix.astyanax.MutationBatch;

import serialization.ISerializer;
import storage.cassandraClient.ICassandraConnectionFactory;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;

public abstract class CassandraArrayItemsRepositoryBase<T> implements IArrayItemsRepository<T> {
    private final static String rowListDone = "doneStatistics";

    private ColumnFamily<String, String> columnFamily;
    private Class<T> storageItemClass;
    private ISerializer serializer;
    private Keyspace keyspace;

    public CassandraArrayItemsRepositoryBase(
            Class<T> storageItemClass,
            ISerializer serializer,
            ICassandraConnectionFactory connectionFactory,
            KeySpaces keyspace,
            ColumnFamily<String, String> columnFamily) {
        this.storageItemClass = storageItemClass;
        this.serializer = serializer;
        this.keyspace = connectionFactory.getKeyspace(keyspace.name());
        this.columnFamily = columnFamily;
    }

    @Override
    public List<T> readItems(String statisticId) {
        try {
            ArrayList<T> result = new ArrayList<>();
            Iterator<Column<String>> columns = keyspace.prepareQuery(columnFamily).getRow(statisticId).execute().getResult().iterator();
            while (columns.hasNext()) {
                Column<String> column = columns.next();
                result.add(serializer.deserialize(column.getStringValue(), storageItemClass));
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to get columns for statisticId %s", statisticId), e);
        }
    }

    @Override
    public Iterable<String> getDoneStatisticIds() {
        try {
            ColumnList<String> queryResult = keyspace.prepareQuery(columnFamily).getRow(rowListDone).execute().getResult();
            return new StringBatchIterable(queryResult.iterator());
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to query done statistics of type %s ", storageItemClass.getSimpleName()), e);
        }
    }

    @Override
    public IArrayItemsWriter<T> getWriter(String statisticId) {
        return new CassandraArrayItemsWriter<T>(serializer, keyspace, columnFamily, statisticId, rowListDone);
    }

    public void remove(String statisticId) {
        try {
            ArrayList<ColumnFamily<String, String>> columnFamilies = new ArrayList<>();
            columnFamilies.add(columnFamily);
            MutationBatch mutationBatch = keyspace.prepareMutationBatch();
            mutationBatch.deleteRow(columnFamilies, statisticId);
            mutationBatch.execute();
            keyspace.prepareColumnMutation(columnFamily, rowListDone, statisticId)
                    .deleteColumn()
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Fail to remove statistics id %s of type %s ", statisticId, storageItemClass.getSimpleName()), e);
        }
    }

    private class StringBatchIterable implements Iterable<String> {
        private Iterator<Column<String>> columns;

        public StringBatchIterable(Iterator<Column<String>> columns) {
            this.columns = columns;
        }

        @Override
        public Iterator<String> iterator() {
            return new StringBatchIterator(columns);
        }

        private class StringBatchIterator implements Iterator<String> {
            private Iterator<Column<String>> columns;

            public StringBatchIterator(Iterator<Column<String>> columns) {
                this.columns = columns;
            }

            @Override
            public boolean hasNext() {
                return columns.hasNext();
            }

            @Override
            public String next() {
                com.netflix.astyanax.model.Column<String> column = columns.next();
                return column.getName();
            }

            @Override
            public void remove() {
                columns.remove();
            }

			@Override
			public void forEachRemaining(Consumer<? super String> arg0) {
				// TODO Auto-generated method stub
			}
        }

		@Override
		public void forEach(Consumer<? super String> arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public Spliterator<String> spliterator() {
			// TODO Auto-generated method stub
			return null;
		}
    }
}
