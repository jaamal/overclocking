package caching.serializers;

public interface ISerializer<T>
{
    int size();

	byte[] serialize(T obj);

	T deserialize(byte[] array);

	void serialize(T obj, byte[] array, int offset);

	T deserialize(byte[] array, int offset);
}
