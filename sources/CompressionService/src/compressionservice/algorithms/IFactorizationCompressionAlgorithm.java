package compressionservice.algorithms;

import java.util.List;
import dataContracts.FactorDef;

public interface IFactorizationCompressionAlgorithm extends ICompressionAlgorithm
{
    boolean supportFactorization();
    List<FactorDef> getFactorization();
}
