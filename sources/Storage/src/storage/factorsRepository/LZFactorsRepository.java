package storage.factorsRepository;

import serialization.ISerializer;
import storage.CassandraArrayItemsRepositoryBase;
import storage.KeySpaces;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;
import dataContracts.LZFactorDef;

public class LZFactorsRepository extends CassandraArrayItemsRepositoryBase<LZFactorDef> implements IFactorsRepository<LZFactorDef>
{
    public LZFactorsRepository(ISerializer serializer, ICassandraConnectionFactory cassandraConnectionFactory)
    {
        super(LZFactorDef.class, serializer, cassandraConnectionFactory, KeySpaces.factorizations, ColumnFamilies.LZFactors);
    }
}
