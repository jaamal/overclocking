package caching.connections;

public class FileConnectionFactory implements IConnectionFactory
{
    private ITemporaryFileFactory temporaryFileFactory;

    public FileConnectionFactory(ITemporaryFileFactory temporaryFileFactory)
    {
        this.temporaryFileFactory = temporaryFileFactory;
    }

    @Override
    public IConnection create()
    {
        return new FileConnection(temporaryFileFactory.getTemporaryFile());
    }
}
