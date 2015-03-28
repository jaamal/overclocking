package serialization.lzFactorizations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import dataContracts.FactorDef;

public interface ILzFactorizationSerializer {
    void serialize(OutputStream stream, FactorDef[] factors) throws IOException;
    FactorDef[] deserialize(InputStream stream) throws IOException;
}
