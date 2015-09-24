package compressionservice.algorithms;

import java.util.List;
import dataContracts.FactorDef;

public interface ICompressionAlgorithm extends IAlgorithm
{
    byte[] getCompressedRepresentation();
    boolean supportFactorization();
    List<FactorDef> getFactorization();
}
