package compressingCore.dataFiltering.automatas;

public class AlphabetFactory implements IAlphabetFactory
{
    @Override
    public IAlphabite create(AlphabiteType alphabiteType)
    {
        switch (alphabiteType)
        {
            case DNA:
                return new Alphabite(new char[]{'a', 'c', 't', 'g'});
            default:
                throw new UnknownAlphabiteTypeException(alphabiteType);
        }
    }
}
