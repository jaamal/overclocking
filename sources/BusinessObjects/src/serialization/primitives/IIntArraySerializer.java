package serialization.primitives;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IIntArraySerializer {
    void serialize(OutputStream stream, int[] array) throws IOException;

    int[] deserialize(InputStream stream) throws IOException;
}
