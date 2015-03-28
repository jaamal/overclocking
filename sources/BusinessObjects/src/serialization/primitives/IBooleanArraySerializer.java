package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IBooleanArraySerializer {
    void serialize(OutputStream stream, boolean[] array) throws IOException;

    boolean[] deserialize(InputStream stream) throws IOException;
}
