package storage.factorsRepository;

import dataContracts.AlgorithmType;

public interface IFactorsRepositoryFactory {
    IFactorsRepository find(AlgorithmType algorithmType);
}
