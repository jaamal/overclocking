package dataContracts;

public class LZFactorDef extends FactorDef
{
    public Long offset = null;
    
    public LZFactorDef(long beginPosition, long length)
    {
        super(beginPosition, length);
    }

    public LZFactorDef(char symbol)
    {
        super(symbol);
    }
}
