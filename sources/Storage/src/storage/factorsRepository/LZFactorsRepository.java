package storage.factorsRepository;

import serialization.ISerializer;
import storage.CassandraArrayItemsRepositoryBase;
import storage.KeySpaces;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;
import dataContracts.FactorDef;

public class LZFactorsRepository extends CassandraArrayItemsRepositoryBase<FactorDef> implements IFactorsRepository
{
    public LZFactorsRepository(ISerializer serializer, ICassandraConnectionFactory cassandraConnectionFactory)
    {
        super(FactorDef.class, serializer, cassandraConnectionFactory, KeySpaces.factorizations, ColumnFamilies.LZFactors);
    }
}
