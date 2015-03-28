package compressionservice.compression.algorithms.analysator;

import java.util.List;

import dataContracts.FactorDef;

public interface IAnalysator
{
    <T extends FactorDef> long countByteSize(List<T> factors);
}
