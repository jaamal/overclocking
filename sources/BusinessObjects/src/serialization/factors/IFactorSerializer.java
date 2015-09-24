package serialization.factors;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import dataContracts.FactorDef;

public interface IFactorSerializer
{
    void serialize(OutputStream stream, FactorDef[] factors) throws IOException;
    FactorDef[] deserialize(InputStream stream) throws IOException;
    long calcSizeInBytes(Iterable<FactorDef> factors);
}
