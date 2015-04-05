package storage.factorsRepository;

import dataContracts.FactorDef;

public interface IFactorsRepositoryFactory {
    IFactorsRepository<FactorDef> getLZRepository();
    IFactorsRepository<FactorDef> getLZ77Repository();
}
