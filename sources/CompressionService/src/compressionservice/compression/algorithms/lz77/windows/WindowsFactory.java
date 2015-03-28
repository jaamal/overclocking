package compressionservice.compression.algorithms.lz77.windows;

public class WindowsFactory implements IWindowFactory
{
    @Override
    public IStringWindow create(int size)
    {
        return new StringWindow(size);
    }
}
