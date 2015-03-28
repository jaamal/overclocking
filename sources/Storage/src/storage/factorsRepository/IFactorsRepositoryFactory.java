package storage.factorsRepository;

import dataContracts.FactorDef;
import dataContracts.LZFactorDef;

public interface IFactorsRepositoryFactory {
    IFactorsRepository<LZFactorDef> getLZRepository();
    IFactorsRepository<FactorDef> getLZ77Repository();
}
