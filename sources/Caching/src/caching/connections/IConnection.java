package caching.connections;

public interface IConnection
{
    void open();
 
	void close();

	byte[] read(long offset, int length);

	void write(long offset, byte[] array);
}
