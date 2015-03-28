package services.blobs;

public interface IBlobService
{
	byte[] drain(String id);
	void write(String id, byte[] content);
}
