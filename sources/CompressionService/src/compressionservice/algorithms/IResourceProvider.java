package compressionservice.algorithms;

import data.charArray.IReadableCharArray;
import dataContracts.DataFactoryType;
import dataContracts.FactorDef;

public interface IResourceProvider {
    FactorDef[] getFactorization(String sourceId);
    IReadableCharArray getText(String sourceId, DataFactoryType dataFactoryType);
}
