package storage.factorsRepository;

import dataContracts.AlgorithmType;
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
    public IFactorsRepository find(AlgorithmType algorithmType)
    {
        switch (algorithmType) {
            case lz77:
                return lz77FactorsRepository;
            case lzInf:
                return lzFactorsRepository;
            default:
                return null;
        }
    }

}
