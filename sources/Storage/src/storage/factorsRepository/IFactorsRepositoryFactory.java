package storage.factorsRepository;

import dataContracts.AlgorithmType;

public interface IFactorsRepositoryFactory {
    IFactorsRepository getLZRepository();
    
    IFactorsRepository find(AlgorithmType algorithmType);
}
