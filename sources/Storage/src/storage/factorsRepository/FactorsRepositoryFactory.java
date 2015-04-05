package storage.factorsRepository;

import serialization.ISerializer;
import storage.cassandraClient.ICassandraConnectionFactory;

public class FactorsRepositoryFactory implements IFactorsRepositoryFactory {

    private LZFactorsRepository lzFactorsRepository;
    private LZ77FactorsRepository lz77FactorsRepository;

    public FactorsRepositoryFactory(ISerializer serializer, ICassandraConnectionFactory cassandraConnectionFactory) {
        lzFactorsRepository = new LZFactorsRepository(serializer, cassandraConnectionFactory);
        lz77FactorsRepository = new LZ77FactorsRepository(serializer, cassandraConnectionFactory);
    }
    
    @Override
    public IFactorsRepository getLZRepository() {
        return lzFactorsRepository;
    }

    @Override
    public IFactorsRepository getLZ77Repository() {
        return lz77FactorsRepository;
    }

}
