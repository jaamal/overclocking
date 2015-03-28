package storage.factorsRepository;

import serialization.ISerializer;
import storage.CassandraArrayItemsRepositoryBase;
import storage.KeySpaces;
import storage.cassandraClient.ColumnFamilies;
import storage.cassandraClient.ICassandraConnectionFactory;
import dataContracts.FactorDef;

public class LZ77FactorsRepository extends CassandraArrayItemsRepositoryBase<FactorDef> implements IFactorsRepository<FactorDef>
{
    public LZ77FactorsRepository(
            ISerializer serializer,
            ICassandraConnectionFactory cassandraConnectionFactory)
    {
        super(FactorDef.class, serializer, cassandraConnectionFactory, KeySpaces.factorizations, ColumnFamilies.LZ77Factors);
    }
}
