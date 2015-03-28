package storage.slpProductsRepository;

import serialization.ISerializer;
import storage.CassandraArrayItemsRepositoryBase;
import storage.KeySpaces;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;
import dataContracts.Product;

public class SlpProductsRepository extends CassandraArrayItemsRepositoryBase<Product> implements ISlpProductsRepository {
    public SlpProductsRepository(ISerializer serializer, ICassandraConnectionFactory cassandraConnectionFactory)
    {
        super(Product.class, serializer, cassandraConnectionFactory, KeySpaces.slps, ColumnFamilies.SLPs);
    }
}
