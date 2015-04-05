package compressionservice.compression.algorithms.lzInf;

import dataContracts.FactorDef;

public interface ILZFactorIterator extends AutoCloseable
{
    public boolean hasFactors();
    public FactorDef getNextFactor();
    public void close();
}
