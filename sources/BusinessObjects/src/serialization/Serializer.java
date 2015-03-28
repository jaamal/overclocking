package serialization;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class Serializer implements ISerializer {

    private static Gson gson = new Gson();
    
    @Override
    public String stringify(Object obj) {
        return gson.toJson(obj);
    }
    @Override
    public <T> String stringify(Object obj, Class<T> objClass) {
        if (obj == null) {
            return stringify(null);
        }
        if (!objClass.isAssignableFrom(obj.getClass())) {
            throw new IllegalArgumentException(String.format("Fail to stringify object of type %s as object of type %s.", obj.getClass().getName(), objClass.getName()));
        }
        return gson.toJson(obj, objClass);
    }

    @Override
    public <T> T deserialize(String json, Class<T> objClass) {
        return gson.fromJson(json, objClass);
    }

    @Override
    public <T> T deserialize(InputStream stream, Class<T> objClass) {
        return gson.fromJson(new InputStreamReader(stream), objClass);
    }
}
