package caching.lineNumberMappers;

public class LineNumberMapperFactory implements ILineNumberMapperFactory
{
    @Override
    public ILineNumberMapper create()
    {
        return new LineNumberMapper();
    }
}
