package compressionservice.compression.algorithms.lz77.windows;

public interface IWindowFactory
{
    IStringWindow create(int size);
}
