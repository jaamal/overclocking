package compressionservice.compression.algorithms.factorization;

import dataContracts.FactorDef;

public interface IFactorIterator extends AutoCloseable
{
    public boolean any();
    public FactorDef next();
}
