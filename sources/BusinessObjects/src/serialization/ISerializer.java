package serialization;

import java.io.InputStream;

public interface ISerializer {
    String stringify(Object obj);
    <T> String stringify(Object obj, Class<T> objClass);
    <T> T deserialize(String json, Class<T> objClass);
    <T> T deserialize(InputStream stream, Class<T> objClass);
}
