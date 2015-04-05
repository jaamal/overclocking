package dataContracts;

public class FactorDef
{
    public final long beginPosition;
    public final long length;
    public final int symbol;
    public final boolean isTerminal;

    //TODO: obsolete constructor
    public FactorDef(
            boolean isTerminal,
            long beginPosition,
            long length,
            int symbol)
    {
        this.beginPosition = beginPosition;
        this.length = length;
        this.symbol = symbol;
        this.isTerminal = isTerminal;
    }
    
    public FactorDef(long beginPosition, long length)
    {
        this.beginPosition = beginPosition;
        this.length = length;
        this.isTerminal = false;
        this.symbol = 0;
    }

    public FactorDef(char symbol)
    {
        this.isTerminal = true;
        this.symbol = symbol;
        this.beginPosition = -1;
        this.length = -1;
    }
    
    public long getEndPosition() {
        return beginPosition + length;
    }
}
